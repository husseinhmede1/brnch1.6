package com.mdsl.utils.enumerations;

import lombok.Getter;

@Getter
public enum ScopeEnum {
	CARD ("card"), 
	CUSTOMER("customer"); 
	
	private final String value;

	ScopeEnum(String value) {
		this.value = value;
	}
}