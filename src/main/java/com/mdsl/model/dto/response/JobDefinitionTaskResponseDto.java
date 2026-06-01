package com.mdsl.model.dto.response;


import java.util.List;

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
@JsonInclude(Include.NON_DEFAULT)
public class JobDefinitionTaskResponseDto {
	private Integer jobTaskId;
	private Integer jobId;
	private String priority;
	private List<JobTaskParametersResponseDto> jobTaskParametersResponseDto;
	private JobTaskResponseDto task;
}
