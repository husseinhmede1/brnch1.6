package com.mdsl.controller;

import java.util.List;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
import com.mdsl.model.dto.request.BankCodeRequestDto;
import com.mdsl.model.dto.response.BankCodeResponseDto;
import com.mdsl.service.BankCodeService;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.Validations;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("Bank Code Controller")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/bankcode")
public class BankCodeController {
	private static final Logger logger = LoggerFactory.getLogger(BankCodeController.class);

	@Autowired
	private BankCodeService bankCodeService;
	
	@ApiOperation(value = "Get Bank Code by Institution",response = BankCodeResponseDto.class)
	@GetMapping("/institution/{id}")
	public ResponseEntity<List<BankCodeResponseDto>> getAllBankCodeByInstitution(@PathVariable("id") String id){
		return ResponseEntity.ok(bankCodeService.getAllBankCodeByInstitution(id));
	}
	
	@ApiOperation(value = "Get Bank Code by Id",response = BankCodeResponseDto.class)
	@GetMapping("/{id}")
	public ResponseEntity<BankCodeResponseDto> getBankCodeById(@PathVariable("id") int id){
		return ResponseEntity.ok(bankCodeService.getBankCodeById(id));
	}
	
	@ApiOperation(value = "Save Bank Code",response = BankCodeResponseDto.class)
	@PostMapping
	public ResponseEntity<BankCodeResponseDto> saveOrUpdateBankCode(@Valid @RequestBody BankCodeRequestDto bankCodeRequestDto, BindingResult bindingResult){
		try {
			Validations.validate(bindingResult);
			return ResponseEntity.ok(bankCodeService.saveOrUpdateBankCode(bankCodeRequestDto));
		} catch (BusinessException e) {
		    throw new BusinessException (e.getMessage(), e.getHttpStatus());
		} catch (Exception e) {
		    logger.error("@BankCodeController#saveOrUpdateBankCode: " + e.getMessage());
		    throw new BusinessException (ResponseCode.BNK_BANK_CODE_NO_SAVE, HttpStatus.BAD_REQUEST);
	    }
	}
	
	@ApiOperation(value = "Delete Bank Code",response = String.class)
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deletebankCode(@PathVariable("id") int id) {
		try {
			bankCodeService.deletebankCodeById(id);
			String message = "An item is deleted with id : " + id;
			return ResponseEntity.ok(message);
		} catch (BusinessException e) {
		    throw new BusinessException (e.getMessage(), e.getHttpStatus());
		} catch (Exception e) {
		    logger.error("@BankCodeController#deletebankCode: " + e.getMessage());
		    throw new BusinessException (ResponseCode.BNK_BANK_CODE_NO_DELETE, HttpStatus.BAD_REQUEST);
	    }
	}
	
}
