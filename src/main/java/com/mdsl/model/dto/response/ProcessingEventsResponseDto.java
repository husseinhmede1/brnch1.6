package com.mdsl.model.dto.response;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessingEventsResponseDto {
	private Integer processingEventsId;
	private Integer taskExecutionLogId;
	private String institutionId;
	private String processingProgram;
	private String fileName;
	private Timestamp executionTime;
	private String successResult;
	private String remarks;
}