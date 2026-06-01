package com.mdsl.utils.enumerations;

import lombok.Getter;

@Getter
public enum CodeSuffixEnum {
	FILE_CONFIG_SERVER("FILE_CONFIG_SERVER"),
	FILE_CONFIG_PORT("FILE_CONFIG_PORT"),
	FILE_CONFIG_USERNAME("FILE_CONFIG_USERNAME"),
	FILE_CONFIG_PASSWORD("FILE_CONFIG_PASSWORD"),
	FILE_CONFIG_PATH("FILE_CONFIG_PATH"), 
	CHARGE_TYPE_ONE_TIME("O"),
	CHARGE_TYPE_RECURRING("R"),
	CAPACITY("CAPACITY"),
	DURATION_SEC("DURATION_SEC");
	
	private final String value;

	public String getValue() {
		return value;
	}

	CodeSuffixEnum(String value) {
		this.value = value;
	}
}