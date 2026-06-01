package com.mdsl.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.mdsl.utils.Validations;
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
import com.mdsl.model.dto.request.ManualMerchantTransactionRequestDto;
import com.mdsl.model.dto.response.ManualMerchantTransactionResponseDto;
import com.mdsl.model.dto.response.PaginationResponseDto;
import com.mdsl.service.ManualMerchantTransactionService;
import com.mdsl.utils.ResponseCode;

@CrossOrigin(origins = "*")
@RestController
//@Transactional(rollbackOn = Exception.class)
@RequestMapping("/manualmerchanttransaction")
public class ManualMerchantTransactionController {

	@Autowired
	private ManualMerchantTransactionService manualMerchantTransactionService;
	private static final Logger logger = LoggerFactory.getLogger(ManualMerchantTransactionController.class);

	public ManualMerchantTransactionController(ManualMerchantTransactionService manualMerchantTransactionService) {
		this.manualMerchantTransactionService = manualMerchantTransactionService;
	}


	@PostMapping("/getAllMerchantTransactions")
	public ResponseEntity<PaginationResponseDto> merchantPagination(
			@RequestBody ManualMerchantTransactionRequestDto manualMerchantTransactionRequestDto, BindingResult bindingResult) {
		Validations.validate(bindingResult);
		ResponseEntity<PaginationResponseDto> responseEntity = null;
		try {
			responseEntity = manualMerchantTransactionService.getAllTransactions(manualMerchantTransactionRequestDto);
		} catch (Exception e) {
			e.printStackTrace();
			responseEntity = new ResponseEntity<>(new PaginationResponseDto(false, null, null, null, null),
					HttpStatus.OK);
		}
		return responseEntity;
	}

	@GetMapping("/{id}")
	public ResponseEntity<ManualMerchantTransactionResponseDto> getManualMerchantTransactionById(
			@PathVariable("id") int id) {
		ManualMerchantTransactionResponseDto transaction = manualMerchantTransactionService
				.getManualMerchantTransactionById(id);
		return ResponseEntity.ok(transaction);
	}

	@PostMapping
	public ResponseEntity saveOrUpdateManualMerchantTransaction(
			@Valid @RequestBody ManualMerchantTransactionRequestDto merchantTransactionRequestDto,
			HttpServletRequest httpServletRequest,BindingResult bindingResult) {
		Validations.validate(bindingResult);
		try {
			return ResponseEntity.ok(manualMerchantTransactionService
					.saveOrUpdateManualMerchantTransaction(merchantTransactionRequestDto));
		} catch (BusinessException e) {
		    throw new BusinessException (e.getMessage(), e.getHttpStatus());
		} catch (Exception e) {
		    logger.error("@ManualMerchantTransactionController#saveOrUpdateManualMerchantTransaction: " + e.getMessage());
		    throw new BusinessException (ResponseCode.MMT_NO_SAVE, HttpStatus.BAD_REQUEST);
	    }
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteManualMerchantTransaction(@PathVariable("id") int id) {
		try {
			manualMerchantTransactionService.deleteManualMerchantTransaction(id);
			String message = "An item is deleted with id : " + id;
			return ResponseEntity.ok(message);
		} catch (BusinessException e) {
		    throw new BusinessException (e.getMessage(), e.getHttpStatus());
		} catch (Exception e) {
		    logger.error("@ManualMerchantTransactionController#deleteManualMerchantTransaction: " + e.getMessage());
		    throw new BusinessException (ResponseCode.MMT_NO_DELETE, HttpStatus.BAD_REQUEST);
	    }
//		return null;
	}

	@PostMapping(value = "/institution")
	public ResponseEntity<PaginationResponseDto> getManualMerchantTransactionByInstitutionId(
			HttpServletRequest httpServletRequest,
			@Valid @RequestBody ManualMerchantTransactionRequestDto manualMerchantTransactionRequestDto,BindingResult bindingResult) {
		Validations.validate(bindingResult);
		ResponseEntity<PaginationResponseDto> responseEntity = null;
		try {
			if (manualMerchantTransactionRequestDto.getInstitutionId() == null) {
				String instId = httpServletRequest.getHeader("instId");
				if (instId != null) {
					manualMerchantTransactionRequestDto.setInstitutionId(instId);
				}
			}
			responseEntity = manualMerchantTransactionService
					.getManualMerchantTransactionByInstitutionId(manualMerchantTransactionRequestDto);

		} catch (Exception e) {
			e.printStackTrace();
			responseEntity = new ResponseEntity<>(new PaginationResponseDto(false, null, null, null, null),
					HttpStatus.OK);
		}
		return responseEntity;
	}

	@PostMapping("/search")
	public ResponseEntity<PaginationResponseDto> getTransactionsBySearch(HttpServletRequest httpServletRequest,
			@RequestBody ManualMerchantTransactionRequestDto manualMerchantTransactionRequestDto) {

		ResponseEntity<PaginationResponseDto> responseEntity = null;
		try {
			if (manualMerchantTransactionRequestDto.getInstitutionId() == null) {
				String instId = httpServletRequest.getHeader("instId");
				if (instId != null)
					manualMerchantTransactionRequestDto.setInstitutionId(instId);
			}
			responseEntity = manualMerchantTransactionService
					.getTransactionsBySearch(manualMerchantTransactionRequestDto);

		} catch (Exception e) {
			e.printStackTrace();
			responseEntity = new ResponseEntity<>(new PaginationResponseDto(false, null, null, null, null),
					HttpStatus.OK);
		}
		return responseEntity;

	}

}
