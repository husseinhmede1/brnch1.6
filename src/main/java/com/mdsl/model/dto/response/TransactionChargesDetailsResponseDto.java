package com.mdsl.model.dto.response;

import com.mdsl.model.entity.DefaultTransactionId;
import com.mdsl.model.entity.Institution;

import lombok.Data;

@Data
public class TransactionChargesDetailsResponseDto {
	private Integer transactionChargeDetailsId;
	private String description;
	private String defaultTransactionId;
	private String institutionId;
}