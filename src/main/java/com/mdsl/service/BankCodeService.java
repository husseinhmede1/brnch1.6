package com.mdsl.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.BankCodeRequestDto;
import com.mdsl.model.dto.response.BankCodeResponseDto;
import com.mdsl.model.entity.BankCode;
import com.mdsl.model.entity.Institution;
import com.mdsl.model.mapper.BankCodeMapper;
import com.mdsl.repository.BankCodeRepository;
import com.mdsl.repository.InstitutionRepository;
import com.mdsl.utils.ResponseCode;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class BankCodeService {
	
	private final BankCodeRepository bankCodeRepository;
	private final InstitutionRepository institutionRepository;
	
	private final BankCodeMapper bankCodeMapper;
	
	public List<BankCodeResponseDto> getAllBankCodeByInstitution(String instId) {
		Institution institution = institutionRepository.findById(instId)
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_INSTITUTION_ID, HttpStatus.NOT_FOUND));
		List<BankCode> allBankInfos = bankCodeRepository.findBankCodeByInstitutionId(institution.getInstitutionId(),Sort.by(Sort.Direction.ASC, "bankCodeId"));
		List<BankCodeResponseDto> allBankCodeResponseDto = new ArrayList<BankCodeResponseDto>();
		allBankInfos.stream().forEach((bankCode) -> {
			BankCodeResponseDto bankCodeResponseDto = bankCodeMapper.toDto(bankCode);
			bankCodeResponseDto.setInstitutionName(institution.getInstitutionName());
			allBankCodeResponseDto.add(bankCodeResponseDto);
		});
		return allBankCodeResponseDto;
	}
	
	public BankCodeResponseDto getBankCodeById(int bankInfoId) {
		BankCode bankCode = bankCodeRepository.findById(bankInfoId)
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_BANK_CODE_NOT_FOUND, HttpStatus.NOT_FOUND));
		Institution institution = institutionRepository.findById(bankCode.getInstitutionId())
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_INSTITUTION_ID, HttpStatus.NOT_FOUND));
		BankCodeResponseDto bankCodeResponseDto = bankCodeMapper.toDto(bankCode);
		bankCodeResponseDto.setInstitutionName(institution.getInstitutionName());
		return(bankCodeResponseDto);
	}
	
	public BankCodeResponseDto saveOrUpdateBankCode(@Valid BankCodeRequestDto bankCodeRequestDto)
	{
		UserDetailsImpl userDetails = (UserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		BankCode bankCode;
		BankCode finalList;
		
		Institution institution=institutionRepository.findById(bankCodeRequestDto.getInstitutionId())
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_INSTITUTION_ID, HttpStatus.NOT_FOUND));
		
		if(Objects.isNull(bankCodeRequestDto.getBankCodeId()) || bankCodeRequestDto.getBankCodeId() ==0)	//case of create
		{
			if(this.bankCodeRepository.existsByBankCodeAndInstitutionId(bankCodeRequestDto.getBankCode().trim().toUpperCase(),bankCodeRequestDto.getInstitutionId())) {
				throw new BusinessException(ResponseCode.BNK_BANK_CODE_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
			}
			
			bankCode=bankCodeMapper.toEntity(bankCodeRequestDto);
			bankCode.setBankCode(bankCodeRequestDto.getBankCode().trim().toUpperCase());
			bankCode.setSwiftCode(bankCodeRequestDto.getSwiftCode().trim().toUpperCase());
			bankCode.setCreatedBy(Integer.valueOf(userDetails.getId()));
			bankCode.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			bankCode.setUserCreate(String.valueOf(userDetails.getId()));
			bankCode.setDateCreate(new Timestamp(System.currentTimeMillis()));
		}
		else	//case of update
		{
			if(this.bankCodeRepository.existsByBankCodeAndInstitutionIdAndBankCodeIdNot(bankCodeRequestDto.getBankCode().trim().toUpperCase(),bankCodeRequestDto.getInstitutionId(), bankCodeRequestDto.getBankCodeId())) {
				throw new BusinessException(ResponseCode.BNK_BANK_CODE_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
			}
			bankCode=bankCodeRepository.findById(bankCodeRequestDto.getBankCodeId())
					.orElseThrow(() -> new BusinessException(ResponseCode.CFG_BANK_CODE_NOT_FOUND, HttpStatus.NOT_FOUND));
			bankCode.setBankCode(bankCodeRequestDto.getBankCode().trim().toUpperCase());
			bankCode.setSwiftCode(bankCodeRequestDto.getSwiftCode().trim().toUpperCase());
			bankCode.setBankName(bankCodeRequestDto.getBankName());
			bankCode.setAltBankName(bankCodeRequestDto.getAltBankName());
			bankCode.setInstitutionId(institution.getInstitutionId());
			bankCode.setCreatedBy(bankCode.getCreatedBy());
			bankCode.setCreatedDate(bankCode.getCreatedDate());
			bankCode.setUserCreate(bankCode.getUserCreate());
			bankCode.setDateCreate(bankCode.getDateCreate());
			bankCode.setUpdatedBy(Integer.valueOf(userDetails.getId()));
			bankCode.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
		}
		
		finalList=bankCodeRepository.save(bankCode);
		BankCodeResponseDto dto=bankCodeMapper.toDto(finalList);
		dto.setInstitutionName(institution.getInstitutionName());
		return dto;
	}

	
	
	public void deletebankCodeById(int id) throws Exception
	{
		bankCodeRepository.findById(id)
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_BANK_CODE_NOT_FOUND, HttpStatus.NOT_FOUND));
		bankCodeRepository.deleteById(id);
		
	}

	
}
