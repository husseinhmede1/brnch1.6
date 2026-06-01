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
import com.mdsl.model.dto.response.RoleMasterResponseDto;
import com.mdsl.model.entity.RoleMaster;
import com.mdsl.service.RoleMasterService;
import com.mdsl.utils.ResponseCode;

import io.swagger.annotations.Api;

@Api(value = "Role Master Controller", description = "REST Apis related to Role Master Entity!!!!")
@CrossOrigin(origins = "*")
@RestController
@Transactional(rollbackOn = Exception.class)
@RequestMapping("/roleMaster")
public class RoleMasterController {
	private RoleMasterService roleMasterService;
	private static final Logger logger = LoggerFactory.getLogger(RoleMasterController.class);

	RoleMasterController(RoleMasterService roleMasterService) {
		this.roleMasterService = roleMasterService;
	}
	
	@GetMapping
	public ResponseEntity<List<RoleMasterResponseDto>> getAllRole() {
		return ResponseEntity.ok(roleMasterService.getAllRole());
	}
	 

	@PostMapping
	public ResponseEntity<RoleMasterResponseDto> saveRole(@Valid @RequestBody RoleMaster roleMaster,
			BindingResult bindingResult) {
		Validations.validate(bindingResult);
		try {
			return ResponseEntity.ok(roleMasterService.saveRole(roleMaster));
		} catch (BusinessException e) {
		    throw new BusinessException (e.getMessage(), e.getHttpStatus());
		} catch (Exception e) {
		    logger.error("@RoleMasterController#saveRole: " + e.getMessage());
		    throw new BusinessException (ResponseCode.ROL_ROLE_MASTER_NO_SAVE, HttpStatus.BAD_REQUEST);
	    }
	}
}
