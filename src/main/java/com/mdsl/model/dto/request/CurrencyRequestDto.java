package com.mdsl.model.dto.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.mdsl.utils.ResponseCode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyRequestDto {

	private int currencyId;

	@NotEmpty(message = ResponseCode.CUR_INVALID_CURRENCY)
	@Size(max = 3, message = ResponseCode.CUR_INVALID_CURRENCY)
	private String currencyCode;

	@NotEmpty(message = ResponseCode.CUR_INVALID_CURRENCY_NAME)
	@Size(max = 50, message = ResponseCode.CUR_INVALID_CURRENCY_NAME)
	private String currencyName;

	@Size(max = 2, message = ResponseCode.CUR_INVALID_ALPHA_LEN)
	private String currCodeALPHA2;

	@Size(max = 3, message = ResponseCode.CUR_INVALID_ALPHA_LEN)
	private String currCodeALPHA3;

	@Size(max = 1, message = ResponseCode.CUR_INVALID_EXPO_LEN)
	private String currExponent;

}
