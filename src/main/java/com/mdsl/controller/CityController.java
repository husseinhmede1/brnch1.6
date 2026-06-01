package com.mdsl.controller;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

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

import com.mdsl.model.dto.request.CityResquestDto;
import com.mdsl.model.dto.response.CityResponseDto;
import com.mdsl.service.CityService;
import com.mdsl.utils.Validations;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/city")
public class CityController {

	@Autowired
	private CityService cityService;
	
	@GetMapping("/country/{id}")
	public ResponseEntity<List<CityResponseDto>> getCityByCountryId(@PathVariable("id") int countryId){
		return ResponseEntity.ok(cityService.getCityByCountryId(countryId));

	}
	
	@GetMapping("/province/{id}")
	public ResponseEntity<List<CityResponseDto>> getCityByProvinceId(@PathVariable("id") int provinceId){
		return ResponseEntity.ok(cityService.getCityByProvinceId(provinceId));

	}
	
	@PostMapping
	public ResponseEntity<CityResponseDto> saveOrUpdateCity(@RequestBody CityResquestDto cityResquestDto,BindingResult bindingResult){
		Validations.validate(bindingResult);
		return ResponseEntity.ok(cityService.saveOrUpdateCity(cityResquestDto));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteCity(@PathVariable(value = "id") int cityId) {
		
		try {
			cityService.deleteProvince(cityId);
			String message = "An item is deleted with id : " + cityId;
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

	@GetMapping
	public ResponseEntity<List<CityResponseDto>> getAllCities(){
		return ResponseEntity.ok(cityService.getAllCities());
	}

	@GetMapping("/{id}")
	public ResponseEntity<CityResponseDto> getCityById(@PathVariable("id") int cityId){
		return ResponseEntity.ok(cityService.getCityById(cityId));
	}
}
