package com.mdsl.model.dto.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.mdsl.utils.ResponseCode;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OutputFileTemplateDetailsRequestDto {
	@Min(value = 0, message = ResponseCode.CFG_INVALID_OUTPUT_TEMPLATE_DTLS_ID)
	@Max(value = 999999999, message = ResponseCode.CFG_INVALID_OUTPUT_TEMPLATE_DTLS_ID)
	private Integer outputTemplateDtlId;
	
	@Size(min=1, max=10, message=ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND)
	@Pattern(regexp="^[a-zA-Z0-9]*$", message=ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND)
	private String institutionId;
	
	@Min(value = 0, message = ResponseCode.CFG_INVALID_OUTPUT_TEMPLATE_HDR_ID)
	@Max(value = 999999999, message = ResponseCode.CFG_INVALID_OUTPUT_TEMPLATE_HDR_ID)
	private Integer outputTemplateHdrId;
	
	@Size(min=0, max=1, message=ResponseCode.CFG_INVALID_FIELD_SECTION)
	@Pattern(regexp="^[a-zA-Z]*$", message=ResponseCode.CFG_INVALID_FIELD_SECTION)
	private String filedSection;
		
	@Min(value = 0, message = ResponseCode.CFG_INVALID_FIELD_SEQUENCE)
	@Max(value = 9999, message = ResponseCode.CFG_INVALID_FIELD_SEQUENCE)
	private Integer fieldSequence;
	
	@Min(value = 0, message = ResponseCode.CFG_INVALID_FIELD_ID)
	@Max(value = 999999999, message = ResponseCode.CFG_INVALID_FIELD_ID)
	private Integer fieldId;
	
	@Size(min=0, max=1, message=ResponseCode.CFG_INVALID_FIELD_PAD)
	@Pattern(regexp="^[L|R|D]?$", message=ResponseCode.CFG_INVALID_FIELD_PAD)
	private String fieldPad;
	
	@Size(max=1, message=ResponseCode.CFG_INVALID_FIELD_PAD_CHAR)
	private String fieldPadChar;
	
	@Min(value = 0, message = ResponseCode.CFG_INVALID_FIELD_LENGTH)
	@Max(value = 9999, message = ResponseCode.CFG_INVALID_FIELD_LENGTH)
	private Integer fieldLength;
	
	@Size(min=0, max=100, message=ResponseCode.CFG_INVALID_FIELD_FORMAT)
	private String fieldFormat;
	
	private String description;
	
	@Size(min=0, max=999, message=ResponseCode.CFG_INVALID_FIELD_CSYNTAX)
	private String fieldCsyntax;
}