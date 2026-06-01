package com.mdsl.model.mapper;

import org.mapstruct.Mapper;

import com.mdsl.model.dto.request.ChargeTypeMasterRequestDto;
import com.mdsl.model.dto.response.ChargeTypeMasterResponseDto;
import com.mdsl.model.entity.ChargeTypeMaster;

@Mapper
public interface ChargeTypeMasterMapper {

	ChargeTypeMasterResponseDto toDto(ChargeTypeMaster chargeTypeMaster);
	ChargeTypeMaster toEntity(ChargeTypeMasterRequestDto chargeTypeMasterRequestDto);
	
}
