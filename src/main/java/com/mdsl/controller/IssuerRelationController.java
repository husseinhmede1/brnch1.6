package com.mdsl.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
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
import com.mdsl.model.dto.request.IssuerRelationPanRangeRequestDto;
import com.mdsl.model.dto.request.IssuerRelationRequestDto;
import com.mdsl.model.dto.request.IssuerRelationSearchRequestDto;
import com.mdsl.model.dto.request.PaginatedRequestDto;
import com.mdsl.model.dto.response.IssuerRelationResponseDto;
import com.mdsl.service.IssueRelationService;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.Validations;

@CrossOrigin(origins = "*")
@RestController
//@Transactional(rollbackOn = Exception.class)
@RequestMapping("/issuer-relation")
public class IssuerRelationController {
	private static final Logger logger = LoggerFactory.getLogger(IssuerRelationController.class);
	private final IssueRelationService issueRelationService;
	
	@Autowired
	public IssuerRelationController(IssueRelationService issueRelationService) {
		this.issueRelationService = issueRelationService;
	}
	
	@GetMapping
	public ResponseEntity<List<IssuerRelationResponseDto>> getAllIssuerRealtions() {
		return ResponseEntity.ok(this.issueRelationService.getAllIssuerRelations());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<IssuerRelationResponseDto> getIssuerRelationById(@PathVariable(value="id") int id) {
		return ResponseEntity.ok(this.issueRelationService.getIssuerRelationById(id));
	}
	
	@GetMapping("/issuer-profile/{issuerAcqProfile}/{instId}")
	public ResponseEntity<List<IssuerRelationResponseDto>> getIssuerRelationByIssuerAcqProfile(@PathVariable(value="issuerAcqProfile") String issuerAcqProfile,@PathVariable(value="instId") String instId) {
		return ResponseEntity.ok(this.issueRelationService.getIssuerRelationByIssuerAcqProfile(issuerAcqProfile,instId));
	}
	
	@PostMapping("/pan")
	public ResponseEntity<List<IssuerRelationResponseDto>> getAllIssuerRelationsByPanRange(@Valid @RequestBody IssuerRelationPanRangeRequestDto issuerRelationPanRangeRequestDto, BindingResult bindingResult, HttpServletRequest request) {
		Validations.validate(bindingResult);
		return ResponseEntity.ok(issueRelationService.getAllIssuerRelationsByPanRange(issuerRelationPanRangeRequestDto));
	}
	
	@PostMapping("/all/{issuerProfileId}")
	public ResponseEntity<List<IssuerRelationResponseDto>> getAllPaginatedIssuerRelationByIssuerId(@PathVariable(value="issuerProfileId") int issuerProfileId, @Valid @RequestBody PaginatedRequestDto paginatedRequestDto, BindingResult bindingResult, HttpServletRequest request){
		Validations.validate(bindingResult);
		return ResponseEntity.ok(issueRelationService.getAllPaginatedIssuerRelationByIssuerId(issuerProfileId, paginatedRequestDto));
	}
	
	@PostMapping("/search")
	public ResponseEntity<List<IssuerRelationResponseDto>> getIssuerRelationsByPanRange(@Valid @RequestBody IssuerRelationSearchRequestDto issuerRelationSearchRequestDto, BindingResult bindingResult, HttpServletRequest request){
		Validations.validate(bindingResult);
		return ResponseEntity.ok(issueRelationService.getIssuerRelationsByPanRange(issuerRelationSearchRequestDto));
	}
	
	@PostMapping
	public ResponseEntity<IssuerRelationResponseDto> saveIssuerRelation(@Valid @RequestBody IssuerRelationRequestDto issuerRelationRequestDto, BindingResult bindingResult) {
		try {
			Validations.validate(bindingResult);
			return ResponseEntity.ok(this.issueRelationService.saveOrUpdateIssuer(issuerRelationRequestDto));
		} catch (BusinessException e) {
		    throw new BusinessException (e.getMessage(), e.getHttpStatus());
		} catch (Exception e) {
		    logger.error("@IssuerRelationController#saveIssuerRelation: " + e.getMessage());
		    throw new BusinessException (ResponseCode.ISS_ISSUER_RELATION_NO_SAVE, HttpStatus.BAD_REQUEST);
	    }	
	}
	
	@DeleteMapping("/{id}")
	public void deleteIssuerRelation(@PathVariable(value="id") int id) {
		try {
			this.issueRelationService.deleteIssuerRelation(id, null);
		} catch (BusinessException e) {
		    throw new BusinessException (e.getMessage(), e.getHttpStatus());
		} catch (Exception e) {
		    logger.error("@IssuerRelationController#deleteIssuerRelation: " + e.getMessage());
		    throw new BusinessException (ResponseCode.ISS_ISSUER_RELATION_NO_DELETE, HttpStatus.BAD_REQUEST);
	    }
	}
}