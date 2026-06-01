package com.mdsl.model.mapper;

import org.mapstruct.Mapper;

import com.mdsl.model.dto.response.TaskExecutionLogResponseDto;
import com.mdsl.model.entity.TaskExecutionLog;

@Mapper
public interface TaskExecutionLogMapper {
	TaskExecutionLogResponseDto toDto(TaskExecutionLog taskExecutionLog);
}
