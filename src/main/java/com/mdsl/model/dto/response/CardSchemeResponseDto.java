package com.mdsl.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardSchemeResponseDto {
	private int recordSequenceNumber;
	private String cardSchemeId;
	private String cardSchemeName;
	private String cardSchemeSpecific;
	private char status;
}
