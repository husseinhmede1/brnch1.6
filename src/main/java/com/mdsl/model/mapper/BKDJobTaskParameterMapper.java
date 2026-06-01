package com.mdsl.model.mapper;

import org.mapstruct.Mapper;

import com.mdsl.model.dto.request.JobTaskParametersRequestDto;
import com.mdsl.model.dto.response.JobTaskParametersResponseDto;
import com.mdsl.model.entity.BKDJobTaskParameter;

@Mapper
public interface BKDJobTaskParameterMapper {

	JobTaskParametersResponseDto toDto(BKDJobTaskParameter bkdJobTaskParameter);
	
	BKDJobTaskParameter toEntity (JobTaskParametersRequestDto jobTaskParamtersRequestDto);
}
