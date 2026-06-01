package com.mdsl.model.mapper;

import org.mapstruct.Mapper;

import com.mdsl.model.dto.response.FileElementResponseDto;
import com.mdsl.model.entity.FileElement;

@Mapper
public interface FileElementMapper {
	FileElementResponseDto toDto(FileElement Element, String fileName); 
}