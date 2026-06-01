package com.mdsl.model.dto.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ProvinceResponseDto {
	private int provinceId;
	private String provStateAbbrev;
	private int provStateNumCd;
	private String provinceState;
	private String provinceStateAlt;
}
