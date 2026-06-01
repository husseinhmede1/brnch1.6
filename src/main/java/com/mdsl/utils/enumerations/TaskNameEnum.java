package com.mdsl.utils.enumerations;

import lombok.Getter;

@Getter
public enum TaskNameEnum {
	TIMPORT_TASK("IMPORT TASK"),
	EOD_PROCESSING ("EOD PROCESSING"),
	PREPARE_ACCOUTING_FILE("PREPARE ACCOUTING FILE"),
	EXPORT_FILE("EXPORT FILE"),
	IMPORT_MERCHANTS("IMPORT MERCHANTS"),
	IMPORT_TERMINALS("IMPORT TERMINALS");
	
	private final String value;

	public String getValue() {
		return value;
	}

	TaskNameEnum(String value) {
		this.value = value;
	}

}
