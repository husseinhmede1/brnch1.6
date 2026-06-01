package com.mdsl.model.mapper;

import com.mdsl.model.dto.request.AccountingTemplateDetailsRequestDto;
import com.mdsl.model.dto.response.AccountingTemplateDetailsResponseDto;
import com.mdsl.model.entity.AccountingTemplateDetails;
import org.mapstruct.Mapper;

@Mapper
public abstract interface AccountingTemplateDetailsMapper
{
  public abstract AccountingTemplateDetailsResponseDto toDto(AccountingTemplateDetails paramAccountingTemplateDetails);

  public abstract AccountingTemplateDetails toEntity(AccountingTemplateDetailsRequestDto paramAccountingTemplateDetailsRequestDto);
}