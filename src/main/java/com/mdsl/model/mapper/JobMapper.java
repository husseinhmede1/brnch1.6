package com.mdsl.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.mdsl.model.dto.request.JobRequestDto;
import com.mdsl.model.dto.response.JobResponseDto;
import com.mdsl.model.entity.Job;

@Mapper
public interface JobMapper {

	JobMapper INSTANCE = Mappers.getMapper(JobMapper.class);

	JobResponseDto toDto(Job Job);

	Job toEntity(JobRequestDto JobRequestDto);
	
	Job clone(Job job);
}