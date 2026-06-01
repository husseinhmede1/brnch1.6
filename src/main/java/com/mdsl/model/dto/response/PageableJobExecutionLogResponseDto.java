package com.mdsl.model.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_DEFAULT)
public class PageableJobExecutionLogResponseDto {
	
	private List<JobExecutionLogResponseDto> jobExecutionLogResponseDto;
	private PaginationCommonResponseDto paginationCommonResponseDto;
}
