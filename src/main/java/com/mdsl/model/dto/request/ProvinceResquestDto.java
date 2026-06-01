package com.mdsl.model.dto.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.mdsl.utils.ResponseCode;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ProvinceResquestDto {

	private int provinceId;

	@NotEmpty(message = ResponseCode.CFG_INVALID_COUNTRY_CODE)
	private int cntryId;

	@NotEmpty(message = ResponseCode.PRV_INVALID_PROVINCE_STATE_ABBREV)
	@Size(max = 3, message = ResponseCode.PRV_INVALID_PROVINCE_STATE_ABBREV)
	private String provStateAbbrev;

	@Size(max = 999999, message = ResponseCode.PRV_INVALID_PROVINCE_STATE_NUM)
	private int provStateNumCd;

	@Size(max = 50, message = ResponseCode.PRV_INVALID_PROVINCE_STATE)
	private String provinceState;

	@Size(max = 50, message = ResponseCode.PRV_INVALID_PROVINCE_STATE_ALT)
	private String provinceStateAlt;
}