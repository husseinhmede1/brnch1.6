package com.mdsl.model.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BankFilesOutputResponseDto {
	private Integer bankFilesOutputId;
	private String institutionId;
	private String bankCode;
	private String outputFileType;
	private String outputFileTypeAbbr;
	private Integer outputTemplateHdrId;
}