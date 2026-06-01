package com.mdsl.utils.enumerations;

import lombok.Getter;

@Getter
public enum AllowStpEnum {
	
	ALLOW_STP("1"), 
	NOT_ALLOW_STP("0");

	private final String value;

	AllowStpEnum(String value) {
		this.value = value;
	}

}
