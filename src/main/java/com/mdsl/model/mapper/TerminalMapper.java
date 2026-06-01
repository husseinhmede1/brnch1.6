package com.mdsl.model.mapper;

import com.mdsl.utils.DateMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mdsl.model.dto.request.TerminalRequestDto;
import com.mdsl.model.dto.response.TerminalResponseDto;
import com.mdsl.model.entity.Terminal;

@Mapper(uses = DateMapper.class)
public interface TerminalMapper {

	
	@Mapping(source="institutionEntity.institutionId",target="institutionId")
	@Mapping(source="institutionEntity.institutionName",target="institutionName")
	@Mapping(source="terminalTypes.terminalTypesId",target="terminalTypesId")
	@Mapping(source="terminalTypes.terminalType",target="terminalTypes")
	@Mapping(source="currency.currencyId",target="currencyId")
	@Mapping(source="currency.currencyCode",target="currencyCode")
	@Mapping(source="currency.currencyName",target="currencyName")
	@Mapping(source="entitiesObject.entityId",target="entityId")
	@Mapping(source="entitiesObject.entityName",target="entityName")
	TerminalResponseDto toResponseDto(Terminal terminal);
	
	@Mapping(source="institutionEntity.institutionId",target="institutionId")
	@Mapping(source="terminalTypes.terminalTypesId",target="terminalTypeId")
	@Mapping(source="currency.currencyId",target="currencyId")
	@Mapping(source="entitiesObject.entityId",target="entityId")
	TerminalRequestDto toRequestDto(Terminal terminal);
	
	@Mapping(source="institutionId",target="institutionEntity.institutionId")
	@Mapping(source="terminalTypeId",target="terminalTypes.terminalTypesId")
	@Mapping(source="currencyId",target="currency.currencyId")
	@Mapping(source="entityId",target="entitiesObject.entityId")
	@Mapping(source="actualStartDate",target="actualStartDate",qualifiedByName = "stringToDate")
	@Mapping(source="terminationDate",target="terminationDate",qualifiedByName = "stringToDate")
	Terminal toEntity(TerminalRequestDto toDto);
}
