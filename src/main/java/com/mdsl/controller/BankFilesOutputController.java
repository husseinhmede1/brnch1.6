package com.mdsl.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.BankFilesOutputRequestDto;
import com.mdsl.model.dto.request.OutputFileTemplateBankCodeMappingRequestDto;
import com.mdsl.model.dto.response.BankCodeResponseDto;
import com.mdsl.model.dto.response.BankFilesOutputResponseDto;
import com.mdsl.service.BankFilesOutputService;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.Validations;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("Bank Files Output Controller")
@CrossOrigin(origins={"*"})
@RestController
@RequestMapping("/bank-files")
public class BankFilesOutputController {
	private static final Logger logger = LoggerFactory.getLogger(BankFilesOutputController.class);
	private final BankFilesOutputService bankFilesOutputService;

	@Autowired
	public BankFilesOutputController(BankFilesOutputService bankFilesOutputService) {
		this.bankFilesOutputService = bankFilesOutputService;
	}

	@ApiOperation(value = "Get all Bank File output by Institution",response = BankFilesOutputResponseDto.class)
	@GetMapping({"/inst/{instId}"})
	public ResponseEntity<List<BankFilesOutputResponseDto>> getAllBankFilesOutputByInstitution(@PathVariable("instId") String instId) {
		return ResponseEntity.ok(this.bankFilesOutputService.getAllBankFilesOutputByInstitution(instId));
	}
	
	@ApiOperation(value = "Get all distinct Bank File output by Institution",response = BankFilesOutputResponseDto.class)
	@GetMapping({"/distinct/inst/{instId}"})
	public ResponseEntity<List<String>> getDistinctBankFilesOutputByInstitution(@PathVariable("instId") String instId) {
		return ResponseEntity.ok(this.bankFilesOutputService.getDistinctBankFilesOutputByInstitution(instId));
	}
	
	@ApiOperation(value = "Get all distinct Bank File output bank codes by Institution",response = BankFilesOutputResponseDto.class)
	@GetMapping({"/distinct/bank-code/inst/{instId}"})
	public ResponseEntity<List<String>> getDistinctBankFilesOutputBankCodesByInstitution(@PathVariable("instId") String instId) {
		return ResponseEntity.ok(this.bankFilesOutputService.getDistinctBankFilesOutputBankCodesByInstitution(instId));
	}
	
	@ApiOperation(value = "Get all Bank File output by Institution and Output File Type",response = String.class)
	@GetMapping({"/output-file-type/inst/{instId}/{outputFileType}"})
	public ResponseEntity<List<String>> getAllBankFilesOutputByInstitutionAndOutputFileType(@PathVariable("instId") String instId, @PathVariable("outputFileType") String outputFileType) {
		return ResponseEntity.ok(this.bankFilesOutputService.getAllBankFilesOutputByInstitutionAndOutputFileType(instId, outputFileType));
	}

	@ApiOperation(value = "Save Bank File output",response = BankFilesOutputResponseDto.class)
	@PostMapping
	public ResponseEntity<BankFilesOutputResponseDto> saveBankFiles(@Valid @RequestBody BankFilesOutputRequestDto bankFilesOutputRequestDto, BindingResult bindingResult) {
		Validations.validate(bindingResult);
		try {
		return ResponseEntity.ok(this.bankFilesOutputService.saveBankFiles(bankFilesOutputRequestDto));
		} catch (BusinessException e) {
		  throw new BusinessException (e.getMessage(), e.getHttpStatus());
	    } catch (Exception e) {
		logger.error("@BankFilesOutputController#saveBankFiles: " + e.getMessage());
		throw new BusinessException (ResponseCode.CFG_BANK_FILES_NO_SAVE, HttpStatus.BAD_REQUEST);
	   }
	}
	
	@ApiOperation(value = "Map Bank File output to Bank",response = BankFilesOutputResponseDto.class)
	@PostMapping("/assign")
	public ResponseEntity<List<BankFilesOutputResponseDto>> mapOutputFileTemplateToBanks(@Valid @RequestBody OutputFileTemplateBankCodeMappingRequestDto outputFileTemplateBankCodeMappingRequestDto, BindingResult bindingResult) {
		Validations.validate(bindingResult);
		return ResponseEntity.ok(this.bankFilesOutputService.mapOutputFileTemplateToBanks(outputFileTemplateBankCodeMappingRequestDto));
	}
	
	@ApiOperation(value = "Unmap Bank File output to Bank",response = BankFilesOutputResponseDto.class)
	@DeleteMapping("/unassign")
	public ResponseEntity<List<BankFilesOutputResponseDto>> unMapOutputFileTemplateToBanks(@Valid @RequestBody OutputFileTemplateBankCodeMappingRequestDto outputFileTemplateBankCodeMappingRequestDto, BindingResult bindingResult) {
		Validations.validate(bindingResult);
		try {
		   return ResponseEntity.ok(this.bankFilesOutputService.unMapOutputFileTemplateToBanks(outputFileTemplateBankCodeMappingRequestDto));
	    } catch (BusinessException e) {
			throw new BusinessException (e.getMessage(), e.getHttpStatus());
	    } catch (Exception e) {
			logger.error("@BankFilesOutputController#unMapOutputFileTemplateToBanks: " + e.getMessage());
			throw new BusinessException (ResponseCode.CFG_BANK_FILES_NO_DELETE, HttpStatus.BAD_REQUEST);
	   }
	}
	
	@ApiOperation(value = "Get all mapped Bank File output by output template header id",response = BankFilesOutputResponseDto.class)
	@GetMapping({"/map/{outputTemplateHdrId}"})
	public ResponseEntity<List<BankCodeResponseDto>> getAllMappedBanksByOutputTemplateHdrId(@PathVariable("outputTemplateHdrId") int outputTemplateHdrId) {
		return ResponseEntity.ok(this.bankFilesOutputService.getAllMappedBanksByOutputTemplateHdrId(outputTemplateHdrId));
	}
}