package com.mdsl.controller;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//import com.mdsl.model.dto.response.RespCodesResponseDto;
//import com.mdsl.service.RespCodesService;

@CrossOrigin(origins = "*")
@RestController
@Transactional(rollbackOn=Exception.class)
@RequestMapping("/v1/lookup/respcodes")
public class RespCodesController {
	
//	private final RespCodesService respCodesService;
//	
//	@Autowired
//	public RespCodesController(RespCodesService respCodesService) {
//		this.respCodesService = respCodesService;
//	}
//	
//	@GetMapping
//	public ResponseEntity<List<RespCodesResponseDto>> getAllRespCodes() {
//		return ResponseEntity.ok(respCodesService.getAllRespCodes());
//	}
//	
//	@GetMapping("/{id}")
//	public ResponseEntity<RespCodesResponseDto> getRespCodeById(@PathVariable("id") int id) {
//		return ResponseEntity.ok(respCodesService.getRespCodeById(id));
//	}
//	
//	@GetMapping("/active")
//	public ResponseEntity<List<RespCodesResponseDto>> getActiveRespCodes() {
//		return ResponseEntity.ok(respCodesService.getActiveRespCodes());
//	}
}