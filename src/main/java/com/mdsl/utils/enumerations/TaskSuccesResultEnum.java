package com.mdsl.utils.enumerations;

import lombok.Getter;

@Getter
public enum TaskSuccesResultEnum {
	T ("Terminated"),
	L ("Log"),
	S("Success"),
	I("Info"),
	F("Failure"),
	E("Error"),
	W("Warning");
	
	private final String value;

	public String getValue() {
		return value;
	}

	TaskSuccesResultEnum(String value) {
		this.value = value;
	}

}
