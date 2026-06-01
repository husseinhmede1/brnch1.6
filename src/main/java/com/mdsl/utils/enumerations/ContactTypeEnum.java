package com.mdsl.utils.enumerations;

import lombok.Getter;

@Getter
public enum ContactTypeEnum {
	EMAIL ("email"), 
	MOBILE("mobile"),
	DELIVERY_ADDRESS("delivery address"),
	BILLING_ADDRESS("billing address"),
	HOME_PHONE("home phone"), 
	WORK_PHONE("work phone"), 
	MAILING_ADDRESS("mailing address");
	
	private final String value;

	ContactTypeEnum(String value) {
		this.value = value;
	}
}