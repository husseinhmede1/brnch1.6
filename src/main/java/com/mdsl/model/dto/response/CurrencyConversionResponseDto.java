package com.mdsl.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyConversionResponseDto {
	private int currencyConversionId;
	private String institutionId;
	private String institutionName;
	private int currencyId;
	private String currencyCode;
	private String currencyName;
	private int baseCurrencyId;
	private String baseCurrencyCode;
	private String  baseCurrencyName;
	private String roundingRule;
	private String rateExpression;
	private String midRateUsed;
	private int recordSeqId;
}