package com.mdsl.model.dto.request;

import javax.validation.constraints.NotNull;

import com.mdsl.utils.ResponseCode;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class DefaultTransactionIdRequestDto {
	private String transactionId;
	
	@NotNull(message=ResponseCode.CFG_INVALID_TRANSACTION_DESCRIPTION)
	private String description;
	
	@NotNull(message=ResponseCode.CFG_INVALID_TRANSACTION_SIGN_FLAG)
	private char signFlag;

	@NotNull(message=ResponseCode.CFG_INVALID_TRANSACTION_USAGE)
	private String transUsage;

	private String institutionId;

	private char updateFlag;

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public char getSignFlag() {
		return signFlag;
	}
	public void setSignFlag(char signFlag) {
		this.signFlag = signFlag;
	}
	
	
}
