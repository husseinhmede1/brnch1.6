package com.mdsl.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MCCListResponseDto {

	private int mccId;
	private String cardSchemeId;
	private String mcc;
	private Integer merchantTypeSystemCodeId;
	private String merchantTypeCodeSuffix;
	private String merchantTypeCodePrefix;
	private String merchantTypeCodeValue;
	private String merchantTypeCodeDescription;
	private String description;
	private String cardSchemeName;
	private Integer pageNo;

	private Integer pageSize;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCardSchemeName() {
		return cardSchemeName;
	}

	public void setCardSchemeName(String cardSchemeName) {
		this.cardSchemeName = cardSchemeName;
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
