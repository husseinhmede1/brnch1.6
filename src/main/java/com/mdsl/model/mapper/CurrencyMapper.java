package com.mdsl.model.mapper;

import org.mapstruct.Mapper;

import com.mdsl.model.dto.request.CurrencyRequestDto;
import com.mdsl.model.dto.response.CurrencyResponseDto;
import com.mdsl.model.entity.Currency;

@Mapper
public interface CurrencyMapper {
	CurrencyResponseDto toDto (Currency currency); 
	Currency toEntity (CurrencyRequestDto currencyRequestDto);
}