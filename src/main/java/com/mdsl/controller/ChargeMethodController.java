package com.mdsl.controller;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import com.mdsl.utils.Validations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.mdsl.model.entity.ChargeMethod;
import com.mdsl.service.ChargeMethodService;
import com.mdsl.utils.ResponseCode;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "Charge MethodControllerr Controller")
@CrossOrigin(origins = "*")
@RestController
@Transactional(rollbackOn = Exception.class)
@RequestMapping("/chargeMethod")
public class ChargeMethodController {
	private ChargeMethodService chargeMethodService;
	private static final Logger logger = LoggerFactory.getLogger(ChargeMethodController.class);

	ChargeMethodController(ChargeMethodService chargeMethodService) {
		this.chargeMethodService = chargeMethodService;
	}
	
	@ApiOperation(value = "Get all Roles",response = ChargeMethod.class)
	@GetMapping
	public ResponseEntity<List<ChargeMethod>> getAllRole() {
		return ResponseEntity.ok(chargeMethodService.getAllChargeMethod());
	}
	 
	@ApiOperation(value = "Save Role",response = ChargeMethod.class)
	@PostMapping
	public ResponseEntity<ChargeMethod> saveRole(@Valid @RequestBody ChargeMethod chargeMethod,
			BindingResult bindingResult) {
		Validations.validate(bindingResult);
		try {
		return ResponseEntity.ok(chargeMethodService.saveChargeMethod(chargeMethod));
		} catch (BusinessException e) {
			throw new BusinessException (e.getMessage(), e.getHttpStatus());
		} catch (Exception e) {
			logger.error("@ChargeMethodController#saveRole: " + e.getMessage());
			throw new BusinessException (ResponseCode.CFG_ROLE_NO_SAVE, HttpStatus.BAD_REQUEST);
		}
	}
}
