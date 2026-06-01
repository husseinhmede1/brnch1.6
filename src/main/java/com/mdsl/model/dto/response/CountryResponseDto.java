package com.mdsl.model.dto.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CountryResponseDto{
	private int cntryId;
    private String cntryCode; 
	private String cntryName; 
	private String cntryNameAlt; 
	private String cntryCodeAlpha2; 
	private String cntryCodeAlpha3; 
	private int currencyId;
	private String currencyCode;
	private String currencyName;
	private String currPattern; 
	private String datePattern;
	private String economicAreaInd;
	private char cntryStatus;
}