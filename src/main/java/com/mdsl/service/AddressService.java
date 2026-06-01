package com.mdsl.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.mdsl.model.dto.request.AddressRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.response.AddressResponseDto;
import com.mdsl.model.entity.Address;
import com.mdsl.model.entity.City;
import com.mdsl.model.entity.Country;
import com.mdsl.model.entity.Entities;
import com.mdsl.model.entity.Institution;
import com.mdsl.model.entity.SystemCode;
import com.mdsl.model.mapper.AddressMapper;
import com.mdsl.repository.AddressRepository;
import com.mdsl.repository.CityRepository;
import com.mdsl.repository.CountryRepository;
import com.mdsl.repository.EntitiesRepository;
import com.mdsl.repository.InstitutionRepository;
import com.mdsl.repository.SystemCodeRepository;
import com.mdsl.swtch.model.dto.request.SwitchMasterAddressRequestDto;
import com.mdsl.swtch.service.SwitchEntityAddressService;
import com.mdsl.swtch.service.SwitchMasterAddressService;
import com.mdsl.utils.ResponseCode;

@Service
//@Transactional(readOnly = true)
public class AddressService {

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private InstitutionRepository institutionRepository;

	@Autowired
	private CountryRepository countryRepository;

	@Autowired
	private CityRepository cityRepository;

	@Autowired
	private EntitiesRepository entitiesRepository;

	@Autowired
	private SystemCodeRepository systemCodeRepository;

	@Autowired
	private AddressMapper addressMapper;

	@Autowired
	private SwitchEntityAddressService switchEntityAddressService;

	@Autowired
	private SwitchMasterAddressService switchMasterAddressService;

	public List<AddressResponseDto> getAllAddress() {
		// TODO Auto-generated method stub
		List<AddressResponseDto> addressResponseDto = new ArrayList<AddressResponseDto>();
		List<Address> address = addressRepository.findAll();
		address.stream().forEach((add) -> {
			AddressResponseDto dto = addressMapper.toDto(add);
			addressResponseDto.add(dto);
		});
		return addressResponseDto;
	}

