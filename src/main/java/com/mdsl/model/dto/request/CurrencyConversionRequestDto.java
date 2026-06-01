package com.mdsl.model.dto.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.mdsl.utils.ResponseCode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyConversionRequestDto {

	private int currencyConversionId;

	@NotEmpty(message = ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND)
	private String institutionId;

	@NotNull(message = ResponseCode.CFG_INVALID_CURRENCY)
	private int currencyId;

	@NotNull(message = ResponseCode.CFG_INVALID_CURRENCY)
	private int baseCurrencyId;

	@NotEmpty(message = ResponseCode.CUR_INVALID_ROUNDING_RULE)
	@Size (max = 4, message = ResponseCode.CUR_INVALID_ROUNDING_RULE)
	private String roundingRule;

	@NotEmpty(message = ResponseCode.CUR_INVALID_RATE_EXPRESSION)
	@Size (max = 1, message = ResponseCode.CUR_INVALID_RATE_EXPRESSION)
	private String rateExpression;

	@Size (max = 1, message = ResponseCode.CUR_INVALID_MID_RATE)
	private String midRateUsed;

	
}
