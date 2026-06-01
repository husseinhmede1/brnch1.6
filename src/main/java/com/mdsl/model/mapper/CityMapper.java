package com.mdsl.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mdsl.model.dto.request.CityResquestDto;
import com.mdsl.model.dto.response.CityResponseDto;
import com.mdsl.model.entity.City;

@Mapper
public interface CityMapper {
	@Mapping(source = "cntryCode.cntryCode",target = "cntryCode")
	@Mapping(source = "provStateAbbrev.provStateAbbrev",target = "provStateAbbrev")
	CityResponseDto toDto(City city);
	
	@Mapping(target = "cntryCode", ignore = true)
	@Mapping(target = "provStateAbbrev", ignore = true)
	City toEntity(CityResquestDto cityResquestDto);
}
