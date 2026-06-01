package com.mdsl.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NonActivityPackageDetailsResponseDto {

	private Integer recordSeqId;
	private int packageDetailsId;
	private String packageId;
	private String packageName;
	private int currencyId;
	private String currencyCode;
	private String currencyName;
	private String institutionId;
	private String institutionName;
	private String chargeCount;
	private int terminalTypesId;
	private String terminalType;
	private String assignedTransactionId;
	private String assignedTransactionDescription;
	private String cardSchemeId;
	private String cardSchemeName;
	private int numberOfInstallments;
	private String chargeFirstTransaction;
	private float amount;
	private float maxAmount;
	private String startDate;
	private String endDate;
	private char status;
	private Integer frequencySystemCodeId;
	private String frequencyCodeSuffix;
	private String frequencyCodePrefix;
	private String frequencyCodeValue;
	private String frequencyCodeDescription;
	private Integer chargeTypeSystemCodeId;
	private String chargeTypeCodeSuffix;
	private String chargeTypeCodePrefix;
	private String chargeTypeCodeValue;
	private String chargeTypeCodeDescription;



	public void setStartDate(Date startDate) {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        this.startDate = formatter.format(startDate);
	}

	public void setEndDate(Date endDate) {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        this.endDate = formatter.format(endDate);
	}

}
