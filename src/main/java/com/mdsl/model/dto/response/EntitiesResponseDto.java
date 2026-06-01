package com.mdsl.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntitiesResponseDto {
	private String entityId;
    private String entityName;
    private String entityNameAlt;
    private String dbaName;
    private String dbaNameAlt;
    private String institutionId;
    private String institutionName;
    private int mccId;
    private String mccName;
    private String mccDescription;
    private String activityPackageId;
    private Integer activityPackageIdRecordSeqId;
    private String activityPackageName;
    private String nonActivityPackageId;
    private String nonActivityPackageName;
    private int entityLevelId;
    private int hierarchyLevel;
    private String typeDescription;
    private String parentId;
    private String parentName;
    private String entityStatus;
    private char onHoldInd;
    private char hotMerchantFlag;
    private String companyRegisterNBR;
    private String companyVatNBR;
    private int bankCodeId;
    private String bankCodeName;
    private String bankName;
    private String defAccountNumber;
    private String defIBAN;
    private int defSettlementCurrency;
    private String defSettlementCurrencyName;
    private String defSettlementCurrencyCode;
    private String contractDate;
    private String expStartDate;
    private String actualStartDate;
    private String terminationDate;
    private String lastTransDate;
    private char associatedPayment;
    private String paymentMethod;
    private String paymentFrequency;
    private int addValueDateDays;
    private char statementType;
    private int salesmanId;
    private String salesmanName;
    private int employeeIncharge;
    private String employeeInchargeName;
    private char eStatementToEntity;
    private char status;
    private Integer acctTemplateHdrId;
    private Byte isCloned;
    private Integer businessTypeSystemCodeId;
    private String businessTypeCodeSuffix;
    private String businessTypeCodePrefix;
    private String businessTypeCodeValue;
    private String businessTypeCodeDescription;
    private Integer pageNo;
    private Integer pageSize;
}