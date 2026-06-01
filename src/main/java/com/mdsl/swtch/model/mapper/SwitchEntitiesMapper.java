package com.mdsl.swtch.model.mapper;

import org.mapstruct.Mapper;

import com.mdsl.swtch.model.dto.request.SwitchEntitiesRequestDto;
import com.mdsl.swtch.model.entity.SwitchEntities;

@Mapper
public interface SwitchEntitiesMapper {
	SwitchEntities toEntity(SwitchEntitiesRequestDto entitiesRequestDto);
}
