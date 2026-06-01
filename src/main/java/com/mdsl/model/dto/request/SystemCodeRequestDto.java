package com.mdsl.model.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.mdsl.utils.ResponseCode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemCodeRequestDto {
	
	private int systemCodeId;
	
	@Size(max = 18, message = ResponseCode.INVALID_SYSTEM_CODE_DETAILS)
	private String codeSuffix;

	@Size(max = 30, message = ResponseCode.SYS_INVALID_CODE_PREFIX)
	private String codePrefix;
	
	private int systemCodeHeaderId;

	private String institutionId;
	
	@Size(max = 100, message = ResponseCode.CFG_INVALID_CODE_VALUE)
	private String codeValue;
	
	@Size(max = 100, message = ResponseCode.SYS_INVALID_CODE_DESCRIPTION)
	private String description;
	
	private char status;
}