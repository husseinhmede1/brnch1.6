package com.mdsl.utils.enumerations;

import lombok.Getter;

@Getter
public enum RelationIndicatorEnum {
	PRIMARY("1"), SECONDARY("2"), OTHER("3");

	private final String value;

	RelationIndicatorEnum(String value) {
		this.value = value;
	}
}