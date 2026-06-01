package com.mdsl.utils.enumerations;

import lombok.Getter;

@Getter
public enum ThreadStatusEnum {

	RUNNING("RUNNING"),
	FINISHED("FINISHED"),
	TERMINATED_BY_FORCE("TERMINATED_BY_FORCE");
	
	private final String value;

	ThreadStatusEnum(String value) {
			this.value = value;
		}

}