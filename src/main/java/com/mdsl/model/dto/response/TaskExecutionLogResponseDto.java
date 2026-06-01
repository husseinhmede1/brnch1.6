package com.mdsl.model.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskExecutionLogResponseDto {
	private Integer taskExecutionLogId;
	private Integer taskId;
	private String taskDetails;
	private String parameter;
	private String startDatetime;
	private String endDatetime;
	private String userName;
}