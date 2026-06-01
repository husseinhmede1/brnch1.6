package com.mdsl.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mdsl.model.dto.request.CountryRequestDto;
import com.mdsl.model.dto.response.CountryResponseDto;
import com.mdsl.model.entity.Country;

@Mapper
public interface CountryMapper {
	@Mapping(source = "currency.currencyId",target = "currencyId")
	@Mapping(source = "currency.currencyCode",target = "currencyCode")
	@Mapping(source = "currency.currencyName",target = "currencyName")
	CountryResponseDto toDto (Country account); 
	
	Country toEntity(CountryRequestDto countryRequestDto);
	
}
