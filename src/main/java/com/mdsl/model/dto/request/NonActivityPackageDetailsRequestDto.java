package com.mdsl.model.dto.request;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.mdsl.utils.ResponseCode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NonActivityPackageDetailsRequestDto {
	
	private int packageDetailsId;
	
	@NotEmpty(message = ResponseCode.CFG_INVALID_ACT_PKG_ID)
	private String packageId;

	@NotNull(message = ResponseCode.CFG_INVALID_CURRENCY)
	private int currencyId;

	@NotEmpty(message = ResponseCode.CFG_INVALID_INSTITUTION_ID)
	private String institutionId;

	@NotNull(message = ResponseCode.CFG_INVALID_CHARGE_TYPE_MASTER_ID)
	private int chargeMasterId;

	@NotEmpty
	private String chargeCount;
	
	@NotNull(message = ResponseCode.CFG_INVALID_TERMINAL_TYPE_ID)
	private int terminalTypeId;

	private Integer frequencyId;

	@NotNull(message = ResponseCode.INVALID_INSTALLMENTS)
	private Integer numberOfInstallments;
	
	@NotEmpty (message = ResponseCode.CHARGE_FIRST_TRANSACTION)
	private String chargeFirstTransaction;
	
	@NotNull(message = ResponseCode.INVALID_AMOUNT)
	private float amount;
	
	@NotNull(message = ResponseCode.INVALID_AMOUNT)
	private float maxAmount;
	
	private Date startDate;
	
	private Date endDate;
	
	private Character status;
	
	public void setStartDate(String startDate) {
		try {
			this.startDate = new SimpleDateFormat("dd/MM/yyyy").parse(startDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public void setEndDate(String endDate) {
		try {
			this.endDate = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

}
