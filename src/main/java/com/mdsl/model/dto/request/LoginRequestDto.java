package com.mdsl.model.dto.request;

import javax.validation.constraints.NotBlank;
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
public class LoginRequestDto {
	@NotBlank(message=ResponseCode.USR_INVALID_USER_NAME)
	@Size(min=1, max=100, message=ResponseCode.USR_INVALID_USER_NAME)
	private String username;
	
	@NotBlank(message=ResponseCode.CFG_INVALID_PASSWORD)
	@Size(min=1, max=100, message=ResponseCode.CFG_INVALID_PASSWORD)
    private String password;
}