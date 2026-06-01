package com.mdsl.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.ChangeStatusRequestDto;
import com.mdsl.model.dto.request.LayoutRequestDto;
import com.mdsl.model.dto.request.PaginationRequestDto;
import com.mdsl.model.dto.response.LayoutResponseDto;
import com.mdsl.model.dto.response.PageableLayoutResponseDto;
import com.mdsl.service.LayoutService;
import io.swagger.annotations.Api;
import org.springframework.transaction.annotation.Transactional;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.Validations;

import io.swagger.annotations.ApiOperation;

@Api("Layout Controller")
@CrossOrigin(origins = "*")
@RestController
@Transactional
@RequestMapping("/v1/input-output/layouts")
public class LayoutController {
	private final LayoutService layoutService;
	private static final Logger logger = LoggerFactory.getLogger(LayoutController.class);
	
	@Autowired
	public LayoutController(LayoutService layoutService) {
		super();
		this.layoutService = layoutService;
	}
	
	@PostMapping("/all")
	@ApiOperation(value = "Get all Layouts",response = PageableLayoutResponseDto.class)
	public ResponseEntity<PageableLayoutResponseDto> getAllLayouts(@Valid @RequestBody PaginationRequestDto paginationRequestDto, HttpServletRequest request){
		return ResponseEntity.ok(layoutService.getAllLayouts(paginationRequestDto, Integer.parseInt(request.getHeader("instId"))));
	}
	
	@GetMapping("/active")
	@ApiOperation(value = "Get All active Layouts",response = LayoutResponseDto.class)
	public ResponseEntity<List<LayoutResponseDto>> getActiveLayouts(HttpServletRequest request){
		return ResponseEntity.ok(layoutService.getActiveLayouts(Integer.parseInt(request.getHeader("instId"))));
	}
	
	@GetMapping("/{layoutId}")
	@ApiOperation(value = "Get Layout by ID")
	public ResponseEntity<LayoutResponseDto> getLayoutById(@PathVariable("layoutId") int layoutId,HttpServletRequest request) {
		return ResponseEntity.ok(layoutService.getLayoutById(layoutId, Integer.parseInt(request.getHeader("instId"))));
	}
	
	@GetMapping("/file/{fileId}")
	@ApiOperation(value = "Get Layouts by File ID", response = LayoutResponseDto.class)
	public ResponseEntity<List<LayoutResponseDto>> getLayoutsByFileId(@PathVariable("fileId") int fileId,HttpServletRequest request) {
		return ResponseEntity.ok(layoutService.getLayoutsByFileId(fileId, Integer.parseInt(request.getHeader("instId"))));
	}
	
	@GetMapping("/file-type/{fileType}")
	@ApiOperation(value = "Get Layouts by File ID", response = LayoutResponseDto.class)
	public ResponseEntity<List<LayoutResponseDto>> getLayoutsByFileType(@PathVariable("fileType") String fileType, HttpServletRequest request) {
		return ResponseEntity.ok(layoutService.getLayoutsByFileType(fileType, Integer.parseInt(request.getHeader("instId"))));
	}
	
	@PostMapping
	@ApiOperation(value = "Save Layout")
	public LayoutResponseDto saveLayout (@Valid @RequestBody LayoutRequestDto layoutRequestDto, BindingResult bindingResult, HttpServletRequest request) {
		Validations.validate(bindingResult); 
		return layoutService.saveLayout(layoutRequestDto, request.getRemoteAddr(), Integer.parseInt(request.getHeader("instId")));
	}
	
	@PostMapping("/status-change")
	@ApiOperation(value = "Change Layout Status")
	public void changeLayoutStatus(@Valid @RequestBody ChangeStatusRequestDto changeLayoutStatusRequestDTO, BindingResult bindingResult, HttpServletRequest request) {
		Validations.validate(bindingResult);
		try {
			layoutService.changeLayoutStatus(changeLayoutStatusRequestDTO, request.getRemoteAddr(), Integer.parseInt(request.getHeader("instId")));
		} catch (BusinessException e) {
			throw new BusinessException (e.getMessage(), e.getHttpStatus());
		} catch (Exception e) {
			logger.error("@LayoutController#changeLayoutStatus: " + e.getMessage());
			throw new BusinessException (ResponseCode.AN_ERROR_OCCURRED, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/{layoutId}")
	@ApiOperation(value = "Delete Layout")
	public void deleteLayout(@PathVariable(value="layoutId") int layoutId, HttpServletRequest request) {
		try {
			layoutService.deleteLayout(layoutId, request.getRemoteAddr(), Integer.parseInt(request.getHeader("instId")));
		} catch (BusinessException e) {
			throw new BusinessException (e.getMessage(), e.getHttpStatus());
		} catch (Exception e) {
			logger.error("@LayoutController#deleteLayout: " + e.getMessage());
			throw new BusinessException (ResponseCode.CFG_LAYOUT_NO_DELETE, HttpStatus.BAD_REQUEST);
		}
 	}
}