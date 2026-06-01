package com.mdsl.model.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InstitutionResponseDto {

	private String institutionId;
	private Integer institutionTypeSystemCodeId;
	private String institutionTypeCodeSuffix;
	private String institutionTypeCodePrefix;
	private String institutionTypeCodeValue;
	private String institutionTypeCodeDescription;
	private String institutionName;
	private String institutionTypeAlt;
	private char status;
}