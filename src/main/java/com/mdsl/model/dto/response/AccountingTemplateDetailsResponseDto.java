package com.mdsl.model.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountingTemplateDetailsResponseDto {
	private Integer acctTemplateDtlId;
    private String institutionId;
    private Integer acctTemplateHdrSubId;
    private String transId;
    private String accountOrigin;
    private String destinationInstitution;
    private String destinationInstitutionName;
    private String accountType;
    private String amountType;
    private double percentageApplied;
    private String percentSrc;
    private String signFlag;
    private String lineDescription;
    private Integer show;
}