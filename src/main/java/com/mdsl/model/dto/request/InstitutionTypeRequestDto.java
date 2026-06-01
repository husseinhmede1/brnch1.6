package com.mdsl.model.dto.request;

import javax.validation.constraints.Size;

import com.mdsl.utils.ResponseCode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InstitutionTypeRequestDto {
	private int institutionTypeId;
	
	@Size(min=1, max=32, message=ResponseCode.INT_INVALID_INSTITUTION_TYPE)
	private String institutionType;
}
