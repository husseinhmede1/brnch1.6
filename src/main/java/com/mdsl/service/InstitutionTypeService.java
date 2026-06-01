package com.mdsl.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.InstitutionTypeRequestDto;
import com.mdsl.model.dto.response.InstitutionTypeResponseDto;
import com.mdsl.model.entity.InstitutionType;
import com.mdsl.model.mapper.InstitutionTypeMapper;
import com.mdsl.repository.InstitutionTypeRepository;
import com.mdsl.utils.ResponseCode;

@Service
public class InstitutionTypeService {

	@Autowired
	private InstitutionTypeRepository institutionTypeRepository;
	@Autowired
	private InstitutionTypeMapper institutionTypeMapper;

	public List<InstitutionTypeResponseDto> getAllInstitutuionType() {
		List<InstitutionType> allInstitutionType = institutionTypeRepository.findAll();
		List<InstitutionTypeResponseDto> allInstitutionTypeResponseDto = new ArrayList<InstitutionTypeResponseDto>();
		allInstitutionType.stream().forEach((institutionType) -> {
			InstitutionTypeResponseDto institutionResponseDto =  institutionTypeMapper.toDto(institutionType);
			allInstitutionTypeResponseDto.add(institutionResponseDto);
		});
		return allInstitutionTypeResponseDto;
	}

	public InstitutionTypeResponseDto saveOrUpdateInstitutionType(InstitutionTypeRequestDto institutionTypeRequestDto) {
		InstitutionType institutionType = null;
		if (institutionTypeRequestDto.getInstitutionTypeId() != 0) {
			institutionType = institutionTypeRepository.findById(institutionTypeRequestDto.getInstitutionTypeId())
					.orElseThrow(() -> new BusinessException(ResponseCode.INT_INSTITUTION_TYPE_NOT_FOUND,
							HttpStatus.NOT_FOUND));
			institutionType.setInstitutionType(institutionTypeRequestDto.getInstitutionType());
			institutionTypeRepository.save(institutionType);
		} else {
			institutionType = new InstitutionType();
			institutionType.setInstitutionType(institutionTypeRequestDto.getInstitutionType());
			institutionTypeRepository.save(institutionType);
		}
		return institutionTypeMapper.toDto(institutionType);
	}

	public String deleteInstitutionType(int Id) {
		institutionTypeRepository.findById(Id)
		.orElseThrow(() -> new BusinessException(ResponseCode.INT_INSTITUTION_TYPE_NOT_FOUND,
				HttpStatus.NOT_FOUND));
		institutionTypeRepository.deleteById(Id);
		return "Institution type Deleted Successfully";
	}
}
