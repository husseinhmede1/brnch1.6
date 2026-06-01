package com.mdsl.model.mapper;

import org.mapstruct.Mapper;

import com.mdsl.model.dto.response.TaskResponseDto;
import com.mdsl.model.entity.Task;

@Mapper
public interface TaskMapper {
	TaskResponseDto toDto(Task task);
}
