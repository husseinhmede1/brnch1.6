package com.mdsl.controller;

import java.util.List;

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
import com.mdsl.model.dto.request.CurrencyConversionRequestDto;
import com.mdsl.model.dto.response.CurrencyConversionResponseDto;
import com.mdsl.service.CurrencyConversionService;
import com.mdsl.utils.ResponseCode;

@CrossOrigin(origins = "*")
@RestController
//@Transactional(rollbackOn = Exception.class)
@RequestMapping("/currencyconversion")
public class CurrencyConversionController {
	private static final Logger logger = LoggerFactory.getLogger(CurrencyConversionController.class);

	@Autowired
	private CurrencyConversionService currencyConversionService;

	@GetMapping
	public ResponseEntity<List<CurrencyConversionResponseDto>> getAllCurrencyConversions() {
		return ResponseEntity.ok(currencyConversionService.getAllCurrencyConversions());
	}

	@GetMapping("/{id}")
	public ResponseEntity<CurrencyConversionResponseDto> getCurrencyConversionById(@PathVariable("id") int id) {
		CurrencyConversionResponseDto currency = currencyConversionService.getCurrencyConversionById(id);
		return ResponseEntity.ok(currency);
	}
	
	@GetMapping("/institution/{id}")
	public ResponseEntity<List<CurrencyConversionResponseDto>> getCurrencyConversionByInstitutionId(@PathVariable("id") String id) {
		List<CurrencyConversionResponseDto> currency = currencyConversionService.getCurrencyConversionByInstitutionId(id);
		return ResponseEntity.ok(currency);
	}

	@PostMapping
	public ResponseEntity saveOrUpdateCurrencyConversion(
			@Valid @RequestBody CurrencyConversionRequestDto currency, BindingResult bindingResult) {
		Validations.validate(bindingResult);
		try {
		return ResponseEntity.ok(currencyConversionService.saveOrUpdateCurrencyConversion(currency));
		} catch (BusinessException e) {
		    throw new BusinessException (e.getMessage(), e.getHttpStatus());
		} catch (Exception e) {
		    logger.error("@CurrencyConversionController#saveOrUpdateCurrencyConversion: " + e.getMessage());
		    throw new BusinessException (ResponseCode.CUR_CURRENCY_CONVERSION_NO_SAVE, HttpStatus.BAD_REQUEST);
	    }
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteCurrencyConversion(@PathVariable("id") int id) {
		
		try {
			currencyConversionService.deleteCurrencyConversion(id);
			String message = "An item is deleted with id : " + id;
			return ResponseEntity.ok(message);
		} catch (BusinessException e) {
		    throw new BusinessException (e.getMessage(), e.getHttpStatus());
		} catch (Exception e) {
			logger.error("@CurrencyConversionController#deleteCurrencyConversion: " + e.getMessage());
			throw new BusinessException(ResponseCode.CUR_CURRENCY_CONVERSION_NO_DELETE, HttpStatus.BAD_REQUEST);
		}
	}

}
