package com.mdsl.model.dto.response;

import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_DEFAULT)
public class UserNewResponseDto {
	private int userId;
	private String username;
	private String firstName;
	private String lastName;
	private String email;
	private char status;
	private Timestamp lastLoginDate;
	private List<UserRoleNewResponseDto> userRoles;
	private List<InstitutionResponseInfoDto> institution;
	@JsonIgnore
	private boolean apiAllowed;
	private String defaultInstitutionId;
	private String defaultInstitutionName;
	private String mobile;
	private Integer preferedLanguageSystemCodeId;
	private String preferedLanguageCodeSuffix;
	private String preferedLanguageCodePrefix;
	private String preferedLanguageCodeValue;
	private String preferedLanguageCodeDescription;
	private Boolean isSystemAdmin;

}