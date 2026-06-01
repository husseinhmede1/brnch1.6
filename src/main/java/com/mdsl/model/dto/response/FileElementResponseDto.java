package com.mdsl.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileElementResponseDto {
	private Integer elementId;
	private String elementName;
	private String elementSection;
	private String isRepeated;
	private String validationRequired;
	private String isMandatory;
	private Integer validationLength;
	private String validationFormat;
}