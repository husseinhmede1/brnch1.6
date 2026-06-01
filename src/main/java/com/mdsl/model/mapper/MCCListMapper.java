package com.mdsl.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mdsl.model.dto.request.MCCListRequestDto;
import com.mdsl.model.dto.response.MCCListResponseDto;
import com.mdsl.model.entity.MCCList;

@Mapper
public interface MCCListMapper {

	@Mapping(source = "cardSchemeTypeMapping.cardSchemeId", target = "cardSchemeId")
	@Mapping(source = "cardSchemeTypeMapping.cardSchemeName", target = "cardSchemeName")
	@Mapping(source = "merchantType.systemCodeId", target = "merchantTypeSystemCodeId")
	@Mapping(source = "merchantType.codeSuffix", target = "merchantTypeCodeSuffix")
	@Mapping(source = "merchantType.codePrefix", target = "merchantTypeCodePrefix")
	@Mapping(source = "merchantType.codeValue", target = "merchantTypeCodeValue")
	@Mapping(source = "merchantType.description", target = "merchantTypeCodeDescription")
	MCCListResponseDto toDto(MCCList mccList);

	MCCList toEntity(MCCListRequestDto dto);

}
