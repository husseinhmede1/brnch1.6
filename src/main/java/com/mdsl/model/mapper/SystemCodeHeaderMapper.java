package com.mdsl.model.mapper;


import org.mapstruct.Mapper;

import com.mdsl.model.dto.request.SystemCodeHeaderRequestDto;
import com.mdsl.model.dto.response.SystemCodeHeaderResponseDto;
import com.mdsl.model.entity.SystemCodeHeader;

@Mapper
public interface SystemCodeHeaderMapper {

	SystemCodeHeaderResponseDto toDto(SystemCodeHeader systemCodeHeader);
	SystemCodeHeader toEntity(SystemCodeHeaderRequestDto systemCodeHeaderRequestDto);
}
