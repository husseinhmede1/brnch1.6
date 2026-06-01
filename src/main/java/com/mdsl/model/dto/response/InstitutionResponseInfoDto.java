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
public class InstitutionResponseInfoDto {
	private String institutionId;
	private String institutionName;
	private String institutionTypeAlt;
	private char status;
}