package com.mdsl.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mdsl.model.dto.request.BankCodeRequestDto;
import com.mdsl.model.dto.response.BankCodeResponseDto;
import com.mdsl.model.entity.BankCode;

@Mapper
public interface BankCodeMapper {
	
	BankCodeResponseDto toDto(BankCode bankCode);
	BankCode toEntity(BankCodeRequestDto bankCodeRequestDto);
}
