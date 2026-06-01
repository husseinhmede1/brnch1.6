package com.mdsl.model.dto.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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
public class BankCodeRequestDto {
	@NotNull(message=ResponseCode.BNK_INVALID_BANK_INFO_ID)
	@Min(value = 0, message = ResponseCode.BNK_INVALID_BANK_INFO_ID)
	@Max(value = 999999999, message = ResponseCode.BNK_INVALID_BANK_INFO_ID)
	private int bankCodeId;
	
	@NotNull(message=ResponseCode.CFG_INVALID_BANK_CODE)
	@Size(min=1, max=10, message=ResponseCode.CFG_INVALID_BANK_CODE)
	@Pattern(regexp="^[a-zA-Z0-9]*$", message=ResponseCode.CFG_INVALID_BANK_CODE)
	private String bankCode;
	
	@NotNull(message=ResponseCode.BNK_INVALID_BANK_NAME)
	@Size(min=1, max=50, message=ResponseCode.BNK_INVALID_BANK_NAME)
	@Pattern(regexp="^[a-zA-Z ]*$", message=ResponseCode.BNK_INVALID_BANK_NAME)
	private String bankName;
	
	@NotNull(message=ResponseCode.BNK_INVALID_ALT_BANK_NAME)
	@Size(min=1, max=100, message=ResponseCode.BNK_INVALID_ALT_BANK_NAME)
	@Pattern(regexp="^[a-zA-Z ]*$", message=ResponseCode.BNK_INVALID_ALT_BANK_NAME)
	private String altBankName;
	
	@Size(min=0, max=15, message=ResponseCode.CFG_INVALID_SWIFT_CODE)
	@Pattern(regexp="^[a-zA-Z0-9]*$", message=ResponseCode.CFG_INVALID_SWIFT_CODE)
	private String swiftCode;
	
	@NotNull(message=ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND)
	@Size(min=1, max=10, message=ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND)
	@Pattern(regexp="^[a-zA-Z0-9]*$", message=ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND)
	private String institutionId;
}