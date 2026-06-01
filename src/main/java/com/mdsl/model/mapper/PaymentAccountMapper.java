package com.mdsl.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mdsl.model.dto.request.PaymentAccountRequestDto;
import com.mdsl.model.dto.response.PaymentAccountResponseDto;
import com.mdsl.model.entity.PaymentAccount;

@Mapper
public interface PaymentAccountMapper {
	
	@Mapping(source = "bankCodeEntity.bankCodeId", target = "bankCodeId")
	@Mapping(source = "bankCodeEntity.bankCode", target = "bankCode")
	
	@Mapping(source = "transferCurrency.currencyId", target = "transferCurrencyId")
	@Mapping(source = "transferCurrency.currencyName", target = "transferCurrencyName")
	
	@Mapping(source = "settlementCurrency.currencyId", target = "settlementCurrencyId")
	@Mapping(source = "settlementCurrency.currencyName", target = "settlementCurrencyName")
	
	@Mapping(source = "institution.institutionId", target = "institutionId")
	
	@Mapping(source = "entityObject.entityId", target = "entityId")
	PaymentAccountResponseDto toDto(PaymentAccount paymentAccount);
	
	PaymentAccount toEntity(PaymentAccountRequestDto paymentAccountRequestDto);

}
