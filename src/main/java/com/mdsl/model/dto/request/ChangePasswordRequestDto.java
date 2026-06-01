package com.mdsl.model.dto.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
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
public class ChangePasswordRequestDto {
	@NotNull(message=ResponseCode.USR_INVALID_USER)
	@Min(value=1, message=ResponseCode.USR_INVALID_USER)
	@Max(value=999999, message=ResponseCode.USR_INVALID_USER)
	private int userId;
	
	@Size(min=1, max=100, message=ResponseCode.CFG_INVALID_PASSWORD)
	private String oldPassword; 
	
	@Size(min=1, max=100, message=ResponseCode.CFG_INVALID_PASSWORD)
	private String newPassword; 
}