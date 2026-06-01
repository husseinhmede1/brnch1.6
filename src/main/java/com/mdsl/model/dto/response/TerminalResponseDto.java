package com.mdsl.model.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
public class TerminalResponseDto {

	private String terminalId;
	private int terminalTypesId;
	private String terminalTypes;
	private char eCommerceFlag;
	private String actualStartDate;
	private String terminationDate;
	private String serialNumber;
	private String institutionId;
	private String institutionName;
	private int currencyId;
	private String currencyCode;
	private String currencyName;
	private int mccId;
	private String mcc;
	private String mccDescription;
	private String status;
	private String entityId;
	private String entityName;
	private Integer pageNo;
	private Integer pageSize;
	private Integer recordSeqId;

	public void setActualStartDate(Date actualStartDate) {
		if (actualStartDate != null) {
			Date date = actualStartDate;
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			String strDate = formatter.format(date);
			this.actualStartDate = strDate;
		} else {
			this.actualStartDate = "";
		}
	}

	public String getTerminationDate() {
		return terminationDate;
	}

	public void setTerminationDate(Date terminationDate) {
		if (terminationDate != null) {
			Date date = terminationDate;
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			String strDate = formatter.format(date);
			this.terminationDate = strDate;
		} else {
			this.terminationDate = "";
		}
	}
}