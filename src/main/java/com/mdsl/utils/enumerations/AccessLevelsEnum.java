package com.mdsl.utils.enumerations;

import lombok.Getter;

@Getter
public enum AccessLevelsEnum {
	GLOBAL_LEVEL('1'),
	INSTITUTION_LEVEL('2'),
	BRANCH_LEVEL('3');
	
	private final char accessLevel;

	AccessLevelsEnum(char accessLevel) {
		this.accessLevel = accessLevel;
	}
}