package com.mdsl.model.dto.response;

import com.mdsl.model.entity.Country;
import com.mdsl.model.entity.Province;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class CityResponseDto {
	private int cityId;
	private String cityAbbrev;
	private String cityName;
	private String cityNameAlt;
	private String cntryCode;
	private String provStateAbbrev;

}