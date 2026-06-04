package com.mdsl.controller;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;import com.mdsl.exceptionHandling.BusinessException;
import lombok.RequiredArgsConstructor;import org.springframework.beans.factory.annotation.Autowired;
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
import com.mdsl.model.dto.request.AcquiringTransactionAdjustmentRequestDto;
import com.mdsl.model.dto.request.AcquiringTransactionRequestDto;
import com.mdsl.model.dto.request.UnhaltRequestDto;
import com.mdsl.model.dto.response.AcquiringTransactionResponseDto;
import com.mdsl.model.dto.response.PaginationResponseDto;
import com.mdsl.model.dto.response.TransactionCurrentSearchResponseDto;
import com.mdsl.service.AcquiringTransactionService;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.Validations;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;

@Api("Acquiring Transactions Controller")
@CrossOrigin(origins = "*")
@RestController
@Log4j2
@RequestMapping("/acquiringtransaction")
@RequiredArgsConstructor
public class AcquiringTransactionController {

	private final AcquiringTransactionService acquiringTransactionService;
	private static final Logger logger = LoggerFactory.getLogger(AcquiringTransactionController.class);

	@ApiOperation(value = "Save Acquiring Transactions", response = AcquiringTransactionResponseDto.class)
	@PostMapping
	public ResponseEntity saveOrUpdateAcquiringTransaction(
			@Valid @RequestBody AcquiringTransactionRequestDto acquiringTransactionRequestDto,
			HttpServletRequest httpServletRequest, BindingResult bindingResult) {
		Validations.validate(bindingResult);
		try{
			String instId = acquiringTransactionRequestDto.getInstitutionId();
			String institutionId = null;
	
			if (instId == null) {
				institutionId = httpServletRequest.getHeader("instId");
			} else {
				institutionId = String.valueOf(instId);
			}
			if (institutionId != null) {
				acquiringTransactionRequestDto.setInstitutionId(instId);
				return ResponseEntity.ok(acquiringTransactionService
						.saveOrUpdateAcquiringTransaction(acquiringTransactionRequestDto));
			} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Institution Id not found");
			}
	   } catch (BusinessException e) {
		  throw new BusinessException (e.getMessage(), e.getHttpStatus());
	   } catch (Exception e) {
		logger.error("@AcquiringTransactionController#saveOrUpdateAcquiringTransaction: " + e.getMessage());
		throw new BusinessException (ResponseCode.CFG_ACQUIRING_TRANSACTION_NO_SAVE, HttpStatus.BAD_REQUEST);
	   }	}

	@ApiOperation(value = "View Acquiring Transaction",response = PaginationResponseDto.class)
	@PostMapping("/getAcquiringTransactions")
	public ResponseEntity<PaginationResponseDto> viewAcquiringTransaction(HttpServletRequest httpServletRequest,
			@RequestBody AcquiringTransactionRequestDto acquiringTransactionRequestDto, BindingResult bindingResult) {
		ResponseEntity<PaginationResponseDto> responseEntity = null;
		Validations.validate(bindingResult);
		try {
			responseEntity = acquiringTransactionService.viewAcquiringTransaction(acquiringTransactionRequestDto);
		} catch (Exception e) {
			e.printStackTrace();
			responseEntity = new ResponseEntity<>(new PaginationResponseDto(false, null, null, null, null),
					HttpStatus.OK);
		}
		return responseEntity;

	}

	@ApiOperation(value = "Get Acquiring Transaction by Id",response = AcquiringTransactionResponseDto.class)
	@GetMapping("/{acquiringTransactionId}")
	public ResponseEntity getAcquiringTransaction(@PathVariable("acquiringTransactionId") int acquiringTransactionId) {
		return ResponseEntity.ok(acquiringTransactionService.getAcquiringTransaction(acquiringTransactionId));
	}
	
	@ApiOperation(value = "Delete Acquiring Transaction")
	@DeleteMapping("/{acquiringTransactionId}")
	public ResponseEntity<String> deleteAcquiringTransaction(
			@PathVariable("acquiringTransactionId") int acquiringTransactionId) {

		try {
			acquiringTransactionService.deleteAcquiringTransaction(acquiringTransactionId);
			String message = "An item is deleted with id : " + acquiringTransactionId;
			return ResponseEntity.ok(message);
		} catch (DataIntegrityViolationException e) {
			System.out.println("reference exists");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("DataIntegrityViolationException");
		} catch (ConstraintViolationException e) {
			System.out.println("reference exists");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("reference exists");
		}
		return null;
	}

	@GetMapping("/representment/{acquiringTransactionId}")
	public ResponseEntity<String> getAcquiringTransactionByRepresentment(
			@PathVariable int acquiringTransactionId) {

		boolean acqTransaction;
		try {
			acqTransaction = acquiringTransactionService.getAcquiringTransactionByRepresenment(acquiringTransactionId);
		} catch (BusinessException ex) {
			return ResponseEntity.status(ex.getHttpStatus()).body(ex.getMessage());
		} catch (DataIntegrityViolationException ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Data integrity violation occurred");
		}

		if (acqTransaction) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Representment of this transaction is already being done");
		}

		return ResponseEntity.ok("Transaction is available for representment");
	}

	@ApiOperation(value = "Get Acquiring Transaction by Reversal",response = Boolean.class)
	@GetMapping("/reversal/{acquiringTransactionId}")
	public ResponseEntity<String> getAcquiringTransactionByReversal(
			@PathVariable("acquiringTransactionId") int acquiringTransactionId) {

		try {
			boolean acqTransaction= acquiringTransactionService.getAcquiringTransactionByReversal(acquiringTransactionId);
			
			if(acqTransaction) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This transaction is already reversed");
			}
		} catch (DataIntegrityViolationException e) {
			System.out.println("reference exists");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("DataIntegrityViolationException");
		} catch (ConstraintViolationException e) {
			System.out.println("reference exists");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("reference exists");
		}
		return null;
	}


	@ApiOperation(value = "Save Representment", response = AcquiringTransactionResponseDto.class)
	@PostMapping("/representment")
	public ResponseEntity<?> saveOrUpdateRepresentment(
			@Valid @RequestBody AcquiringTransactionRequestDto acquiringTransactionRequestDto,
			HttpServletRequest httpServletRequest,
			BindingResult bindingResult) {
		Validations.validate(bindingResult);
		try {
			String institutionId = httpServletRequest.getHeader("instId");
			if (institutionId == null || institutionId.isEmpty()) {
				throw new BusinessException("Institution ID not found in request headers", HttpStatus.BAD_REQUEST);
			}
			acquiringTransactionRequestDto.setInstitutionId(institutionId);
			return ResponseEntity.ok(acquiringTransactionService.saveOrUpdateRepresentment(acquiringTransactionRequestDto));

		} catch (BusinessException ex) {
			return ResponseEntity.status(ex.getHttpStatus()).body(ex.getMessage());
		} catch (DataIntegrityViolationException ex) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body("Data integrity violation occurred: " + ex.getMostSpecificCause().getMessage());
		} catch (ConstraintViolationException ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Validation error: " + ex.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An unexpected error occurred: " + ex.getMessage());
		}
	}

	@ApiOperation(value = "Save Halt Pay Transaction",response = AcquiringTransactionResponseDto.class)
	@PostMapping("/haltpaytransaction")
	public ResponseEntity saveOrUpdateHaltPayTransaction(
			@Valid @RequestBody AcquiringTransactionRequestDto acquiringTransactionRequestDto,
			HttpServletRequest httpServletRequest, BindingResult bindingResult) throws ParseException, CloneNotSupportedException {
		Validations.validate(bindingResult);
		String institutionId = httpServletRequest.getHeader("instId");
		acquiringTransactionRequestDto.setInstitutionId(institutionId);
		
		return ResponseEntity
				.ok(acquiringTransactionService.saveOrUpdateHaltPayTransaction(acquiringTransactionRequestDto));
	}

	@ApiOperation(value = "Unhalt transaction", response = AcquiringTransactionResponseDto.class)
	@PostMapping("/unhaltTransaction")
	public ResponseEntity<?> unhaltPayTransaction(
			@Valid @RequestBody UnhaltRequestDto unhaltRequestDto,
			HttpServletRequest httpServletRequest,
			BindingResult bindingResult) {
		Validations.validate(bindingResult);
		try {
			String institutionId = httpServletRequest.getHeader("instId");
			if (institutionId == null || institutionId.isEmpty()) {
				throw new BusinessException("Institution ID not found in request headers", HttpStatus.BAD_REQUEST);
			}
			unhaltRequestDto.setInstitutionId(institutionId);
			return ResponseEntity.ok(acquiringTransactionService.unhaltTransaction(unhaltRequestDto));
		} catch (BusinessException ex) {
			return ResponseEntity.status(ex.getHttpStatus()).body(ex.getMessage());
		} catch (DataIntegrityViolationException ex) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body("Data integrity violation occurred: " + ex.getMostSpecificCause().getMessage());
		} catch (ConstraintViolationException ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Validation error: " + ex.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An unexpected error occurred: " + ex.getMessage());
		}
	}

	@ApiOperation(value = "Save Reversal Transaction", response = AcquiringTransactionResponseDto.class)
	@PostMapping("/reversalTransaction")
	public ResponseEntity<?> saveOrUpdateReversalTransaction(
			@Valid @RequestBody AcquiringTransactionRequestDto acquiringTransactionRequestDto,
			HttpServletRequest httpServletRequest,
			BindingResult bindingResult) {

		Validations.validate(bindingResult);
		try {
			String institutionId = httpServletRequest.getHeader("instId");
			if (institutionId == null || institutionId.isEmpty()) {
				throw new BusinessException("Institution ID not found in request headers", HttpStatus.BAD_REQUEST);
			}

			acquiringTransactionRequestDto.setInstitutionId(institutionId);

			return ResponseEntity.ok(
					acquiringTransactionService.saveOrUpdateReversalTransaction(acquiringTransactionRequestDto)
			);

		} catch (BusinessException ex) {
			return ResponseEntity.status(ex.getHttpStatus()).body(ex.getMessage());
		} catch (DataIntegrityViolationException ex) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body("Data integrity violation occurred: " + ex.getMostSpecificCause().getMessage());
		} catch (ConstraintViolationException ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Validation error: " + ex.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An unexpected error occurred: " + ex.getMessage());
		}
	}

	@ApiOperation(value = "Get Acquiring Transactions by Institution Id", response = PaginationResponseDto.class)
	@PostMapping("/institution/{institutionid}")
	public ResponseEntity<PaginationResponseDto> getAcquireTransactionByInstitutionId(
			@PathVariable String institutionid,
			HttpServletRequest httpServletRequest,
			@RequestBody AcquiringTransactionRequestDto acquiringTransactionRequestDto,
			BindingResult bindingResult) {
		Validations.validate(bindingResult);
		if (institutionid == null || institutionid.isEmpty()) {
			throw new BusinessException("Institution ID is required", HttpStatus.BAD_REQUEST);
		}

		return acquiringTransactionService.getAcquiringTransactionByInstitutionId(
				institutionid, acquiringTransactionRequestDto);
	}

	@ApiOperation(value = "Get Acquiring Transactions by Entity Id",response = AcquiringTransactionResponseDto.class)
	@GetMapping("/entities/{entitiesid}")
	public ResponseEntity getAcquireTransactionByEntityId(@PathVariable("entitiesid") String entitiesid) {
		return ResponseEntity.ok(acquiringTransactionService.getAcquiringTransactionByEntitiesId(entitiesid));
	}

	@ApiOperation(value = "Get Acquiring Transactions by Entity Id and Institution Id", response = PaginationResponseDto.class)
	@PostMapping("/{entitiesid}/{institutionid}")
	public ResponseEntity<PaginationResponseDto> getAcquireTransactionByEntityIdAndInstitutionId(
			@PathVariable String entitiesid,
			@PathVariable String institutionid,
			HttpServletRequest httpServletRequest,
			@RequestBody AcquiringTransactionRequestDto acquiringTransactionRequestDto,
			BindingResult bindingResult) {

		Validations.validate(bindingResult);
		if (entitiesid == null || institutionid == null) {
			throw new BusinessException("Entity ID and Institution ID are required", HttpStatus.BAD_REQUEST);
		}

		return acquiringTransactionService.getAcquiringTransactionByEntitiesIdAndInstitutionId(
				entitiesid, institutionid, acquiringTransactionRequestDto);
	}

	@ApiOperation(value = "Search Acquiring Transactions", response = PaginationResponseDto.class)
	@PostMapping("/search")
	public ResponseEntity<PaginationResponseDto> getAcquireTransactionBySearch(
			HttpServletRequest httpServletRequest,
			@RequestBody AcquiringTransactionRequestDto acquiringTransactionRequestDto,
			BindingResult bindingResult) {
		if (acquiringTransactionRequestDto.getMerchantName() != null) {
			if (acquiringTransactionRequestDto.getMerchantName().length() > 50 || !acquiringTransactionRequestDto.getMerchantName().matches("^[a-zA-Z0-9 ]*$")) {
				throw new BusinessException(ResponseCode.MRC_INVALID_MERCHANT_NAME,HttpStatus.BAD_REQUEST);
			}
		}
		Validations.validate(bindingResult);

		String instId = acquiringTransactionRequestDto.getInstitutionId();
		if (instId == null) {
			instId = httpServletRequest.getHeader("instId");
		}

		if (instId == null) {
			throw new BusinessException("Institution ID is required", HttpStatus.BAD_REQUEST);
		}

		acquiringTransactionRequestDto.setInstitutionId(instId);

		return acquiringTransactionService.getAcquiringTransactionBySearchImproved(acquiringTransactionRequestDto);
	}

	@ApiOperation(value = "Search Acquiring Transactions", response = PaginationResponseDto.class)
	@PostMapping("/all")
	public ResponseEntity<PaginationResponseDto> getAllAcquireTransactionBySearch(
			HttpServletRequest httpServletRequest,
			@RequestBody AcquiringTransactionRequestDto acquiringTransactionRequestDto,
			BindingResult bindingResult) {
		if (acquiringTransactionRequestDto.getMerchantName() != null) {
			if (acquiringTransactionRequestDto.getMerchantName().length() > 50 || !acquiringTransactionRequestDto.getMerchantName().matches("^[a-zA-Z0-9 ]*$")) {
				throw new BusinessException(ResponseCode.MRC_INVALID_MERCHANT_NAME,HttpStatus.BAD_REQUEST);
			}
		}
		Validations.validate(bindingResult);

		String instId = acquiringTransactionRequestDto.getInstitutionId();
		if (instId == null) {
			instId = httpServletRequest.getHeader("instId");
		}

		if (instId == null) {
			throw new BusinessException("Institution ID is required", HttpStatus.BAD_REQUEST);
		}

		acquiringTransactionRequestDto.setInstitutionId(instId);

		return acquiringTransactionService.getAcquiringTransactionBySearchImproved(acquiringTransactionRequestDto);
	}

	@ApiOperation(value = "Apply Accounting Adjustment", response = TransactionCurrentSearchResponseDto.class)
	@PostMapping("/account-adjustment")
	public ResponseEntity applyAccountingAdjustment(
			@Valid @RequestBody AcquiringTransactionAdjustmentRequestDto acquiringTransactionAdjustmentRequestDto,
			BindingResult bindingResult,
			HttpServletRequest request) {
		Validations.validate(bindingResult);
		try {
			List<TransactionCurrentSearchResponseDto> responseDtos =
					acquiringTransactionService.applyAccountingAdjustment(acquiringTransactionAdjustmentRequestDto);

			return ResponseEntity.ok(responseDtos);

		} catch (BusinessException ex) {
			return ResponseEntity.status(ex.getHttpStatus()).body(ex.getMessage());
		} catch (DataIntegrityViolationException ex) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body("Data integrity violation occurred: " + ex.getMostSpecificCause().getMessage());
		} catch (ConstraintViolationException ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Validation error: " + ex.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An unexpected error occurred: " + ex.getMessage());
		}
	}
}
