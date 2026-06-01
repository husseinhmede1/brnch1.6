package com.mdsl.model.dto.request;

import java.util.List;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
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
public class UserRequestDto {

	@Min (value=0, message=ResponseCode.CFG_USER_ID_SIZE)
	@Max (value=999999999, message=ResponseCode.CFG_USER_ID_SIZE)
	private Integer userId;
	
	@NotNull(message=ResponseCode.CFG_INVALID_FIRST_NAME)
	@Size(min = 1, max = 100, message = ResponseCode.CFG_INVALID_FIRST_NAME)
	private String firstName;
	
	@NotNull(message=ResponseCode.CFG_INVALID_LAST_NAME)
	@Size(min = 1, max = 100, message = ResponseCode.CFG_INVALID_LAST_NAME)
	private String lastName;

	@NotBlank(message=ResponseCode.CFG_INVALID_USER_NAME)
	@Size(min = 1, max = 100, message = ResponseCode.CFG_INVALID_USER_NAME)
	private String username;
	
	@NotNull(message=ResponseCode.CFG_INVALID_MOBILE)
	@Size(min = 0, max = 20, message = ResponseCode.CFG_INVALID_MOBILE)
	@Pattern(regexp="[0-9]*", message=ResponseCode.CFG_INVALID_MOBILE)
	private String mobile;
	
	@NotNull(message=ResponseCode.CFG_INVALID_EMAIL)
	@Size(min = 1, max = 100, message = ResponseCode.CFG_INVALID_EMAIL)
	@Email(message=ResponseCode.CFG_INVALID_EMAIL)
	private String email;
	
	@NotNull(message=ResponseCode.CFG_INVALID_LANGUAGE)
	private int preferedLanguage; 
	
	private String defaultInstitutionId;
	
	@Size(min=0, max=1, message = ResponseCode.CFG_INVALID_STATUS)
	@Pattern(regexp="[0-1]", message=ResponseCode.CFG_INVALID_STATUS)
	private String status;

	List<InstitutionRoleRequestDto> roleIds;
	
	List<String> institutionId;
}