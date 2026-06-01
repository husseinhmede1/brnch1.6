package com.mdsl.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validation;

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

import com.mdsl.model.dto.request.NonActivityFeeQueryRequestDto;
import com.mdsl.model.dto.request.NonActivityFeeQueryRequestDto;
import com.mdsl.model.dto.request.NonActivityFeeQueryRequestDto;
import com.mdsl.model.dto.response.CurrencyConversionResponseDto;
import com.mdsl.model.dto.response.NonActivityFeeQueryResponseDto;
import com.mdsl.model.dto.response.PaginationResponseDto;
import com.mdsl.model.dto.response.NonActivityFeeQueryResponseDto;
import com.mdsl.service.NonActivityFeeQueryService;
import com.mdsl.utils.Validations;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/nonactivityfeequery")
public class NonActivityFeeQueryController {

	@Autowired
	private NonActivityFeeQueryService nonActivityFeeQueryService;

	public NonActivityFeeQueryController(NonActivityFeeQueryService nonActivityFeeQueryService) {
		this.nonActivityFeeQueryService = nonActivityFeeQueryService;
	}

	@PostMapping("/getAllNonActivityFeeQueries")
	public ResponseEntity<PaginationResponseDto> nonActivityFeeQueryPagination(
		@Valid @RequestBody NonActivityFeeQueryRequestDto nonActivityFeeQueryRequestDto,BindingResult bindingResult) {
		Validations.validate(bindingResult);
		ResponseEntity<PaginationResponseDto> responseEntity = null;
		try {
			responseEntity = nonActivityFeeQueryService.getAllTransactions(nonActivityFeeQueryRequestDto);

		} catch (Exception e) {
			e.printStackTrace();
			responseEntity = new ResponseEntity<>(new PaginationResponseDto(false, null, null, null, null),
					HttpStatus.OK);
		}
		return responseEntity;
	}

	@GetMapping("/{id}")
	public ResponseEntity<NonActivityFeeQueryResponseDto> getNonActivityFeeQueryById(@PathVariable("id") int id) {
		NonActivityFeeQueryResponseDto transaction = nonActivityFeeQueryService.getNonActivityFeeQueryById(id);
		return ResponseEntity.ok(transaction);
	}

	@PostMapping
	public ResponseEntity saveOrUpdateNonActivityFeeQuery(
			@Valid @RequestBody NonActivityFeeQueryRequestDto transactionDto, HttpServletRequest httpServletRequest,BindingResult bindingResult) {
		Validations.validate(bindingResult);
		String instId="";
		if(transactionDto.getInstitutionId()==null || transactionDto.getInstitutionId().equals("")) {
			instId = httpServletRequest.getHeader("instId");
			if (instId != null) {
				transactionDto.setInstitutionId(instId);
			} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Institution Id not found");
			}
		}
		 
		return ResponseEntity.ok(nonActivityFeeQueryService.saveOrUpdateNonActivityFeeQuery(transactionDto));
		
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteNonActivityFeeQuery(@PathVariable("id") int id) {
		try {
			nonActivityFeeQueryService.deleteNonActivityFeeQuery(id);
			String message = "An item is deleted with id : " + id;
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

	@PostMapping("/institution")
	public ResponseEntity<PaginationResponseDto> getNonActivityFeeQueryByInstitutionIdPagination(
			HttpServletRequest httpServletRequest,
		@Valid @RequestBody NonActivityFeeQueryRequestDto nonActivityFeeQueryRequestDto,BindingResult bindingResult) {

		Validations.validate(bindingResult);
		ResponseEntity<PaginationResponseDto> responseEntity = null;
		try {
			if (nonActivityFeeQueryRequestDto.getInstitutionId() == null) {
				String instId = httpServletRequest.getHeader("instId");
				if (instId != null) {
					nonActivityFeeQueryRequestDto.setInstitutionId(instId);
				}
			}
			responseEntity = nonActivityFeeQueryService
					.getNonActivityFeeQueryByInstitutionId(nonActivityFeeQueryRequestDto);

		} catch (Exception e) {
			e.printStackTrace();
			responseEntity = new ResponseEntity<>(new PaginationResponseDto(false, null, null, null, null),
					HttpStatus.OK);
		}
		return responseEntity;
	}

	@PostMapping("/search")
	public ResponseEntity<PaginationResponseDto> getQueriesBySearch(HttpServletRequest httpServletRequest,
			@Valid @RequestBody NonActivityFeeQueryRequestDto nonActivityFeeQueryRequestDto,BindingResult bindingResult) {
		Validations.validate(bindingResult);
		ResponseEntity<PaginationResponseDto> responseEntity = null;
		try {
			if (nonActivityFeeQueryRequestDto.getInstitutionId() == null) {
				String instId = httpServletRequest.getHeader("instId");
				if (instId != null) {
					nonActivityFeeQueryRequestDto.setInstitutionId(instId);
				}
			}
			responseEntity = nonActivityFeeQueryService.getQueriesBySearch(nonActivityFeeQueryRequestDto);

		} catch (Exception e) {
			e.printStackTrace();
			responseEntity = new ResponseEntity<>(new PaginationResponseDto(false, null, null, null, null),
					HttpStatus.OK);
		}
		return responseEntity;

	}
}
