package com.mdsl.model.dto.request;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.mdsl.utils.ResponseCode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class 	JobDefinitionTaskRequestDto {
	@Min(value=0, message=ResponseCode.CFG_INVALID_TASK_ID)
	@Max(value=9999, message=ResponseCode.CFG_INVALID_TASK_ID)
	private Integer taskId;

	@Min(value=0, message=ResponseCode.CFG_INVALID_PRIORITY)
	@Max(value=999, message=ResponseCode.CFG_INVALID_PRIORITY)
	private Integer priority;
	
	@Valid
	private List<JobTaskParametersRequestDto> jobTaskParamtersRequestDto;
}