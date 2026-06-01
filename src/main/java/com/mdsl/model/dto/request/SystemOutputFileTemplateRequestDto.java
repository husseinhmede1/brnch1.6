package com.mdsl.model.dto.request;

import com.mdsl.utils.ResponseCode;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
@Builder
public class SystemOutputFileTemplateRequestDto {

	@Size(max = 30, message = ResponseCode.CFG_INVALID_CODE_PREFIX)
	private String codePrefix;

	@Size(max = 100, message = ResponseCode.CFG_INVALID_CODE_VALUE)
	private String codeResult;

	@Size(max = 30, message = ResponseCode.CFG_INVALID_OUTPUT_FILE_TYPE)
	private String outputFileType;

	@Size(max = 7, message = ResponseCode.CFG_INVALID_MERCHANT_SUB_SUMMARY)
	private String merchantSubSummary;

	private String instSubSummary;

	private String summaryBy;
}
