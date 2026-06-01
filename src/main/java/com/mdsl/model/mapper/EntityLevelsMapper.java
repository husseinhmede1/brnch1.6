package com.mdsl.model.mapper;

import org.mapstruct.Mapper;

import com.mdsl.model.dto.request.EntityLevelsRequestDto;
import com.mdsl.model.dto.response.EntityLevelsResponseDto;
import com.mdsl.model.entity.EntityLevels;

@Mapper
public interface EntityLevelsMapper {
	EntityLevelsResponseDto toDto(EntityLevels entityLevels);
	
	EntityLevels toEntity(EntityLevelsRequestDto entityLevelsRequestDto);
	
	EntityLevels clone (EntityLevels entityLevels); 
}
