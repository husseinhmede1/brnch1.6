package com.mdsl.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mdsl.model.dto.request.NonActivityPackageRequestDto;
import com.mdsl.model.dto.response.NonActivityPackageResponseDto;
import com.mdsl.model.entity.NonActivityPackage;

@Mapper
public interface NonActivityPackageMapper {
	@Mapping(source = "institution.institutionId", target = "institutionId")
	@Mapping(source = "institution.institutionName", target = "institutionName")
	NonActivityPackageResponseDto toDto (NonActivityPackage nonActivityPackage); 
	
	NonActivityPackage toEntity (NonActivityPackageRequestDto nonActivityPackageRequestDto);
}
