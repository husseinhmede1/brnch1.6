package com.mdsl.model.mapper;

import org.mapstruct.Mapper;

import com.mdsl.model.dto.response.ProcessingEventsResponseDto;
import com.mdsl.model.entity.ProcessingEvents;

@Mapper
public interface ProcessingEventsMapper {
	ProcessingEventsResponseDto toDto(ProcessingEvents processingEvents);
	ProcessingEvents toEntity(ProcessingEventsResponseDto processingEventsResponseDto);
}
