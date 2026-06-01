package com.mdsl.model.mapper;

import org.mapstruct.Mapper;

import com.mdsl.model.dto.request.OutputFileTemplateDetailsRequestDto;
import com.mdsl.model.dto.response.OutputFileTemplateDetailsResponseDto;
import com.mdsl.model.entity.OutputFileTemplateDetails;

@Mapper
public interface OutputFileTemplateDetailsMapper {
	OutputFileTemplateDetailsResponseDto toDto(OutputFileTemplateDetails outputFileTemplateDetails);
	OutputFileTemplateDetails toEntity(OutputFileTemplateDetailsRequestDto outputFileTemplateDetailsRequestDto);
}
