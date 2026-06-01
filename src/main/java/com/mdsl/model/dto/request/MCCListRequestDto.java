package com.mdsl.model.dto.request;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.mdsl.model.dto.response.SortDTO;
import com.mdsl.utils.ResponseCode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MCCListRequestDto {

	private int mccId; 
	private String cardSchemeId;
	
	@NotEmpty(message=ResponseCode.MCC_INVALID_DETAILS)
	@NotBlank(message = ResponseCode.MCC_INVALID_DETAILS)
	@Size(max = 40, message = ResponseCode.MCC_INVALID_DETAILS)
	private String mcc;
	
	@NotNull(message=ResponseCode.MRC_INVALID_MERCHANT_TYPE_ID)
	@Min(value = 1, message = ResponseCode.MRC_INVALID_MERCHANT_TYPE_ID)
	@Max(value = 999999999, message = ResponseCode.MRC_INVALID_MERCHANT_TYPE_ID)
	private int merchantTypeId;
	
	@NotEmpty(message=ResponseCode.MCC_INVALID_DESCRIPTION)
	@NotBlank(message = ResponseCode.MCC_INVALID_DESCRIPTION)
	@Size(max = 800, message = ResponseCode.MCC_INVALID_DESCRIPTION)
	private String description;
	
	private List<SortDTO> sort;

	private String sortOrder = "";

	private Integer pageNo = 0;

	private Integer pageSize = 20;
	
	private String search;

	public int getMccId() {
		return mccId;
	}

	public void setMccId(int mccId) {
		this.mccId = mccId;
	}

	public String getCardSchemeId() {
		return cardSchemeId;
	}

	public void setCardSchemeId(String cardSchemeId) {
		this.cardSchemeId = cardSchemeId;
	}

	public String getMcc() {
		return mcc;
	}

	public void setMcc(String mcc) {
		this.mcc = mcc;
	}

	public int getMerchantTypeId() {
		return merchantTypeId;
	}

	public void setMerchantTypeId(int merchantTypeId) {
		this.merchantTypeId = merchantTypeId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
	
	

	
}
