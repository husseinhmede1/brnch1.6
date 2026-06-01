package com.mdsl.model.dto.response;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.*;

@Getter
@Setter
public class CurrencyRateResponseDto {
	private int currencyRateId;
	private String institutionId;
	private String institutionName;
	private int currencyId;
	private String currencyCode;
	private String currencyName;
	private String effectiveDate;
	private float buyRate;
	private float midRate;
	private float sellRate;
	private int recordSeqId;
	private Integer pageNo;
	private Integer pageSize;
	public void setEffectiveDate(Date effectiveDate) {
		Date date = effectiveDate;
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		String strDate = formatter.format(date);
		this.effectiveDate = strDate;
	}
}
