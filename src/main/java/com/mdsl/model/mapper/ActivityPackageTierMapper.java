package com.mdsl.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mdsl.model.dto.request.ActivityPackageTierRequestDto;
import com.mdsl.model.dto.response.ActivityPackageTierResponseDto;
import com.mdsl.model.entity.ActivityPackageTier;

@Mapper
public interface ActivityPackageTierMapper {

	@Mapping(source="activityPackageDetail.packageDetailId",target="packageDetailId")
	ActivityPackageTierResponseDto toDto(ActivityPackageTier activityPackageTier);
	
	ActivityPackageTier toEntity(ActivityPackageTierRequestDto activityPackageTierRequestDto);
}
