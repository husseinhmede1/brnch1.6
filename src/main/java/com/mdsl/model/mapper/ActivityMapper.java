package com.mdsl.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mdsl.model.dto.response.ActivityResponseDto;
import com.mdsl.model.entity.Activity;

@Mapper
public interface ActivityMapper {
	ActivityResponseDto toDto (Activity activity); 
}