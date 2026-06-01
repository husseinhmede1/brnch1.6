package com.mdsl.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemCodeResponseDto {
	
	private int systemCodeId;
	private String codeSuffix;
	private String codePrefix;
	private String codeValue;
	private String description;
	private String institutionId;
	private String institutionName;
	private int systemCodeHeaderId;
	private Character status;

}
