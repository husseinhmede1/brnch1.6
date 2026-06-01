package com.mdsl.utils.enumerations;

import lombok.Getter;

@Getter
public enum StatementEnum {
	YES ('Y'),
	NO ('N');
	
	private final char value;

	public char getValue() {
		return value;
	}

	StatementEnum(char value) {
		this.value = value;
	}
}
