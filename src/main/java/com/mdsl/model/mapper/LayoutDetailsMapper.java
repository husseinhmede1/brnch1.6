package com.mdsl.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mdsl.model.dto.request.LayoutDetailsRequestDto;
import com.mdsl.model.dto.response.LayoutDetailsResponseDto;
import com.mdsl.model.entity.LayoutDetails;

@Mapper
public interface LayoutDetailsMapper {
    LayoutDetailsResponseDto toDto(LayoutDetails layoutDetails);

    @Mapping(source="paddingType", target="elementPaddingType")
    @Mapping(source="paddingValue", target="elementPaddingValue")

    LayoutDetails toEntity(LayoutDetailsRequestDto layoutDetailsRequestDto);
}