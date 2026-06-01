package com.mdsl.model.dto.request;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import com.mdsl.model.dto.response.SortDTO;
import com.mdsl.utils.ResponseCode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;


@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyRateRequestDto {
	
	private int currencyRateId;
	
	@NotNull(message = ResponseCode.CFG_INVALID_INSTITUTION_ID)
	private String institutionId;
	
	@NotNull(message=ResponseCode.CFG_INVALID_CURRENCY)
//	@Size (max = 3, message = ResponseCode.CFG_INVALID_CURR_CODE)
	private int currencyId;
	
	@NotNull(message = ResponseCode.CUR_INVALID_DATE)
	private Date effectiveDate;
	
	private Date fromDate;
	
	private Date toDate;
	
	@NotNull(message = ResponseCode.CUR_INVALID_BUY_RATE)
	@DecimalMin(value = "0.0", message = ResponseCode.CUR_INVALID_BUY_RATE)
	@DecimalMax(value = "999999999.999999999", message = ResponseCode.CUR_INVALID_BUY_RATE)
	private float buyRate;
	
	@NotNull(message=ResponseCode.CUR_INVALID_MID_RATE)
	@DecimalMin(value = "0.0", message = ResponseCode.CUR_INVALID_MID_RATE)
	@DecimalMax(value = "999999999.999999999", message = ResponseCode.CUR_INVALID_MID_RATE)
	private float midRate;
	
	@NotNull(message=ResponseCode.CUR_INVALID_SELL_RATE)
	@DecimalMin(value = "0.0", message = ResponseCode.CUR_INVALID_SELL_RATE)
	@DecimalMax(value = "999999999.999999999", message = ResponseCode.CUR_INVALID_SELL_RATE)
	private float sellRate;
	
	private List<SortDTO> sort;

	private String sortOrder = "";
	
	private Integer pageNo = 0;
	  
	private Integer pageSize = 20;
	
	
	
	
	public void setEffectiveDate(String effectiveDate) {
		try {
			this.effectiveDate = new SimpleDateFormat("dd/MM/yyyy").parse(effectiveDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public void setFromDate(String fromDate) {
		try {
			this.fromDate = new SimpleDateFormat("dd/MM/yyyy").parse(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public void setToDate(String toDate) {
		try {
			this.toDate = new SimpleDateFormat("dd/MM/yyyy").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public int getCurrencyRateId() {
		return currencyRateId;
	}

	public void setCurrencyRateId(int currencyRateId) {
		this.currencyRateId = currencyRateId;
	}

	public String getInstitutionId() {
		return institutionId;
	}

	public void setInstitutionId(String institutionId) {
		this.institutionId = institutionId;
	}

	public int getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(int currencyId) {
		this.currencyId = currencyId;
	}

	

	public List<SortDTO> getSort() {
		if (this.sort == null || this.sort.isEmpty()) {
			return new ArrayList<SortDTO>();
		}
		return this.sort;
	}

	public void setSort(List<SortDTO> sort) {
		this.sort = sort;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public float getBuyRate() {
		return buyRate;
	}

	public void setBuyRate(float buyRate) {
		this.buyRate = buyRate;
	}

	public float getMidRate() {
		return midRate;
	}

	public void setMidRate(float midRate) {
		this.midRate = midRate;
	}

	public float getSellRate() {
		return sellRate;
	}

	public void setSellRate(float sellRate) {
		this.sellRate = sellRate;
	}

}