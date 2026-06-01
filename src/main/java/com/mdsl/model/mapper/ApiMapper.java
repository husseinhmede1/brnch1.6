package com.mdsl.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.mdsl.model.dto.response.ApiListResponseDto;
import com.mdsl.model.dto.response.ApiObjectsResponseDto;
import com.mdsl.model.dto.response.ApiResponseDto;
import com.mdsl.model.entity.Api;

@Mapper
public interface ApiMapper {
	ApiMapper INSTANCE = Mappers.getMapper(ApiMapper.class);

	ApiResponseDto toDto (Api api);

	@Mapping(source="instId", target="instId")
	@Mapping(source="apiDesc", target="apiDesc")
	@Mapping(source="stp", target="stp")
	@Mapping(source="apiFunction", target="apiFunction")
	ApiResponseDto toApi (Api api);
}