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
public class IssuerRelationRequestDto {
	
	@NotNull(message=ResponseCode.ISS_INVALID_ISSUER_RELATION_ID)
	@Min(value = 0, message = ResponseCode.ISS_INVALID_ISSUER_RELATION_ID)
	@Max(value = 999999999, message = ResponseCode.ISS_INVALID_ISSUER_RELATION_ID)
	private int recordSeqId;
	
	@NotNull(message=ResponseCode.ISS_INVALID_ISSUER_ACQ_PROFILE)
	@Size(min=1, max=4, message=ResponseCode.ISS_INVALID_ISSUER_ACQ_PROFILE)
	@Pattern(regexp="^[a-zA-Z0-9-*_]*$", message=ResponseCode.ISS_INVALID_ISSUER_ACQ_PROFILE)
	private String issuerAcqProfile;

	@NotNull(message=ResponseCode.CFG_INVALID_INSTITUTION_ID)
	@Size(min=1, max=10, message=ResponseCode.CFG_INVALID_INSTITUTION_ID)
	@Pattern(regexp="^[a-zA-Z0-9]*$", message=ResponseCode.CFG_INVALID_INSTITUTION_ID)
	private String institutionId;
	
	@NotNull(message=ResponseCode.ISS_INVALID_PAN_RANGE)
	@Size(min=1, max=19, message=ResponseCode.ISS_INVALID_PAN_RANGE)
	@Pattern(regexp="^[0-9]*$", message=ResponseCode.ISS_INVALID_PAN_RANGE)
	private String panRangeFrom;
	
	@NotNull(message=ResponseCode.ISS_INVALID_PAN_RANGE)
	@Size(min=1, max=19, message=ResponseCode.ISS_INVALID_PAN_RANGE)
	@Pattern(regexp="^[0-9]*$", message=ResponseCode.ISS_INVALID_PAN_RANGE)
	private String panRangeTo;
	
	//@NotNull(message=ResponseCode.CFG_INVALID_COUNTRY)
	@Size(min=0, max=3, message=ResponseCode.CFG_INVALID_COUNTRY_ID)
	@Pattern(regexp="^[a-zA-Z0-9]*$", message=ResponseCode.CFG_INVALID_COUNTRY_ID)
	private String cntryCode;
}