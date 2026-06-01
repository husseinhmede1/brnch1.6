package com.mdsl.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mdsl.model.dto.request.CurrencyRateRequestDto;
import com.mdsl.model.dto.response.CurrencyRateResponseDto;
import com.mdsl.model.entity.CurrencyRate;

@Mapper
public interface CurrencyRateMapper {
	@Mapping(source = "currency.currencyId",target = "currencyId")
	@Mapping(source = "currency.currencyName",target = "currencyName")
	@Mapping(source = "currency.currencyCode",target = "currencyCode")
	@Mapping(source = "institution.institutionId", target = "institutionId")
	@Mapping(source = "institution.institutionName", target = "institutionName")
	CurrencyRateResponseDto toDto (CurrencyRate currencyRate); 
	
	CurrencyRate toEntity (CurrencyRateRequestDto currencyRateRequestDto);
}