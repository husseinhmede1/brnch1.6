package com.mdsl.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.RequiredArgsConstructor;
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
import com.mdsl.model.dto.request.AccountingTemplateDetailsRequestDto;
import com.mdsl.model.dto.response.AccountingTemplateDetailsResponseDto;
import com.mdsl.service.AccountingTemplateDetailsService;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.Validations;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("Accounting Template Details Controller")
@CrossOrigin(origins={"*"})
@RestController
@RequestMapping("/accounting-details")
@RequiredArgsConstructor
public class AccountingTemplateDetailsController {

	private final AccountingTemplateDetailsService accountingTemplateDetailsService;
	private static final Logger logger = LoggerFactory.getLogger(AccountingTemplateDetailsController.class);

	  @ApiOperation(value = "Get all Accounting Template Details by Accounting Template Header Sub Id",response = AccountingTemplateDetailsResponseDto.class)
	  @GetMapping("/accountingHdrSub/{id}")
	  public ResponseEntity<List<AccountingTemplateDetailsResponseDto>> getAllAccountingTemplateDetailsByAccountingTemplateHDRSubId(@PathVariable("id") int id) {
	    return ResponseEntity.ok(this.accountingTemplateDetailsService.getAllAccountingTemplateDetailsByAccountingTemplateHDRSubId(id));
	  }

	  @GetMapping("/{id}")
	  public ResponseEntity<AccountingTemplateDetailsResponseDto> getAccountTemplateDetailsById(@PathVariable("id") int id) {
	    return ResponseEntity.ok(this.accountingTemplateDetailsService.getAccountTemplateDetailsById(id));
	  }

	  @PostMapping
	  public ResponseEntity<AccountingTemplateDetailsResponseDto> saveAccountingTemplateDetails(@Valid @RequestBody AccountingTemplateDetailsRequestDto accountingTemplateDetailsRequestDto, BindingResult bindingResult) {
		  Validations.validate(bindingResult);
		  try {
	      return ResponseEntity.ok(this.accountingTemplateDetailsService.saveAccountingTemplateDetails(accountingTemplateDetailsRequestDto));
	   } catch (BusinessException e) {
		  throw new BusinessException (e.getMessage(), e.getHttpStatus());
	   } catch (Exception e) {
		  logger.error("@AccountingTemplateDetailsController#saveAccountingTemplateDetails: " + e.getMessage());
		  throw new BusinessException (ResponseCode.CFG_ACCOUNTING_TEMPLATE_DETAILS_NO_SAVE, HttpStatus.BAD_REQUEST);
	   }
	  }

	  @DeleteMapping("/{id}")
	  public void deleteAccountingTemplateDetails(@PathVariable("id") int id) {
	   try {
	    this.accountingTemplateDetailsService.deleteAccountingTemplateDetails(id);
	   } catch (BusinessException e) {
		  throw new BusinessException (e.getMessage(), e.getHttpStatus());
	   } catch (Exception e) {
		logger.error("@AccountingTemplateDetailsController#deleteAccountingTemplateDetails: " + e.getMessage());
		throw new BusinessException (ResponseCode.CFG_ACCOUNTING_TEMPLATE_DETAILS_NO_DELETE, HttpStatus.BAD_REQUEST);
	   }
	  }
}