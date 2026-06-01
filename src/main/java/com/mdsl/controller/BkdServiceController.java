package com.mdsl.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;
import javax.validation.Valid;

import com.mdsl.model.dto.response.FileTypesResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mdsl.model.dto.request.BkdServiceRequestDto;
import com.mdsl.model.dto.response.BkdServiceResponseDto;
import com.mdsl.service.BkdServiceService;
import com.mdsl.utils.Validations;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("Bkd Service Controller")
@RestController
@Transactional
@RequestMapping("/config/bkd-service")
public class BkdServiceController {
	private final BkdServiceService bkdServiceService;
	
	@Autowired
	public BkdServiceController(BkdServiceService bkdServiceService) {
		this.bkdServiceService = bkdServiceService;
	}
	
	@GetMapping
	@ApiOperation(value = "Get all Bkd Services", response = BkdServiceResponseDto.class)
	public ResponseEntity<List<BkdServiceResponseDto>> getAllBkdServices() {
		return ResponseEntity.ok(bkdServiceService.getAllBkdServices());
	}
	
	@PostMapping("/batch-size")
	@ApiOperation(value = "Update Bkd Services' batch size")
	public ResponseEntity<BkdServiceResponseDto> updateBkdServiceBatchSize (@Valid @RequestBody BkdServiceRequestDto bkdServiceRequestDto, BindingResult bindingResult, HttpServletRequest request) {
		Validations.validate(bindingResult); 
		return ResponseEntity.ok(bkdServiceService.updateBkdServiceBatchSize(bkdServiceRequestDto, Integer.parseInt(request.getHeader("instId")), request.getRemoteAddr()));
	}
	
	@GetMapping("/parameter/{id}")
	@ApiOperation(value = "Get Parameter Name by Parameter Service ID")
	public ResponseEntity<String> findParameterName(@PathVariable("id") int parameterServiceId, HttpServletRequest request) {
		return ResponseEntity.ok(bkdServiceService.findParameterName(parameterServiceId));
	}

	@GetMapping("/file-type")
	public ResponseEntity<FileTypesResponseDto> getFileTypes(){
		return ResponseEntity.ok(bkdServiceService.getFileTypes());
	}
}