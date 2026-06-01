package com.mdsl.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mdsl.model.dto.request.SystemCodeRequestDto;
import com.mdsl.model.dto.response.SystemCodeResponseDto;
import com.mdsl.model.entity.SystemCode;

@Mapper
public interface SystemCodeMapper {

	@Mapping(source = "institution.institutionId", target = "institutionId")
	@Mapping(source = "institution.institutionName", target = "institutionName")
	@Mapping(source = "systemCodeHeader.systemCodeHeaderId", target = "systemCodeHeaderId")
	SystemCodeResponseDto toDto(SystemCode systemCode);
	
	SystemCode toEntity(SystemCodeRequestDto systemCodeRequestDto);
}
