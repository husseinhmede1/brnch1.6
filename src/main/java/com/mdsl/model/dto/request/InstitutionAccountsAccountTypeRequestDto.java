package com.mdsl.model.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.mdsl.utils.ResponseCode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InstitutionAccountsAccountTypeRequestDto {
	
	@NotNull(message = ResponseCode.CFG_INVALID_INSTITUTION_ID)
    @Size(min = 1, max = 10, message = ResponseCode.CFG_INVALID_INSTITUTION_ID)
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = ResponseCode.CFG_INVALID_INSTITUTION_ID)
    private String institutionId;
	
    @Size(min = 0, max = 10, message = ResponseCode.CFG_INVALID_INSTITUTION_ID)
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = ResponseCode.CFG_INVALID_INSTITUTION_ID)
    private String chargingInstitution;
	
	@NotNull(message = ResponseCode.CFG_INVALID_BANK_CODE)
    @Size(min = 1, max = 10, message = ResponseCode.CFG_INVALID_BANK_CODE)
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = ResponseCode.CFG_INVALID_BANK_CODE)
    private String bankCode;
	
	@Size(min = 0, max = 2, message = ResponseCode.ACT_INVALID_ACCOUNT_ORIGIN)
    @Pattern(regexp = "^[a-zA-Z]*$", message = ResponseCode.ACT_INVALID_ACCOUNT_ORIGIN)
    private String accountOrigin;

}
