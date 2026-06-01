package com.mdsl.utils.enumerations;

public enum EntityLevelEnum {

	
	CHAIN("CHAIN"),
	
	MERCHANT("MERCHANT"),

	OUTLET("OUTLET");

	private final String value;

	EntityLevelEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
}
