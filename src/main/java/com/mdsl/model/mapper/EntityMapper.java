package com.mdsl.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mdsl.model.dto.request.EntityRequestDto;
import com.mdsl.model.dto.response.EntitiesResponseDto;
import com.mdsl.model.entity.Entities;

@Mapper
public interface EntityMapper {
	
	@Mapping(source="institution.institutionId",target="institutionId")
 	@Mapping(source="institution.institutionName",target="institutionName")
	@Mapping(source="salesman.employeeId",target="salesmanId")
	@Mapping(source="salesman.employeeName",target="salesmanName")
	@Mapping(source="employeeIncharge.employeeId",target="employeeIncharge")
	@Mapping(source="employeeIncharge.employeeName",target="employeeInchargeName")
	@Mapping(source="defBankCodeEntity.bankCodeId",target="bankCodeId")
	@Mapping(source="defBankCodeEntity.bankCode",target="bankCodeName")
	@Mapping(source="defBankCodeEntity.bankName",target="bankName")
	@Mapping(source="activityFeePKGEntity.packageId",target="activityPackageId")
	@Mapping(source="activityFeePKGEntity.packageName",target="activityPackageName")
	@Mapping(source="nonactivityFeePKGEntity.packageId",target="nonActivityPackageId")
	@Mapping(source="nonactivityFeePKGEntity.packageName",target="nonActivityPackageName")
	@Mapping(source = "defaultSettlementCurrency.currencyId",target = "defSettlementCurrency")
	@Mapping(source = "defaultSettlementCurrency.currencyCode",target = "defSettlementCurrencyCode")
	@Mapping(source = "defaultSettlementCurrency.currencyName",target = "defSettlementCurrencyName")
	@Mapping(source = "parentIdEntity.entityId",target = "parentId")
	@Mapping(source = "parentIdEntity.entityName",target = "parentName")
	@Mapping(source = "businessType.systemCodeId", target = "businessTypeSystemCodeId")
	@Mapping(source = "businessType.codeSuffix", target = "businessTypeCodeSuffix")
	@Mapping(source = "businessType.codePrefix", target = "businessTypeCodePrefix")
	@Mapping(source = "businessType.codeValue", target = "businessTypeCodeValue")
	@Mapping(source = "businessType.description", target = "businessTypeCodeDescription")
	EntitiesResponseDto toDto(Entities entities);
	
	@Mapping(source="activityFeePKGEntity.packageId",target="activityPackageId")
	@Mapping(source="nonactivityFeePKGEntity.packageId",target="nonActivityPackageId")
	@Mapping(source = "parentIdEntity.entityId",target = "parentId")
 	EntityRequestDto toDtoReq(Entities entities);
	
	@Mapping(source="parentId",target="parentIdEntity.entityId")
	Entities toEntity(EntityRequestDto entityRequestDto);

}
