package com.mdsl.model.mapper;

import org.mapstruct.Mapper;

import com.mdsl.model.dto.request.RoleActivityRequestDto;
import com.mdsl.model.dto.response.RoleActivityResponseDto;
import com.mdsl.model.entity.RoleActivity;

@Mapper
public interface RoleActivityMapper {
	RoleActivityResponseDto toDto (RoleActivity roleActivity);
	RoleActivity toEntity (RoleActivityRequestDto roleActivityRequestDto); 
}