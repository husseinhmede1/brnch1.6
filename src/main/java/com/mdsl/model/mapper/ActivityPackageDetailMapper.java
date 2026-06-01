package com.mdsl.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mdsl.model.dto.request.ActivityPackageDetailRequestDto;
import com.mdsl.model.dto.response.ActivityPackageDetailResponseDto;
import com.mdsl.model.entity.ActivityPackageDetail;

@Mapper
public interface ActivityPackageDetailMapper {
	@Mapping(source = "institution.institutionId", target = "institutionId")
	@Mapping(source = "institution.institutionName", target = "institutionName")
	@Mapping(source = "currency.currencyCode", target = "currencyCode")
	@Mapping(source = "currency.currencyId", target = "currencyCodeId")
	@Mapping(source = "cardScheme.cardSchemeId", target = "cardSchemeId")
	@Mapping(source = "cardScheme.cardSchemeName", target = "cardScheme")
	@Mapping(source = "issuerAcqProfileEntity.profileId", target = "issuerId")
	@Mapping(source = "issuerAcqProfileEntity.issuerAcqProfile", target = "issuer")
	@Mapping(source = "activityPackageEntity.packageId", target = "packageId")
	ActivityPackageDetailResponseDto toDto(ActivityPackageDetail activityPackageDetail);

	ActivityPackageDetail toEntity(ActivityPackageDetailRequestDto activityPackageRequestDto);
}
