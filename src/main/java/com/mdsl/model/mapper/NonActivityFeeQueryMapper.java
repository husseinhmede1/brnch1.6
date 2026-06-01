package com.mdsl.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mdsl.model.dto.request.NonActivityFeeQueryRequestDto;
import com.mdsl.model.dto.response.NonActivityFeeQueryResponseDto;
import com.mdsl.model.entity.NonActivityFeeQuery;

@Mapper
public interface NonActivityFeeQueryMapper {
	
	@Mapping(source = "transactionCurrency.currencyId",target = "transactionCurrencyId")
	@Mapping(source = "transactionCurrency.currencyCode",target = "transactionCurrencyCode")
	@Mapping(source = "transactionCurrency.currencyName",target = "transactionCurrencyName")
	@Mapping(source = "transactionEntity.transactionId",target = "transactionId")
	@Mapping(source = "transactionEntity.description",target = "transactionDescription")
	@Mapping(source = "entitiesObject.entityId",target = "entityId")
	@Mapping(source = "entitiesObject.entityName",target = "entityName")
	@Mapping(source = "institution.institutionId",target = "institutionId")
	@Mapping(source = "institution.institutionName",target = "institutionName")
	NonActivityFeeQueryResponseDto toDto (NonActivityFeeQuery nonActivityFeeQuery);
	
	NonActivityFeeQuery toEntity (NonActivityFeeQueryRequestDto nonActivityFeeQueryRequestDto);
}
