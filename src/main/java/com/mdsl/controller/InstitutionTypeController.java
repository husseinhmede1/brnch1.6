package com.mdsl.controller;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.mdsl.model.dto.request.InstitutionTypeRequestDto;
import com.mdsl.model.dto.response.InstitutionTypeResponseDto;
import com.mdsl.service.InstitutionTypeService;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.Validations;

import io.swagger.annotations.Api;

@Api(value = "Institution Controller", description = "REST Apis related to Institution Type Entity!!!!")
@CrossOrigin(origins = "*")
@RestController
@Transactional(rollbackOn = Exception.class)
@RequestMapping("/institutiontype")
public class InstitutionTypeController {
	private InstitutionTypeService institutionTypeService;
	private static final Logger logger = LoggerFactory.getLogger(InstitutionControlController.class);

	InstitutionTypeController(InstitutionTypeService institutionTypeService) {
		this.institutionTypeService = institutionTypeService;
	}
	
	@GetMapping
	public ResponseEntity<List<InstitutionTypeResponseDto>> getAllInstitutionTypes() {
		return ResponseEntity.ok(institutionTypeService.getAllInstitutuionType());
	}
	 

	@PostMapping
	public ResponseEntity<InstitutionTypeResponseDto> saveOrUpdateInstitutionsType(@Valid @RequestBody  InstitutionTypeRequestDto institutionTypeRequestDto,
			BindingResult bindingResult) {
		Validations.validate(bindingResult);
		try {
			return ResponseEntity.ok(institutionTypeService.saveOrUpdateInstitutionType(institutionTypeRequestDto));
		} catch (BusinessException e) {
		    throw new BusinessException (e.getMessage(), e.getHttpStatus());
		} catch (Exception e) {
		    logger.error("@InstitutionTypeController#saveOrUpdateInstitutionsType: " + e.getMessage());
		    throw new BusinessException (ResponseCode.INT_INSTITUTION_TYPE_NO_SAVE, HttpStatus.BAD_REQUEST);
	    }
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteInstitutionsType(@PathVariable(value = "id") int Id) {
		try {
			return ResponseEntity.ok(institutionTypeService.deleteInstitutionType(Id));
		} catch (BusinessException e) {
		    throw new BusinessException (e.getMessage(), e.getHttpStatus());
		} catch (Exception e) {
		    logger.error("@InstitutionTypeController#deleteInstitutionsType: " + e.getMessage());
		    throw new BusinessException (ResponseCode.INT_INSTITUTION_TYPE_NO_DELETE, HttpStatus.BAD_REQUEST);
	    }
	}
}
