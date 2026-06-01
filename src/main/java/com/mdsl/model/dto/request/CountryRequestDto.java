
package com.mdsl.model.dto.request;

import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.mdsl.utils.ResponseCode;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CountryRequestDto {
	private int cntryId;

	@Size(min=1, max = 3, message = ResponseCode.CFG_INVALID_COUNTRY_CODE)
	private String cntryCode;
	@Size(max = 50, message = ResponseCode.CFG_INVALID_COUNTRY_NAME)
	private String cntryName;
	@Size(max = 100, message = ResponseCode.CFG_INVALID_COUNTRY_NAME_ALT)
	private String cntryNameAlt;
	@Size(max = 2, message = ResponseCode.CFG_INVALID_COUNTRY_ALPHA)
	private String cntryCodeAlpha2;
	@Size(max = 3, message = ResponseCode.CFG_INVALID_COUNTRY_ALPHA)
	private String cntryCodeAlpha3;
//	@Size(max = 3, message = ResponseCode.CFG_INVALID_CURR_CODE_LENGTH)
	private int currencyId;
	@Size(max = 20, message = ResponseCode.CUR_INVALID_CURRENCY_PATTERN)
	private String currPattern;
	@Size(max = 20, message = ResponseCode.CUR_INVALID_DATE_PATTERN)
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private String datePattern;
	@Size(max = 4, message = ResponseCode.CFG_ENOCOMIC_AREA)
	private String economicAreaInd;
}
