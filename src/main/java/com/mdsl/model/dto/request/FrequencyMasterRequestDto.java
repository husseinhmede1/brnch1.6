package com.mdsl.model.dto.request;

import javax.validation.constraints.NotEmpty;

import com.mdsl.utils.ResponseCode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FrequencyMasterRequestDto {
	private int frequencyId;
	
	@NotEmpty(message = ResponseCode.FRQ_INVALID_FREQUENCY_LIMIT)
	private String frequency;
}
