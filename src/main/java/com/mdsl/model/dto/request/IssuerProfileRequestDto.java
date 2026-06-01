package com.mdsl.model.dto.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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
public class IssuerProfileRequestDto {
	@NotNull(message=ResponseCode.ISS_INVALID_ISSUER_PROFILE_ID)
	@Min(value = 0, message = ResponseCode.ISS_INVALID_ISSUER_PROFILE_ID)
	@Max(value = 999, message = ResponseCode.ISS_INVALID_ISSUER_PROFILE_ID)
	private Integer profileId;
	
	@NotNull(message=ResponseCode.ISS_INVALID_ISSUER_PROFILE_DESCRIPTION)
	@Size(min=1, max=50, message=ResponseCode.ISS_INVALID_ISSUER_PROFILE_DESCRIPTION)
	@Pattern(regexp="^[a-zA-Z0-9 ]*$", message=ResponseCode.ISS_INVALID_ISSUER_PROFILE_DESCRIPTION)
	private String profileDescription;
	
	@NotNull(message=ResponseCode.ISS_INVALID_ISSUER_ACQ_PROFILE)
	@Size(min=1, max=4, message=ResponseCode.ISS_INVALID_ISSUER_ACQ_PROFILE)
	@Pattern(regexp="^[a-zA-Z0-9_*-]*$", message=ResponseCode.ISS_INVALID_ISSUER_ACQ_PROFILE)
	private String issuerAcqProfile;

	@NotNull(message=ResponseCode.CFG_INVALID_INSTITUTION_ID)
	@Size(min=1, max=10, message=ResponseCode.CFG_INVALID_INSTITUTION_ID)
	@Pattern(regexp="^[a-zA-Z0-9]*$", message=ResponseCode.CFG_INVALID_INSTITUTION_ID)
	private String institutionId;
}