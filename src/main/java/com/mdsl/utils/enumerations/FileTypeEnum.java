package com.mdsl.utils.enumerations;

import lombok.Getter;

@Getter
public enum FileTypeEnum {
	INPUT("Input"),
	OUTPUT("Output");
	
	private final String value;

	FileTypeEnum(String value) {
		this.value = value;
	}
}