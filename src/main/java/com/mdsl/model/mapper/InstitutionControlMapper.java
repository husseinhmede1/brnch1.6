package com.mdsl.model.mapper;

import org.mapstruct.Mapper;

import com.mdsl.model.dto.request.InstitutionControlRequestDto;
import com.mdsl.model.dto.response.InstitutionControlResponseDto;
import com.mdsl.model.entity.InstitutionControl;

@Mapper
public interface InstitutionControlMapper {
	InstitutionControlResponseDto toDto(InstitutionControl institutionControl);
	InstitutionControl toEntity(InstitutionControlRequestDto institutionControlRequestDto);
}
