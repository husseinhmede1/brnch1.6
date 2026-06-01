package com.mdsl.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.response.OutputFileTemplateDetailsResponseDto;
import com.mdsl.model.entity.OutputFileTemplateDetails;
import com.mdsl.model.entity.OutputFileTemplateHdr;
import com.mdsl.model.entity.SystemCode;
import com.mdsl.model.mapper.OutputFileTemplateDetailsMapper;
import com.mdsl.repository.OutputFileTemplateDetailsRepository;
import com.mdsl.repository.OutputFileTemplateHdrRepository;
import com.mdsl.repository.SystemCodeRepository;
import com.mdsl.utils.ResponseCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OutputFileTemplateDetailsService {
	
	private final OutputFileTemplateDetailsRepository outputFileTemplateDetailsRepository;
	private final OutputFileTemplateHdrRepository outputFileTemplateHdrRepository;
	private final SystemCodeRepository systemCodeRepository;
	
	private final OutputFileTemplateDetailsMapper outputFileTemplateDetailsMapper;
	
	public List<OutputFileTemplateDetailsResponseDto> getOutputFileTemplateDetailsByOutputFileTemplateHdrId(int outputTemplateHdrId) {
		List<OutputFileTemplateDetailsResponseDto> allOutputFileTemplateDetailsResponseDtos = new ArrayList<OutputFileTemplateDetailsResponseDto>();
        OutputFileTemplateHdr outputFileTemplateHdr = this.outputFileTemplateHdrRepository.findById(outputTemplateHdrId).orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_OUTPUT_TEMPLATE_HDR_ID, HttpStatus.NOT_FOUND));
        List<OutputFileTemplateDetails> outputFileTemplateDetails = this.outputFileTemplateDetailsRepository.findByOutputTemplateHdrId(outputFileTemplateHdr.getOutputTemplateHdrId());
        for(OutputFileTemplateDetails outputFileTemplateDetail : outputFileTemplateDetails) {
        	SystemCode systemCode = systemCodeRepository.findById(outputFileTemplateDetail.getFieldId()).orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_FIELD_ID, HttpStatus.NOT_FOUND));
        	OutputFileTemplateDetailsResponseDto outputFileTemplateDetailsResponseDto = this.outputFileTemplateDetailsMapper.toDto(outputFileTemplateDetail);
        	outputFileTemplateDetailsResponseDto.setDescription(systemCode.getDescription());
        	allOutputFileTemplateDetailsResponseDtos.add(outputFileTemplateDetailsResponseDto);
        }
        return allOutputFileTemplateDetailsResponseDtos;
	}
	
	public List<OutputFileTemplateDetailsResponseDto> getOutputFileTemplateDetailsByOutputFileTemplateHdrIdAndFieldSection(int outputTemplateHdrId, String fieldSection) {
		List<OutputFileTemplateDetailsResponseDto> allOutputFileTemplateDetailsResponseDtos = new ArrayList<OutputFileTemplateDetailsResponseDto>();
        OutputFileTemplateHdr outputFileTemplateHdr = this.outputFileTemplateHdrRepository.findById(outputTemplateHdrId).orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_OUTPUT_TEMPLATE_HDR_ID, HttpStatus.NOT_FOUND));
        List<OutputFileTemplateDetails> outputFileTemplateDetails = this.outputFileTemplateDetailsRepository.findByOutputTemplateHdrIdAndFieldSection(outputFileTemplateHdr.getOutputTemplateHdrId(), fieldSection);
        for(OutputFileTemplateDetails outputFileTemplateDetail : outputFileTemplateDetails) {
        	SystemCode systemCode = systemCodeRepository.findById(outputFileTemplateDetail.getFieldId()).orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_FIELD_ID, HttpStatus.NOT_FOUND));
        	OutputFileTemplateDetailsResponseDto outputFileTemplateDetailsResponseDto = this.outputFileTemplateDetailsMapper.toDto(outputFileTemplateDetail);
        	outputFileTemplateDetailsResponseDto.setDescription(systemCode.getDescription());
        	allOutputFileTemplateDetailsResponseDtos.add(outputFileTemplateDetailsResponseDto);
        }
        return allOutputFileTemplateDetailsResponseDtos;
	}
}