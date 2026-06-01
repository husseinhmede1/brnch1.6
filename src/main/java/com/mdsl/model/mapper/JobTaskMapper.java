package com.mdsl.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.mdsl.model.dto.request.JobTaskRequestDto;
import com.mdsl.model.dto.response.JobTaskResponseDto;
import com.mdsl.model.entity.JobTask;

@Mapper
public interface JobTaskMapper {

	JobTaskMapper INSTANCE = Mappers.getMapper(JobTaskMapper.class);

//	@Mapping(target="serviceId", source="service.serviceId")
//	@Mapping(target="serviceCode", source="service.serviceCode")
//	@Mapping(target="serviceDesc", source="service.serviceDesc")
	JobTaskResponseDto toDto(JobTask jobTask);

	JobTask toEntity(JobTaskRequestDto jobTaskRequestDto);
}