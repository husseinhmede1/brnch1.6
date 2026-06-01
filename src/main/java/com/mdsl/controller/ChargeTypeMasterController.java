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
import com.mdsl.model.dto.request.ChargeTypeMasterRequestDto;
import com.mdsl.model.dto.response.ChargeTypeMasterResponseDto;
import com.mdsl.service.ChargeTypeMasterService;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.Validations;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/chargetypemaster")
public class ChargeTypeMasterController {
	private static final Logger logger = LoggerFactory.getLogger(ChargeTypeMasterController.class);

	@Autowired
	private ChargeTypeMasterService chargeTypeMasterService;
	
	@GetMapping
	public ResponseEntity<List<ChargeTypeMasterResponseDto>> getAllChargeTypeMaster()
	{
		return ResponseEntity.ok(chargeTypeMasterService.fetchAllChargeTypeMaster());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ChargeTypeMasterResponseDto> getChargeTypeMasterById(@PathVariable("id") int id)
	{
		return ResponseEntity.ok(chargeTypeMasterService.fetchChargeTypeMasterById(id));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteChargeTypeMaster(@PathVariable("id") int id)
	{
		
		chargeTypeMasterService.deleteChargeTypeMasterById(id);
		String message="An item is deleted with id : "+id;
		return ResponseEntity.ok(message);
	}
	
	@PostMapping
	public ResponseEntity<ChargeTypeMasterResponseDto> saveOrUpdateChargeTypeMaster(@Valid @RequestBody 
										ChargeTypeMasterRequestDto chargeTypeMasterRequestDto, BindingResult bindingResult)
	{
		Validations.validate(bindingResult);
		try {
			return ResponseEntity.ok(chargeTypeMasterService.saveOrUpdateChargeTypeMaster(chargeTypeMasterRequestDto));
		} catch (BusinessException e) {
			throw new BusinessException (e.getMessage(), e.getHttpStatus());
		} catch (Exception e) {
			logger.error("@ChargeTypeMasterController#saveOrUpdateChargeTypeMaster: " + e.getMessage());
			throw new BusinessException (ResponseCode.CFG_CHARGE_TYPE_MASTER_NO_SAVE, HttpStatus.BAD_REQUEST);
		}
	}

}
