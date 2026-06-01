package com.mdsl.model.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InstitutionAccountsResponseDto {
	private Integer institutionAcctsId;
    private String institutionId;
    private String accountType;
    private String accountDescription;
    private String cardSchemeId;
    private String issuerAcqProfile;
    private String currencyCode;
    private String currencyName;
    private String accountOrigin;
    private String chargingInstitution;
    private String chargingInstitutionName;
    private String bankCode;
    private String accountNumber;
    private String iban;
    private String branch;
    private String beneficiaryName;
}