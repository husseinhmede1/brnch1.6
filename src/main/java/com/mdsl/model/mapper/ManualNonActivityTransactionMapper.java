package com.mdsl.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mdsl.model.dto.request.ManualNonActivityTransactionRequestDto;
import com.mdsl.model.dto.response.ManualNonActivityTransactionResponseDto;
import com.mdsl.model.entity.ManualNonActivityTransaction;

@Mapper
public interface ManualNonActivityTransactionMapper {
	
	@Mapping(source = "transactionCurrency.currencyId",target = "transactionCurrencyId")
	@Mapping(source = "transactionCurrency.currencyCode",target = "transactionCurrencyCode")
	@Mapping(source = "transactionCurrency.currencyName",target = "transactionCurrencyName")
	@Mapping(source = "transactionEntity.transactionId",target = "transactionId")
	@Mapping(source = "transactionEntity.description",target = "transactionDescription")
	@Mapping(source = "entitiesObject.entityId",target = "entityId")
	@Mapping(source = "entitiesObject.entityName",target = "entityName")
	@Mapping(source = "institution.institutionId",target = "institutionId")
	@Mapping(source = "institution.institutionName",target = "institutionName")
	@Mapping(source = "reasonCode.systemCodeId",target = "systemCodeId")
	@Mapping(source = "reasonCode.codeSuffix",target = "codeSuffix")
	@Mapping(source = "reasonCode.codePrefix",target = "codePrefix")
	@Mapping(source = "reasonCode.codeValue",target = "codeValue")
	@Mapping(source = "reasonCode.description",target = "codeDescription")
	ManualNonActivityTransactionResponseDto toDto (ManualNonActivityTransaction manualNonActivityTransaction);
	
	ManualNonActivityTransaction toEntity (ManualNonActivityTransactionRequestDto manualNonActivityTransactionRequestDto);
}
