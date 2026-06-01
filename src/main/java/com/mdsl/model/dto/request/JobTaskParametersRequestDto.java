package com.mdsl.model.dto.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import com.mdsl.utils.ResponseCode;

import lombok.Getter;

@Getter
public class JobTaskParametersRequestDto {
	@Min(value=0, message=ResponseCode.CFG_INVALID_SERVICE_ID)
	@Max(value=999999999, message=ResponseCode.CFG_INVALID_SERVICE_ID)
	public Integer parametersServiceId;
	
	@Size(min=0, max=100, message=ResponseCode.CFG_INVALID_JOB_TASK_PARAMETER_VALUE)
//	@Pattern(
//	    regexp="^$|^[a-zA-Z0-9 _/:\\\\]*$|^(0[1-9]|1[0-2])-(19|20)\\d{2}$|^(0[1-9]|1[0-2])-(\\d{2})-(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)-(\\d{2})$", 
//	    message=ResponseCode.CFG_INVALID_JOB_TASK_PARAMETER_VALUE
//	)
	public String parameterValue;

}