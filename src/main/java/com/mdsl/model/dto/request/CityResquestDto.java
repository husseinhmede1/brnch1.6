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
public class CityResquestDto {
	private int cityId;

	//@NotEmpty(message = ResponseCode.CFG_INVALID_COUNTRY_ID)
	private String cntryCode;

	@NotEmpty(message = ResponseCode.CFG_INVALID_PROVINCE_ID)
	private String provStateAbbrev;
	@NotEmpty(message = ResponseCode.CFG_INVALID_CITY_ABBREV)
	@Size(max = 3, message = ResponseCode.CFG_INVALID_CITY_ABBREV)
	private String cityAbbrev;
	@NotEmpty(message = ResponseCode.CFG_INVALID_CITY_NAME)
	@Size(max = 50, message = ResponseCode.CFG_INVALID_CITY_NAME)	
	private String cityName;
	@NotEmpty(message = ResponseCode.CFG_INVALID_CITY_NAME_ALT)
	@Size(max = 100, message = ResponseCode.CFG_INVALID_CITY_NAME_ALT)
	private String cityNameAlt;
}
