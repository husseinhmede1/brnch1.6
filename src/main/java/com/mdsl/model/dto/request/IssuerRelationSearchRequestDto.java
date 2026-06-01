package com.mdsl.model.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.mdsl.utils.ResponseCode;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IssuerRelationSearchRequestDto {
	
	@NotNull(message=ResponseCode.ISS_INVALID_ISSUER_ACQ_PROFILE)
	@Size(min=1, max=4, message=ResponseCode.ISS_INVALID_ISSUER_ACQ_PROFILE)
	@Pattern(regexp="^[a-zA-Z0-9*-_]*$", message=ResponseCode.ISS_INVALID_ISSUER_ACQ_PROFILE)
	private String issuerAcqProfile;
	
	@Size(min=0, max=19, message=ResponseCode.ISS_INVALID_PAN_RANGE)
	private String panRange;
	
	@NotNull(message=ResponseCode.CFG_INVALID_SEARCH_CRITERIA)
	private boolean isLikeSearch;
	
	private String institutionId;
	
	private PaginatedRequestDto paginationRequestDto;

}
