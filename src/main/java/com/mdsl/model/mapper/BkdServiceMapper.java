package com.mdsl.model.mapper;

import org.mapstruct.Mapper;

import com.mdsl.model.dto.request.BkdServiceRequestDto;
import com.mdsl.model.dto.response.BkdServiceResponseDto;
import com.mdsl.model.entity.BKDService;

@Mapper
public interface BkdServiceMapper {
	BkdServiceResponseDto toDto(BKDService bkdService);
	BKDService toEntity(BkdServiceRequestDto bkdServiceRequestDto);
}
