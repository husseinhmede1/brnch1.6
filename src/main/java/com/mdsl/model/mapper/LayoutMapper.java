package com.mdsl.model.mapper;

import com.mdsl.model.dto.request.LayoutRequestDto;
import com.mdsl.model.dto.response.LayoutResponseDto;
import com.mdsl.model.entity.Layout;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface LayoutMapper {
    Layout toEntity(LayoutRequestDto layoutRequestDto);
    LayoutResponseDto toDto(Layout layout);
}
