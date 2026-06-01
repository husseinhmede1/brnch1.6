package com.mdsl.model.dto.request;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.constraints.*;

import com.mdsl.utils.ResponseCode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityPackageDetailRequestDto {

	@NotNull(message = ResponseCode.CFG_INVALID_ACT_PKG_DETAILS_ID)
	@Digits(integer = 10, fraction = 0, message = ResponseCode.CFG_INVALID_ACT_PKG_DETAILS_ID)
	private String packageDetailId;
	
	@Pattern(regexp = "^[A-Za-z0-9]*$", message = ResponseCode.CFG_INVALID_ACT_PKG_ID)
	private String packageId;

	@Min(value = 1,  message=ResponseCode.CFG_INVALID_CHARGE_METHOD_ID)
	private String chargeMethodId;

	@Min(value = 1, message=ResponseCode.CFG_INVALID_CURRENCY)
	private String currencyCodeId;

	private int tranGroupId;
	
	private String tranGroupName;

	private String tranId;

	@NotEmpty(message=ResponseCode.CFG_INVALID_CARDSCHEME_ID)
	private String cardSchemeId;
	
	@Min(value = 1, message=ResponseCode.CFG_INVALID_ISSUER_ID)
	private String issuerId;

	private char tips;

	private float fixAmount;

	
	private float percentageAmount;


	private float minAmount;


	private float maxAmount;

	private Date startDate;

	private Date endDate;
	
	private Character status;
	
	public void setStartDate(String startDate) {
		try {
			if(startDate.equals("") || startDate.equals(null)) {
				this.startDate = null;
			}
			else {
				this.startDate = new SimpleDateFormat("dd/MM/yyyy").parse(startDate);
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public void setEndDate(String endDate) {
		try {
			if(endDate.equals("") || endDate.equals(null)) {
				this.endDate = null;
			}
			else {
				this.endDate = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

}
