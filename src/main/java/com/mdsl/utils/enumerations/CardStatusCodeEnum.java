package com.mdsl.utils.enumerations;

import lombok.Getter;

@Getter
public enum CardStatusCodeEnum {
	ISSUED_NEW("70"),
	ACTIVE("50"),
	WARM("51"),
	HOT_WITH_PICKUP("52"),
	LOST("53"),
	DAMAGED("54"),
	STOLEN("57"),
	TERMINATED("60"),
	PIN_TRIES_EXCEEDED("75"),
	EMBOSSED("90"),
	EXPIRED("66");
	
	private final String statusCode;

	CardStatusCodeEnum(String statusCode) {
		this.statusCode = statusCode;
	}
}