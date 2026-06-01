package com.mdsl.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.mdsl.model.dto.request.JobDefinitionTaskRequestDto;
import com.mdsl.model.dto.response.JobDefinitionTaskResponseDto;
import com.mdsl.model.entity.JobDefinitionTask;

@Mapper
public interface JobDefinitionTaskMapper {

	JobDefinitionTaskMapper INSTANCE = Mappers.getMapper(JobDefinitionTaskMapper.class);

	JobDefinitionTaskResponseDto toDto(JobDefinitionTask Job);

//	@Mapping(source="taskId", target="task.taskId")
	JobDefinitionTask toEntity(JobDefinitionTaskRequestDto JobRequestDto);
}