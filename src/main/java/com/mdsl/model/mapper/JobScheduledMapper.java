package com.mdsl.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.mdsl.model.dto.request.JobScheduledRequestDto;
import com.mdsl.model.dto.response.JobScheduledResponseDto;
import com.mdsl.model.entity.JobScheduled;

@Mapper
public interface JobScheduledMapper {
	JobScheduledMapper INSTANCE = Mappers.getMapper(JobScheduledMapper.class);
	
//	@Mapping(source="job.jobId", target="jobId")
//	@Mapping(source="job.jobName", target="jobName")
	JobScheduledResponseDto toDto (JobScheduled JobScheduled);
	
	JobScheduled toEntity (JobScheduledRequestDto JobScheduledRequestDto); 
}