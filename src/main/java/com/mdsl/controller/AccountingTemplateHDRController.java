package com.mdsl.controller;


import java.util.List;

import javax.validation.Valid;


import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;import org.springframework.beans.factory.annotation.Autowired;
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
import com.mdsl.model.dto.request.AccountingTemplateHDRRequestDto;
import com.mdsl.model.dto.request.AccountingTemplateHdrEntityMappingRequestDto;
import com.mdsl.model.dto.response.AccountingTemplateHDRResponseDto;
import com.mdsl.model.dto.response.EntitiesResponseDto;
import com.mdsl.service.AccountingTemplateHDRService;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.Validations;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("Accounting Template Header Controller")
@CrossOrigin(origins={"*"})
@RestController
@RequestMapping("/accounting-template")
@RequiredArgsConstructor
public class AccountingTemplateHDRController {
	 private final AccountingTemplateHDRService accountingTemplateHDRService;
	 private static final Logger logger = LoggerFactory.getLogger(AccountingTemplateHDRController.class);

	  @ApiOperation(value = "Get all Accounting Template Headers by Institution",response = AccountingTemplateHDRResponseDto.class)
	  @GetMapping("/inst/{instId}")
	  public ResponseEntity<List<AccountingTemplateHDRResponseDto>> getAllAccountingTemplateHDRsByInstitution(@PathVariable("instId") String instId) {
	    return ResponseEntity.ok(this.accountingTemplateHDRService.getAllAccountingTemplateHDRsByInstitution(instId));
	  }

	  @ApiOperation(value = "Get Accounting Template Header by Id",response = AccountingTemplateHDRResponseDto.class)
	  @GetMapping("/{id}")
	  public ResponseEntity<AccountingTemplateHDRResponseDto> getAccountingTemplateHDRById(@PathVariable("id") int id) {
	    return ResponseEntity.ok(this.accountingTemplateHDRService.getAccountingTemplateHDRById(id));
	  }

	  @ApiOperation(value = "Save Accounting Template Header - Details",response = AccountingTemplateHDRResponseDto.class)
	  @PostMapping
	  public ResponseEntity<AccountingTemplateHDRResponseDto> saveAccountingTemplateHDR(@Valid @RequestBody AccountingTemplateHDRRequestDto accountingTemplateHDRRequestDto, BindingResult bindingResult) {
		  Validations.validate(bindingResult);
	   try {
	    return ResponseEntity.ok(this.accountingTemplateHDRService.saveAccountingTemplateHDR(accountingTemplateHDRRequestDto));
	   } catch (BusinessException e) {
		  throw new BusinessException (e.getMessage(), e.getHttpStatus());
	   } catch (Exception e) {
		logger.error("@AccountingTemplateHDRController#saveAccountingTemplateHDR: " + e.getMessage());
		throw new BusinessException (ResponseCode.CFG_ACCOUNTING_TEMPLATE_HDR_NO_SAVE, HttpStatus.BAD_REQUEST);
	   }
	  }
	  
	  @ApiOperation(value = "Delete Accounting Template Header - Details")
	  @DeleteMapping("/{id}")
	  public void deleteAccountingTemplateHDR(@PathVariable("id") int id) {
       try {
	    this.accountingTemplateHDRService.deleteAccountingTemplateHDR(id);
	   } catch (BusinessException e) {
		  throw new BusinessException (e.getMessage(), e.getHttpStatus());
	   } catch (Exception e) {
		logger.error("@AccountingTemplateHDRController#deleteAccountingTemplateHDR: " + e.getMessage());
		throw new BusinessException (ResponseCode.CFG_ACCOUNTING_TEMPLATE_HDR_NO_DELETE, HttpStatus.BAD_REQUEST);
	   }
	  }

	  @ApiOperation(value = "Get Mapped Entities by Accounting Template Id",response = EntitiesResponseDto.class)
	  @GetMapping("/mapped-entities/{id}")
	  public ResponseEntity<List<EntitiesResponseDto>> getMappedEntitiesByAccountingTemplateId(@PathVariable("id") int id) {
	    return ResponseEntity.ok(this.accountingTemplateHDRService.getMappedEntitiesByAccountingTemplateId(id));
	  }

	  @ApiOperation(value = "Map Accounting Template to Entity",response = AccountingTemplateHdrEntityMappingRequestDto.class)
	  @PostMapping("/assignment")
	  public ResponseEntity<AccountingTemplateHdrEntityMappingRequestDto> mapAccountingTemplateWithEntity(@Valid @RequestBody AccountingTemplateHdrEntityMappingRequestDto requestDto, BindingResult bindingResult) {
	    Validations.validate(bindingResult);
	    return ResponseEntity.ok(this.accountingTemplateHDRService.mapAccountingTemplateWithEntity(requestDto));
	  }
}
