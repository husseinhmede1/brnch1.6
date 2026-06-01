package com.mdsl.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentAccountResponseDto {
	private int paymentAccountId;
	private String accountNumber;
	private String iban;
	private double currencyMarkup;
	private int bankCodeId;
	private String bankCode;
	private int transferCurrencyId;
	private String transferCurrencyName;
	private int settlementCurrencyId;
	private String settlementCurrencyName;
	private String institutionId;
	private String entityId;
	private String branch;
	private String beneficiaryName;
}