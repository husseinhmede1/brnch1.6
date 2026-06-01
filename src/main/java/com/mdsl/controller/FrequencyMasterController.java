package com.mdsl.controller;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.mdsl.model.dto.request.FrequencyMasterRequestDto;
import com.mdsl.model.dto.response.FrequencyMasterResponseDto;
import com.mdsl.service.FrequencyMasterService;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.Validations;

@CrossOrigin(origins = "*")
@RestController
@Transactional(rollbackOn=Exception.class)
@RequestMapping("/frequency")
public class FrequencyMasterController {
private final FrequencyMasterService frequencyMasterService;
	private static final Logger logger = LoggerFactory.getLogger(FrequencyMasterController.class);

	public FrequencyMasterController(FrequencyMasterService frequencyMasterService) 
	{
		this.frequencyMasterService = frequencyMasterService;
	}
	
	@GetMapping
	public ResponseEntity<List<FrequencyMasterResponseDto>> getAllFrequencies() {
		return ResponseEntity.ok(frequencyMasterService.getAllFrequencies());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<FrequencyMasterResponseDto> getFrequencyById(@PathVariable("id") int id) {
		FrequencyMasterResponseDto frequency = frequencyMasterService.getFrequencyById(id);
		return ResponseEntity.ok(frequency);		
	}
	
	@PostMapping
    public ResponseEntity<FrequencyMasterResponseDto> saveOrUpdateFrequency(@Valid @RequestBody FrequencyMasterRequestDto frequencyMasterRequestDto, 
    		BindingResult bindingResult)
    {
		Validations.validate(bindingResult);
	  try {
        return ResponseEntity.ok(frequencyMasterService.saveOrUpdateFrequency(frequencyMasterRequestDto));
      } catch (BusinessException e) {
		  throw new BusinessException (e.getMessage(), e.getHttpStatus());
	  } catch (Exception e) {
	      logger.error("@FrequencyMasterController#saveOrUpdateFrequency: " + e.getMessage());
	      throw new BusinessException (ResponseCode.FRQ_FREQUENCY_NO_SAVE, HttpStatus.BAD_REQUEST);
	  }
    }
	
	@DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFrequency(@PathVariable("id") int id)
    {
	  try {
		frequencyMasterService.deleteFrequency(id);
        return ResponseEntity.ok("Deleted Successfully!");
      } catch (BusinessException e) {
		  throw new BusinessException (e.getMessage(), e.getHttpStatus());
	  } catch (Exception e) {
	      logger.error("@FrequencyMasterController#deleteFrequency: " + e.getMessage());
	      throw new BusinessException (ResponseCode.FRQ_FREQUENCY_NO_DELETE, HttpStatus.BAD_REQUEST);
	  }
    }
}
