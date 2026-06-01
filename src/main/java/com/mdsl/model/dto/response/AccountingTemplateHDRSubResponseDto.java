package com.mdsl.model.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountingTemplateHDRSubResponseDto {
	private Integer acctTemplateHdrSubId;
	private Integer acctTemplateHdrId;
	private String institutionId;
	private String bankCode;
	private String tenplateDescription;
}
