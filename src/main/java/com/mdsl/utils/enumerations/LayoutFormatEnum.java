package com.mdsl.utils.enumerations;

import lombok.Getter;

@Getter
public enum LayoutFormatEnum {
	XML("1"),
	EXCEL("2"),
	TXT("3"),
	CSV("4"),
	DAT("5");

	private final String value;

	LayoutFormatEnum(String value) {
		this.value = value;
	}
}