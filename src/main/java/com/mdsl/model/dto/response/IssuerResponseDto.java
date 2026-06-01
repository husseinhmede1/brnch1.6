package com.mdsl.model.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssuerResponseDto {
	private int issuerId;
	private String institutionId;
	private String profile;
	private String description;
	private List<IssuerRelationResponseDto> issuerRelation;
}
