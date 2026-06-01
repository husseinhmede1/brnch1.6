package com.mdsl.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mdsl.model.dto.request.CardSchemeRequestDto;
import com.mdsl.model.dto.response.CardSchemeResponseDto;
import com.mdsl.model.entity.CardScheme;

@Mapper
public interface CardSchemeMapper {

	 CardSchemeResponseDto toDto(CardScheme cardScheme);
	 CardScheme toEntity(CardSchemeRequestDto cardSchemeRequestDto);
}
