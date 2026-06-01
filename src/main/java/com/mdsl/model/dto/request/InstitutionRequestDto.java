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
public class InstitutionRequestDto {
	
	@Size(min=0, max=10, message=ResponseCode.INVALID_INSTITUTION_ID)
	@Size(min=0, max=10, message=ResponseCode.CFG_INVALID_INSTITUTION_ID)
	private String institutionId;
	
	@Min(value = 1,  message=ResponseCode.INT_INVALID_INSTITUTION_TYPE_ID)
	@Min(value = 1,  message=ResponseCode.INT_INVALID_INSTITUTION_TYPE_ID)
	private int institutionTypeId;
	
	@Size(min=1, max=50, message=ResponseCode.INT_INVALID_INSTITUTION_NAME)
	@Size(min=1, max=50, message=ResponseCode.INT_INVALID_INSTITUTION_NAME)
	private String institutionName;
	
	@Size(min=0, max=100, message=ResponseCode.INT_INVALID_INSTITUTION_TYPE_ALT)
	@Size(min=0, max=100, message=ResponseCode.INT_INVALID_INSTITUTION_TYPE_ALT)
	private String institutionTypeAlt;
	
	private Character status;
	
	private char updateFlag; 
}
