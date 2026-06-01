package com.mdsl.model.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageableTaskExecutionLogResponseDto {
	private List<TaskExecutionLogResponseDto> taskExecutionLogResponseDto;
	private PaginatedResponseDto paginatedResponseDto;
}
