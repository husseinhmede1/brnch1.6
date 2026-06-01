package com.mdsl.model.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountingTemplateHDRResponseDto {
	 private Integer acctTemplateHdrId;
	 private String institutionId;
	 private String accountTemplate;
	 private String templateDescription;
}
