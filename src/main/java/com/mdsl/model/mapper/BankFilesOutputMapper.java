package com.mdsl.model.mapper;

import org.mapstruct.Mapper;

import com.mdsl.model.dto.request.BankFilesOutputRequestDto;
import com.mdsl.model.dto.response.BankFilesOutputResponseDto;
import com.mdsl.model.entity.BankFilesOutput;

@Mapper
public interface BankFilesOutputMapper {
	BankFilesOutputResponseDto toDto(BankFilesOutput bankFilesOutput);
	BankFilesOutput toEntity(BankFilesOutputRequestDto bankFilesOutputRequestDto);
}	
