package com.mdsl.swtch.model.mapper;

import org.mapstruct.Mapper;

import com.mdsl.swtch.model.dto.request.SwitchMasterAddressRequestDto;
import com.mdsl.swtch.model.entity.SwitchMasterAddress;

@Mapper
public interface SwitchMasterAddressMapper {
	SwitchMasterAddress toEntity(SwitchMasterAddressRequestDto masterAddressRequestDto);
}
