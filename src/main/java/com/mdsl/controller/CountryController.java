package com.mdsl.controller;

import java.util.List;

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
import com.mdsl.model.dto.request.ChangeStatusRequestDto;
import com.mdsl.model.dto.request.CountryRequestDto;
import com.mdsl.model.dto.response.CountryResponseDto;
import com.mdsl.service.CountryService;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.Validations;
//@Transactional
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/country")
public class CountryController {
	private static final Logger logger = LoggerFactory.getLogger(CountryController.class);

	@Autowired
	private CountryService countryService;
	
	@GetMapping
	public ResponseEntity<List<CountryResponseDto>> getAllCountries() {
		return ResponseEntity.ok(countryService.getAllCountries());
	}
	
	@PostMapping
	public ResponseEntity<CountryResponseDto> saveOrUpdateCountry(@Valid @RequestBody CountryRequestDto countryDto,
			BindingResult bindingResult) {
		Validations.validate(bindingResult);
		try {
			return ResponseEntity.ok(countryService.addOrUpdateCountry(countryDto));
		} catch (BusinessException e) {
		    throw new BusinessException (e.getMessage(), e.getHttpStatus());
		} catch (Exception e) {
		    logger.error("@CountryController#saveOrUpdateCountry: " + e.getMessage());
		    throw new BusinessException (ResponseCode.CNT_COUNTRY_NO_SAVE, HttpStatus.BAD_REQUEST);
	    }
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteCountry(@PathVariable(value="id") int id){
		
		try {
			countryService.deleteCountry(id);
			String message = "An item is deleted with id : " + id;
			return ResponseEntity.ok(message);
		} catch (BusinessException e) {
		    throw new BusinessException (e.getMessage(), e.getHttpStatus());
		} catch (Exception e) {
		    logger.error("@CountryController#deleteCountry: " + e.getMessage());
		    throw new BusinessException (ResponseCode.CNT_COUNTRY_NO_DELETE, HttpStatus.BAD_REQUEST);
	    }
//		return null;
	}
	
	@PostMapping("/status-change")
	public ResponseEntity<String> changeCountryStatus(@RequestBody ChangeStatusRequestDto changeStatusRequestDto) {
		int id = changeStatusRequestDto.getId();
		return ResponseEntity.ok(countryService.updateStatus(id));
	}
	
	@GetMapping("/active-country")
	public ResponseEntity<List<CountryResponseDto>> getActiveCountries() {
		return ResponseEntity.ok(countryService.getActiveCountries());
	} 
	
}
