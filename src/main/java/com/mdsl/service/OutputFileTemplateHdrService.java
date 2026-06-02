package com.mdsl.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.OutputFileTemplateDetailsRequestDto;
import com.mdsl.model.dto.request.OutputFileTemplateHdrRequestDto;
import com.mdsl.model.dto.response.BankFilesOutputResponseDto;
import com.mdsl.model.dto.response.OutputFileTemplateHdrResponseDto;
import com.mdsl.model.entity.BankFilesOutput;
import com.mdsl.model.entity.Institution;
import com.mdsl.model.entity.OutputFileTemplateDetails;
import com.mdsl.model.entity.OutputFileTemplateHdr;
import com.mdsl.model.mapper.BankFilesOutputMapper;
import com.mdsl.model.mapper.OutputFileTemplateDetailsMapper;
import com.mdsl.model.mapper.OutputFileTemplateHdrMapper;
import com.mdsl.repository.BankFilesOutputRepository;
import com.mdsl.repository.InstitutionRepository;
import com.mdsl.repository.OutputFileTemplateDetailsRepository;
import com.mdsl.repository.OutputFileTemplateHdrRepository;
import com.mdsl.utils.MakerCheckerEngine;
import com.mdsl.utils.ResponseCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OutputFileTemplateHdrService {
	
	private final OutputFileTemplateHdrRepository outputFileTemplateHdrRepository;
	private final InstitutionRepository institutionRepository;
	private final OutputFileTemplateDetailsRepository outputFileTemplateDetailsRepository;
	private final BankFilesOutputRepository bankFilesOutputRepository;
	
	private final OutputFileTemplateHdrMapper outputFileTemplateHdrMapper;
	private final OutputFileTemplateDetailsMapper outputFileTemplateDetailsMapper;
	private final BankFilesOutputMapper bankFilesOutputMapper;
    private final MakerCheckerEngine makerCheckerEngine;

	public List<OutputFileTemplateHdrResponseDto> getAllOutputFileTemplateHdrsByInstitution(String institutionId) {
		List<OutputFileTemplateHdrResponseDto> allOutputFileTemplateResponseDtos = new ArrayList<OutputFileTemplateHdrResponseDto>();
        Institution institution = this.institutionRepository.findById(institutionId.trim()).orElseThrow(() -> new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
        List<OutputFileTemplateHdr> outputFileTemplateHdrs = this.outputFileTemplateHdrRepository.findByInstitutionId(institution.getInstitutionId());
        outputFileTemplateHdrs.stream().forEach((outputFileTemplateHdr) -> {
        	List<BankFilesOutput> listBankFilesOutput  = this.bankFilesOutputRepository.findByOutputTemplateHdrId(outputFileTemplateHdr.getOutputTemplateHdrId());
        	OutputFileTemplateHdrResponseDto outputFileTemplateHdrResponseDto = this.outputFileTemplateHdrMapper.toDto(outputFileTemplateHdr);
        	for(BankFilesOutput bankFilesOutput : listBankFilesOutput) {
        		List<BankFilesOutputResponseDto> listBankFilesOutputResponseDto = new ArrayList<BankFilesOutputResponseDto>();
        		BankFilesOutputResponseDto bankFilesOutputResponseDto = this.bankFilesOutputMapper.toDto(bankFilesOutput);
        		listBankFilesOutputResponseDto.add(bankFilesOutputResponseDto);
            	outputFileTemplateHdrResponseDto.setBankFilesOutputResponseDto(listBankFilesOutputResponseDto);
        	}
        	allOutputFileTemplateResponseDtos.add(outputFileTemplateHdrResponseDto);
		});
        return allOutputFileTemplateResponseDtos;
	}
	
	public OutputFileTemplateHdrResponseDto getOutputFileTemplateHdrById(int id) {
		OutputFileTemplateHdr outputFileTemplateHdr = this.outputFileTemplateHdrRepository.findById(id).orElseThrow(() -> new BusinessException(ResponseCode.CFG_OUTPUT_TEMPLATE_HDR_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
		OutputFileTemplateHdrResponseDto outputFileTemplateHdrResponseDto = this.outputFileTemplateHdrMapper.toDto(outputFileTemplateHdr);
		List<BankFilesOutput> listBankFilesOutput  = this.bankFilesOutputRepository.findByOutputTemplateHdrId(outputFileTemplateHdr.getOutputTemplateHdrId());
    	for(BankFilesOutput bankFilesOutput : listBankFilesOutput) {
    		List<BankFilesOutputResponseDto> listBankFilesOutputResponseDto = new ArrayList<BankFilesOutputResponseDto>();
    		BankFilesOutputResponseDto bankFilesOutputResponseDto = this.bankFilesOutputMapper.toDto(bankFilesOutput);
    		listBankFilesOutputResponseDto.add(bankFilesOutputResponseDto);
        	outputFileTemplateHdrResponseDto.setBankFilesOutputResponseDto(listBankFilesOutputResponseDto);
    	}
		return outputFileTemplateHdrResponseDto;
    } 
	
	@Transactional
	public OutputFileTemplateHdrResponseDto saveOutputFileTemplateHdr(OutputFileTemplateHdrRequestDto outputFileTemplateHdrRequestDto) {
        UserDetailsImpl userDetails = (UserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<OutputFileTemplateHdr> requestOutputFileTemplateHdr = this.outputFileTemplateHdrRepository.findById(outputFileTemplateHdrRequestDto.getOutputTemplateHdrId());
        
        if (this.institutionRepository.findById(outputFileTemplateHdrRequestDto.getInstitutionId().trim()).isEmpty()) {
            throw new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.BAD_REQUEST);
        }
        
        OutputFileTemplateHdr saveOutputFileTemplateHdr;
        OutputFileTemplateHdr savedOutputFileTemplateHdr;
        
        if (requestOutputFileTemplateHdr.isPresent()) { //Case of update
            if (this.outputFileTemplateHdrRepository.existsByInstitutionIdAndOutputFileTypeAndOutputFileTypeAbbrAndOutputTemplateHdrIdNot(outputFileTemplateHdrRequestDto.getInstitutionId(), outputFileTemplateHdrRequestDto.getOutputFileType(), outputFileTemplateHdrRequestDto.getOutputFileTypeAbbr(), outputFileTemplateHdrRequestDto.getOutputTemplateHdrId())) {
                throw new BusinessException(ResponseCode.CFG_OUTPUT_FILE_TEMPLATE_HDR_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
            }
            saveOutputFileTemplateHdr = this.outputFileTemplateHdrMapper.toEntity(outputFileTemplateHdrRequestDto);
            saveOutputFileTemplateHdr.setCreatedBy(requestOutputFileTemplateHdr.get().getCreatedBy());
            saveOutputFileTemplateHdr.setCreatedDate(requestOutputFileTemplateHdr.get().getCreatedDate());
            saveOutputFileTemplateHdr.setUpdatedBy(Integer.valueOf(userDetails.getId()));
            saveOutputFileTemplateHdr.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
            savedOutputFileTemplateHdr = this.outputFileTemplateHdrRepository.save(saveOutputFileTemplateHdr);
            
            List<BankFilesOutput> bankFilesOutputs = this.bankFilesOutputRepository.findByOutputTemplateHdrId(savedOutputFileTemplateHdr.getOutputTemplateHdrId());
       		if (makerCheckerEngine.processIfRequired(outputFileTemplateHdrRequestDto, OutputFileTemplateHdrService.class.getName(), "saveOutputFileTemplateHdr", "")) {
    			return null;
    		}
            for(BankFilesOutput bankFilesOutput : bankFilesOutputs) {
            	bankFilesOutput.setOutputFileType(savedOutputFileTemplateHdr.getOutputFileType());
            	bankFilesOutput.setOutputFileTypeAbbr(savedOutputFileTemplateHdr.getOutputFileTypeAbbr());
            	bankFilesOutput.setUpdatedBy(Integer.valueOf(userDetails.getId()));
            	bankFilesOutput.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
            	
            	this.bankFilesOutputRepository.save(bankFilesOutput);
            }
        } else { //Case of create
            if (this.outputFileTemplateHdrRepository.existsByInstitutionIdAndOutputFileTypeAndOutputFileTypeAbbr(outputFileTemplateHdrRequestDto.getInstitutionId(), outputFileTemplateHdrRequestDto.getOutputFileType(), outputFileTemplateHdrRequestDto.getOutputFileTypeAbbr())) {
                throw new BusinessException(ResponseCode.CFG_OUTPUT_FILE_TEMPLATE_HDR_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
            }
            saveOutputFileTemplateHdr = this.outputFileTemplateHdrMapper.toEntity(outputFileTemplateHdrRequestDto);
            saveOutputFileTemplateHdr.setCreatedBy(Integer.valueOf(userDetails.getId()));
            saveOutputFileTemplateHdr.setCreatedDate(new Timestamp(System.currentTimeMillis()));
       		if (makerCheckerEngine.processIfRequired(outputFileTemplateHdrRequestDto, OutputFileTemplateHdrService.class.getName(), "saveOutputFileTemplateHdr", "")) {
    			return null;
    		}
            savedOutputFileTemplateHdr = this.outputFileTemplateHdrRepository.save(saveOutputFileTemplateHdr);
        }
        
        outputFileTemplateDetailsRepository.deleteByOutputTemplateHdrId(savedOutputFileTemplateHdr.getOutputTemplateHdrId());
        outputFileTemplateDetailsRepository.flush();
        
        int i = 1;
		if (!Objects.isNull(outputFileTemplateHdrRequestDto.getOutputFileTemplateDetailsHeaderRequestDtos())) {
			for (OutputFileTemplateDetailsRequestDto outputFileTemplateDetailsRequestDto : outputFileTemplateHdrRequestDto.getOutputFileTemplateDetailsHeaderRequestDtos()) {
				OutputFileTemplateDetails outputFileTemplateDetails = outputFileTemplateDetailsMapper.toEntity(outputFileTemplateDetailsRequestDto);
				outputFileTemplateDetails.setOutputTemplateHdrId(savedOutputFileTemplateHdr.getOutputTemplateHdrId());
				outputFileTemplateDetails.setFieldSequence(i);
				outputFileTemplateDetails.setInstitutionId(outputFileTemplateHdrRequestDto.getInstitutionId());
				outputFileTemplateDetails.setFieldLength(outputFileTemplateDetailsRequestDto.getFieldLength());
				outputFileTemplateDetails.setFieldPad(outputFileTemplateDetailsRequestDto.getFieldPad());
				outputFileTemplateDetails.setFieldPadChar(outputFileTemplateDetailsRequestDto.getFieldPadChar());
				outputFileTemplateDetails.setFieldFormat(outputFileTemplateDetailsRequestDto.getFieldFormat());
				if(Objects.nonNull(outputFileTemplateDetailsRequestDto.getFieldCsyntax()) && !outputFileTemplateDetailsRequestDto.getFieldCsyntax().trim().equals("")) {
					outputFileTemplateDetails.setFieldCsyntax(outputFileTemplateDetailsRequestDto.getFieldCsyntax());
				}
				outputFileTemplateDetails.setFieldSection("H");
				outputFileTemplateDetails.setCreatedBy(Integer.valueOf(userDetails.getId()));
				outputFileTemplateDetails.setCreatedDate(new Timestamp(System.currentTimeMillis()));
				outputFileTemplateDetailsRepository.save(outputFileTemplateDetails);
				i++;
			}
		}
		
		i = 1;
		if (!Objects.isNull(outputFileTemplateHdrRequestDto.getOutputFileTemplateDetailsDetailRequestDtos())) {
			for (OutputFileTemplateDetailsRequestDto outputFileTemplateDetailsRequestDto : outputFileTemplateHdrRequestDto.getOutputFileTemplateDetailsDetailRequestDtos()) {
				OutputFileTemplateDetails outputFileTemplateDetails = outputFileTemplateDetailsMapper.toEntity(outputFileTemplateDetailsRequestDto);
				outputFileTemplateDetails.setFieldSequence(i);
				outputFileTemplateDetails.setOutputTemplateHdrId(savedOutputFileTemplateHdr.getOutputTemplateHdrId());
				outputFileTemplateDetails.setInstitutionId(outputFileTemplateHdrRequestDto.getInstitutionId());
				outputFileTemplateDetails.setFieldLength(outputFileTemplateDetailsRequestDto.getFieldLength());
				outputFileTemplateDetails.setFieldPad(outputFileTemplateDetailsRequestDto.getFieldPad());
				outputFileTemplateDetails.setFieldPadChar(outputFileTemplateDetailsRequestDto.getFieldPadChar());
				outputFileTemplateDetails.setFieldFormat(outputFileTemplateDetailsRequestDto.getFieldFormat());
				if(Objects.nonNull(outputFileTemplateDetailsRequestDto.getFieldCsyntax()) && !outputFileTemplateDetailsRequestDto.getFieldCsyntax().trim().equals("")) {
					outputFileTemplateDetails.setFieldCsyntax(outputFileTemplateDetailsRequestDto.getFieldCsyntax());
				}
				outputFileTemplateDetails.setFieldSection("D");
				outputFileTemplateDetails.setCreatedBy(Integer.valueOf(userDetails.getId()));
				outputFileTemplateDetails.setCreatedDate(new Timestamp(System.currentTimeMillis()));
				outputFileTemplateDetailsRepository.save(outputFileTemplateDetails);
				i++;
			}
		}
		
		i = 1;
		if (!Objects.isNull(outputFileTemplateHdrRequestDto.getOutputFileTemplateDetailsFooterRequestDtos())) {
			for (OutputFileTemplateDetailsRequestDto outputFileTemplateDetailsRequestDto : outputFileTemplateHdrRequestDto.getOutputFileTemplateDetailsFooterRequestDtos()) {
				OutputFileTemplateDetails outputFileTemplateDetails = outputFileTemplateDetailsMapper.toEntity(outputFileTemplateDetailsRequestDto);
				outputFileTemplateDetails.setFieldSequence(i);
				outputFileTemplateDetails.setOutputTemplateHdrId(savedOutputFileTemplateHdr.getOutputTemplateHdrId());
				outputFileTemplateDetails.setInstitutionId(outputFileTemplateHdrRequestDto.getInstitutionId());
				outputFileTemplateDetails.setFieldLength(outputFileTemplateDetailsRequestDto.getFieldLength());
				outputFileTemplateDetails.setFieldPad(outputFileTemplateDetailsRequestDto.getFieldPad());
				outputFileTemplateDetails.setFieldPadChar(outputFileTemplateDetailsRequestDto.getFieldPadChar());
				outputFileTemplateDetails.setFieldFormat(outputFileTemplateDetailsRequestDto.getFieldFormat());
				if(Objects.nonNull(outputFileTemplateDetailsRequestDto.getFieldCsyntax()) && !outputFileTemplateDetailsRequestDto.getFieldCsyntax().trim().equals("")) {
					outputFileTemplateDetails.setFieldCsyntax(outputFileTemplateDetailsRequestDto.getFieldCsyntax());
				}
				outputFileTemplateDetails.setFieldSection("F");
				outputFileTemplateDetails.setCreatedBy(Integer.valueOf(userDetails.getId()));
				outputFileTemplateDetails.setCreatedDate(new Timestamp(System.currentTimeMillis()));
				outputFileTemplateDetailsRepository.save(outputFileTemplateDetails);
				i++;
			}
		}
        
        return this.outputFileTemplateHdrMapper.toDto(savedOutputFileTemplateHdr);
    }
    
	@Transactional
    public void deleteOutputFileTemplateHdr(int id) {
    	OutputFileTemplateHdr outputFileTemplateHdr = this.outputFileTemplateHdrRepository.findById(id).orElseThrow(() -> new BusinessException(ResponseCode.CFG_OUTPUT_TEMPLATE_HDR_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
    	this.bankFilesOutputRepository.deleteByOutputTemplateHdrId(outputFileTemplateHdr.getOutputTemplateHdrId());
    	outputFileTemplateDetailsRepository.deleteByOutputTemplateHdrId(outputFileTemplateHdr.getOutputTemplateHdrId());
   		if (makerCheckerEngine.processIfRequired(id, OutputFileTemplateHdrService.class.getName(), "deleteOutputFileTemplateHdr", "")) {
			return;
		}
    	this.outputFileTemplateHdrRepository.delete(outputFileTemplateHdr);
    }
}