package com.mdsl.model.mapper;

import com.mdsl.model.dto.request.AccountingTemplateHDRRequestDto;
import com.mdsl.model.dto.response.AccountingTemplateHDRResponseDto;
import com.mdsl.model.entity.AccountingTemplateHDR;
import org.mapstruct.Mapper;

@Mapper
public interface AccountingTemplateHDRMapper
{
  AccountingTemplateHDRResponseDto toDto(AccountingTemplateHDR paramAccountingTemplateHDR);

  AccountingTemplateHDR toEntity(AccountingTemplateHDRRequestDto paramAccountingTemplateHDRRequestDto);
}