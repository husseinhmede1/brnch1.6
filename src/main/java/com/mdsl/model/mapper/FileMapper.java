package com.mdsl.model.mapper;

import org.mapstruct.Mapper;

import com.mdsl.model.dto.response.FileResponseDto;
import com.mdsl.model.entity.File;

@Mapper
public interface FileMapper {
	FileResponseDto toDto(File file); 
}