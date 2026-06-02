package com.mdsl.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mdsl.model.dto.response.BusinessTypeResponseDto;
import com.mdsl.model.entity.BusinessType;
import com.mdsl.model.mapper.BusinessTypeMapper;
import com.mdsl.repository.BusinessTypeRepository;
import com.mdsl.utils.MakerCheckerEngine;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BusinessTypeServices {
	@Autowired
	private BusinessTypeRepository businessTypeRepository;
	@Autowired
	private BusinessTypeMapper businessTypeMapper;
    private final MakerCheckerEngine makerCheckerEngine;

	public List<BusinessTypeResponseDto> getAllBusinessType() {
		List<BusinessType> allBusinessType = businessTypeRepository.findAll();
		List<BusinessTypeResponseDto> allBusinessTypeResponseDto = new ArrayList<BusinessTypeResponseDto>();
		for (BusinessType businessType : allBusinessType) {
			BusinessTypeResponseDto businessTypeResponseDto = businessTypeMapper.toDto(businessType);
			allBusinessTypeResponseDto.add(businessTypeResponseDto);
		}
		return allBusinessTypeResponseDto;
	}
	
	public BusinessTypeResponseDto saveBusinessType(BusinessType businessType) {
		if (makerCheckerEngine.processIfRequired(businessType, BusinessTypeServices.class.getName(), "saveBusinessType", "")){
			return null;
		}
		businessTypeRepository.save(businessType);
		return businessTypeMapper.toDto(businessType);
	}
	


}
