package com.mdsl.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.ContactRequestDto;
import com.mdsl.model.dto.response.ContactResponseDto;
import com.mdsl.model.dto.response.NonActivityPackageDetailsResponseDto;
import com.mdsl.model.dto.response.ContactResponseDto;
import com.mdsl.model.entity.Address;
import com.mdsl.model.entity.City;
import com.mdsl.model.entity.Contact;
import com.mdsl.model.entity.Country;
import com.mdsl.model.entity.Entities;
import com.mdsl.model.entity.Institution;
import com.mdsl.model.entity.Contact;
import com.mdsl.model.mapper.ContactMapper;
import com.mdsl.repository.AddressRepository;
import com.mdsl.repository.CityRepository;
import com.mdsl.repository.ContactRepository;
import com.mdsl.repository.CountryRepository;
import com.mdsl.repository.EntitiesRepository;
import com.mdsl.repository.InstitutionRepository;
import com.mdsl.utils.MakerCheckerEngine;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.enumerations.StatementEnum;
import com.mdsl.utils.enumerations.StatusEnum;

@Service
@Transactional(rollbackOn=Exception.class)
public class ContactService {

	@Autowired
	private ContactRepository contactRepository;

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private InstitutionRepository institutionRepository;

	@Autowired
	private CountryRepository countryRepository;

	@Autowired
	private EntitiesRepository entitiesRepository;

	@Autowired
	private CityRepository cityRepository;

	@Autowired
	private ContactMapper contactMapper;

	@Autowired
	private MakerCheckerEngine makerCheckerEngine;

	public List<ContactResponseDto> getAllContacts() {
		List<Contact> contacts = contactRepository.findAll(Sort.by(Sort.Direction.ASC, "contactId"));

		if (contacts.isEmpty()) {
			throw new BusinessException(ResponseCode.INVALID_CONTACT_ID, HttpStatus.NOT_FOUND);
		}

		List<ContactResponseDto> contactResponseDtos = new ArrayList<>();
		contacts.forEach(cnt -> {
			if (cnt != null) {
				ContactResponseDto dto = contactMapper.toDto(cnt);
				contactResponseDtos.add(dto);
			}
		});

		return contactResponseDtos;
	}

	public List<ContactResponseDto> getContactByEntitiesId(String entity,String instId) {
		if (entity == null || entity.trim().isEmpty()) {
			throw new BusinessException(ResponseCode.ENT_ENTITY_CODE_NOT_FOUND, HttpStatus.BAD_REQUEST);
		}
		Institution institution = institutionRepository.findById(instId).orElseThrow(() -> new BusinessException(ResponseCode.INVALID_INSTITUTION_ID, HttpStatus.NOT_FOUND));
		List<Contact> contacts = contactRepository.findContactsByEntityAndInstitution(entity,institution, Sort.by(Sort.Direction.ASC, "contactId"));

		List<ContactResponseDto> contactResponseDtos = new ArrayList<>();
		contacts.forEach(contact -> {
			if (contact != null) {
				ContactResponseDto dto = contactMapper.toDto(contact);
				contactResponseDtos.add(dto);
			}
		});
		return contactResponseDtos;
	}

	public ContactRequestDto getContactById(int contactId) {
		Contact contact = contactRepository.findById(contactId)
				.orElseThrow(() -> new BusinessException(ResponseCode.INVALID_CONTACT_ID, HttpStatus.NOT_FOUND));
		ContactRequestDto contactRequestDto = contactMapper.toreqDto(contact);
		return contactRequestDto;
	}

