package com.mdsl.model.dto.request;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
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
public class RoleRequestDto {
	@Min(value=0, message=ResponseCode.ROL_INVALID_ROLE_ID)
	@Max(value=999999, message=ResponseCode.ROL_INVALID_ROLE_ID)
	private Integer roleId;
	
	@NotBlank(message=ResponseCode.ROL_INVALID_ROLE_NAME)
	@Size(min = 1, max = 100, message = ResponseCode.ROL_INVALID_ROLE_NAME)
	@Pattern(regexp="^[a-zA-Z0-9_]*$", message=ResponseCode.ROL_INVALID_ROLE_NAME)
	private String roleName;
	
	@NotBlank(message=ResponseCode.ROL_INVALID_ROLE_DESCRIPTION)
	@Size(min = 1, max = 200, message = ResponseCode.ROL_INVALID_ROLE_DESCRIPTION)
	@Pattern(regexp = "^[a-zA-Z0-9_\\- ]*$", message=ResponseCode.ROL_INVALID_ROLE_DESCRIPTION)
	private String roleDesc;
	
	@Size(min=1, max=1, message = ResponseCode.CFG_INVALID_STATUS)
	@Pattern(regexp="[0-1]", message=ResponseCode.CFG_INVALID_STATUS)
	private String status;
	
	@Valid
	private List<RoleActivityRequestDto> roleActivities;
}