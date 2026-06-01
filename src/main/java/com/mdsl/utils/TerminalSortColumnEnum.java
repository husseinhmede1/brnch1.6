package com.mdsl.utils;

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
