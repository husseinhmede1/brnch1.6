package com.mdsl.model.mapper;

import org.mapstruct.Mapper;

import com.mdsl.model.dto.request.MerchantTypeRequestDto;
import com.mdsl.model.dto.response.MerchantTypeResponseDto;
import com.mdsl.model.entity.MerchantType;

@Mapper
public interface MerchantTypeMapper {

	default MerchantTypeResponseDto toDto(MerchantType merchantType)
	{
		MerchantTypeResponseDto a = new MerchantTypeResponseDto();
		a.setMerchantTypeId(merchantType.getMerchantTypeId());
		a.setMerchantTypeName(merchantType.getMerchantTypeName());
		return a;
	}
	
	default MerchantType toEntity(MerchantTypeRequestDto dto)
	{
		MerchantType a=new MerchantType();
		a.setMerchantTypeName(dto.getMerchantTypeName());
		return a;
	}
}
