package com.mdsl.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssuerRelationResponseDto {
	int recordSeqId;
	private String issuerAcqProfile;
	private String panRangeFrom;
	private String panRangeTo;
	private String institutionId;
	private String cntryName;
	private String cntryCode;
	private int nbOfRecords;
}
