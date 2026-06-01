package com.mdsl.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mdsl.model.dto.response.SystemCodeHeaderResponseDto;
import com.mdsl.model.dto.response.SystemCodeResponseDto;
import com.mdsl.service.SystemCodeHeaderService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/system-header-code")
public class SystemCodeHeaderController {

	@Autowired
	private SystemCodeHeaderService systemCodeHeaderService;
	
	@GetMapping
	public ResponseEntity<List<SystemCodeHeaderResponseDto>> getAllSystemCodesHeader() {
		return ResponseEntity.ok(systemCodeHeaderService.fetchAllSystemCodesHeader());
	}
}
