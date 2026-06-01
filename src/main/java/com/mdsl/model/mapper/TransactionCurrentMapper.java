package com.mdsl.model.mapper;

import org.mapstruct.Mapper;

import com.mdsl.model.dto.response.TransactionCurrentResponseDto;
import com.mdsl.model.dto.response.TransactionCurrentSearchResponseDto;
import com.mdsl.model.entity.TransactionCurrent;

@Mapper
public interface TransactionCurrentMapper {
	TransactionCurrentResponseDto toDto(TransactionCurrent transactionCurrent);
	TransactionCurrentSearchResponseDto toSearchDto(TransactionCurrent transactionCurrent);
}
