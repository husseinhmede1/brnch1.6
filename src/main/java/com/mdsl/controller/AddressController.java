package com.mdsl.controller;

import java.util.List;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import com.mdsl.model.dto.request.AddressRequestDto;
import com.mdsl.utils.Validations;
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
import com.mdsl.model.dto.response.AddressResponseDto;
import com.mdsl.model.dto.response.CityResponseDto;
import com.mdsl.model.dto.response.CountryResponseDto;
import com.mdsl.service.AddressService;
import com.mdsl.service.CityService;
import com.mdsl.service.CountryService;
import com.mdsl.utils.ResponseCode;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("Address Controller")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/address")
public class AddressController {
	private static final Logger logger = LoggerFactory.getLogger(AddressController.class);

	@Autowired
	private AddressService addressService;

	@Autowired
	private CityService cityService;

	@Autowired
	private CountryService countryService;

	@ApiOperation(value = "Get all Addresses",response = AddressResponseDto.class)
	@GetMapping
	public ResponseEntity<List<AddressResponseDto>> getAllAddress() {
		return ResponseEntity.ok(addressService.getAllAddress());
	}

	@ApiOperation(value = "Get all Countries",response = CountryResponseDto.class)
	@GetMapping("/countries")
	public ResponseEntity<List<CountryResponseDto>> getAllCountries() {
		return ResponseEntity.ok(countryService.getAllCountries());
	}

	@ApiOperation(value = "Get City by Country Id",response = CityResponseDto.class)
	@GetMapping("/country/{id}")
	public ResponseEntity<List<CityResponseDto>> getCityByCountryId(@PathVariable("id") int countryId) {
		return ResponseEntity.ok(cityService.getCityByCountryId(countryId));

	}

	@ApiOperation(value = "Get Address by Entity Id",response = AddressResponseDto.class)
	@GetMapping("/entities/{entityId}")
	public ResponseEntity<List<AddressResponseDto>> getAddressByEntityId(@PathVariable("entityId") String entityId) {
		return ResponseEntity.ok(addressService.getAddressByEntitiesId(entityId));
	}

	@ApiOperation(value = "Save Address",response = AddressResponseDto.class)
	@PostMapping
	public ResponseEntity<AddressResponseDto> saveOrUpdateAddress(@Valid @RequestBody AddressRequestDto addressRequestDto, BindingResult bindingResult) {
		Validations.validate(bindingResult);
		try {
		return ResponseEntity.ok(addressService.saveOrUpdateAddress(addressRequestDto));
	   } catch (BusinessException e) {
			  throw new BusinessException (e.getMessage(), e.getHttpStatus());
		   } catch (Exception e) {
			logger.error("@AddressController#saveOrUpdateAddress: " + e.getMessage());
			throw new BusinessException (ResponseCode.CFG_ADDRESS_NO_SAVE, HttpStatus.BAD_REQUEST);
		   }
	}

	@ApiOperation(value = "Delete Address",response = String.class)
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteAddress(@PathVariable(value = "id") int id) {
		try {
			addressService.deleteAddress(id);
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
}
