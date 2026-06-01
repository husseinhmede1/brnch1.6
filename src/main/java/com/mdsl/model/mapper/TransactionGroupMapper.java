package com.mdsl.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mdsl.model.dto.response.TransactionGroupDto;
import com.mdsl.model.entity.TransactionGroup;

@Mapper
public interface TransactionGroupMapper {

	@Mapping(source="institution.institutionId",target="institutionId")
	TransactionGroupDto toDto(TransactionGroup transactionGroup);
	
	@Mapping(source="institutionId",target="institution.institutionId")
	TransactionGroup toEntity(TransactionGroupDto transactionGroupDto);
}
