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
public class BankFilesOutputRequestDto {
	@NotNull(message=ResponseCode.CFG_INVALID_BANK_FILES_OUTPUT_ID)
	@Min(value = 0, message = ResponseCode.CFG_INVALID_BANK_FILES_OUTPUT_ID)
	@Max(value = 999999999, message = ResponseCode.CFG_INVALID_BANK_FILES_OUTPUT_ID)
	private Integer bankFilesOutputId;
	
	@NotNull(message=ResponseCode.CFG_INVALID_INSTITUTION_ID)
	@Size(min=1, max=10, message=ResponseCode.CFG_INVALID_INSTITUTION_ID)
	@Pattern(regexp="^[a-zA-Z0-9]*$", message=ResponseCode.CFG_INVALID_INSTITUTION_ID)
	private String institutionId;
	
	@NotNull(message=ResponseCode.CFG_INVALID_BANK_CODE)
	@Size(min=1, max=10, message=ResponseCode.CFG_INVALID_BANK_CODE)
	@Pattern(regexp="^[a-zA-Z0-9]*$", message=ResponseCode.CFG_INVALID_BANK_CODE)
	private String bankCode;
	
	@NotNull(message=ResponseCode.CFG_INVALID_OUTPUT_FILE_TYPE)
	@Size(min=1, max=30, message=ResponseCode.CFG_INVALID_OUTPUT_FILE_TYPE)
	@Pattern(regexp="^[a-zA-Z ]*$", message=ResponseCode.CFG_INVALID_OUTPUT_FILE_TYPE)
	private String outputFileType;
	
	@NotNull(message=ResponseCode.CFG_INVALID_OUTPUT_TEMPLATE_HDR_ID)
	@Min(value = 0, message = ResponseCode.CFG_INVALID_OUTPUT_TEMPLATE_HDR_ID)
	@Max(value = 999999999, message = ResponseCode.CFG_INVALID_OUTPUT_TEMPLATE_HDR_ID)
	private int outputTemplateHdrId;
	
	@NotNull(message=ResponseCode.CFG_INVALID_OUTPUT_FILE_TYPE_ABBR)
	@Size(min=1, max=50, message=ResponseCode.CFG_INVALID_OUTPUT_FILE_TYPE_ABBR)
	private String outputFileTypeAbbr;
}
