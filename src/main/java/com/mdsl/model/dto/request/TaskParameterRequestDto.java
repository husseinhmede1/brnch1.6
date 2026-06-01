package com.mdsl.model.dto.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.mdsl.utils.ResponseCode;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskParameterRequestDto {
	
	@NotNull(message=ResponseCode.TSK_INVALID_TASK_PARAMETER)
	@Min(value = 0, message = ResponseCode.TSK_INVALID_TASK_PARAMETER)
	@Max(value = 999999999, message = ResponseCode.TSK_INVALID_TASK_PARAMETER)
	private Integer taskParamId;
	
	private String parameterValue;

}
