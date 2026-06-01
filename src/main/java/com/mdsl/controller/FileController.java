package com.mdsl.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mdsl.model.dto.response.FileResponseDto;
import com.mdsl.model.dto.response.ListFileElementResponseDto;
import com.mdsl.service.FileService;
import com.mdsl.utils.ResponseCode;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api("File Controller")
@Transactional
@RequestMapping("/v1/config/input-output")
public class FileController {
	private final FileService fileService;
	
	@Autowired
	public FileController(FileService fileService) {
		super();
		this.fileService = fileService;
	}
	
	@GetMapping
	@ApiOperation(value = "Get All Files")
	public ResponseEntity<List<FileResponseDto>> getAllFiles(HttpServletRequest request){
		return ResponseEntity.ok(fileService.getAllFiles()); 
	}
	
	@GetMapping("/active")
	@ApiOperation(value = "Get all Active Files")
	public ResponseEntity<List<FileResponseDto>> getActiveFiles(HttpServletRequest request){
		return ResponseEntity.ok(fileService.getActiveFiles());
	}
	
	@GetMapping("/{fileId}")
	@ApiOperation(value = "Get All elements by File ID")
	public ResponseEntity<ListFileElementResponseDto> getAllElementsByFileId(@PathVariable("fileId") @Size(min = 1, max = 9999, message = ResponseCode.CFG_INVALID_FILE) int fileId, HttpServletRequest request) {
		return ResponseEntity.ok(fileService.getAllElementsByFileId(fileId));
	}
}