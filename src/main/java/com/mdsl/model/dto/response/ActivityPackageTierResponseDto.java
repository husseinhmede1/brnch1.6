package com.mdsl.model.dto.response;

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
public class ActivityPackageTierResponseDto {
	private int activityPackageTierId;
	
	private int packageDetailId;

	private Integer frequencySystemCodeId;

	private String frequencyCodeSuffix;

	private String frequencyCodePrefix;

	private String frequencyCodeValue;

	private String frequencyCodeDescription;

	private float percentageAmount;

	private float fixAmount;
	
	private float startAmount;
	
	private float uptoAmount;
}
