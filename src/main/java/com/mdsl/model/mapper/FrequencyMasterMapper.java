package com.mdsl.model.mapper;

import org.mapstruct.Mapper;

import com.mdsl.model.dto.request.FrequencyMasterRequestDto;
import com.mdsl.model.dto.response.FrequencyMasterResponseDto;
import com.mdsl.model.entity.FrequencyMaster;

@Mapper
public interface FrequencyMasterMapper {
	FrequencyMasterResponseDto toDto (FrequencyMaster frequencyMaster); 
	FrequencyMaster toEntity (FrequencyMasterRequestDto frequencyMasterRequestDto);
}
