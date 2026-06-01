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
import com.mdsl.model.dto.request.AccountingTemplateHDRSubRequestDto;
import com.mdsl.model.dto.response.AccountingTemplateHDRSubResponseDto;
import com.mdsl.service.AccountingTemplateHDRSubService;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.Validations;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("Accounting Template Sub Header Controller")
@CrossOrigin(origins={"*"})
@RestController
@RequestMapping("/accounting-template-sub")
@RequiredArgsConstructor
public class AccountingTemplateHDRSubController {
	  private static final Logger logger = LoggerFactory.getLogger(AccountingTemplateHDRSubController.class);
	  private final AccountingTemplateHDRSubService accountingTemplateHDRSubService;

	  @ApiOperation(value = "Get all Accounting Template Sub Headers by Accounting Template Header Id",response = AccountingTemplateHDRSubResponseDto.class)
	  @GetMapping("/header/{acctTemplateHdrId}")
	  public ResponseEntity<List<AccountingTemplateHDRSubResponseDto>> getAllAccountingTemplateHDRSubByAccrTemplateHdrId(@PathVariable("acctTemplateHdrId") Integer acctTemplateHdrId) {
	    return ResponseEntity.ok(this.accountingTemplateHDRSubService.getAllAccountingTemplateHDRSubByAccrTemplateHdrId(acctTemplateHdrId));
	  }
	  
	  @ApiOperation(value = "Get Accounting Template Sub Header by Id",response = AccountingTemplateHDRSubResponseDto.class)
	  @GetMapping("/{id}")
	  public ResponseEntity<AccountingTemplateHDRSubResponseDto> getAccountingTemplateHDRSubById(@PathVariable("id") int id) {
	    return ResponseEntity.ok(this.accountingTemplateHDRSubService.getAccountingTemplateHDRSubById(id));
	  }
	  
	  @ApiOperation(value = "Save Accounting Template Sub Header",response = AccountingTemplateHDRSubResponseDto.class)
	  @PostMapping
	  public ResponseEntity<AccountingTemplateHDRSubResponseDto> saveAccountingTemplateHDRSub(@Valid @RequestBody AccountingTemplateHDRSubRequestDto accountingTemplateHDRSubRequestDto, BindingResult bindingResult) {
		  Validations.validate(bindingResult);
		try {
		  return ResponseEntity.ok(this.accountingTemplateHDRSubService.saveAccountingTemplateHDRSub(accountingTemplateHDRSubRequestDto));
	    } catch (BusinessException e) {
			throw new BusinessException (e.getMessage(), e.getHttpStatus());
		} catch (Exception e) {
			logger.error("@AccountingTemplateHDRSubController#saveAccountingTemplateHDRSub: " + e.getMessage());
			throw new BusinessException (ResponseCode.CFG_ACCOUNTING_TEMPLATE_HDR_SUB_NO_SAVE, HttpStatus.BAD_REQUEST);
		}
	  }
	  
	  @ApiOperation(value = "Delete Accounting Template Sub Header")
	  @DeleteMapping("/{id}")
	  public void deleteAccountingTemplateSubHDR(@PathVariable("id") int id) {
		  try {
			  this.accountingTemplateHDRSubService.deleteAccountingTemplateSubHDR(id);
		  } catch (BusinessException e) {
			  throw new BusinessException (e.getMessage(), e.getHttpStatus());
		  } catch (Exception e) {
			  logger.error("@AccountingTemplateHDRSubController#deleteAccountingTemplateSubHDR: " + e.getMessage());
			  throw new BusinessException (ResponseCode.CFG_ACCOUNTING_TEMPLATE_HDR_SUB_NO_DELETE, HttpStatus.BAD_REQUEST);
		   }
	  }

}
