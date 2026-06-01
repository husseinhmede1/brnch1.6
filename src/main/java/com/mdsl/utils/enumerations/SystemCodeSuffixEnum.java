package com.mdsl.utils.enumerations;

import lombok.Getter;

@Getter
public enum SystemCodeSuffixEnum {
	DATE_RANGE("DATE_RANGE"),
	JOB_MAX_SCHEDULE("JOB_MAX_SCHEDULE"),
	JOB_MAX_SCHEDULE_MONTH("JOB_MAX_SCHEDULE_MONTH"),
	JOB_SCHEDULER("JOB_SCHEDULER"),
	JOB_SCHEDULER_MAX_END_DATE("JOB_SCHEDULER_MAX_END_DATE"),
	JOB_CANCEL_EXEC_DETAILS("JOB_CANCEL_EXEC_DETAILS");
	private final String value;
	
	SystemCodeSuffixEnum(String value) {
		this.value = value;
	}
}