	public ContactRequestDto saveOrUpdateContact(ContactRequestDto contactRequestDto) {
		Contact saveContact;
		Address saveAddress;

		if (contactRequestDto == null) {
			throw new BusinessException(ResponseCode.VAL_ERROR_OCCURRED, HttpStatus.BAD_REQUEST);
		}

		Institution institution = institutionRepository.findById(contactRequestDto.getInstitutionId())
				.orElseThrow(() -> new BusinessException(ResponseCode.INVALID_INSTITUTION_ID, HttpStatus.NOT_FOUND));

		Entities entity = entitiesRepository.findByEntityIdAndInstitution(contactRequestDto.getEntityId(),institution)
				.orElseThrow(() -> new BusinessException(ResponseCode.ENT_ENTITY_CODE_NOT_FOUND, HttpStatus.NOT_FOUND));

		UserDetailsImpl userDetails = null;
		if (SecurityContextHolder.getContext() != null
				&& SecurityContextHolder.getContext().getAuthentication() != null
				&& SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) {
			userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}

		Country country = null;
		City city = null;

		if (contactRequestDto.getCntryId() != 0) {
			country = countryRepository.findById(contactRequestDto.getCntryId())
					.orElseThrow(() -> new BusinessException(ResponseCode.INVALID_COUNTRY_ID, HttpStatus.NOT_FOUND));
		}

		if (contactRequestDto.getCityId() != 0) {
			city = cityRepository.findById(contactRequestDto.getCityId())
					.orElseThrow(() -> new BusinessException(ResponseCode.INVALID_CITY_ID, HttpStatus.NOT_FOUND));
		}

		if (contactRequestDto.getContactId() == 0) {
			saveAddress = contactMapper.toEntityAddress(contactRequestDto);
			saveAddress.setCityCode(city);
			saveAddress.setCntryCode(country);
			saveAddress.setInstitution(institution);
			saveAddress.setDate(new Date());
			
			saveAddress.setEntitiesObject(entity);
			saveAddress.setEntities(entity.getEntityId());

			saveAddress.setUserCreate(userDetails != null ? String.valueOf(userDetails.getId()) : "system");
			saveAddress.setRecord_seq_id(0);

			saveContact = contactMapper.toEntityContact(contactRequestDto);
			saveContact.setRecord_seq_id(0);
			saveContact.setAddress(saveAddress);
			
			saveContact.setEntity(entity.getEntityId());
			saveContact.setEntityObject(entity);

			saveContact.setUserCreate(userDetails != null ? String.valueOf(userDetails.getId()) : "system");
			saveContact.setContactStatus(StatusEnum.ENABLED.getValue());
			saveContact.setReceiveEstatement(
					String.valueOf(contactRequestDto.getReceiveEstatement()).equalsIgnoreCase("y")
							? StatementEnum.YES.getValue()
							: StatementEnum.NO.getValue());
			saveContact.setInstitution(institution);
			saveContact.setDate(new Date());

		} else {
			Contact contact = contactRepository.findById(contactRequestDto.getContactId())
					.orElseThrow(() -> new BusinessException(ResponseCode.INVALID_CONTACT_ID, HttpStatus.NOT_FOUND));

			Address contactAddress = contact.getAddress();
			if (contactAddress == null || contactAddress.getAddressId() == null) {
				throw new BusinessException(ResponseCode.INVALID_ADDRESS_ID, HttpStatus.NOT_FOUND);
			}

			Address address = addressRepository.findById(contactAddress.getAddressId())
					.orElseThrow(() -> new BusinessException(ResponseCode.INVALID_ADDRESS_ID, HttpStatus.NOT_FOUND));

			contactRequestDto.setAddressId(contactAddress.getAddressId());
			char status = contact.getContactStatus();

			saveAddress = contactMapper.toEntityAddress(contactRequestDto);
			saveAddress.setCityCode(city);
			saveAddress.setCntryCode(country);
			saveAddress.setInstitution(institution);
			
			saveAddress.setEntitiesObject(entity);
			saveAddress.setEntities(entity.getEntityId());

			saveAddress.setUserCreate(userDetails != null ? String.valueOf(userDetails.getId()) : "system");
			saveAddress.setDate(address.getDate() != null ? address.getDate() : new Date());

			saveContact = contactMapper.toEntityContact(contactRequestDto);
			saveContact.setContactStatus(status == '1' ? '1' : '0');
			saveContact.setReceiveEstatement(
					String.valueOf(contactRequestDto.getReceiveEstatement()).equalsIgnoreCase("y")
							? StatementEnum.YES.getValue()
							: StatementEnum.NO.getValue());
			saveContact.setAddress(saveAddress);
			
			saveContact.setEntity(entity.getEntityId());
			saveContact.setEntityObject(entity);

			saveContact.setInstitution(institution);
			saveContact.setDate(contact.getDate() != null ? contact.getDate() : new Date());
			saveContact.setUserCreate(userDetails != null ? String.valueOf(userDetails.getId()) : "system");
		}

		if (makerCheckerEngine.processIfRequired(contactRequestDto, this.getClass().getName(), new Object() {}.getClass().getEnclosingMethod().getName(), "")) {
			return null;
		}
		saveAddress = addressRepository.save(saveAddress);
		saveContact.setAddress(saveAddress);
		saveContact = contactRepository.save(saveContact);

		contactRequestDto.setAddressId(saveAddress.getAddressId());
		contactRequestDto.setContactId(saveContact.getContactId());

		return contactRequestDto;
	}

	public void updateStatus(int id) {
		char enabled = contactRepository.findById(id).get().getContactStatus();
		if (makerCheckerEngine.processIfRequired(id, this.getClass().getName(), new Object() {}.getClass().getEnclosingMethod().getName(), "")) {
			return;
		}
		if (enabled == '1')
			contactRepository.UpdateStatus(id, '0');
		else {
			contactRepository.UpdateStatus(id, '1');
		}

	}

	public void deleteContact(int id)  throws Exception {
		contactRepository.findById(id)
				.orElseThrow(() -> new BusinessException(ResponseCode.INVALID_CONTACT_ID, HttpStatus.NOT_FOUND));
		if (makerCheckerEngine.processIfRequired(id, this.getClass().getName(), new Object() {}.getClass().getEnclosingMethod().getName(), "")) {
			return;
		}
		contactRepository.deleteById(id);
	}

	public List<ContactResponseDto> getActiveContacts() {
		List<ContactResponseDto> contactResponseDto = new ArrayList<ContactResponseDto>();
		List<Contact> activeContacts = contactRepository.findByContactStatus(StatusEnum.ENABLED.getValue(),
				Sort.by(Sort.Direction.ASC, "contactId"));
		activeContacts.stream().forEach((contact) -> {
			ContactResponseDto cdto = contactMapper.toDto(contact);
			contactResponseDto.add(cdto);
		});
		return contactResponseDto;
	}
}
