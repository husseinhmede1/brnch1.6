package com.mdsl.model.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskParameterResponseDto {
	private Integer taskParamId;
	private Integer taskId;
	private String parameter;
	private Character validity;
	private String condition;
	private String parameterType;
	private Integer sequence;
}