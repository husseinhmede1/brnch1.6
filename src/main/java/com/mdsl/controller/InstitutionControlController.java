package com.mdsl.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.InstitutionControlRequestDto;
import com.mdsl.model.dto.response.InstitutionControlResponseDto;
import com.mdsl.service.InstitutionControlService;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.Validations;

@CrossOrigin(origins={"*"})
@RestController
@RequestMapping("/institution-control")
public class InstitutionControlController {
	private static final Logger logger = LoggerFactory.getLogger(InstitutionControlController.class);
	private final InstitutionControlService institutionControlService;

	  @Autowired
	  public InstitutionControlController(InstitutionControlService institutionControlService) {
	    this.institutionControlService = institutionControlService;
	  }
	  
	  @GetMapping({"/inst/{instId}"})
	  public ResponseEntity<InstitutionControlResponseDto> getInstitutionControlByInstId(@PathVariable("instId") String instId) {
	    return ResponseEntity.ok(this.institutionControlService.getInstitutionControlByInstId(instId));
	  }
	  
	  @PostMapping
	  public ResponseEntity<InstitutionControlResponseDto> saveInstitutionControl(@Valid @RequestBody InstitutionControlRequestDto institutionControlRequestDto, BindingResult bindingResult) {
		  Validations.validate(bindingResult);
	  try {
	    return ResponseEntity.ok(this.institutionControlService.saveInstitutionControl(institutionControlRequestDto));
	  } catch (BusinessException e) {
		  throw new BusinessException (e.getMessage(), e.getHttpStatus());
	  } catch (Exception e) {
	      logger.error("@InstitutionControlController#saveInstitutionControl: " + e.getMessage());
	      throw new BusinessException (ResponseCode.INT_INSTITUTION_CONTROL_NO_SAVE, HttpStatus.BAD_REQUEST);
	  }
	  }

}
