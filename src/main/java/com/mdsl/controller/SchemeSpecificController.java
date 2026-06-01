package com.mdsl.controller;

import java.util.List;

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

import com.mdsl.model.dto.request.SchemeSpecificRequestDto;
import com.mdsl.model.dto.response.SchemeSpecificResponseDto;
import com.mdsl.service.SchemeSpecificService;
import com.mdsl.utils.Validations;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/schemespecific")
@RequiredArgsConstructor
public class SchemeSpecificController {

	private static final Logger logger = LoggerFactory.getLogger(SchemeSpecificController.class);
	private final SchemeSpecificService schemeSpecificService;
	
	@GetMapping
	public ResponseEntity<List<SchemeSpecificResponseDto>> getAllSchemeSpecific()
	{
		return ResponseEntity.ok(schemeSpecificService.fetchAllSchemeSpecific());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<SchemeSpecificResponseDto> getSchemeSpecificById(@PathVariable("id") int id)
	{
		return ResponseEntity.ok(schemeSpecificService.fetchSchemeSpecificById(id));
	}
	
	@PostMapping
	public ResponseEntity<SchemeSpecificResponseDto> saveOrUpdateSchemeSpecific(@Valid @RequestBody 
			SchemeSpecificRequestDto schemeSpecificRequestDto, BindingResult bindingResult)
	{
		Validations.validate(bindingResult);
		return ResponseEntity.ok(schemeSpecificService.saveOrUpdateSchemeSpecific(schemeSpecificRequestDto));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteSchemeSpecific(@PathVariable("id") int id) {
		try {
			schemeSpecificService.deleteSchemeSpecificById(id);
			String message = "An item is deleted with id : " + id;
			return ResponseEntity.ok(message);
		}  catch(BusinessException ex){
				logger.error("@SchemeSpecificController#deleteSchemeSpecific-business exception "+ex.toString());
				throw new BusinessException(ex.getMessage(),ex.getHttpStatus());
			} catch(Exception ex){
			logger.error("@SchemeSpecificController#deleteSchemeSpecific-generic exception "+ex.toString());
			throw new BusinessException(ResponseCode.VAL_ERROR_OCCURRED,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}