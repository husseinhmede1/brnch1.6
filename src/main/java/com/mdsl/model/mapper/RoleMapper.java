package com.mdsl.model.mapper;

import com.mdsl.utils.enumerations.AdminDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mdsl.model.dto.request.RoleRequestDto;
import com.mdsl.model.dto.response.RoleResponseDto;
import com.mdsl.model.entity.Role;

import java.util.List;

@Mapper
public interface RoleMapper {

	@Mapping(target = "isSystemAdminRole",expression = "java(isSystemAdminRole(role))")
	RoleResponseDto toDto(Role role);

	List<RoleResponseDto> toDto(List<Role> roles);
	Role toEntity (RoleRequestDto roleRequestDto);

	default Boolean isSystemAdminRole(Role role) {
		return role != null
				&& AdminDetails.ROLE.getValue().equalsIgnoreCase(role.getRoleName());
	}
}