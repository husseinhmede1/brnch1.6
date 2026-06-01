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
public class AccountingTemplateHDRSubRequestDto {
	
	@NotNull(message = ResponseCode.CFG_INVALID_ACCOUNT_TEMPLATE_SUB)
    @Min(value = 0L, message = ResponseCode.CFG_INVALID_ACCOUNT_TEMPLATE_SUB)
    @Max(value = 999999999L, message = ResponseCode.CFG_INVALID_ACCOUNT_TEMPLATE_SUB)
	private Integer acctTemplateHdrSubId;
	
	@NotNull(message = ResponseCode.CFG_INVALID_ACCOUNT_TEMPLATE)
    @Min(value = 0L, message = ResponseCode.CFG_INVALID_ACCOUNT_TEMPLATE)
    @Max(value = 999999999L, message = ResponseCode.CFG_INVALID_ACCOUNT_TEMPLATE)
	private Integer acctTemplateHdrId;
	
    @NotNull(message = ResponseCode.CFG_INVALID_INSTITUTION_ID)
    @Size(min = 1, max = 10, message = ResponseCode.CFG_INVALID_INSTITUTION_ID)
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = ResponseCode.CFG_INVALID_INSTITUTION_ID)
    private String institutionId;
    
    @NotNull(message=ResponseCode.CFG_INVALID_BANK_CODE)
	@Size(min=1, max=10, message=ResponseCode.CFG_INVALID_BANK_CODE)
	@Pattern(regexp="^[a-zA-Z0-9]*$", message=ResponseCode.CFG_INVALID_BANK_CODE)
    private String bankCode;
    
    @NotNull(message = ResponseCode.ACT_INVALID_TEMPLATE_DESCRIPTION)
    @Size(min = 1, max = 50, message = ResponseCode.ACT_INVALID_TEMPLATE_DESCRIPTION)
    @Pattern(regexp = "^[a-zA-Z0-9 ]*$", message = ResponseCode.ACT_INVALID_TEMPLATE_DESCRIPTION)
    private String tenplateDescription;
}
