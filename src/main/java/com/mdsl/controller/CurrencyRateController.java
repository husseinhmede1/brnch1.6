package com.mdsl.controller;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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
import com.mdsl.exceptionHandling.RestResponseEntityExceptionHandler;
import com.mdsl.model.dto.request.CurrencyRateRequestDto;
import com.mdsl.model.dto.response.CurrencyRateResponseDto;
import com.mdsl.model.dto.response.PaginationResponseDto;
import com.mdsl.service.ActivityPackageDetailService;
import com.mdsl.service.CurrencyRateService;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.Validations;

@CrossOrigin(origins = "*")
@RestController
//@Transactional(rollbackOn = Exception.class)
@RequestMapping("/currencyrate")
public class CurrencyRateController {
	private static final Logger logger = LoggerFactory.getLogger(CurrencyRateController.class);

	@Autowired
	private CurrencyRateService currencyRateService;
	
	@Autowired
	private ActivityPackageDetailService activityPackageDetailService;

	@GetMapping
	public ResponseEntity<List<CurrencyRateResponseDto>> getAllCurrencyRates() {
		return ResponseEntity.ok(currencyRateService.getAllCurrencyRates());
	}

	@GetMapping("/{id}")
	public ResponseEntity<CurrencyRateResponseDto> getCurrencyRateById(@PathVariable("id") int id) {
		CurrencyRateResponseDto currency = currencyRateService.getCurrencyRateById(id);
		return ResponseEntity.ok(currency);
	}

	@GetMapping("/institution/{id}")
	public ResponseEntity<List<CurrencyRateResponseDto>> getCurrencyRateByInstitutionId(@PathVariable("id") String id) {
		List<CurrencyRateResponseDto> currency = currencyRateService.getCurrencyRateByInstitutionId(id);
		return ResponseEntity.ok(currency);
	}

	@PostMapping
	public ResponseEntity saveOrUpdateCurrencyRate(@Valid @RequestBody CurrencyRateRequestDto currency,
			BindingResult bindingResult) {
		Validations.validate(bindingResult);
		try {
			return ResponseEntity.ok(currencyRateService.saveOrUpdateCurrencyRate(currency));
		} catch (BusinessException e) {
		    throw new BusinessException (e.getMessage(), e.getHttpStatus());
		} catch (Exception e) {
		    logger.error("@CurrencyRateController#saveOrUpdateCurrencyRate: " + e.getMessage());
		    throw new BusinessException (ResponseCode.CUR_RATE_NO_SAVE, HttpStatus.BAD_REQUEST);
	    }
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteCurrencyRate(@PathVariable("id") int id) {
		try {
			currencyRateService.deleteCurrencyRate(id);
			String message = "An item is deleted with id : " + id;
			return ResponseEntity.ok(message);
		} catch (BusinessException e) {
		    throw new BusinessException (e.getMessage(), e.getHttpStatus());
		} catch (Exception e) {
		    logger.error("@CurrencyRateController#deleteCurrencyRate: " + e.getMessage());
		    throw new BusinessException (ResponseCode.CUR_RATE_NO_DELETE, HttpStatus.BAD_REQUEST);
	    }
	}

	@PostMapping("/search/{instId}")
	public ResponseEntity<PaginationResponseDto> getCurrencyRatesBySearch(@PathVariable("instId") String instId,
			@RequestBody CurrencyRateRequestDto currencyRateRequestDto,BindingResult bindingResult) {
		Validations.validate(bindingResult);
		ResponseEntity<PaginationResponseDto> responseEntity = null;
		try {
			responseEntity = currencyRateService.getCurrencyRatesBySearch(currencyRateRequestDto, instId);
		} catch (Exception e) {
			e.printStackTrace();
			responseEntity = new ResponseEntity<>(new PaginationResponseDto(false, null, null, null, null),
					HttpStatus.OK);
		}
		return responseEntity;
	}

}
