package com.mdsl.controller;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import com.mdsl.utils.Validations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.response.BusinessTypeResponseDto;
import com.mdsl.model.entity.BusinessType;
import com.mdsl.service.BusinessTypeServices;
import com.mdsl.utils.ResponseCode;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("Business Type Controller")
@CrossOrigin(origins = "*")
@RestController
@Transactional(rollbackOn = Exception.class)
@RequestMapping("/businesstype")
public class BusinessTypeController {
	private static final Logger logger = LoggerFactory.getLogger(BusinessTypeController.class);

	@Autowired
	private BusinessTypeServices businessTypeServices;
	
	@ApiOperation(value = "Get all Business Types",response = BusinessTypeResponseDto.class)
	@GetMapping
	public ResponseEntity<List<BusinessTypeResponseDto>> getAllBusinessType() {
		return ResponseEntity.ok(businessTypeServices.getAllBusinessType());
	}

	@ApiOperation(value = "Save Business Type",response = BusinessTypeResponseDto.class)
	@PostMapping
	public ResponseEntity<BusinessTypeResponseDto> saveBusinessType(@Valid @RequestBody BusinessType businessType,
			BindingResult bindingResult) {
		Validations.validate(bindingResult);
		try {
			return ResponseEntity.ok(businessTypeServices.saveBusinessType(businessType));
		} catch (BusinessException e) {
			throw new BusinessException (e.getMessage(), e.getHttpStatus());
	    } catch (Exception e) {
			logger.error("@BusinessTypeController#saveBusinessType: " + e.getMessage());
			throw new BusinessException (ResponseCode.CFG_BUSINESS_TYPE_NO_SAVE, HttpStatus.BAD_REQUEST);
	   }
	}

}
