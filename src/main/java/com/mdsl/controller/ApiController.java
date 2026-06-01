package com.mdsl.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.mdsl.model.dto.response.PageableApiResponseDto;
import lombok.RequiredArgsConstructor;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.mdsl.model.dto.request.ApiListRequestDto;
import com.mdsl.model.dto.request.PaginationRequestDto;
import com.mdsl.model.dto.response.ObjectResponseDto;
import com.mdsl.service.APIService;
import com.mdsl.utils.Validations;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("API Controller")
@CrossOrigin(origins={"*"})
@RestController
@RequestMapping("/v1/api/list")
@RequiredArgsConstructor
public class ApiController {

	private final APIService apiService;
	

	@GetMapping("/objects")
	@ApiOperation(value = "Get all API Objects",response = ObjectResponseDto.class)
	public ResponseEntity<List<ObjectResponseDto>> getApiObjects (HttpServletRequest request) {
		return ResponseEntity.ok(apiService.getApiObjects(Integer.parseInt(request.getHeader("instId"))));
	}
	
	@PostMapping("/objects/{object}")
	@ApiOperation(value = "Get API List")
	public ResponseEntity<PageableApiResponseDto> getApi (@PathVariable(name="object") String object, HttpServletRequest request, @Valid @RequestBody PaginationRequestDto paginationRequestDto) {
		return ResponseEntity.ok(apiService.getApi(object, Integer.parseInt(request.getHeader("instId")), paginationRequestDto));
	}
	
	@PostMapping("/objects-list")
	@ApiOperation(value = "Get all API List")
	public ResponseEntity<PageableApiResponseDto> getAllApis (HttpServletRequest request, @Valid @RequestBody PaginationRequestDto paginationRequestDto) {
		return ResponseEntity.ok(apiService.getAllApis(Integer.parseInt(request.getHeader("instId")), paginationRequestDto));
	}
	
	@PostMapping
	@ApiOperation(value = "Update STP Flag")
	public void updateStpFlag(@Valid @RequestBody List<ApiListRequestDto> apiListRequestDto, BindingResult bindingResult, HttpServletRequest request) {
		Validations.validate(bindingResult);
		apiService.updateStpFlag(apiListRequestDto, Integer.parseInt(request.getHeader("instId"))); 
	}
}