package com.mdsl.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import com.mdsl.utils.Validations;
import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.ChangeStatusRequestDto;
import com.mdsl.model.dto.request.DefaultTransactionIdRequestDto;
import com.mdsl.model.dto.request.DeleteDefaultTransactionIdRequestDto;
import com.mdsl.model.dto.request.SaveOrUpdateDefaultTransactionIdRequestDto;
import com.mdsl.model.dto.response.DefaultTransactionIdResponseDto;
import com.mdsl.service.DefaultTransactionIdService;
import com.mdsl.utils.ResponseCode;

@CrossOrigin(origins = "*")
@RestController
//@Transactional(rollbackOn = Exception.class)
@RequestMapping("/defaulttransactionid")
public class DefaultTransactionIdController {
	private static final Logger logger = LoggerFactory.getLogger(DefaultTransactionIdController.class);

	@Autowired
	private DefaultTransactionIdService defaultTransactionIdService;

	@PostMapping
	public ResponseEntity addDefaulTransactionId(@Valid @RequestBody DefaultTransactionIdRequestDto defaultTransactionIdRequestDto,BindingResult bindingResult,HttpServletRequest httpServletRequest) {
		Validations.validate(bindingResult);
		try {
			return ResponseEntity.ok(defaultTransactionIdService.saveOrUpdateDefaultTransactionId(
					SaveOrUpdateDefaultTransactionIdRequestDto.builder()
							.defaultTransactionIdRequestDto(defaultTransactionIdRequestDto)
							.instId(httpServletRequest.getHeader("instId"))
							.build()));
		} catch (BusinessException e) {
		    throw new BusinessException (e.getMessage(), e.getHttpStatus());
		} catch (Exception e) {
		    logger.error("@DefaultTransactionIdController#addDefaulTransactionId: " + e.getMessage());
		    throw new BusinessException (ResponseCode.CFG_DEFAULT_TRANSACTION_NO_SAVE, HttpStatus.BAD_REQUEST);
	    }
		
	}

	@GetMapping
	public ResponseEntity viewDefaultTransactionId() {
		return ResponseEntity.ok(defaultTransactionIdService.viewDefaultTransactionId());
	}

	@GetMapping("/{defaulttransactionid}")
	public ResponseEntity getDefaultTransactionId(@PathVariable("defaulttransactionid") String defaultTransactionId,HttpServletRequest httpServletRequest) {
		return ResponseEntity.ok(defaultTransactionIdService.getDefaultTransactionId(defaultTransactionId,httpServletRequest.getHeader("instId")));
	}

	@PostMapping("/usage")
	public ResponseEntity<List<DefaultTransactionIdResponseDto>> getDefaultTransactionIdByTransUsage(
			@RequestBody DefaultTransactionIdRequestDto requestDto,BindingResult bindingResult,HttpServletRequest httpServletRequest) {
		Validations.validate(bindingResult);
		return ResponseEntity.ok(defaultTransactionIdService.getDefaultTransactionIdByTransUsage(requestDto,httpServletRequest.getHeader("instId")));
	}
	
	@PostMapping("/usage/{instId}")
	public ResponseEntity<List<DefaultTransactionIdResponseDto>> getDefaultTransactionIdByTransUsageAndInstitutionId(
			@RequestBody DefaultTransactionIdRequestDto requestDto,BindingResult bindingResult, @PathVariable("instId") String instId) {
		Validations.validate(bindingResult);
		return ResponseEntity.ok(defaultTransactionIdService.getDefaultTransactionIdByTransUsageAndInstitutionId(requestDto, instId));
	}

	@DeleteMapping("/{defaulttransactionid}")
	public ResponseEntity<String> deleteDefaultTransactionId(
			@PathVariable("defaulttransactionid") String defaultTransactionId,HttpServletRequest httpServletRequest) {
		try {
			defaultTransactionIdService.deleteDefaultTransactionId(DeleteDefaultTransactionIdRequestDto.builder()
					.defaultTransactionId(defaultTransactionId)
					.instId(httpServletRequest.getHeader("instId"))
					.build());
			String message = "An item is deleted with id : " + defaultTransactionId;
			return ResponseEntity.ok(message);
		} catch (BusinessException e) {
		    throw new BusinessException (e.getMessage(), e.getHttpStatus());
		} catch (Exception e) {
		    logger.error("@DefaultTransactionIdController#deleteDefaultTransactionId: " + e.getMessage());
		    throw new BusinessException (ResponseCode.CFG_DEFAULT_TRANSACTION_NO_DELETE, HttpStatus.BAD_REQUEST);
	    }
//		return null;
	}
	
	@GetMapping("/institution/{institutionid}")
	public ResponseEntity getDefaultTransactionIdByInstitutionId(@PathVariable("institutionid") String institutionid) {

		return ResponseEntity.ok(defaultTransactionIdService.getDefaultTransactionByInstitution(institutionid));
	}
	
	@GetMapping("/inst/{institutionid}")
	public ResponseEntity getInstDefaultTransactionIdByInstitutionId(@PathVariable("institutionid") String institutionid) {

		return ResponseEntity.ok(defaultTransactionIdService.getInstDefaultTransactionIdByInstitutionId(institutionid));
	}
	
	@PostMapping("/status-change")
	public ResponseEntity<String> changeStatus(@Valid @RequestBody ChangeStatusRequestDto changeStatusRequestDto,BindingResult bindingResult, HttpServletRequest request) {
		Validations.validate(bindingResult);
		changeStatusRequestDto.setInstId(request.getHeader("instId"));
		return ResponseEntity.ok(defaultTransactionIdService.changeStatus(changeStatusRequestDto));
	}

	@GetMapping("get-all/{institutionId}")
	public ResponseEntity<List<String>> getAllDefaultTransactionId(@PathVariable("institutionId") String institutionId) {
		return defaultTransactionIdService.getAllDefaultTransactionId(institutionId);
	}
}
