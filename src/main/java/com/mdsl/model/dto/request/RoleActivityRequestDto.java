package com.mdsl.model.dto.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
public class RoleActivityRequestDto {

	@NotNull(message=ResponseCode.CFG_INVALID_ACTIVITY)
	@Min(value=1, message=ResponseCode.CFG_INVALID_ACTIVITY)
	@Max(value=999999999, message=ResponseCode.CFG_INVALID_ACTIVITY)
	private int activityId;

	@NotEmpty(message=ResponseCode.ROL_INVALID_ACCESS_VIEW)
	@Size(min=1, max=1, message = ResponseCode.ROL_INVALID_ACCESS_VIEW)
	@Pattern(regexp="^[01]$", message=ResponseCode.ROL_INVALID_ACCESS_VIEW)
	private String accessView;
	
	@NotEmpty(message=ResponseCode.ROL_INVALID_ACCESS_ADD)
	@Size(min=1, max=1, message = ResponseCode.ROL_INVALID_ACCESS_ADD)
	@Pattern(regexp="^[01]$", message=ResponseCode.ROL_INVALID_ACCESS_ADD)
	private String accessAdd;

	@NotEmpty(message=ResponseCode.ROL_INVALID_ACCESS_UPDATE)
	@Size(min=1, max=1, message = ResponseCode.ROL_INVALID_ACCESS_UPDATE)
	@Pattern(regexp="^[01]$", message=ResponseCode.ROL_INVALID_ACCESS_UPDATE)
	private String accessUpdate; 
	
	@NotEmpty(message=ResponseCode.ROL_INVALID_ACCESS_DELETE)
	@Size(min=1, max=1, message = ResponseCode.ROL_INVALID_ACCESS_DELETE)
	@Pattern(regexp="^[01]$", message=ResponseCode.ROL_INVALID_ACCESS_DELETE)
	private String accessDelete;

	@NotEmpty(message=ResponseCode.ROL_INVALID_ACCESS_VIEW)
	@Size(min=1, max=1, message = ResponseCode.ROL_INVALID_ACCESS_VIEW)
	@Pattern(regexp="^[01]$", message=ResponseCode.ROL_INVALID_ACCESS_VIEW)
	private String accessChecker;
}