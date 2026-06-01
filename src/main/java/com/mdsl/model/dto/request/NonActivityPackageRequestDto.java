package com.mdsl.model.dto.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.mdsl.utils.ResponseCode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NonActivityPackageRequestDto {
	
	@Size(min=0, max=6, message=ResponseCode.CFG_INVALID_ACT_PKG_ID)
	private String packageId;

	@Size (min=1, max = 50, message = ResponseCode.CFG_INVALID_PKG_LENGTH)
	private String packageName;

	@NotEmpty(message = ResponseCode.CFG_INVALID_INSTITUTION_ID)
	private String institutionId;
	
	private char updateFlag;
}
