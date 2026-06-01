package com.mdsl.controller;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.utils.ResponseCode;
import lombok.RequiredArgsConstructor;
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

import com.mdsl.model.dto.request.ProvinceResquestDto;
import com.mdsl.model.dto.response.ProvinceResponseDto;
import com.mdsl.service.ProvinceService;
import com.mdsl.utils.Validations;

//@Transactional
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/province")
@RequiredArgsConstructor
public class ProvinceController {

	private static final Logger logger = LoggerFactory.getLogger(ProvinceController.class);
	private final ProvinceService provinceService;
	
	@GetMapping("/country/{id}")
	public ResponseEntity<List<ProvinceResponseDto>> getProvinceByCountry(@PathVariable("id") int countryId) {
		return ResponseEntity.ok(provinceService.getProvinceByCountry(countryId));
	}
	@GetMapping("/province")
	public ResponseEntity<List<ProvinceResponseDto>> getAllProvince() {
		return ResponseEntity.ok(provinceService.getAllProvince());
	}
	@PostMapping
	public ResponseEntity<ProvinceResponseDto> saveOrUpdateProvince(@Valid @RequestBody ProvinceResquestDto ProvinceResquestDto, BindingResult bindingResult){
		Validations.validate(bindingResult);
		try {
			return ResponseEntity.ok(provinceService.saveOrUpdateProvince(ProvinceResquestDto));
		} catch(BusinessException ex){
		logger.error("@ProvinceController#saveOrUpdateProvince-business exception "+ex.toString());
		throw new BusinessException(ex.getMessage(),ex.getHttpStatus());
		} catch(Exception ex){
		logger.error("@ProvinceController#saveOrUpdateProvince-generic exception "+ex.toString());
		throw new BusinessException(ResponseCode.PRV_CANNOT_SAVE_UPDATE_PROVINCE,HttpStatus.INTERNAL_SERVER_ERROR);
	}
		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteProvince(@PathVariable(value = "id") int provinceId) {
		try {
			provinceService.deleteProvince(provinceId);
			String message = "An item is deleted with id : " + provinceId;
			return ResponseEntity.ok(message);
		} catch(BusinessException ex){
			logger.error("@ProvinceController#saveOrUpdateProvince-business exception "+ex.toString());
			throw new BusinessException(ex.getMessage(),ex.getHttpStatus());
		} catch(Exception ex){
			logger.error("@ProvinceController#saveOrUpdateProvince-generic exception "+ex.toString());
			throw new BusinessException(ResponseCode.PRV_CANNOT_SAVE_UPDATE_PROVINCE,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
