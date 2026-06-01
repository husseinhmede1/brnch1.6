package com.mdsl.utils.enumerations;

import lombok.Getter;

@Getter
public enum StatusEnum {
	ENABLED ('1'),
	HIDDEN ('2'),
	DISABLED('0'); 
	
	private final char value;

	public char getValue() {
		return value;
	}

	StatusEnum(char value) {
		this.value = value;
	}
}