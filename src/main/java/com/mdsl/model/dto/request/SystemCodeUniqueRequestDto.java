package com.mdsl.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SystemCodeUniqueRequestDto {
	private String codePrefix;
	private String codeSuffix;
	private String institutionId;
}