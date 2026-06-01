package com.mdsl.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.BankFilesOutputRequestDto;
import com.mdsl.model.dto.request.OutputFileTemplateBankCodeMappingRequestDto;
import com.mdsl.model.dto.response.BankCodeResponseDto;
import com.mdsl.model.dto.response.BankFilesOutputResponseDto;
import com.mdsl.model.entity.BankCode;
import com.mdsl.model.entity.BankFilesOutput;
import com.mdsl.model.entity.Institution;
import com.mdsl.model.entity.OutputFileTemplateHdr;
import com.mdsl.model.mapper.BankCodeMapper;
import com.mdsl.model.mapper.BankFilesOutputMapper;
import com.mdsl.repository.BankCodeRepository;
import com.mdsl.repository.BankFilesOutputRepository;
import com.mdsl.repository.InstitutionRepository;
import com.mdsl.repository.OutputFileTemplateHdrRepository;
import com.mdsl.utils.ResponseCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BankFilesOutputService {
	
	private final BankFilesOutputRepository bankFilesOutputRepository;
	private final InstitutionRepository institutionRepository;
	private final BankCodeRepository bankCodeRepository;
	private final OutputFileTemplateHdrRepository outputFileTemplateHdrRepository;
	
	private final BankFilesOutputMapper bankFilesOutputMapper;
	private final BankCodeMapper bankCodeMapper;
	
	public List<BankFilesOutputResponseDto> getAllBankFilesOutputByInstitution(String instId) {
		Institution institution = institutionRepository.findById(instId)
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
		List<BankFilesOutput> allBankFilesOutput = bankFilesOutputRepository.findByInstitutionId(institution.getInstitutionId());
		List<BankFilesOutputResponseDto> allBankFilesOutputResponseDto = new ArrayList<BankFilesOutputResponseDto>();
		allBankFilesOutput.stream().forEach((bankFileOutput) -> {
			allBankFilesOutputResponseDto.add(bankFilesOutputMapper.toDto(bankFileOutput));
		});
		return allBankFilesOutputResponseDto;
	}
	
	public List<String> getDistinctBankFilesOutputBankCodesByInstitution(String instId) {
		Institution institution = institutionRepository.findById(instId)
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
		List<String> allBankFilesOutput = bankFilesOutputRepository.getDistinctBankCodesByInstitutionId(institution.getInstitutionId());
		return allBankFilesOutput;
	}
	
	public List<String> getDistinctBankFilesOutputByInstitution(String instId) {
		Institution institution = institutionRepository.findById(instId)
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
		List<String> allBankFilesOutput = bankFilesOutputRepository.getDistinctOutputFileTypesByInstitutionId(institution.getInstitutionId());
		return allBankFilesOutput;
	}
	
	public List<String> getAllBankFilesOutputByInstitutionAndOutputFileType(String instId, String outputFileType) {
		Institution institution = institutionRepository.findById(instId)
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
		List<String> allBankFilesOutput = bankFilesOutputRepository.getOutputFileTypeAbbrByStartsWith(institution.getInstitutionId(), outputFileType);
		return allBankFilesOutput;
	}
	
	public BankFilesOutputResponseDto saveBankFiles(final BankFilesOutputRequestDto bankFilesOutputRequestDto) {
        UserDetailsImpl userDetails = (UserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<BankFilesOutput> requestBankFilesOutput = this.bankFilesOutputRepository.findById(bankFilesOutputRequestDto.getBankFilesOutputId());
        if (this.institutionRepository.findById(bankFilesOutputRequestDto.getInstitutionId().trim()).isEmpty()) {
            throw new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        if (this.bankCodeRepository.findByBankCodeAndInstitutionId(bankFilesOutputRequestDto.getBankCode().trim(), bankFilesOutputRequestDto.getInstitutionId().trim()).isEmpty()) {
            throw new BusinessException(ResponseCode.CFG_BANK_CODE_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        if (this.outputFileTemplateHdrRepository.findById(bankFilesOutputRequestDto.getOutputTemplateHdrId()).isEmpty()) {
            throw new BusinessException(ResponseCode.CFG_OUTPUT_TEMPLATE_HDR_ID_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        
        BankFilesOutput savedBankFilesOutput;
        if (requestBankFilesOutput.isPresent()) { //Case of update
        	if(this.bankFilesOutputRepository.existsByInstitutionIdAndBankCodeAndOutputFileTypeAndOutputFileTypeAbbrAndBankFilesOutputIdNot(bankFilesOutputRequestDto.getInstitutionId(), bankFilesOutputRequestDto.getBankCode(), bankFilesOutputRequestDto.getOutputFileType(), bankFilesOutputRequestDto.getOutputFileTypeAbbr(), bankFilesOutputRequestDto.getBankFilesOutputId())) {
        		throw new BusinessException(ResponseCode.CFG_INVALID_BANK_FILES_OUTPUT_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
        	}
        	
        	BankFilesOutput saveBankFilesOutput = this.bankFilesOutputMapper.toEntity(bankFilesOutputRequestDto);
            saveBankFilesOutput.setCreatedBy(requestBankFilesOutput.get().getCreatedBy());
            saveBankFilesOutput.setCreatedDate(requestBankFilesOutput.get().getCreatedDate());
            saveBankFilesOutput.setUpdatedBy(Integer.valueOf(userDetails.getId()));
            saveBankFilesOutput.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
            savedBankFilesOutput = this.bankFilesOutputRepository.save(saveBankFilesOutput);
        } else { //Case of create
        	if(this.bankFilesOutputRepository.existsByInstitutionIdAndBankCodeAndOutputFileTypeAndOutputFileTypeAbbr(bankFilesOutputRequestDto.getInstitutionId(), bankFilesOutputRequestDto.getBankCode(), bankFilesOutputRequestDto.getOutputFileType(), bankFilesOutputRequestDto.getOutputFileTypeAbbr())) {
        		throw new BusinessException(ResponseCode.CFG_INVALID_BANK_FILES_OUTPUT_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
        	}
        	BankFilesOutput saveBankFilesOutput = this.bankFilesOutputMapper.toEntity(bankFilesOutputRequestDto);
            saveBankFilesOutput.setCreatedBy(Integer.valueOf(userDetails.getId()));
            saveBankFilesOutput.setCreatedDate(new Timestamp(System.currentTimeMillis()));
            savedBankFilesOutput = this.bankFilesOutputRepository.save(saveBankFilesOutput);
        }
        return this.bankFilesOutputMapper.toDto(savedBankFilesOutput);
    }
	
	public List<BankFilesOutputResponseDto> mapOutputFileTemplateToBanks(OutputFileTemplateBankCodeMappingRequestDto outputFileTemplateBankCodeMappingRequestDto) {
	    UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    OutputFileTemplateHdr outputFileTemplateHdr = this.outputFileTemplateHdrRepository.findById(outputFileTemplateBankCodeMappingRequestDto.getOutputFileTemplateHdrId())
	            .orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_OUTPUT_TEMPLATE_HDR_ID, HttpStatus.NOT_FOUND));
	    List<BankFilesOutputResponseDto> listBankFilesOutputResponseDto = new ArrayList<BankFilesOutputResponseDto>();

	    for (String bankCode : outputFileTemplateBankCodeMappingRequestDto.getBankCodes()) {
	        if (this.bankFilesOutputRepository.existsByBankCodeAndOutputTemplateHdrId(bankCode, outputFileTemplateBankCodeMappingRequestDto.getOutputFileTemplateHdrId())) {
	            continue;
	        }

	        BankCode bankInfo = this.bankCodeRepository.findByBankCodeAndInstitutionId(bankCode, outputFileTemplateHdr.getInstitutionId())
	                .orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_BANK_CODE, HttpStatus.NOT_FOUND));

	        BankFilesOutput saveBankFilesOutput = new BankFilesOutput();
	        saveBankFilesOutput.setBankCode(bankInfo.getBankCode());
	        saveBankFilesOutput.setInstitutionId(outputFileTemplateHdr.getInstitutionId());
	        saveBankFilesOutput.setOutputFileType(outputFileTemplateHdr.getOutputFileType());
	        saveBankFilesOutput.setOutputFileTypeAbbr(outputFileTemplateHdr.getOutputFileTypeAbbr());
	        saveBankFilesOutput.setCreatedBy(Integer.valueOf(userDetails.getId()));
	        saveBankFilesOutput.setCreatedDate(new Timestamp(System.currentTimeMillis()));
	        saveBankFilesOutput.setOutputTemplateHdrId(outputFileTemplateHdr.getOutputTemplateHdrId());

	        if (this.bankFilesOutputRepository.existsByInstitutionIdAndBankCodeAndOutputFileTypeAndOutputFileTypeAbbr(saveBankFilesOutput.getInstitutionId(), saveBankFilesOutput.getBankCode(), saveBankFilesOutput.getOutputFileType(), saveBankFilesOutput.getOutputFileTypeAbbr())) {
	            throw new BusinessException(ResponseCode.CFG_INVALID_BANK_FILES_OUTPUT_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
	        }

	        BankFilesOutput savedBankFilesOutput = this.bankFilesOutputRepository.save(saveBankFilesOutput);
	        listBankFilesOutputResponseDto.add(this.bankFilesOutputMapper.toDto(savedBankFilesOutput));
	    }

	    return listBankFilesOutputResponseDto;
	}
	
	public List<BankCodeResponseDto> getAllMappedBanksByOutputTemplateHdrId(int outputTemplateHdrId) {
		List<BankCodeResponseDto> bankCodeResponseDto = new ArrayList<BankCodeResponseDto>();
		List<BankFilesOutput> bankFilesOutputs = this.bankFilesOutputRepository.findByOutputTemplateHdrId(outputTemplateHdrId);
		
		for(BankFilesOutput bankFilesOutput : bankFilesOutputs) {
			BankCode bankInfo = this.bankCodeRepository.findByBankCodeAndInstitutionId(bankFilesOutput.getBankCode(), bankFilesOutput.getInstitutionId())
					.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_BANK_CODE, HttpStatus.NOT_FOUND));
			bankCodeResponseDto.add(this.bankCodeMapper.toDto(bankInfo));
		}
		
		return bankCodeResponseDto;
	}
	
	public List<BankFilesOutputResponseDto> unMapOutputFileTemplateToBanks(OutputFileTemplateBankCodeMappingRequestDto outputFileTemplateBankCodeMappingRequestDto) {
	    OutputFileTemplateHdr outputFileTemplateHdr = this.outputFileTemplateHdrRepository.findById(outputFileTemplateBankCodeMappingRequestDto.getOutputFileTemplateHdrId())
	            .orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_OUTPUT_TEMPLATE_HDR_ID, HttpStatus.NOT_FOUND));
	    List<BankFilesOutputResponseDto> listBankFilesOutputResponseDto = new ArrayList<BankFilesOutputResponseDto>();

	    for (String bankCode : outputFileTemplateBankCodeMappingRequestDto.getBankCodes()) {
	    	if (!this.bankFilesOutputRepository.existsByBankCodeAndOutputTemplateHdrId(bankCode, outputFileTemplateBankCodeMappingRequestDto.getOutputFileTemplateHdrId())) {
	    		continue;
		    }
	        BankFilesOutput bankFilesOutput = this.bankFilesOutputRepository.findByBankCodeAndOutputTemplateHdrId(bankCode, outputFileTemplateHdr.getOutputTemplateHdrId())
	        		.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_BANK_FILES_OUTPUT_ID, HttpStatus.NOT_FOUND));
	        listBankFilesOutputResponseDto.add(this.bankFilesOutputMapper.toDto(bankFilesOutput));
	        this.bankFilesOutputRepository.delete(bankFilesOutput);
	    }
	    return listBankFilesOutputResponseDto;
	}
}