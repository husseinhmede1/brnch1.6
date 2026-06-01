package com.mdsl.swtch.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.swtch.model.dto.request.SwitchEntityAddressRequestDto;
import com.mdsl.swtch.model.dto.request.SwitchMasterAddressRequestDto;
import com.mdsl.swtch.model.entity.SwitchEntityAddress;
import com.mdsl.swtch.model.entity.SwitchMasterAddress;
import com.mdsl.swtch.model.entity.SwitchSeqCtrl;
import com.mdsl.swtch.model.mapper.SwitchEntityAddressMapper;
import com.mdsl.swtch.model.mapper.SwitchMasterAddressMapper;
import com.mdsl.swtch.repository.SwitchEntityAddressRepository;
import com.mdsl.swtch.repository.SwitchMasterAddressRepository;
import com.mdsl.swtch.repository.SwitchSeqCtrlRepository;
import com.mdsl.utils.ResponseCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SwitchMasterAddressService {
	
	private final SwitchMasterAddressRepository masterAddressRepository;
	private final SwitchSeqCtrlRepository seqCtrlRepository;
	private final SwitchEntityAddressRepository entityAddressRepository;
	
	private final SwitchMasterAddressMapper masterAddressMapper;
	private final SwitchEntityAddressMapper entityAddressMapper;
	
	public void saveMasterAddress(SwitchMasterAddressRequestDto masterAddressRequestDto) {
		Optional<SwitchEntityAddress> optEntityAddress = this.entityAddressRepository.findById(masterAddressRequestDto.getEntityId());
		
		if(optEntityAddress.isEmpty()) {	//add
			SwitchMasterAddress masterAddress = this.masterAddressMapper.toEntity(masterAddressRequestDto);
		    SwitchSeqCtrl seqCtrl = this.seqCtrlRepository.findBySeqNameAndInstitutionId("address_id", masterAddressRequestDto.getInstitutionId())
		    		.orElseThrow(() -> new BusinessException(ResponseCode.CFG_MASTER_ADDRESS_NOT_FOUND, HttpStatus.NOT_FOUND));
		    
		    seqCtrl.setLastSeqNbr(seqCtrl.getLastSeqNbr() + 1);

		    masterAddress.setAddressId(seqCtrl.getLastSeqNbr());
		    masterAddress.setCountry(masterAddressRequestDto.getCntryCode());
		    masterAddress.setGeocode(masterAddressRequestDto.getCntryCode());
		    
		    SwitchMasterAddress savedMasterAddress = this.masterAddressRepository.save(masterAddress);

		    this.seqCtrlRepository.save(seqCtrl);
			
			SwitchEntityAddressRequestDto switchEntityAddressRequestDto = new SwitchEntityAddressRequestDto();
			switchEntityAddressRequestDto.setAddressId(savedMasterAddress.getAddressId());
			switchEntityAddressRequestDto.setEntityId(masterAddressRequestDto.getEntityId());
			switchEntityAddressRequestDto.setInstitutionId(masterAddressRequestDto.getInstitutionId());

			SwitchEntityAddress entityAddress = this.entityAddressMapper.toEntity(switchEntityAddressRequestDto);
			entityAddress.setAddressRole("POS");
			entityAddress.setEffectiveDate(new Date(System.currentTimeMillis()));
			this.entityAddressRepository.save(entityAddress);
		}
		else {	//update

			SwitchMasterAddress masterAddress = this.masterAddressRepository.findByAddressIdAndInstitutionId(optEntityAddress.get().getAddressId(), masterAddressRequestDto.getInstitutionId())
					.orElseThrow(() -> new BusinessException(ResponseCode.CFG_MASTER_ADDRESS_NOT_FOUND, HttpStatus.NOT_FOUND));

			masterAddress.setAddress1(masterAddressRequestDto.getAddress1());
			masterAddress.setAddress2(masterAddressRequestDto.getAddress2());
			masterAddress.setAddress3(masterAddressRequestDto.getAddress3());
			masterAddress.setAddress4(masterAddressRequestDto.getAddress4());
			masterAddress.setInstitutionId(masterAddressRequestDto.getInstitutionId());
			masterAddress.setCity(masterAddressRequestDto.getCity());
			masterAddress.setPhone1(masterAddressRequestDto.getPhone1());
			masterAddress.setPhone2(masterAddressRequestDto.getPhone2());
			masterAddress.setCntryCode(masterAddressRequestDto.getCntryCode());
		    masterAddress.setCountry(masterAddressRequestDto.getCntryCode());
		    masterAddress.setGeocode(masterAddressRequestDto.getCntryCode());
		    masterAddress.setFax(masterAddressRequestDto.getFax());
		    masterAddress.setUrl(masterAddressRequestDto.getUrl());
		    masterAddress.setEmailAddress(masterAddressRequestDto.getEmailAddress());
		    
		    SwitchMasterAddress savedMasterAddress = this.masterAddressRepository.save(masterAddress);
			
			SwitchEntityAddressRequestDto switchEntityAddressRequestDto = new SwitchEntityAddressRequestDto();
			switchEntityAddressRequestDto.setAddressId(savedMasterAddress.getAddressId());
			switchEntityAddressRequestDto.setEntityId(masterAddressRequestDto.getEntityId());
			switchEntityAddressRequestDto.setInstitutionId(masterAddressRequestDto.getInstitutionId());

			SwitchEntityAddress entityAddress = this.entityAddressMapper.toEntity(switchEntityAddressRequestDto);
			entityAddress.setAddressRole("POS");
			entityAddress.setEffectiveDate(new Date(System.currentTimeMillis()));
			this.entityAddressRepository.save(entityAddress);
		}
		
	}
	
	public void deleteMasterAddress(int addressId, String instId) {
		SwitchMasterAddress masterAddress = this.masterAddressRepository.findByAddressIdAndInstitutionId(addressId, instId)
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_MASTER_ADDRESS_NOT_FOUND, HttpStatus.NOT_FOUND));
		this.masterAddressRepository.delete(masterAddress);
	}

}
