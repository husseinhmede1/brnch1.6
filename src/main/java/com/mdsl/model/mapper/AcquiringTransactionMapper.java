package com.mdsl.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mdsl.model.dto.request.AcquiringTransactionRequestDto;
import com.mdsl.model.dto.request.TerminalRequestDto;
import com.mdsl.model.dto.request.UnhaltRequestDto;
import com.mdsl.model.dto.response.AcquiringTransactionResponseDto;
import com.mdsl.model.dto.response.TransactionGroupDto;
import com.mdsl.model.entity.AcquiringTransaction;
import com.mdsl.model.entity.Terminal;
import com.mdsl.model.entity.TransactionGroup;

@Mapper
public interface AcquiringTransactionMapper {

	@Mapping(source = "institution.institutionName", target = "institutionName")
	@Mapping(source = "institution.institutionId", target = "institutionId")
	@Mapping(source = "transactionCurrency.currencyId", target = "transactionCurrencyId")
	@Mapping(source = "transactionCurrency.currencyCode", target = "transactionCurrencyCode")
	@Mapping(source = "transactionCurrency.currencyName", target = "transactionCurrencyName")
	@Mapping(source = "entitiesObject.entityId", target = "entitiesId")
	@Mapping(source = "entitiesObject.entityName", target = "entityName")
	@Mapping(source = "billingCurrency.currencyId", target = "billingCurrencyId")
	@Mapping(source = "billingCurrency.currencyCode", target = "billingCurrencyCode")
	@Mapping(source = "billingCurrency.currencyName", target = "billingCurrencyName")
	@Mapping(source = "tipsCurrency.currencyId", target = "tipsCurrencyId")
	@Mapping(source = "tipsCurrency.currencyCode", target = "tipsCurrencyCode")
	@Mapping(source = "tipsCurrency.currencyName", target = "tipsCurrencyName")
	@Mapping(source = "dccCurrency.currencyId", target = "dccCurrencyId")
	@Mapping(source = "dccCurrency.currencyCode", target = "dccCurrencyCode")
	@Mapping(source = "dccCurrency.currencyName", target = "dccCurrencyName")
	@Mapping(source = "dccMerchantSettlAmountCurrency.currencyId", target = "dccMerchantSettlAmountCurrencyId")
	@Mapping(source = "dccMerchantSettlAmountCurrency.currencyCode", target = "dccMerchantSettlAmountCurrencyCode")
	@Mapping(source = "dccMerchantSettlAmountCurrency.currencyName", target = "dccMerchantSettlAmountCurrencyName")
	@Mapping(source = "feeAmount1Currency.currencyId", target = "feeAmount1CurrencyId")
	@Mapping(source = "feeAmount1Currency.currencyCode", target = "feeAmount1CurrencyCode")
	@Mapping(source = "feeAmount1Currency.currencyName", target = "feeAmount1CurrencyName")
	@Mapping(source = "feeAmount2Currency.currencyId", target = "feeAmount2CurrencyId")
	@Mapping(source = "feeAmount2Currency.currencyCode", target = "feeAmount2CurrencyCode")
	@Mapping(source = "feeAmount2Currency.currencyName", target = "feeAmount2CurrencyName")
	@Mapping(source = "settlementCurrency.currencyId", target = "settlementCurrencyId")
	@Mapping(source = "settlementCurrency.currencyCode", target = "settlementCurrencyCode")
	@Mapping(source = "settlementCurrency.currencyName", target = "settlementCurrencyName")	
	@Mapping(source = "localCurrency.currencyId", target = "localCurrencyId")
	@Mapping(source = "localCurrency.currencyCode", target = "localCurrencyCode")
	@Mapping(source = "localCurrency.currencyName", target = "localCurrencyName")	
	@Mapping(source = "merchantAccountCurr.currencyId", target = "merchantAccountCurrId")
	@Mapping(source = "merchantAccountCurr.currencyCode", target = "merchantAccountCurrCode")
	@Mapping(source = "merchantAccountCurr.currencyName", target = "merchantAccountCurrName")	
	@Mapping(source = "cardScheme.cardSchemeId", target = "cardSchemeId")
	@Mapping(source = "cardScheme.cardSchemeName", target = "cardSchemeName")	
	@Mapping(source = "reasonCode.systemCodeId",target = "systemCodeId")
	@Mapping(source = "reasonCode.codeSuffix",target = "codeSuffix")
	@Mapping(source = "reasonCode.codePrefix",target = "codePrefix")
	@Mapping(source = "reasonCode.codeValue",target = "codeValue")
	@Mapping(source = "reasonCode.description",target = "codeDescription")
	@Mapping(source = "transactionId.transactionId",target = "transactionId")
	@Mapping(source = "transactionId.description",target = "transactionDescription")
	AcquiringTransactionResponseDto toDto(AcquiringTransaction acquiringTransaction);
	
	AcquiringTransactionResponseDto toDto(UnhaltRequestDto unhaltRequestDto);

	@Mapping(source = "institutionId", target = "institution.institutionId")
	@Mapping(source = "transactionId", target = "transactionId.transactionId")
	@Mapping(source = "entitiesId", target = "entitiesObject.entityId")
	@Mapping(source = "transactionCurrencyId", target = "transactionCurrency.currencyId")
	@Mapping(source = "billingCurrencyId", target = "billingCurrency.currencyId")
	@Mapping(source = "localCurrencyId", target = "localCurrency.currencyId")
	@Mapping(source = "tipsCurrencyId", target = "tipsCurrency.currencyId")
	@Mapping(source = "dccCurrencyId", target = "dccCurrency.currencyId")
	@Mapping(source = "feeAmount1CurrencyId", target = "feeAmount1Currency.currencyId")
	@Mapping(source = "feeAmount2CurrencyId", target = "feeAmount2Currency.currencyId")
	@Mapping(source = "settlementCurrencyId", target = "settlementCurrency.currencyId")
	@Mapping(source = "cardSchemeId", target = "cardScheme.cardSchemeId")
	@Mapping(source = "merchantAccountCurrId", target = "merchantAccountCurr.currencyId")
	@Mapping(source = "dccMerchantSettlAmountCurrencyId", target = "dccMerchantSettlAmountCurrency.currencyId")
	@Mapping(source = "systemCodeId", target = "reasonCode.systemCodeId")
	AcquiringTransaction toEntity(AcquiringTransactionRequestDto toDto);


}
