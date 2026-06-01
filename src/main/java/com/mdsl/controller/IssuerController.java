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

import com.mdsl.service.IssuerService;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.Validations;
import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.InstitutionRequestDto;
import com.mdsl.model.dto.request.IssuerRequestDto;
import com.mdsl.model.dto.response.InstitutionResponseDto;
import com.mdsl.model.dto.response.IssuerResponseDto;
import com.mdsl.service.IssueRelationService;

import io.swagger.annotations.Api;

@Api(value = "Issuer Controller", description = "REST Apis related to Issuer and IssuerRelation Entity!!!!")
@CrossOrigin(origins = "*")
@RestController
@Transactional(rollbackOn = Exception.class)
@RequestMapping("/issuer")
public class IssuerController {
	private IssuerService issuerService;
	private IssueRelationService issuerRelationService;
	private static final Logger logger = LoggerFactory.getLogger(IssuerController.class);

	IssuerController(IssuerService issuerService,IssueRelationService issuerRelationService) {
		this.issuerService = issuerService;
		this.issuerRelationService = issuerRelationService;
		
	}
	
	@GetMapping
	public ResponseEntity<List<IssuerResponseDto>> getAllIssuer() {
		return ResponseEntity.ok(issuerService.getAllIssuer());
	}
	
	@GetMapping("/institution/{id}")
	public ResponseEntity<List<IssuerResponseDto>> getAllIssuerByIntitutionId(@PathVariable(value = "id") String instId) {
		return ResponseEntity.ok(issuerService.getAllIssuerByIstitutionId(instId));
	}
	
	@PostMapping
	public ResponseEntity<IssuerResponseDto> saveOrUpdateIssuer(
			@Valid @RequestBody IssuerRequestDto issuerRequestDto, BindingResult bindingResult) {
		Validations.validate(bindingResult);
		try {
			return ResponseEntity.ok(issuerService.saveOrUpdateIssuer(issuerRequestDto));
		} catch (BusinessException e) {
		    throw new BusinessException (e.getMessage(), e.getHttpStatus());
		} catch (Exception e) {
		    logger.error("@IssuerController#saveOrUpdateIssuer: " + e.getMessage());
		    throw new BusinessException (ResponseCode.ISS_ISSUER_NO_SAVE, HttpStatus.BAD_REQUEST);
	    }
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteIssuer(@PathVariable(value = "id") int issuerId) {
		try {
		return ResponseEntity.ok(issuerService.deleteIssuer(issuerId));
		} catch (BusinessException e) {
		    throw new BusinessException (e.getMessage(), e.getHttpStatus());
		} catch (Exception e) {
		    logger.error("@IssuerController#deleteIssuer: " + e.getMessage());
		    throw new BusinessException (ResponseCode.ISS_ISSUER_NO_DELETE, HttpStatus.BAD_REQUEST);
	    }
	}
}
