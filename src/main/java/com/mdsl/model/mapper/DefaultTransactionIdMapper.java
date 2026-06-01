package com.mdsl.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mdsl.model.dto.request.DefaultTransactionIdRequestDto;
import com.mdsl.model.dto.response.DefaultTransactionIdResponseDto;
import com.mdsl.model.entity.DefaultTransactionId;

@Mapper
public interface DefaultTransactionIdMapper {

	@Mapping(source="institution.institutionId",target="institutionId")
	@Mapping(source="institution.institutionName",target="institutionName")
	DefaultTransactionIdResponseDto toDto(DefaultTransactionId defaultTransactionId);
	
	@Mapping(source="institutionId",target="institution.institutionId")
	DefaultTransactionId toEntity(DefaultTransactionIdRequestDto defauTransactionIdRequestDto);
}
