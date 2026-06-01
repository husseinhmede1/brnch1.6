package com.mdsl.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mdsl.model.dto.request.CurrencyConversionRequestDto;
import com.mdsl.model.dto.response.CurrencyConversionResponseDto;
import com.mdsl.model.entity.CurrencyConversion;

@Mapper
public interface CurrencyConversionMapper {
	@Mapping(source = "currency.currencyId",target = "currencyId")
	@Mapping(source = "currency.currencyCode",target = "currencyCode")
	@Mapping(source = "currency.currencyName",target = "currencyName")
	@Mapping(source = "baseCurrency.currencyId",target = "baseCurrencyId")
	@Mapping(source = "baseCurrency.currencyName",target = "baseCurrencyName")
	@Mapping(source = "baseCurrency.currencyCode",target = "baseCurrencyCode")
	@Mapping(source = "institution.institutionId", target = "institutionId")
	@Mapping(source = "institution.institutionName", target = "institutionName")
	CurrencyConversionResponseDto toDto (CurrencyConversion currencyConversion); 
	
	CurrencyConversion toEntity (CurrencyConversionRequestDto currencyConversionRequestDto);
}
