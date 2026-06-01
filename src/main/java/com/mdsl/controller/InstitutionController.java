package com.mdsl.controller;

import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.mdsl.model.dto.request.ChangeStatusRequestDto;
import com.mdsl.model.dto.request.InstitutionRequestDto;
import com.mdsl.model.dto.response.InstitutionResponseDto;
import com.mdsl.service.InstitutionService;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.Validations;

import io.swagger.annotations.Api;

@Api(value = "Institution Controller", description = "REST Apis related to Institution Entity!!!!")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/institution")
public class InstitutionController {
	private static final Logger logger = LoggerFactory.getLogger(InstitutionController.class);

	private InstitutionService institutionService;

	InstitutionController(InstitutionService institutionService) {
		this.institutionService = institutionService;
	}

	@GetMapping
	public ResponseEntity<List<InstitutionResponseDto>> getAllInstitutions() {
		return ResponseEntity.ok(institutionService.getAllInstitutuion());
	}

	@GetMapping("/{id}")
	public ResponseEntity<InstitutionResponseDto> getInstitutionById(@PathVariable(value = "id") String instId) {

		return ResponseEntity.ok(institutionService.getInstitutuionById(instId));
	}

	@PostMapping
	public ResponseEntity saveOrUpdateInstitutions(@Valid @RequestBody InstitutionRequestDto institutionRequestDto,
			BindingResult bindingResult) {
		Validations.validate(bindingResult);
		try {
			return ResponseEntity.ok(institutionService.saveOrUpdateInstitution(institutionRequestDto));
		} catch (BusinessException e) {
		    throw new BusinessException (e.getMessage(), e.getHttpStatus());
		} catch (Exception e) {
		    logger.error("@InstitutionController#saveOrUpdateInstitutions: " + e.getMessage());
		    throw new BusinessException (ResponseCode.INT_INSTITUTION_NO_SAVE, HttpStatus.BAD_REQUEST);
	    }
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteInstitution(@PathVariable(value = "id") String instId) {
		try {
			institutionService.deleteInstitution(instId);
			String message = "An item is deleted with id : " + instId;
			return ResponseEntity.ok(message);
		} catch (BusinessException e) {
		    throw new BusinessException (e.getMessage(), e.getHttpStatus());
		} catch (Exception e) {
		    logger.error("@InstitutionController#deleteInstitution: " + e.getMessage());
		    throw new BusinessException (ResponseCode.INT_INSTITUTION_NO_DELETE, HttpStatus.BAD_REQUEST);
	    }
	}

	@PostMapping("/status-change")
	public ResponseEntity<String> changeInstitutionStatus(@Valid @RequestBody ChangeStatusRequestDto changeInstitutionStatusRequestDTO,BindingResult bindingResult, HttpServletRequest request) {
		Validations.validate(bindingResult);
		return ResponseEntity.ok(institutionService.changeStatus(changeInstitutionStatusRequestDTO));
	}

	@GetMapping("/active-institutions")
	public ResponseEntity<List<InstitutionResponseDto>> getActiveInstitutuions() {
		return ResponseEntity.ok(institutionService.getActiveInstitutuions());
	}
	
	@GetMapping("/active-all-institutions")
	public ResponseEntity<List<InstitutionResponseDto>> getAllActiveInstitutuions() {
		return ResponseEntity.ok(institutionService.getAllActiveInstitutuions());
	}
}
