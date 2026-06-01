package com.mdsl.model.dto.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
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
public class PaymentAccountRequestDto {

	private int paymentAccountId;
	
	@NotEmpty(message=ResponseCode.PAY_INVALID_ACCOUNY_NUMBER)
	@NotBlank(message = ResponseCode.PAY_INVALID_ACCOUNY_NUMBER)
	@Size(max = 30, message = ResponseCode.PAY_INVALID_ACCOUNY_NUMBER)
	private String accountNumber;
	
	@NotEmpty(message=ResponseCode.PAY_INVALID_IBAN)
	@NotBlank(message = ResponseCode.PAY_INVALID_IBAN)
	@Size(max = 30, message = ResponseCode.PAY_INVALID_IBAN)
	private String iban;
	
	private double currencyMarkup;
	
	@NotEmpty(message=ResponseCode.CFG_INVALID_INSTITUTION_ID)
	private String institutionId;
	
	@NotEmpty(message=ResponseCode.CFG_INVALID_ENTITY_ID)
	private String entityId;
	
	@NotNull(message=ResponseCode.CFG_INVALID_BANK_CODE)
	@Min(value = 1, message = ResponseCode.CFG_INVALID_BANK_CODE)
	@Max(value = 999999999, message = ResponseCode.CFG_INVALID_BANK_CODE)
	private int bankCodeId;
	
	@NotNull(message = ResponseCode.BRA_INVALID_BRANCH)
    @Size(min = 1, max = 10, message = ResponseCode.BRA_INVALID_BRANCH)
    @Pattern(regexp = "^[a-zA-Z0-9-_ ]*$", message = ResponseCode.BRA_INVALID_BRANCH)
    private String branch;
	
	@NotNull(message = ResponseCode.PAY_INVALID_BENEFICIARY_NAME)
    @Size(min = 1, max = 50, message = ResponseCode.PAY_INVALID_BENEFICIARY_NAME)
    @Pattern(regexp = "^[a-zA-Z0-9-_ ]*$", message = ResponseCode.PAY_INVALID_BENEFICIARY_NAME)
    private String beneficiaryName;
	
	private int transferCurrencyId;
	private int settlementCurrencyId;
	
	
	
	
		
	
	
}
