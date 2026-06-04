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
import com.mdsl.model.dto.request.SchemeSpecificRequestDto;
import com.mdsl.model.dto.response.SchemeSpecificResponseDto;
import com.mdsl.model.entity.SchemeSpecific;
import com.mdsl.model.mapper.SchemeSpecificMapper;
import com.mdsl.repository.SchemeSpecificRepository;
import com.mdsl.utils.MakerCheckerEngine;
import com.mdsl.utils.ResponseCode;

@Service
public class SchemeSpecificService {

	@Autowired
	private SchemeSpecificRepository schemeSpecificRepository;

	@Autowired
	private SchemeSpecificMapper schemeSpecificMapper;

	@Autowired
	private MakerCheckerEngine makerCheckerEngine;

	public List<SchemeSpecificResponseDto> fetchAllSchemeSpecific() 
	{
		List<SchemeSpecific> schemeSpecific=schemeSpecificRepository.findAll(Sort.by(Sort.Direction.ASC, "schemeSpecificId"));
		List<SchemeSpecificResponseDto> dto=new ArrayList<SchemeSpecificResponseDto>();
		
		for(SchemeSpecific temp:schemeSpecific)
		{
			SchemeSpecificResponseDto dtoTemp=schemeSpecificMapper.toDto(temp);
			dto.add(dtoTemp);
			
		}
		return dto;
	}

	public SchemeSpecificResponseDto fetchSchemeSpecificById(int id) 
	{
		SchemeSpecific schemeSpecific=schemeSpecificRepository.findById(id).orElseThrow(() -> new BusinessException(ResponseCode.CFG_CARDSCHEME_SPEC_NOT_FOUND, HttpStatus.NOT_FOUND));
		SchemeSpecificResponseDto dto=schemeSpecificMapper.toDto(schemeSpecific);
		return dto;
	}

	public SchemeSpecificResponseDto saveOrUpdateSchemeSpecific(@Valid SchemeSpecificRequestDto schemeSpecificRequestDto) 
	{
		SchemeSpecific schemeSpecific;
		SchemeSpecific finalList;
		
		if(Objects.isNull(schemeSpecificRequestDto.getSchemeSpecificId()) || schemeSpecificRequestDto.getSchemeSpecificId()==0)
		{
			schemeSpecific=schemeSpecificMapper.toEntity(schemeSpecificRequestDto);
		}
		else
		{
			schemeSpecific=schemeSpecificRepository.findById(schemeSpecificRequestDto.getSchemeSpecificId())
					.orElseThrow(() -> new BusinessException(ResponseCode.CFG_CARDSCHEME_SPEC_NOT_FOUND, HttpStatus.NOT_FOUND));
			
			schemeSpecific.setSchemeSpecificName(schemeSpecificRequestDto.getSchemeSpecificName());
		}

		if (makerCheckerEngine.processIfRequired(schemeSpecificRequestDto, this.getClass().getName(), new Object() {}.getClass().getEnclosingMethod().getName(), "")) {
			return null;
		}
		finalList=schemeSpecificRepository.save(schemeSpecific);
		SchemeSpecificResponseDto dto=schemeSpecificMapper.toDto(finalList);
		return dto;
	}

	public void deleteSchemeSpecificById(int id) throws Exception {
		schemeSpecificRepository.findById(id).orElseThrow(
				() -> new BusinessException(ResponseCode.CFG_CARDSCHEME_SPEC_NOT_FOUND, HttpStatus.NOT_FOUND));
		if (makerCheckerEngine.processIfRequired(id, this.getClass().getName(), new Object() {}.getClass().getEnclosingMethod().getName(), "")) {
			return;
		}
		schemeSpecificRepository.deleteById(id);
	}
	
	
	

}
