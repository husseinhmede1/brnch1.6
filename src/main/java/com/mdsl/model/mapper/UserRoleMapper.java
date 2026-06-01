package com.mdsl.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mdsl.model.dto.request.UserRoleRequestDto;
import com.mdsl.model.dto.response.UserRoleNewResponseDto;
import com.mdsl.model.dto.response.UserRoleResponseDto;
import com.mdsl.model.entity.UserRole;

@Mapper
public interface UserRoleMapper {
	UserRoleResponseDto toDto (UserRole userRole); 
	
	@Mapping(source="role.roleId",target="roleId")
	@Mapping(source="role.roleName",target="roleName")
	@Mapping(source="role.roleDesc",target="roleDesc")
	@Mapping(source="institution.institutionId",target="instId")
	@Mapping(source="institution.institutionName",target="instName")
	@Mapping(source="role.status",target="status")
	@Mapping(source="role.roleActivities",target="roleActivities")
	UserRoleNewResponseDto toResponseDto(UserRole userRole);
}