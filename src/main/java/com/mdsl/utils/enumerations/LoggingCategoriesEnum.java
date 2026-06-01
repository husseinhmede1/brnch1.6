package com.mdsl.utils.enumerations;

import lombok.Getter;

@Getter
public enum LoggingCategoriesEnum {
	SECURITY ('1'),
	USER_ACTIVITY ('2'),
	CARD_TYPE_ACTIVITY ('3'),
	CARD_ACTIVITY ('4'), 
	CONFIGURATION ('5'), 
	JOBS('6'); 
	
	private final char value;

	LoggingCategoriesEnum (char value) {
		this.value = value;
	}
}
