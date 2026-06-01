package com.mdsl.model.mapper;

import org.mapstruct.Mapper;

import com.mdsl.model.dto.request.OutputFileTemplateHdrRequestDto;
import com.mdsl.model.dto.response.OutputFileTemplateHdrResponseDto;
import com.mdsl.model.entity.OutputFileTemplateHdr;

@Mapper
public interface OutputFileTemplateHdrMapper {
  OutputFileTemplateHdrResponseDto toDto(OutputFileTemplateHdr outputFileTemplateHdr);
  OutputFileTemplateHdr toEntity(OutputFileTemplateHdrRequestDto outputFileTemplateHdrRequestDto);
}
