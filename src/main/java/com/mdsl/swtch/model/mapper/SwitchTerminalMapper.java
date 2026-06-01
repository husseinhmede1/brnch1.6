package com.mdsl.swtch.model.mapper;

import org.mapstruct.Mapper;

import com.mdsl.swtch.model.dto.request.SwitchTerminalRequestDto;
import com.mdsl.swtch.model.entity.SwitchTerminal;

@Mapper
public interface SwitchTerminalMapper {
	SwitchTerminal toEntity(SwitchTerminalRequestDto terminalRequestDto);
}
