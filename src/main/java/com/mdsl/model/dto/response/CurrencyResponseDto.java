package com.mdsl.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyResponseDto {
	private int currencyId;
	private String currencyCode;
	private String currencyName;
	private String currCodeALPHA2;
	private String currCodeALPHA3;
	private String currExponent;
	private int recordSeqId;
	private char status;
}
