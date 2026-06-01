package com.mdsl.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.mdsl.model.dto.request.JobExecutionLogRequestDto;
import com.mdsl.model.dto.response.JobExecutionLogResponseDto;
import com.mdsl.model.entity.JobExecutionLog;

@Mapper
public interface JobExecutionLogMapper {

	JobExecutionLogMapper INSTANCE = Mappers.getMapper(JobExecutionLogMapper.class);
	
	@Mapping(source="job.jobId", target="jobId")
	@Mapping(source="job.jobName", target="jobName")
	JobExecutionLogResponseDto toDto (JobExecutionLog JobExecutionLog);
	
	JobExecutionLog toEntity (JobExecutionLogRequestDto JobExecutionLogRequestDto); 
}