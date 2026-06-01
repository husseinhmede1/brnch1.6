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
public class AccountingTemplateHDRRequestDto {
	@NotNull(message = ResponseCode.CFG_INVALID_ACCOUNT_TEMPLATE)
    @Min(value = 0L, message = ResponseCode.CFG_INVALID_ACCOUNT_TEMPLATE)
    @Max(value = 999999999L, message = ResponseCode.CFG_INVALID_ACCOUNT_TEMPLATE)
    private Integer acctTemplateHdrId;
	
    @NotNull(message = ResponseCode.INT_INVALID_INSTITUTION)
    @Size(min = 1, max = 10, message = ResponseCode.INT_INVALID_INSTITUTION)
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = ResponseCode.INT_INVALID_INSTITUTION)
    private String institutionId;
    
    @NotNull(message = ResponseCode.ACT_INVALID_ACCOUNT_TEMPLATE)
    @Size(min = 1, max = 10, message = ResponseCode.ACT_INVALID_ACCOUNT_TEMPLATE)
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = ResponseCode.ACT_INVALID_ACCOUNT_TEMPLATE)
    private String accountTemplate;
    
    @NotNull(message = ResponseCode.ACT_INVALID_TEMPLATE_DESCRIPTION)
    @Size(min = 1, max = 50, message = ResponseCode.ACT_INVALID_TEMPLATE_DESCRIPTION)
    @Pattern(regexp = "^[a-zA-Z0-9 ]*$", message = ResponseCode.ACT_INVALID_TEMPLATE_DESCRIPTION)
    private String templateDescription;
    
}