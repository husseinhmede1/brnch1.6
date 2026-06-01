package com.mdsl.swtch.service;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.swtch.model.dto.request.SwitchEntityAddressRequestDto;
import com.mdsl.swtch.model.entity.SwitchEntityAddress;
import com.mdsl.swtch.model.mapper.SwitchEntityAddressMapper;
import com.mdsl.swtch.repository.SwitchEntityAddressRepository;
import com.mdsl.utils.ResponseCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SwitchEntityAddressService {
	
	private final SwitchEntityAddressRepository entityAddressRepository;
	
	private final SwitchEntityAddressMapper entityAddressMapper;
	
	private final SwitchMasterAddressService masterAddressService;
	
	public void saveEntityAddress(SwitchEntityAddressRequestDto entityAddressRequestDto) {
		SwitchEntityAddress entityAddress = this.entityAddressMapper.toEntity(entityAddressRequestDto);
		entityAddress.setAddressRole("POS");
		entityAddress.setEffectiveDate(new Date(System.currentTimeMillis()));
		this.entityAddressRepository.save(entityAddress);
	}
	
	public void deleteEntityAddress(String entityId) {
		SwitchEntityAddress entityAddress = this.entityAddressRepository.findById(entityId)
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_ENTITY_ID, HttpStatus.NOT_FOUND));
		this.entityAddressRepository.delete(entityAddress);
		
		this.masterAddressService.deleteMasterAddress(entityAddress.getAddressId(), entityAddress.getInstitutionId());
		
	}

}
