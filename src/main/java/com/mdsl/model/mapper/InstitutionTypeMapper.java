package com.mdsl.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mdsl.model.dto.request.InstitutionTypeRequestDto;
import com.mdsl.model.dto.response.InstitutionTypeResponseDto;
import com.mdsl.model.entity.InstitutionType;


@Mapper
public interface InstitutionTypeMapper {
	@Mapping(source="institutionType.institutionType",target="institutionType")
	@Mapping(source="institutionType.institutionTypeId",target="institutionTypeId")
	InstitutionTypeResponseDto toDto(InstitutionType institutionType);
	
	InstitutionType toEntity(InstitutionTypeRequestDto dto);
}
