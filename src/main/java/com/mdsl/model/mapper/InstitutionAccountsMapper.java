package com.mdsl.model.mapper;

import com.mdsl.model.dto.request.InstitutionAccountsRequestDto;
import com.mdsl.model.dto.response.InstitutionAccountsResponseDto;
import com.mdsl.model.entity.InstitutionAccounts;
import org.mapstruct.Mapper;

@Mapper
public abstract interface InstitutionAccountsMapper
{
  public abstract InstitutionAccountsResponseDto toDto(InstitutionAccounts paramInstitutionAccounts);

  public abstract InstitutionAccounts toEntity(InstitutionAccountsRequestDto paramInstitutionAccountsRequestDto);
}