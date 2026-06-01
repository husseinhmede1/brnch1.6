package com.mdsl.utils.enumerations;

public enum TerminalSortColumnEnum {

	TERMINALID("terminalId");
	
	 private final String value;

	 TerminalSortColumnEnum(String value) {
	    this.value = value;
	  }

	  public String getValue() {
	    return value;
	  }
}
