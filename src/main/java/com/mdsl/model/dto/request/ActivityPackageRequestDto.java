package com.mdsl.model.dto.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import com.mdsl.utils.ResponseCode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityPackageRequestDto {
	
	@Size(min=0, max=6, message=ResponseCode.CFG_INVALID_ACT_PKG_ID)
	private String packageId;
	
	@Size(min=1, max=50, message=ResponseCode.CFG_INVALID_ACT_PKG_NAME)
	private String packageName;
	
	@Size(min=1, max=10,  message=ResponseCode.CFG_INVALID_INST)
	private String institutionId;
	
	private char updateFlag;
}
