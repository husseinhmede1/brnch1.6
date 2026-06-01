package com.mdsl.utils.enumerations;

import lombok.Getter;

@Getter
public enum JobTaskExecutionStatusEnum {
	START('0'),
	INPROCESS ('1'),
	STOP ('2'),
	COMPLETED('3'),
	ERROR('4'),
	FORCE_STOP('5');
	
	private final char value;

	JobTaskExecutionStatusEnum(char value) {
		this.value = value;
	}
}