package com.mdsl.model.dto.response;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.*;

@Getter
@Setter
public class NonActivityFeeQueryResponseDto {
	
	private int nonActivityFeeQueryId;
	private String microfilmRefNbr;
	private int refNbrSeq;
	private String entityId;
	private String entityName;
	private String institutionId;
	private String institutionName;
	private String transactionId;
	private String transactionDescription;
	private String transactionDate;
	private float transactionAmount;
	private int transactionCurrencyId;
	private String transactionCurrencyCode;
	private String transactionCurrencyName;
	private String reversalReason;
	private char manualEntry;
	private String transDesc;
	private String processingDate;
	private String processingRefNbr;
	private String reason;
	private String description;
	private Integer pageNo;
	private Integer pageSize;
	private char reversalFlag;
	
	

	public void setTransactionDate(Date transactionDate) {
		Date date = transactionDate;
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		String strDate = formatter.format(date);
		this.transactionDate = strDate;
	}

	public void setProcessingDate(Date processingDate) {
		Date date = processingDate;
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		String strDate = formatter.format(date);
		this.processingDate = strDate;
	}
}
