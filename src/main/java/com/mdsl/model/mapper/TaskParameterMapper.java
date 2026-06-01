package com.mdsl.model.mapper;

import org.mapstruct.Mapper;

import com.mdsl.model.dto.response.TaskParameterResponseDto;
import com.mdsl.model.entity.TaskParameter;

@Mapper
public interface TaskParameterMapper {
	TaskParameterResponseDto toDto(TaskParameter taskParameter);
}
