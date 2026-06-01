package com.mdsl.model.dto.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.mdsl.utils.ResponseCode;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InstitutionControlRequestDto {
	
	@NotNull(message = ResponseCode.INT_INVALID_INSTITUTION_CONTROL)
    @Min(value = 0, message = ResponseCode.INT_INVALID_INSTITUTION_CONTROL)
    @Max(value = 999999999, message = ResponseCode.INT_INVALID_INSTITUTION_CONTROL)
    private Integer recordSeqId;
	
    @NotNull(message = ResponseCode.CFG_INVALID_INSTITUTION_ID)
    @Size(min = 1, max = 10, message = ResponseCode.CFG_INVALID_INSTITUTION_ID)
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = ResponseCode.CFG_INVALID_INSTITUTION_ID)
    private String institutionId;
   
    @NotEmpty(message = ResponseCode.CUR_INVALID_CURRENCY_CODE)
	@Size(max = 3, message = ResponseCode.CUR_INVALID_CURRENCY_CODE)
	private String baseCurrency;
    
    @NotEmpty(message = ResponseCode.MER_INVALID_MERCHANT_RATE_USAGE)
    @Size(max=1, message = ResponseCode.MER_INVALID_MERCHANT_RATE_USAGE)
	@Pattern(regexp="[B-M-S]", message=ResponseCode.MER_INVALID_MERCHANT_RATE_USAGE)
	private String merchantRateUsage;
    
    @NotNull(message = ResponseCode.MER_INVALID_WEEKDAY)
    @Size(min = 1, max = 10, message = ResponseCode.MER_INVALID_WEEKDAY)
    @Pattern(regexp = "^[a-zA-Z]*$", message = ResponseCode.MER_INVALID_WEEKDAY)
    private String weekDay;
    
    @NotNull(message = ResponseCode.INT_INVALID_OUTPUT_DIRECTORY)
    @Size(min = 1, max = 40, message = ResponseCode.INT_INVALID_OUTPUT_DIRECTORY)
    private String outputDirectory;
    
    @NotNull(message = ResponseCode.INT_INVALID_INPUT_DIRECTORY)
    @Size(min = 1, max = 40, message = ResponseCode.INT_INVALID_INPUT_DIRECTORY)
    private String inputDirectory;
    
    @NotNull(message = ResponseCode.INT_INVALID_DISCOUNT_RETURN_ON)
    @Min(value = 0, message = ResponseCode.INT_INVALID_DISCOUNT_RETURN_ON)
    @Max(value = 31, message = ResponseCode.INT_INVALID_DISCOUNT_RETURN_ON)
    private Integer discountReturnOn;
    
    @NotEmpty(message = ResponseCode.INT_INVALID_EOD_PROCESS_BY_TXN)
    @Size(max=1, message = ResponseCode.INT_INVALID_EOD_PROCESS_BY_TXN)
	@Pattern(regexp="[0-1]", message=ResponseCode.INT_INVALID_EOD_PROCESS_BY_TXN)
	private String eodProcessByTxn;

}
