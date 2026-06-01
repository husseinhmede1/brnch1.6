package com.mdsl.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mdsl.model.dto.request.NonActivityPackageDetailsRequestDto;
import com.mdsl.model.dto.response.NonActivityPackageDetailsResponseDto;
import com.mdsl.model.entity.NonActivityPackageDetails;

@Mapper
public interface NonActivityPackageDetailsMapper {
	@Mapping(source = "institution.institutionId", target = "institutionId")
	@Mapping(source = "institution.institutionName", target = "institutionName")
	@Mapping(source = "nonActivityPackageEntity.packageId", target = "packageId")
	@Mapping(source = "nonActivityPackageEntity.packageName", target = "packageName")
	@Mapping(source = "currency.currencyId", target = "currencyId")
	@Mapping(source = "currency.currencyCode", target = "currencyCode")
	@Mapping(source = "currency.currencyName", target = "currencyName")
	@Mapping(source = "terminalTypes.terminalTypesId", target = "terminalTypesId")
	@Mapping(source = "terminalTypes.terminalType", target = "terminalType")
	@Mapping(source = "cardScheme.cardSchemeId", target = "cardSchemeId")
	@Mapping(source = "cardScheme.cardSchemeName", target = "cardSchemeName")
	@Mapping(source = "assignedTransaction.transactionId", target = "assignedTransactionId")
	@Mapping(source = "assignedTransaction.description", target = "assignedTransactionDescription")
	NonActivityPackageDetailsResponseDto toDto (NonActivityPackageDetails nonActivityPackageDetails); 
	
	NonActivityPackageDetails toEntity (NonActivityPackageDetailsRequestDto nonActivityPackageDetailsRequestDto);
}
