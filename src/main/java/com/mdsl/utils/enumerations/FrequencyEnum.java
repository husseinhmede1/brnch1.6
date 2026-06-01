package com.mdsl.utils.enumerations;

import lombok.Getter;

@Getter
public enum FrequencyEnum {
	ONE_TIME(0), DAILY(1), MONTHLY(2);

	private final Integer value;

	FrequencyEnum(Integer value) {
		this.value = value;
	}
	
	public static FrequencyEnum get(Integer value) {
		for(FrequencyEnum f : FrequencyEnum.values()) {
			if(f.value.equals(value)) {
				return f;
			}
		}
		return null;
	}
	
}