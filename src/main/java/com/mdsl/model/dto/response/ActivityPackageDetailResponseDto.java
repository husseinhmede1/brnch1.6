package com.mdsl.model.dto.response;

import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class ActivityPackageDetailResponseDto {
	private int packageDetailId;

	private String packageId;

	private int currencyCodeId;

	private String currencyCode;

	private int tranGroupId;

	private String tranGroupName;

	private String cardSchemeId;

	private String institutionId;

	private String institutionName;

	private String cardScheme;

	private Integer chargeMethodSystemCodeId;

	private String chargeMethodCodeSuffix;

	private String chargeMethod;

	private String tranName;

	private String tranId;

	private int issuerId;

	private String issuer;

	private Character tips;

	private float fixAmount;

	private float percentageAmount;

	private float minAmount;

	private float maxAmount;

	private String startDate;

	private String endDate;

	private char status;
	
	


	public void setStartDate(Date startDate) {
		if (startDate != null) {
			Date date = startDate;
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			String strDate = formatter.format(date);
			this.startDate = strDate;
		} else {
			this.startDate = "";
		}
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		if (endDate != null) {
			Date date = endDate;
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			String edDate = formatter.format(date);
			this.endDate = edDate;
		}
		else {
			this.endDate = "";
		}
	}
}
