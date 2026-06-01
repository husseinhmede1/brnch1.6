package com.mdsl.model.dto.response;


import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class JobScheduledMonitoringResponseDto {
	private Integer jobId;
	private String jobName; 
	private Date startTime;
	private String lastRunStatus;
	private Date lastRunStart;
	private Date lastRunEnd;
	private String status;
	//private String jobExecutionDetails;
}