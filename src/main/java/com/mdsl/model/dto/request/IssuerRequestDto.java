package com.mdsl.model.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.mdsl.utils.ResponseCode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssuerRequestDto {
	
	
	private int issuerId;
	
	@Size(min=1, max=4, message=ResponseCode.ISS_INVALID_ISSUER_PROFILE)
	private int profile;
	@Size(min=0, max=50, message=ResponseCode.ISS_INVALID_ISSUER_PROFILE_DESCRIPTION)
	private String description;
	@NotNull(message=ResponseCode.CFG_INVALID_INSTITUTION_ID)
	@Size(min=1, max=10, message=ResponseCode.CFG_INVALID_INSTITUTION_ID)
	@Pattern(regexp="^[a-zA-Z0-9]*$", message=ResponseCode.CFG_INVALID_INSTITUTION_ID)
	private String institutionId;
	private IssuerRelationRequestDto issuerRelation;
	
}
