package com.mdsl.model.dto.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.mdsl.utils.ResponseCode;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InstitutionAccountsRequestDto {
	@NotNull(message = ResponseCode.INT_INVALID_INSTITUTION_ACCOUNTS)
    @Min(value = 0, message = ResponseCode.INT_INVALID_INSTITUTION_ACCOUNTS)
    @Max(value = 999999999, message = ResponseCode.INT_INVALID_INSTITUTION_ACCOUNTS)
    private Integer institutionAcctsId;
	
    @NotNull(message = ResponseCode.CFG_INVALID_INSTITUTION_ID)
    @Size(min = 1, max = 10, message = ResponseCode.CFG_INVALID_INSTITUTION_ID)
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = ResponseCode.CFG_INVALID_INSTITUTION_ID)
    private String institutionId;
    
    @NotNull(message = ResponseCode.ACT_INVALID_ACCOUNT_TYPE)
    @Size(min = 1, max = 6, message = ResponseCode.ACT_INVALID_ACCOUNT_TYPE)
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = ResponseCode.ACT_INVALID_ACCOUNT_TYPE)
    private String accountType;
    
    @NotNull(message = ResponseCode.ACT_INVALID_ACCOUNT_TYPE_DESC)
    @Size(min = 1, max = 50, message = ResponseCode.ACT_INVALID_ACCOUNT_TYPE_DESC)
    @Pattern(regexp = "^[a-zA-Z0-9 ]*$", message = ResponseCode.ACT_INVALID_ACCOUNT_TYPE_DESC)
    private String accountDescription;
    
    @NotNull(message = ResponseCode.CFG_INVALID_CARDSCHEME_ID)
    @Size(min = 1, max = 6, message = ResponseCode.CFG_INVALID_CARDSCHEME_ID)
    @Pattern(regexp = "^[0-9*]*$", message = ResponseCode.CFG_INVALID_CARDSCHEME_ID)
    private String cardSchemeId;
    
    @NotNull(message = ResponseCode.ISS_INVALID_ISSUER_ACQ_PROFILE)
    @Size(min = 1, max = 4, message = ResponseCode.ISS_INVALID_ISSUER_ACQ_PROFILE)
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = ResponseCode.ISS_INVALID_ISSUER_ACQ_PROFILE)
    private String issuerAcqProfile;
    
    @NotNull(message = ResponseCode.CUR_INVALID_CURRENCY)
    @Size(min = 1, max = 3, message = ResponseCode.CUR_INVALID_CURRENCY)
    @Pattern(regexp = "^[0-9]*$", message = ResponseCode.CUR_INVALID_CURRENCY)
    private String currencyCode;
    
    @Size(min = 0, max = 2, message = ResponseCode.ACT_INVALID_ACCOUNT_ORIGIN)
    @Pattern(regexp = "^[a-zA-Z]*$", message = ResponseCode.ACT_INVALID_ACCOUNT_ORIGIN)
    private String accountOrigin;
    
    @Size(min = 0, max = 10, message = ResponseCode.CFG_INVALID_INSTITUTION_ID)
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = ResponseCode.CFG_INVALID_INSTITUTION_ID)
    private String chargingInstitution;
    
    @NotNull(message = ResponseCode.CFG_INVALID_BANK_CODE)
    @Size(min = 1, max = 10, message = ResponseCode.CFG_INVALID_BANK_CODE)
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = ResponseCode.CFG_INVALID_BANK_CODE)
    private String bankCode;
    
    @NotNull(message = ResponseCode.MER_INVALID_MERCHANT_ACCOUNT_NUMBER)
    @Size(min = 1, max = 30, message = ResponseCode.MER_INVALID_MERCHANT_ACCOUNT_NUMBER)
    @Pattern(regexp = "^[0-9]*$", message = ResponseCode.MER_INVALID_MERCHANT_ACCOUNT_NUMBER)
    private String accountNumber;
    
    @NotNull(message = ResponseCode.JOB_INVALID_DEF_IBAN)
    @Size(min = 1, max = 30, message = ResponseCode.JOB_INVALID_DEF_IBAN)
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = ResponseCode.JOB_INVALID_DEF_IBAN)
    private String iban;
    
    @NotNull(message = ResponseCode.BRA_INVALID_BRANCH)
    @Size(min = 1, max = 10, message = ResponseCode.BRA_INVALID_BRANCH)
    @Pattern(regexp = "^[a-zA-Z0-9-_ ]*$", message = ResponseCode.BRA_INVALID_BRANCH)
    private String branch;
    
    @NotNull(message = ResponseCode.JOB_INVALID_BENEFICIARY_NAME)
    @Size(min = 1, max = 50, message = ResponseCode.JOB_INVALID_BENEFICIARY_NAME)
    @Pattern(regexp = "^[a-zA-Z0-9-_ ]*$", message = ResponseCode.JOB_INVALID_BENEFICIARY_NAME)
    private String beneficiaryName;
}