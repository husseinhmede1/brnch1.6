package com.mdsl.controller;

import java.util.List;

import javax.naming.Binding;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import com.mdsl.model.dto.request.ManualNonActivityFeesPackageRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.mdsl.model.dto.request.ManualNonActivityTransactionRequestDto;
import com.mdsl.model.dto.request.NonActivityFeeQueryRequestDto;
import com.mdsl.model.dto.request.ManualNonActivityTransactionRequestDto;
import com.mdsl.model.dto.response.CurrencyConversionResponseDto;
import com.mdsl.model.dto.response.ManualNonActivityTransactionResponseDto;
import com.mdsl.model.dto.response.ManualNonActivityTransactionResponseDto;
import com.mdsl.model.dto.response.PaginationResponseDto;
import com.mdsl.service.ManualNonActivityTransactionService;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.Validations;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/manualnonactivitytransaction")
public class ManualNonActivityTransactionController {
	@Autowired
	private ManualNonActivityTransactionService manualNonActivityTransactionService;
	private static final Logger logger = LoggerFactory.getLogger(ManualNonActivityTransactionController.class);

	public ManualNonActivityTransactionController(ManualNonActivityTransactionService manualNonActivityTransactionService) {
		this.manualNonActivityTransactionService = manualNonActivityTransactionService;
	}

	@PostMapping("/getAllNonActivityTransactions")
	public ResponseEntity<PaginationResponseDto> nonActivityPagination(@Valid @RequestBody ManualNonActivityTransactionRequestDto manualNonActivityTransactionRequestDto,BindingResult bindingResult) {
		Validations.validate(bindingResult);
		ResponseEntity<PaginationResponseDto> responseEntity = null;
		try {
			responseEntity = manualNonActivityTransactionService
					.getAllTransactions(manualNonActivityTransactionRequestDto);

		} catch (Exception e) {
			e.printStackTrace();
			responseEntity = new ResponseEntity<>(new PaginationResponseDto(false, null, null, null, null),
					HttpStatus.OK);
		}
		return responseEntity;
	}

	@GetMapping("/{id}")
	public ResponseEntity<ManualNonActivityTransactionResponseDto> getManualNonActivityTransactionById(@PathVariable("id") int id) {
		ManualNonActivityTransactionResponseDto transaction = manualNonActivityTransactionService
				.getManualNonActivityTransactionById(id);
		return ResponseEntity.ok(transaction);
	}

	@PostMapping
	public ResponseEntity saveOrUpdateManualNonActivityTransaction(@Valid @RequestBody ManualNonActivityTransactionRequestDto transactionDto,BindingResult bindingResult,HttpServletRequest httpServletRequest) {
		Validations.validate(bindingResult);
		try {
			if (transactionDto.getInstitutionId() != null) {
				return ResponseEntity.ok(manualNonActivityTransactionService.saveOrUpdateManualNonActivityTransaction(transactionDto));
			}else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Institution Id not found");
			}
		} catch (BusinessException e) {
		    throw new BusinessException (e.getMessage(), e.getHttpStatus());
		} catch (Exception e) {
		    logger.error("@ManualNonActivityTransactionController#saveOrUpdateManualNonActivityTransaction: " + e.getMessage());
		    throw new BusinessException (ResponseCode.MNT_NO_SAVE, HttpStatus.BAD_REQUEST);
	    }
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteManualNonActivityTransaction(@PathVariable("id") int id) {
		try {
			manualNonActivityTransactionService.deleteManualNonActivityTransaction(id);
			String message = "An item is deleted with id : " + id;
			return ResponseEntity.ok(message);
		} catch (BusinessException e) {
		    throw new BusinessException (e.getMessage(), e.getHttpStatus());
		} catch (Exception e) {
		    logger.error("@ManualNonActivityTransactionController#saveOrUpdateManualNonActivityTransaction: " + e.getMessage());
		    throw new BusinessException (ResponseCode.MNT_NO_DELETE, HttpStatus.BAD_REQUEST);
	    }
	}
	@PostMapping("/institution")
	public ResponseEntity<PaginationResponseDto> getManualNonActivityTransactionByInstitutionIdPagination(@Valid @RequestBody ManualNonActivityFeesPackageRequestDto manualNonActivityTransactionRequestDto, BindingResult bindingResult,HttpServletRequest httpServletRequest) {
		Validations.validate(bindingResult);
		ResponseEntity<PaginationResponseDto> responseEntity = null;
		try {
			if (manualNonActivityTransactionRequestDto.getInstitutionId() == null) {
				String instId = httpServletRequest.getHeader("instId");
				if (instId != null) {
					manualNonActivityTransactionRequestDto.setInstitutionId(instId);
				}
			}
			responseEntity = manualNonActivityTransactionService
					.getManualNonActivityTransactionByInstitutionId(manualNonActivityTransactionRequestDto);

		} catch (Exception e) {
			e.printStackTrace();
			responseEntity = new ResponseEntity<>(new PaginationResponseDto(false, null, null, null, null),
					HttpStatus.OK);
		}
		return responseEntity;
	}

	@PostMapping("/search")
	public ResponseEntity<PaginationResponseDto> manualNonActivityTransactionRequestDto(@Valid @RequestBody ManualNonActivityTransactionRequestDto manualNonActivityTransactionRequestDto,BindingResult bindingResult,HttpServletRequest httpServletRequest) {
		Validations.validate(bindingResult);
		ResponseEntity<PaginationResponseDto> responseEntity = null;
		try {
			if (manualNonActivityTransactionRequestDto.getInstitutionId() == null) {
				String instId = httpServletRequest.getHeader("instId");
				if (instId != null) {
					manualNonActivityTransactionRequestDto.setInstitutionId(instId);
				}
			}
			responseEntity = manualNonActivityTransactionService
					.getTransactionsBySearch(manualNonActivityTransactionRequestDto);

		} catch (Exception e) {
			e.printStackTrace();
			responseEntity = new ResponseEntity<>(new PaginationResponseDto(false, null, null, null, null),
					HttpStatus.OK);
		}
		return responseEntity;
	}
}
