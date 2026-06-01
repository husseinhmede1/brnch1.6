package com.mdsl.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

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

import com.mdsl.model.dto.request.CardSchemeRequestDto;
import com.mdsl.model.dto.request.ChangeStatusRequestDto;
import com.mdsl.model.dto.response.CardSchemeResponseDto;
import com.mdsl.service.CardSchemeService;
import com.mdsl.utils.Validations;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("Card Scheme Controller")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/cardscheme")
public class CardSchemeController {
	
	
	@Autowired
	private CardSchemeService cardSchemeService;
	
	@ApiOperation(value = "Get all Card Schemes",response = CardSchemeResponseDto.class)
	@GetMapping
	public ResponseEntity<List<CardSchemeResponseDto>>  getAllCardScheme(){
		return ResponseEntity.ok(cardSchemeService.fetchAllCardScheme());
	}
	
	
	@ApiOperation(value = "Get Card Scheme by Id",response = CardSchemeResponseDto.class)
	@GetMapping("/{id}")
	public ResponseEntity<CardSchemeResponseDto> getCardSchemeById(@PathVariable("id") String id){
		return ResponseEntity.ok(cardSchemeService.fetchCardSchemeById(id));
	}
	
	@ApiOperation(value = "Save Card Scheme",response = CardSchemeResponseDto.class)
	@PostMapping
	public ResponseEntity<CardSchemeResponseDto> saveOrUpdateCardScheme(@Valid @RequestBody CardSchemeRequestDto cardSchemeRequestDto,BindingResult bindingResult){
		Validations.validate(bindingResult);
		return ResponseEntity.ok(cardSchemeService.saveOrUpdateCardScheme(cardSchemeRequestDto));
	}
	
	@ApiOperation(value = "Delete Card Scheme",response = String.class)
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteCardScheme(@PathVariable("id") String id){
		try {
			cardSchemeService.deleteCardSchemeById(id);
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
	
	@ApiOperation(value = "Change Card Scheme Status")
	@PostMapping("/status-change")
	public ResponseEntity<String> changeCardSchemeStatus(@Valid @RequestBody ChangeStatusRequestDto changeCardSchemeStatusRequestDTO, BindingResult bindingResult, HttpServletRequest request){
		return ResponseEntity.ok(cardSchemeService.changeStatus(changeCardSchemeStatusRequestDTO));
	}
	
	@ApiOperation(value = "Get All Active Card Schemes",response = CardSchemeResponseDto.class)
	@GetMapping("/active-cardscheme")
	public ResponseEntity<List<CardSchemeResponseDto>> getActiveCardScheme() 
	{
		return ResponseEntity.ok(cardSchemeService.getActiveCardScheme());
	} 

}
