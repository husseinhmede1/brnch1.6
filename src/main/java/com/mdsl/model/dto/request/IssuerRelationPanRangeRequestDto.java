package com.mdsl.model.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.mdsl.utils.ResponseCode;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IssuerRelationPanRangeRequestDto {
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
