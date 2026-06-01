package com.mdsl.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mdsl.model.dto.request.IssuerRequestDto;
import com.mdsl.model.dto.response.IssuerResponseDto;
import com.mdsl.model.entity.IssuerProfile;

@Mapper
public interface IssuerMapper {
	
	IssuerResponseDto toDto(IssuerProfile issuer);
	IssuerProfile toEntity(IssuerRequestDto issuerRequestDto);
}
