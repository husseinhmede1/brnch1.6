package com.mdsl.utils.enumerations;

import lombok.Getter;

@Getter
public enum SystemCodePrefixEnum {
	DATE_RANGE("DATE_RANGE"),
	JOB("JOB");


	private final String value;
	
	SystemCodePrefixEnum(String value) {
		this.value = value;
	}
}
