package com.mdsl.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NonActivityPackageResponseDto {
	private String packageId;
	private String packageName;
	private String institutionId;
	private String institutionName;
	private char status;
}