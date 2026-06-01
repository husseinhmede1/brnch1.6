package com.mdsl.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.MerchantTypeRequestDto;
import com.mdsl.model.dto.response.MerchantTypeResponseDto;
import com.mdsl.model.entity.MerchantType;
import com.mdsl.model.mapper.MerchantTypeMapper;
import com.mdsl.repository.MerchantTypeRepository;
import com.mdsl.utils.ResponseCode;

@Service
public class MerchantTypeService {
	
	
	@Autowired
	private MerchantTypeRepository merchantTypeRepository;
	
	@Autowired
	private MerchantTypeMapper merchantTypeMapper;

	public List<MerchantTypeResponseDto> fetchAllMerchantType() 
	{
		List<MerchantType> merchantType = merchantTypeRepository.findAll(Sort.by(Sort.Direction.ASC, "merchantTypeId"));
		List<MerchantTypeResponseDto> dto = new ArrayList<MerchantTypeResponseDto>();
		for(MerchantType temp:merchantType)
		{
			MerchantTypeResponseDto dtoTemp = merchantTypeMapper.toDto(temp);
			dto.add(dtoTemp);
		}
		return dto;
	}

	public MerchantTypeResponseDto fetchMerchantTypeById(int id) 
	{
		MerchantType merchantType = merchantTypeRepository.findById(id).orElseThrow(() -> new BusinessException(ResponseCode.MRC_MERCHANT_TYPE_NOT_FOUND, HttpStatus.NOT_FOUND));
		MerchantTypeResponseDto dto = merchantTypeMapper.toDto(merchantType);
		return dto;
	}

	public MerchantTypeResponseDto saveOrUpdateMerchantType(@Valid MerchantTypeRequestDto merchantTypeRequestDto)
	{
		MerchantType merchantType;
		MerchantType finalList;
		
		if(Objects.isNull(merchantTypeRequestDto.getMerchantTypeId()) || merchantTypeRequestDto.getMerchantTypeId()==0)
		{
			merchantType= merchantTypeMapper.toEntity(merchantTypeRequestDto);
		}
		else
		{
			merchantType = merchantTypeRepository.findById(merchantTypeRequestDto.getMerchantTypeId())
					.orElseThrow(() -> new BusinessException(ResponseCode.MRC_MERCHANT_TYPE_NOT_FOUND, HttpStatus.NOT_FOUND));
			merchantType.setMerchantTypeName(merchantTypeRequestDto.getMerchantTypeName());
		}
		
		finalList=merchantTypeRepository.save(merchantType);
		MerchantTypeResponseDto dto=merchantTypeMapper.toDto(finalList);
		return dto;
	}

	public void deleteMerchantTypeById(int id) 
	{
		 merchantTypeRepository.findById(id).orElseThrow(() -> new BusinessException(ResponseCode.MRC_MERCHANT_TYPE_NOT_FOUND, HttpStatus.NOT_FOUND));
		merchantTypeRepository.deleteById(id);
	}
	
	

}
