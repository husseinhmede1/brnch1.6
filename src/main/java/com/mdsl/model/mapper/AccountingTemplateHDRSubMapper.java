package com.mdsl.model.mapper;

import org.mapstruct.Mapper;

import com.mdsl.model.dto.request.AccountingTemplateHDRSubRequestDto;
import com.mdsl.model.dto.response.AccountingTemplateHDRSubResponseDto;
import com.mdsl.model.entity.AccountingTemplateHDRSub;

@Mapper
public interface AccountingTemplateHDRSubMapper {
	AccountingTemplateHDRSubResponseDto toDto(AccountingTemplateHDRSub accountingTemplateHDRSub);
	AccountingTemplateHDRSub toEntity(AccountingTemplateHDRSubRequestDto accountingTemplateHDRSubRequestDto);
}
