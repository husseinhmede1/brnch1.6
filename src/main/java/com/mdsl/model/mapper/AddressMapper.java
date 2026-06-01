package com.mdsl.model.mapper;

import com.mdsl.model.dto.request.AddressRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mdsl.model.dto.response.AddressResponseDto;
import com.mdsl.model.entity.Address;

@Mapper
public interface AddressMapper {

	@Mapping(source="institution.institutionId",target="institutionId")
	@Mapping(source="entitiesObject.entityId",target="entityId")
	@Mapping(source="cntryCode.cntryId",target="cntryId")
	@Mapping(source="cityCode.cityId",target="cityId")
	AddressResponseDto toDto(Address address);

	Address toEntity(AddressResponseDto addressResponseDto);

	Address toEntity(AddressRequestDto addressResponseDto);
}
