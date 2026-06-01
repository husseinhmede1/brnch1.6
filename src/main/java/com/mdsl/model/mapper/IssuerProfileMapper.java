package com.mdsl.model.mapper;

import org.mapstruct.Mapper;

import com.mdsl.model.dto.request.IssuerProfileRequestDto;
import com.mdsl.model.dto.response.IssuerProfileResponseDto;
import com.mdsl.model.entity.IssuerProfile;

@Mapper
public interface IssuerProfileMapper {
	IssuerProfileResponseDto toDto(IssuerProfile issuerProfile);
	IssuerProfile toEntity(IssuerProfileRequestDto issuerProfileRequestDto);
}
