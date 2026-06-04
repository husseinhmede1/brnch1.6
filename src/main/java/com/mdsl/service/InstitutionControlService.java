package com.mdsl.service;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.InstitutionControlRequestDto;
import com.mdsl.model.dto.response.InstitutionControlResponseDto;
import com.mdsl.model.entity.Institution;
import com.mdsl.model.entity.InstitutionControl;
import com.mdsl.model.mapper.InstitutionControlMapper;
import com.mdsl.repository.CurrencyRepository;
import com.mdsl.repository.InstitutionControlRepository;
import com.mdsl.repository.InstitutionRepository;
import com.mdsl.utils.MakerCheckerEngine;
import com.mdsl.utils.ResponseCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InstitutionControlService {
	
	private final InstitutionControlRepository institutionControlRepository;
	private final InstitutionRepository institutionRepository;
	private final CurrencyRepository currencyRepository;
	
	private final InstitutionControlMapper institutionControlMapper;
	private final MakerCheckerEngine makerCheckerEngine;

	public InstitutionControlResponseDto getInstitutionControlByInstId(String instId) {
		Institution institution = this.institutionRepository.findById(instId)
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
		InstitutionControl institutionControl = this.institutionControlRepository.findByInstitutionId(institution.getInstitutionId())
				.orElseThrow(() -> new BusinessException(ResponseCode.INT_INSTITUTION_CONTROL_NOT_FOUND, HttpStatus.NOT_FOUND));
		return this.institutionControlMapper.toDto(institutionControl);
	}
	
	 public InstitutionControlResponseDto saveInstitutionControl(InstitutionControlRequestDto institutionControlRequestDto) {
		 	Optional<InstitutionControl> requestInstitutionControl = this.institutionControlRepository.findById(institutionControlRequestDto.getRecordSeqId());
		 
	        if (this.institutionRepository.findById(institutionControlRequestDto.getInstitutionId().trim()).isEmpty()) {
	            throw new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND);
	        }
	        if (this.currencyRepository.findByCurrencyCode(institutionControlRequestDto.getBaseCurrency().trim()).isEmpty()) {
	            throw new BusinessException(ResponseCode.CUR_CURRENCY_NOT_FOUND, HttpStatus.NOT_FOUND);
	        }
	        
	        InstitutionControl savedInstitutionControl;
	        if (makerCheckerEngine.processIfRequired(institutionControlRequestDto, this.getClass().getName(), new Object() {}.getClass().getEnclosingMethod().getName(), "")) {
	        	return null;
	        }
	        if (requestInstitutionControl.isPresent()) { //Case of update
	        	if(this.institutionControlRepository.existsByInstitutionIdAndRecordSeqIdNot(institutionControlRequestDto.getInstitutionId(), institutionControlRequestDto.getRecordSeqId())) {
	        		throw new BusinessException(ResponseCode.INT_INSTITUTION_CONTROL_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
	        	}
	        	
	        	InstitutionControl saveInstitutionControl = this.institutionControlMapper.toEntity(institutionControlRequestDto);
	        	savedInstitutionControl = this.institutionControlRepository.save(saveInstitutionControl);
	        } else { //Case of create
	        	if(this.institutionControlRepository.existsByInstitutionId(institutionControlRequestDto.getInstitutionId())) {
	        		throw new BusinessException(ResponseCode.INT_INSTITUTION_CONTROL_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
	        	}
	        	
	        	InstitutionControl saveInstitutionControl = this.institutionControlMapper.toEntity(institutionControlRequestDto);
	        	savedInstitutionControl = this.institutionControlRepository.save(saveInstitutionControl);
	        }
	    	return this.institutionControlMapper.toDto(savedInstitutionControl);
	 }
	 
	 public void deleteInstitutionControlByInstitutionId(String instId) {
		 InstitutionControl institutionControl = this.institutionControlRepository.findByInstitutionId(instId)
					.orElseThrow(() -> new BusinessException(ResponseCode.INT_INSTITUTION_CONTROL_NOT_FOUND, HttpStatus.NOT_FOUND));
		 if (makerCheckerEngine.processIfRequired(instId, this.getClass().getName(), new Object() {}.getClass().getEnclosingMethod().getName(), "")) {
			 return;
		 }
	     this.institutionControlRepository.delete(institutionControl);
	 }

}
