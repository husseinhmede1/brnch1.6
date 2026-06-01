package com.mdsl.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_DEFAULT)
public class InstitutionControlResponseDto {
	private Integer recordSeqId;
	private String institutionId;
	private String baseCurrency;
	private String merchantRateUsage;
	private String weekDay;
	private String outputDirectory;
	private String inputDirectory;
	private Integer discountReturnOn;
	private String eodProcessByTxn;
}