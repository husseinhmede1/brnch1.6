package com.mdsl.utils.enumerations;

import lombok.Getter;

@Getter
public enum JobFrequencyEnum {
	ONE_TIME(0), 
	DAILY(1),
	MONTHLY(2), 
	CONTINUOUS(3);

	private final Integer value;

	JobFrequencyEnum(Integer value) {
		this.value = value;
	}
}