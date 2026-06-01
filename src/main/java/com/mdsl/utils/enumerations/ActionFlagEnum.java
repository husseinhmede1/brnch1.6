package com.mdsl.utils.enumerations;

import lombok.Getter;

@Getter
public enum ActionFlagEnum {

	ADD("A"),
	UPDATE("U"),
	DELETE("D");
	
	private final String value;

	ActionFlagEnum(String value) {
		this.value = value;
	}
}
