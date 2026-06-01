package com.mdsl.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mdsl.model.dto.request.AcquiringTransactionRequestDto;
import com.mdsl.model.dto.request.RepresentmentRequestDto;
import com.mdsl.model.dto.response.AcquiringTransactionResponseDto;
import com.mdsl.model.dto.response.RepresentmentResponseDto;
import com.mdsl.model.entity.AcquiringTransaction;
import com.mdsl.model.entity.Representment;

@Mapper
public interface RepresentmentMapper {

	RepresentmentResponseDto toDto(Representment representment);
	Representment toEntity(RepresentmentRequestDto representmentRequestDto);
}