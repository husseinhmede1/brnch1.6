package com.mdsl.swtch.model.mapper;

import org.mapstruct.Mapper;

import com.mdsl.swtch.model.dto.request.SwitchEntityAddressRequestDto;
import com.mdsl.swtch.model.entity.SwitchEntityAddress;

@Mapper
public interface SwitchEntityAddressMapper {
	SwitchEntityAddress toEntity(SwitchEntityAddressRequestDto entityAddressRequestDto);
}
