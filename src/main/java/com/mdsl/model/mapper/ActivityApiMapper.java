package com.mdsl.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mdsl.model.dto.response.ActivityApiResponseDto;
import com.mdsl.model.entity.ActivityApi;

@Mapper
public interface ActivityApiMapper {
	ActivityApiResponseDto toDto (ActivityApi activityApi); 
}