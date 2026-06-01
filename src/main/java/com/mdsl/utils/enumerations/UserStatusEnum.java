package com.mdsl.utils.enumerations;

import lombok.Getter;

@Getter
public enum UserStatusEnum {
	DISABLED('0'),
	ENABLED ('1'),
	FORCE_CHANGE_PASSWORD_ENABLED ('2'),
	FORCE_CHANGE_PASSWORD_DISABLED ('3'),
	INACTIVE('4'),
	BLOCKED('5'),
	EXPIRED('6');
	
	private final char value;

	UserStatusEnum(char value) {
		this.value = value;
	}
}