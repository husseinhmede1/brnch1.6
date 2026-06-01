package com.mdsl.model.mapper;

import org.mapstruct.Mapper;

import com.mdsl.model.dto.response.BusinessTypeResponseDto;
import com.mdsl.model.entity.BusinessType;

@Mapper
public interface BusinessTypeMapper {
	BusinessTypeResponseDto toDto(BusinessType businessType);
}
