package com.mdsl.model.mapper;

import org.mapstruct.Mapper;

import com.mdsl.model.dto.request.SchemeSpecificRequestDto;
import com.mdsl.model.dto.response.SchemeSpecificResponseDto;
import com.mdsl.model.entity.SchemeSpecific;

@Mapper
public interface SchemeSpecificMapper {
	SchemeSpecificResponseDto toDto(SchemeSpecific schemeSpecific);
	SchemeSpecific toEntity(SchemeSpecificRequestDto schemeSpecificRequestDto);
}
