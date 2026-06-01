package com.mdsl.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponseDto {
	private int accountId; 
	private String accountNumber; 
	private int accountTypeId; 
	private String accountTypeDesc;
	private int branchId; 
	private String branchCode; 
	private String branchName; 
	private String currencyCode; 
	private double availableBalance; 
	private double ledgerBalance; 
	private double cumHoldAmount; 
}