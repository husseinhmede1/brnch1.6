package com.mdsl.utils.enumerations;

import lombok.Getter;

@Getter
public enum ReportFormatEnum {

	HTML("html"), 
	PDF("pdf"), 
	EXCEL("excel");

	private final String value;

	ReportFormatEnum(String value) {
		this.value = value;
	}
}
