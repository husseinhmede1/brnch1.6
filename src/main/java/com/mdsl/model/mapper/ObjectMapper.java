package com.mdsl.model.mapper;

import com.mdsl.model.dto.response.ObjectResponseDto;
import com.mdsl.model.objects.ObjectAndScope;
import org.mapstruct.Mapper;

@Mapper
public interface ObjectMapper {
	ObjectResponseDto toDto(ObjectAndScope objectAndScope);
}