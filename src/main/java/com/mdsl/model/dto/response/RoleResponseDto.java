package com.mdsl.model.dto.response;

import java.util.List;

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
public class RoleResponseDto {
	private int roleId;
	private String roleName;
	private String roleDesc;
	private char status;
	private Boolean isSystemAdminRole;
	private List<RoleActivityResponseDto> roleActivities;
}