package com.mdsl.model.dto.request;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.mdsl.utils.ResponseCode;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Builder
public class AccountingTemplateDetailsRequestDto {
    @NotNull(message = ResponseCode.ACT_INVALID_ACCOUNTING_TEMPLATE_DTL_ID)
    @Min(value = 0, message = ResponseCode.ACT_INVALID_ACCOUNTING_TEMPLATE_DTL_ID)
    @Max(value = 999999999, message = ResponseCode.ACT_INVALID_ACCOUNTING_TEMPLATE_DTL_ID)
    private Integer acctTemplateDtlId;
    
    @NotNull(message = ResponseCode.CFG_INVALID_INSTITUTION_ID)
    @Size(min = 1, max = 10, message = ResponseCode.CFG_INVALID_INSTITUTION_ID)
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = ResponseCode.CFG_INVALID_INSTITUTION_ID)
    private String institutionId;
    
    @NotNull(message = ResponseCode.CFG_INVALID_ACCOUNT_TEMPLATE_SUB)
    @Min(value = 0, message = ResponseCode.CFG_INVALID_ACCOUNT_TEMPLATE_SUB)
    @Max(value = 999999999, message = ResponseCode.CFG_INVALID_ACCOUNT_TEMPLATE_SUB)
    private Integer acctTemplateHdrSubId;
    
    @NotNull(message = ResponseCode.CFG_INVALID_TRANS_ID)
    @Size(min = 1, max = 12, message = ResponseCode.CFG_INVALID_TRANS_ID)
    @Pattern(regexp = "^[a-zA-Z0-9_-]*$", message = ResponseCode.CFG_INVALID_TRANS_ID)
    private String transId;
    
    @NotNull(message = ResponseCode.ACT_INVALID_ACCOUNT_ORIGIN)
    @Size(min = 1, max = 2, message = ResponseCode.ACT_INVALID_ACCOUNT_ORIGIN)
    @Pattern(regexp = "^[a-zA-Z]*$", message = ResponseCode.ACT_INVALID_ACCOUNT_ORIGIN)
    private String accountOrigin;
    
    @Size(min = 0, max = 10, message = ResponseCode.INT_INVALID_INSTITUTION)
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = ResponseCode.INT_INVALID_INSTITUTION)
    private String destinationInstitution;
    
    @Size(min = 0, max = 6, message = ResponseCode.ACT_INVALID_ACCOUNT_TYPE)
    @Pattern(regexp = "^[a-zA-Z0-9 ]*$", message = ResponseCode.ACT_INVALID_ACCOUNT_TYPE)
    private String accountType;
    
    @NotNull(message = ResponseCode.CFG_INVALID_AMOUNT_TYPE)
    @Size(min = 1, max = 6, message = ResponseCode.CFG_INVALID_AMOUNT_TYPE)
    @Pattern(regexp = "^[a-zA-Z]*$", message = ResponseCode.CFG_INVALID_AMOUNT_TYPE)
    private String amountType;
    
    @Digits(integer = 7, fraction = 3, message = ResponseCode.CFG_INVALID_PERCENTAGE_APPLIED)
    private Double percentageApplied;
    
    @Size(min = 0, max = 5, message = ResponseCode.CFG_INVALID_PERCENT_SRC)
    @Pattern(regexp = "^[a-zA-Z]*$", message = ResponseCode.CFG_INVALID_PERCENT_SRC)
    private String percentSrc;
    
    @NotNull(message = ResponseCode.CFG_INVALID_SIGN_FLAG)
    @Size(min = 1, max = 2, message = ResponseCode.CFG_INVALID_SIGN_FLAG)
    @Pattern(regexp = "^[a-zA-Z]*$", message = ResponseCode.CFG_INVALID_SIGN_FLAG)
    private String signFlag;
    
    @NotNull(message = ResponseCode.CFG_INVALID_LINE_DESCRIPTION)
    @Size(min = 1, max = 50, message = ResponseCode.CFG_INVALID_LINE_DESCRIPTION)
    @Pattern(regexp = "^[a-zA-Z0-9 ]*$", message = ResponseCode.CFG_INVALID_LINE_DESCRIPTION)
    private String lineDescription;
    
    @Min(value = 0, message = ResponseCode.CFG_INVALID_SHOW_FLAG)
    @Max(value = 1, message = ResponseCode.CFG_INVALID_SHOW_FLAG)
    private Integer show;
}