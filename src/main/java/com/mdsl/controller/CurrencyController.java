package com.mdsl.controller;

import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validation;

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
import com.mdsl.model.dto.request.ChangeStatusRequestDto;
import com.mdsl.model.dto.request.CurrencyRequestDto;
import com.mdsl.model.dto.response.CurrencyResponseDto;
import com.mdsl.service.CurrencyService;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.Validations;

@CrossOrigin(origins = "*")
@RestController
//@Transactional(rollbackOn = Exception.class)
@RequestMapping("/currency")
public class CurrencyController {

	@Autowired
	private CurrencyService currencyService;
	private static final Logger logger = LoggerFactory.getLogger(CurrencyController.class);

	public CurrencyController(CurrencyService currencyService) {
		this.currencyService = currencyService;
	}

	@GetMapping
	public ResponseEntity<List<CurrencyResponseDto>> getAllCurrencies() {
		List<CurrencyResponseDto> currencies = currencyService.getAllCurrencies();
		return ResponseEntity.ok(currencies);
	}

	@GetMapping("/active-currencies")
	public ResponseEntity<List<CurrencyResponseDto>> getActiveCurrencies() {
		List<CurrencyResponseDto> currencies = currencyService.getActiveCurrencies();
		return ResponseEntity.ok(currencies);
	}

	@GetMapping("/{id}")
	public ResponseEntity<CurrencyResponseDto> getCurrencyById(@PathVariable("id") int id) {
		CurrencyResponseDto currency = currencyService.getCurrencyById(id);
		return ResponseEntity.ok(currency);
	}

	@PostMapping
	public ResponseEntity saveOrUpdateCurrency(@Valid @RequestBody CurrencyRequestDto currency,
			BindingResult bindingResult) {
		Validations.validate(bindingResult);
		try {
			CurrencyResponseDto currencyResponseDto = currencyService.saveOrUpdateCurrency(currency);
			return ResponseEntity.ok(currencyResponseDto);
		} catch (BusinessException e) {
		    throw new BusinessException (e.getMessage(), e.getHttpStatus());
		} catch (Exception e) {
		    logger.error("@CurrencyController#saveOrUpdateCurrency: " + e.getMessage());
		    throw new BusinessException (ResponseCode.CUR_CURRENCY_NO_SAVE, HttpStatus.BAD_REQUEST);
	    }
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteCurrency(@PathVariable("id") int id) {
		try {
			currencyService.deleteCurrency(id);
			String message = "An item is deleted with id : " + id;
			return ResponseEntity.ok(message);
		} catch (BusinessException e) {
		    throw new BusinessException (e.getMessage(), e.getHttpStatus());
		} catch (Exception e) {
		    logger.error("@CurrencyController#deleteCurrency: " + e.getMessage());
		    throw new BusinessException (ResponseCode.CUR_CURRENCY_NO_DELETE, HttpStatus.BAD_REQUEST);
	    }
	}

	@PostMapping("/status-change")
	public ResponseEntity<String> changeCurrencyStatus(@Valid @RequestBody ChangeStatusRequestDto changeStatusRequestDto,BindingResult bindingResult) {
		Validations.validate(bindingResult);
		return ResponseEntity.ok(currencyService.changeStatus(changeStatusRequestDto));
	}
}
