package com.mdsl.controller;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.mdsl.model.dto.request.IssuerProfileRequestDto;
import com.mdsl.model.dto.response.IssuerProfileResponseDto;
import com.mdsl.service.IssuerProfileService;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.Validations;

@CrossOrigin(origins = "*")
@RestController
@Transactional(rollbackOn = Exception.class)
@RequestMapping("/issuer-profile")
public class IssuerProfileController {
	private static final Logger logger = LoggerFactory.getLogger(IssuerProfileController.class);
	private final IssuerProfileService issuerProfileService;
	
	@Autowired
	public IssuerProfileController(IssuerProfileService issuerProfileService) {
		this.issuerProfileService = issuerProfileService;
	}
	
	@GetMapping
	public ResponseEntity<List<IssuerProfileResponseDto>> getAllIssuerProfiles() {
		return ResponseEntity.ok(this.issuerProfileService.getAllIssuerProfiles());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<IssuerProfileResponseDto> getIssuerProfileById(@PathVariable(value="id") int id) {
		return ResponseEntity.ok(this.issuerProfileService.getIssuerProfileById(id));
	}
	
	@GetMapping("/issuerAcqProfile/{issuerAcqProfile}")
	public ResponseEntity<IssuerProfileResponseDto> getIssuerProfileByIssuerAcqProfile(@PathVariable(value="issuerAcqProfile") String issuerAcqProfile) {
		return ResponseEntity.ok(this.issuerProfileService.getIssuerProfileByIssuerAcqProfile(issuerAcqProfile));
	}
	
	@GetMapping("/inst/{id}")
	public ResponseEntity<List<IssuerProfileResponseDto>> getIssuerProfilesByInstitutionId(@PathVariable(value="id") String instId) {
		return ResponseEntity.ok(this.issuerProfileService.getIssuerProfilesByInstitutionId(instId));
	}
	
	@PostMapping
	public ResponseEntity<IssuerProfileResponseDto> saveIssuerProfile(@Valid @RequestBody IssuerProfileRequestDto issuerProfileRequestDto, BindingResult bindingResult) {
		Validations.validate(bindingResult);
		try {
			return ResponseEntity.ok(this.issuerProfileService.saveIssuerProfile(issuerProfileRequestDto));
		} catch (BusinessException e) {
		    throw new BusinessException (e.getMessage(), e.getHttpStatus());
		} catch (Exception e) {
		    logger.error("@IssuerProfileController#saveIssuerProfile: " + e.getMessage());
		    throw new BusinessException (ResponseCode.ISS_ISSUER_PROFILE_NO_SAVE, HttpStatus.BAD_REQUEST);
	    }
	}
	
	@DeleteMapping("/{id}")
	public void deleteIssuerProfile(@PathVariable(value="id") int id) {
		try {
			this.issuerProfileService.deleteIssuerProfile(id);
		} catch (BusinessException e) {
		    throw new BusinessException (e.getMessage(), e.getHttpStatus());
		} catch (Exception e) {
		    logger.error("@IssuerProfileController#deleteIssuerProfile: " + e.getMessage());
		    throw new BusinessException (ResponseCode.ISS_ISSUER_PROFILE_NO_DELETE, HttpStatus.BAD_REQUEST);
	    }
	}
}