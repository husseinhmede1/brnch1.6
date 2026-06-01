package com.mdsl.model.dto.request;

import java.util.List;

import javax.validation.Valid;
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
public class OutputFileTemplateHdrRequestDto {
	@NotNull(message=ResponseCode.CFG_INVALID_OUTPUT_TEMPLATE_HDR_ID)
	@Min(value = 0, message = ResponseCode.CFG_INVALID_OUTPUT_TEMPLATE_HDR_ID)
	@Max(value = 999999999, message = ResponseCode.CFG_INVALID_OUTPUT_TEMPLATE_HDR_ID)
	private Integer outputTemplateHdrId;
	
	@NotNull(message=ResponseCode.CFG_INVALID_INSTITUTION_ID)
	@Size(min=1, max=10, message=ResponseCode.CFG_INVALID_INSTITUTION_ID)
	@Pattern(regexp="^[a-zA-Z0-9]*$", message=ResponseCode.CFG_INVALID_INSTITUTION_ID)
	private String institutionId;
	
	@NotNull(message=ResponseCode.CFG_INVALID_OUTPUT_FILE_TYPE)
	@Size(min=1, max=30, message=ResponseCode.CFG_INVALID_OUTPUT_FILE_TYPE)
	@Pattern(regexp="^[a-zA-Z ]*$", message=ResponseCode.CFG_INVALID_OUTPUT_FILE_TYPE)
	private String outputFileType;
	
	@NotNull(message=ResponseCode.CFG_INVALID_OUTPUT_DESCRIPTION)
	@Size(min=1, max=50, message=ResponseCode.CFG_INVALID_OUTPUT_DESCRIPTION)
	@Pattern(regexp="^[a-zA-Z0-9 ]*$", message=ResponseCode.CFG_INVALID_OUTPUT_DESCRIPTION)
	private String outputDescription;
	
	@NotNull(message=ResponseCode.CFG_INVALID_SUM_PER_ACCOUNT)
	@Size(min=1, max=3, message=ResponseCode.CFG_INVALID_SUM_PER_ACCOUNT)
	@Pattern(regexp="^[a-zA-Z]*$", message=ResponseCode.CFG_INVALID_SUM_PER_ACCOUNT)
	private String sumPerAccount;
	
	@NotNull(message=ResponseCode.CFG_INVALID_MERCHANT_SUB_SUMMARY)
	@Size(min=1, max=10, message=ResponseCode.CFG_INVALID_MERCHANT_SUB_SUMMARY)
	@Pattern(regexp="^[a-zA-Z]*$", message=ResponseCode.CFG_INVALID_MERCHANT_SUB_SUMMARY)
	private String merchantSubSummary;
	
	@NotNull(message=ResponseCode.INT_INVALID_INST_SUB_SUMMARY)
	@Size(min=1, max=10, message=ResponseCode.INT_INVALID_INST_SUB_SUMMARY)
	@Pattern(regexp="^[a-zA-Z]*$", message=ResponseCode.INT_INVALID_INST_SUB_SUMMARY)
	private String instSubSummary;
	
	@NotNull(message=ResponseCode.CFG_INVALID_OUTPUT_FORMAT)
	@Size(min=1, max=30, message=ResponseCode.CFG_INVALID_OUTPUT_FORMAT)
	@Pattern(regexp="^[a-zA-Z ]*$", message=ResponseCode.CFG_INVALID_OUTPUT_FORMAT)
	private String outputFormat;
	
	@Size(min=0, max=4, message=ResponseCode.CFG_INVALID_SEPARATOR)
	private String separator;
	
	@NotNull(message=ResponseCode.CFG_INVALID_OUTPUT_FILE_ABBRV)
	@Size(min=1, max=50, message=ResponseCode.CFG_INVALID_OUTPUT_FILE_ABBRV)
	@Pattern(regexp="^[a-zA-Z0-9_]*$", message=ResponseCode.CFG_INVALID_OUTPUT_FILE_ABBRV)
	private String outputFileTypeAbbr;
	
	@Valid
	private List<OutputFileTemplateDetailsRequestDto> outputFileTemplateDetailsHeaderRequestDtos;
	
	@Valid
	private List<OutputFileTemplateDetailsRequestDto> outputFileTemplateDetailsDetailRequestDtos;
	
	@Valid
	private List<OutputFileTemplateDetailsRequestDto> outputFileTemplateDetailsFooterRequestDtos;
}