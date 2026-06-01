package com.mdsl.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mdsl.model.dto.request.InstitutionRequestDto;
import com.mdsl.model.dto.response.InstitutionResponseDto;
import com.mdsl.model.dto.response.InstitutionResponseInfoDto;
import com.mdsl.model.entity.Institution;

@Mapper
public interface InstitutionMapper {

	@Mapping(source = "institutionType.systemCodeId", target = "institutionTypeSystemCodeId")
	@Mapping(source = "institutionType.codeSuffix", target = "institutionTypeCodeSuffix")
	@Mapping(source = "institutionType.codePrefix", target = "institutionTypeCodePrefix")
	@Mapping(source = "institutionType.codeValue", target = "institutionTypeCodeValue")
	@Mapping(source = "institutionType.description", target = "institutionTypeCodeDescription")
	InstitutionResponseDto toDto(Institution institution);
	
	InstitutionResponseInfoDto toDtoInfo (Institution institution);
	
	Institution toEntity(InstitutionRequestDto institutionRequestDto );
	
}
