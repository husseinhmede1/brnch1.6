package com.mdsl.model.dto.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.mdsl.utils.ResponseCode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountInformationRequestDto {
	@NotEmpty(message=ResponseCode.ACT_ACCOUNT_ORIGIN)
	@Size(min=5, max=32, message=ResponseCode.ACT_ACCOUNT_ORIGIN)
	private String accountNumber; 
	
	@NotEmpty(message=ResponseCode.CFG_INVALID_RELATION_IND)
	@Pattern(regexp="[0-3]", message=ResponseCode.CFG_INVALID_RELATION_IND)
	private String relationIndicator; 
	
	@NotEmpty(message=ResponseCode.ACT_INVALID_ACCOUNT_TYPE)
	@Size(min=1, max=10, message=ResponseCode.ACT_INVALID_ACCOUNT_TYPE)
	private String accountType;
	
	@NotEmpty(message=ResponseCode.CUR_INVALID_CURRENCY)
	@Size(min=3, max=3, message=ResponseCode.CUR_INVALID_CURRENCY)
	private String currencyCode; 
	
	@NotNull(message=ResponseCode.BRA_INVALID_BRANCH)
	@Min(value=1, message=ResponseCode.BRA_INVALID_BRANCH)
	@Max(value=999999, message=ResponseCode.BRA_INVALID_BRANCH)
	private String accountBranchId;
	
	@Size(min=0, max=10, message=ResponseCode.BRA_INVALID_BRANCH_CODE)
	private String accountBranchCode; 
}