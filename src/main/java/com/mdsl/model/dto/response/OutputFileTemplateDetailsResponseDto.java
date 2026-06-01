package com.mdsl.model.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OutputFileTemplateDetailsResponseDto {
  private Integer outputTemplateDtlId;
  private String institutionId;
  private Integer outputTemplateHdrId;
  private String fieldSection;
  private Integer fieldSequence;
  private Integer fieldId;
  private String fieldPad;
  private String fieldPadChar;
  private Integer fieldLength;
  private String description;
  private String fieldFormat;
  private String fieldCsyntax;
}