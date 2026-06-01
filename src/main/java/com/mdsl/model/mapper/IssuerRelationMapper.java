package com.mdsl.model.mapper;

import org.mapstruct.Mapper;

import com.mdsl.model.dto.request.IssuerRelationRequestDto;
import com.mdsl.model.dto.response.IssuerRelationResponseDto;
import com.mdsl.model.entity.IssuerRelation;

@Mapper
public interface IssuerRelationMapper {
	
	IssuerRelationResponseDto toDto(IssuerRelation issuerRelation);
	IssuerRelation toEntity(IssuerRelationRequestDto issuerRelationRequestDto);
}
