package com.mdsl.utils.enumerations;

public enum CommonSortColumnEnum {

	TERMINALID("terminalId"),

	MCC("mcc"),

	CARDSCHEME("cardSchemeTypeMapping.cardSchemeName"),

	ENTITYNAME("entityName"),

	DBANAME("dbaName"),

	CURRENCYNAME("currency.currencyName"),

	EFFECTIVEDATE("effectiveDate"),

	PROCESSINGDATE("processingDate"),

	TRANSACTIONDATE("transactionDate"),
	
	ACQUIRINGTERMINAL("terminal.terminalId"),
	
	OUTLET("entitiesObject.entityId"),

	OUTLETNAME("entitiesObject.entityName"),
	
	MERCHANT_NAME("merchantName"),

	ENTITY_NAME("ENTITY_NAME"),
	DBA_NAME("DBA_NAME");
	
	private final String value;

	CommonSortColumnEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
