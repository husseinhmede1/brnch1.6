package com.mdsl.model.mapper;

import org.mapstruct.Mapper;

import com.mdsl.model.dto.response.TerminalTypeResponseDto;
import com.mdsl.model.dto.response.TerminalTypesDto;
import com.mdsl.model.entity.TerminalTypes;

@Mapper
public interface TerminalTypeMapper {
	 
	TerminalTypesDto toDto(TerminalTypes terminalTypes);
	TerminalTypes toEntity(TerminalTypesDto terminalTypesDto);
	TerminalTypeResponseDto toResponseDto(TerminalTypes terminalTypes);
}
