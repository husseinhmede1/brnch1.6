package com.mdsl.model.dto.response;


import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_EMPTY)
public class JobResponseDto {
	private Integer jobId;
	private String jobName;
	private String jobDescription;
	private String enabled;
	private Integer instId;
	private String alertSuccess;
	private String successEmail;
	private String alertFailure;
	private String failEmail;
	private Integer lastExecId;
	private String lastRunResult;
	private Integer frequency;
	private String startDate;
	private String endDate;
	private String recurring;
	private Integer recurringFreq;
	private Integer monthDay;
	private Integer maxExceTime;
	private String status;
	Set<JobDefinitionTaskResponseDto> jobDefinitionTask;
}
