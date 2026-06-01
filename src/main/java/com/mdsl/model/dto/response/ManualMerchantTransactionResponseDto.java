package com.mdsl.model.dto.response;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ManualMerchantTransactionResponseDto {
	private int merchantTransactionId;
	private String institutionId;
	private String institutionName;
	private String pan;
	private int cardSeqNbr;
	private String cardNumber;
	private String transactionId;
	private String transactionDescription;
	private String reversalFlag;
	private int systemCodeId;
	private String codeSuffix;
	private String codePrefix;
	private char codePattern;
	private String codeValue;
	private String codeDescription;
	private String comments;
	private String entityId;
	private String entityName;
	private String terminalId;
	private float transactionAmount;
	private int transactionCurrencyId;
	private String transactionCurrencyCode;
	private String transactionCurrencyName;
	private float tipsAmount;
	private int tipsCurrencyId;
	private String tipsCurrencyCode;
	private String tipsCurrencyName;
	private String transactionDate;
	private String authorizationNumber;
	private String expiryDate;
	private Integer pageNo;
	private Integer pageSize;
	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	
	public String getExpiryDate() {
		return expiryDate;
	}

	public String getTransactionDescription() {
		return transactionDescription;
	}

	public void setTransactionDescription(String transactionDescription) {
		this.transactionDescription = transactionDescription;
	}

	public void setExpiryDate(Date expiryDate) {
		if(expiryDate!=null) {
			Date date = expiryDate;
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			String strDate = formatter.format(date);
			this.expiryDate = strDate;
		}else {
			this.expiryDate="";
		}
		
	}

	public void setTransactionDate(Date transactionDate) {
		Date date = transactionDate;
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		String strDate = formatter.format(date);
		this.transactionDate = strDate;
	}

	public int getMerchantTransactionId() {
		return merchantTransactionId;
	}

	public void setMerchantTransactionId(int merchantTransactionId) {
		this.merchantTransactionId = merchantTransactionId;
	}

	public String getInstitutionId() {
		return institutionId;
	}

	public void setInstitutionId(String institutionId) {
		this.institutionId = institutionId;
	}

	public String getInstitutionName() {
		return institutionName;
	}

	public void setInstitutionName(String institutionName) {
		this.institutionName = institutionName;
	}

	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public int getCardSeqNbr() {
		return cardSeqNbr;
	}

	public void setCardSeqNbr(int cardSeqNbr) {
		this.cardSeqNbr = cardSeqNbr;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getReversalFlag() {
		return reversalFlag;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public void setReversalFlag(String reversalFlag) {
		this.reversalFlag = reversalFlag;
	}

	public int getSystemCodeId() {
		return systemCodeId;
	}

	public void setSystemCodeId(int systemCodeId) {
		this.systemCodeId = systemCodeId;
	}

	public String getCodeSuffix() {
		return codeSuffix;
	}

	public void setCodeSuffix(String codeSuffix) {
		this.codeSuffix = codeSuffix;
	}

	public String getCodePrefix() {
		return codePrefix;
	}

	public void setCodePrefix(String codePrefix) {
		this.codePrefix = codePrefix;
	}

	public char getCodePattern() {
		return codePattern;
	}

	public void setCodePattern(char codePattern) {
		this.codePattern = codePattern;
	}

	public String getCodeValue() {
		return codeValue;
	}

	public void setCodeValue(String codeValue) {
		this.codeValue = codeValue;
	}

	public String getCodeDescription() {
		return codeDescription;
	}

	public void setCodeDescription(String codeDescription) {
		this.codeDescription = codeDescription;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	
	
	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	

	public float getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(float transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public int getTransactionCurrencyId() {
		return transactionCurrencyId;
	}

	public void setTransactionCurrencyId(int transactionCurrencyId) {
		this.transactionCurrencyId = transactionCurrencyId;
	}

	public String getTransactionCurrencyCode() {
		return transactionCurrencyCode;
	}

	public void setTransactionCurrencyCode(String transactionCurrencyCode) {
		this.transactionCurrencyCode = transactionCurrencyCode;
	}

	public String getTransactionCurrencyName() {
		return transactionCurrencyName;
	}

	public void setTransactionCurrencyName(String transactionCurrencyName) {
		this.transactionCurrencyName = transactionCurrencyName;
	}

	public float getTipsAmount() {
		return tipsAmount;
	}

	public void setTipsAmount(float tipsAmount) {
		this.tipsAmount = tipsAmount;
	}

	public int getTipsCurrencyId() {
		return tipsCurrencyId;
	}

	public void setTipsCurrencyId(int tipsCurrencyId) {
		this.tipsCurrencyId = tipsCurrencyId;
	}

	public String getTipsCurrencyCode() {
		return tipsCurrencyCode;
	}

	public void setTipsCurrencyCode(String tipsCurrencyCode) {
		this.tipsCurrencyCode = tipsCurrencyCode;
	}

	public String getTipsCurrencyName() {
		return tipsCurrencyName;
	}

	public void setTipsCurrencyName(String tipsCurrencyName) {
		this.tipsCurrencyName = tipsCurrencyName;
	}

	public String getTransactionDate() {
		return transactionDate;
	}

	public String getAuthorizationNumber() {
		return authorizationNumber;
	}

	public void setAuthorizationNumber(String authorizationNumber) {
		this.authorizationNumber = authorizationNumber;
	}

}
