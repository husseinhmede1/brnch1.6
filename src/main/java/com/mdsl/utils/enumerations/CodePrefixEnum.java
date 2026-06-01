package com.mdsl.utils.enumerations;

import lombok.Getter;

@Getter
public enum CodePrefixEnum {
	BUSINESS_TYPE ("BUSINESS_TYPE"),
	FIELD_ID ("FIELD_ID"),
	COMPANY_TYPE("COMPANY_TYPE"),
	MERCHANT_TYPE("MERCHANT_TYPE"),
	OUTPUT_FORMAT("OUTPUT_FORMAT"),
	FILE_CONFIG("FILE_CONFIG"),
	FREQUENCY_MASTER("MD_FREQUENCY_MASTER"),
	CHARGE_TYPE("CHARGE_TYPE"),
	RATE_LIMIT("RATE_LIMIT");
	
	private final String value;

	public String getValue() {
		return value;
	}

	CodePrefixEnum(String value) {
		this.value = value;
	}
}