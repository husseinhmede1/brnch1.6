package com.mdsl.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityPackageResponseDto {
	private Integer recordSeqId;

	private String packageId;
	
	private String packageName;
	
	private String institutionId;
	
	private String institutionName;

	private char status;
}
