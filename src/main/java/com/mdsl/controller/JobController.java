package com.mdsl.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.ImportRequestDto;
import com.mdsl.model.dto.response.RunTaskResponseDto;
import com.mdsl.service.ImportMerchantsService;
import com.mdsl.service.ImportTerminalService;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.Validations;

@CrossOrigin(origins={"*"})
@RestController
@RequestMapping("/job")
public class JobController {
	private static final Logger logger = LoggerFactory.getLogger(JobController.class);
	private final ImportMerchantsService importMerchantsService;
	private final ImportTerminalService importTerminalService;
	
	@Autowired
	public JobController(ImportMerchantsService importMerchantsService, ImportTerminalService importTerminalService) {
		super();
		this.importMerchantsService = importMerchantsService;
		this.importTerminalService = importTerminalService;
	}
	
	@PostMapping({"/import-merchant"})
	  public ResponseEntity<RunTaskResponseDto> uploadMerchantFiles(@Valid @RequestBody ImportRequestDto importRequestDto, BindingResult bindingResult) {
		Validations.validate(bindingResult);
		try {
			return ResponseEntity.ok(this.importMerchantsService.uploadMerchantFiles(importRequestDto));
		} catch (BusinessException e) {
		    throw new BusinessException (e.getMessage(), e.getHttpStatus());
		} catch (Exception e) {
		    logger.error("@JobController#uploadMerchantFiles: " + e.getMessage());
		    throw new BusinessException (ResponseCode.JOB_UPLOAD_MERCHANT_FILES, HttpStatus.BAD_REQUEST);
	    }	
	  }
	
	@PostMapping({"/import-terminal"})
	  public ResponseEntity<RunTaskResponseDto> uploadTerminalFiles(@Valid @RequestBody ImportRequestDto importRequestDto, BindingResult bindingResult) {
		Validations.validate(bindingResult);
		try {
			return ResponseEntity.ok(this.importTerminalService.uploadTerminalFiles(importRequestDto));
			} catch (BusinessException e) {
			    throw new BusinessException (e.getMessage(), e.getHttpStatus());
			} catch (Exception e) {
			    logger.error("@JobController#uploadTerminalFiles: " + e.getMessage());
			    throw new BusinessException (ResponseCode.JOB_UPLOAD_TERMINAL_FILES, HttpStatus.BAD_REQUEST);
		    }	
		  }
}
