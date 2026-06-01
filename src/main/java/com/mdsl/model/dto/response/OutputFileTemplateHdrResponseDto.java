package com.mdsl.model.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OutputFileTemplateHdrResponseDto {
	private Integer outputTemplateHdrId;
	private String institutionId;
	private String outputFileType;
	private String outputDescription;
	private String sumPerAccount;
	private String merchantSubSummary;
	private String instSubSummary;
	private String outputFormat;
	private String separator;
	private String outputFileTypeAbbr;
	private List<BankFilesOutputResponseDto> bankFilesOutputResponseDto;
}