package com.mdsl.model.dto.response;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ManualNonActivityTransactionResponseDto {

	private int manualNonActivityTransactionId;
	private String institutionId;
	private String institutionName;
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
	private float transactionAmount;
	private int transactionCurrencyId;
	private String transactionCurrencyCode;
	private String transactionCurrencyName;
	private String transactionDate;
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
	
	public String getTransactionDescription() {
		return transactionDescription;
	}

	public void setTransactionDescription(String transactionDescription) {
		this.transactionDescription = transactionDescription;
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

	public int getManualNonActivityTransactionId() {
		return manualNonActivityTransactionId;
	}

	public void setManualNonActivityTransactionId(int manualNonActivityTransactionId) {
		this.manualNonActivityTransactionId = manualNonActivityTransactionId;
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

	

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getReversalFlag() {
		return reversalFlag;
	}

	public void setReversalFlag(String reversalFlag) {
		this.reversalFlag = reversalFlag;
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

	public String getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		Date date = transactionDate;
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		String strDate = formatter.format(date);
		this.transactionDate = strDate;
	}

}
