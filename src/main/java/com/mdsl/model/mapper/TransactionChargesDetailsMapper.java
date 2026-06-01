package com.mdsl.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mdsl.model.dto.request.TransactionChargesDetailDto;

import com.mdsl.model.entity.TransactionChargeDetails;

@Mapper
public interface TransactionChargesDetailsMapper {

	@Mapping(source="institution.institutionId",target="institutionId")
	TransactionChargesDetailDto toDto(TransactionChargeDetails transactionChargeDetails);
	
	@Mapping(source="institutionId",target="institution.institutionId")
	TransactionChargeDetails toEntity(TransactionChargesDetailDto chargesDetailDto);
}
