package com.mdsl.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mdsl.model.dto.request.ActivityPackageRequestDto;
import com.mdsl.model.dto.response.ActivityPackageResponseDto;
import com.mdsl.model.entity.ActivityPackage;

@Mapper
public interface ActivityPackageMapper {
	
	@Mapping(source="institution.institutionName",target="institutionName")
	@Mapping(source="institution.institutionId",target="institutionId")
	ActivityPackageResponseDto toDto(ActivityPackage activityPackage);
	
	ActivityPackage toEntity(ActivityPackageRequestDto activityPackageRequestDto);
}
