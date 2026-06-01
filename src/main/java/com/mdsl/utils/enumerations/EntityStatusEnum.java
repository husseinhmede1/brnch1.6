package com.mdsl.utils.enumerations;

import lombok.Getter;

@Getter
public enum EntityStatusEnum {
	PILOT ("Pilot"), 
	TEST("Test"), 
	PRODUCTION("Production");
	
	private final String value;

	EntityStatusEnum(String value) {
		this.value = value;
	}

}
