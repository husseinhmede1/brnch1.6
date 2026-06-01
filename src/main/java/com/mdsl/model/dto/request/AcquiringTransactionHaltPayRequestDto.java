package com.mdsl.model.dto.request;

import java.util.Date;

import lombok.Data;

@Data
public class AcquiringTransactionHaltPayRequestDto {
	private int acquiringTransactionId;
	private String payHaltStatus;
	private String payHaltComment;
	private char confirmStoppingPayment;
	private int institutionId;
}
