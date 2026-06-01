package com.mdsl.model.mapper;

import org.mapstruct.Mapper;

import com.mdsl.model.dto.response.RoleMasterResponseDto;
import com.mdsl.model.entity.RoleMaster;

@Mapper
public interface RoleMasterMapper {
	RoleMasterResponseDto toDto(RoleMaster roleMaster);
}