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
@JsonInclude(Include.NON_EMPTY)
public class JobTaskResponseDto {
	private Integer taskId;
	private String taskName;
	private String taskDescription;
	private String serviceMode;
	private List<BKDParameterResponseDto> parameterResponseDto;
}