	public AddressResponseDto saveOrUpdateAddress(AddressRequestDto addressResponseDto) {
		Address saveAddress;
		Entities entity = null;
		Institution institution = institutionRepository.findById(addressResponseDto.getInstitutionId())
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_INSTITUTION_ID, HttpStatus.NOT_FOUND));

		SystemCode switchSystemCode = this.systemCodeRepository.findByCodePrefixAndCodeValueAndInstitution_InstitutionId("SWITCH_ENABLED", "SWITCH_ENABLED_FLAG", "SYSTEM")
				.orElseThrow(() -> new BusinessException(ResponseCode.INVALID_SYSTEM_CODE_ID, HttpStatus.NOT_FOUND));

		if (!addressResponseDto.getEntityId().equals(null) || !addressResponseDto.getEntityId().equals("")) {
			entity = entitiesRepository.findByEntityIdAndInstitution(addressResponseDto.getEntityId(),institution)
					.orElseThrow(() -> new BusinessException(ResponseCode.ENT_ENTITY_CODE_NOT_FOUND, HttpStatus.NOT_FOUND));
		}
		Country country = null;
		City city = null;
		UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		if (addressResponseDto.getCntryId() != 0) {
			country = countryRepository.findById(addressResponseDto.getCntryId())
					.orElseThrow(() -> new BusinessException(ResponseCode.INVALID_COUNTRY_ID, HttpStatus.NOT_FOUND));
		}

		if (addressResponseDto.getCityId() != 0) {
			city = cityRepository.findById(addressResponseDto.getCityId())
					.orElseThrow(() -> new BusinessException(ResponseCode.INVALID_CITY_ID, HttpStatus.NOT_FOUND));
		}

		if (Objects.isNull(addressResponseDto.getAddressId()) || addressResponseDto.getAddressId() == 0) {
			saveAddress = addressMapper.toEntity(addressResponseDto);
			saveAddress.setCityCode(city);
			saveAddress.setCntryCode(country);
			saveAddress.setInstitution(institution);
			
			saveAddress.setEntities(entity.getEntityId());
			saveAddress.setEntitiesObject(entity);

			saveAddress.setDate(new Date());
		} else {
			Address address = addressRepository.findById(addressResponseDto.getAddressId())
					.orElseThrow(() -> new BusinessException(ResponseCode.INVALID_ADDRESS_ID, HttpStatus.NOT_FOUND));
			saveAddress = addressMapper.toEntity(addressResponseDto);
			saveAddress.setCityCode(city);
			saveAddress.setCntryCode(country);
			saveAddress.setInstitution(institution);
			
			saveAddress.setEntitiesObject(entity);
			saveAddress.setEntities(entity.getEntityId());
			
			saveAddress.setRecord_seq_id(0);
			//saveAddress.setEntities(entity);
			saveAddress.setDate(address.getDate());

		}
		saveAddress.setUserCreate(Integer.valueOf(userDetails.getId()).toString());
		saveAddress = addressRepository.save(saveAddress);
		if(switchSystemCode.getCodeSuffix().equals("1")) {
			this.saveSwitchMasterAddress(saveAddress);
		}
		AddressResponseDto addDto = addressMapper.toDto(saveAddress);
		return addDto;
	}

	public void deleteAddress(int id) throws Exception {
		Address address = addressRepository.findById(id)
				.orElseThrow(() -> new BusinessException(ResponseCode.INVALID_ADDRESS_ID, HttpStatus.NOT_FOUND));
		addressRepository.delete(address);

		SystemCode switchSystemCode = this.systemCodeRepository.findByCodePrefixAndCodeValueAndInstitution_InstitutionId("SWITCH_ENABLED", "SWITCH_ENABLED_FLAG", "SYSTEM")
				.orElseThrow(() -> new BusinessException(ResponseCode.INVALID_SYSTEM_CODE_ID, HttpStatus.NOT_FOUND));
		if(switchSystemCode.getCodeSuffix().equals("1")) {
			this.switchEntityAddressService.deleteEntityAddress(address.getEntitiesObject().getEntityId());
		}
	}

	public List<AddressResponseDto> getAddressByEntitiesId(String id) {

		List<AddressResponseDto> addressResponseDtos = new ArrayList<AddressResponseDto>();
		List<Address> addresses = addressRepository.findAddressByEntityId(id, Sort.by(Sort.Direction.ASC, "addressId"));

		addresses.stream().forEach((address) -> {
			AddressResponseDto addressResponseDto = addressMapper.toDto(address);
			addressResponseDtos.add(addressResponseDto);
		});

		return addressResponseDtos;
	}

	public void saveSwitchMasterAddress(Address address) {
		SwitchMasterAddressRequestDto switchMasterAddressRequestDto = new SwitchMasterAddressRequestDto();

		switchMasterAddressRequestDto.setInstitutionId(address.getInstitution().getInstitutionId());
		switchMasterAddressRequestDto.setAddress1(address.getAddress1());
		switchMasterAddressRequestDto.setAddress2(address.getAddress2());
		switchMasterAddressRequestDto.setAddress3(address.getAddress3());
		switchMasterAddressRequestDto.setAddress4(address.getAddress4());
		switchMasterAddressRequestDto.setEntityId(address.getEntitiesObject().getEntityId());
		switchMasterAddressRequestDto.setPhone1(address.getPhone1());
		switchMasterAddressRequestDto.setPhone2(address.getPhone2());
		switchMasterAddressRequestDto.setGeocode(address.getGeoCode());
		switchMasterAddressRequestDto.setFax(address.getFax());
		switchMasterAddressRequestDto.setUrl(address.getUrl());
		switchMasterAddressRequestDto.setEmailAddress(address.getEmailAddress1());

		if(!Objects.isNull(address.getCityCode())) {
			switchMasterAddressRequestDto.setCity(address.getCityCode().getCityName());
		}

		if(!Objects.isNull(address.getCntryCode())) {
			switchMasterAddressRequestDto.setCntryCode(address.getCntryCode().getCntryCode());
			switchMasterAddressRequestDto.setCountry(address.getCntryCode().getCntryName());
		}

		this.switchMasterAddressService.saveMasterAddress(switchMasterAddressRequestDto);
	}

}