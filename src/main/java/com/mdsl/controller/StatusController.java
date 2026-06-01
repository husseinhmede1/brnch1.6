package com.mdsl.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//import com.mdsl.model.dto.response.StatusResponseDto;
//import com.mdsl.service.StatusService;

@CrossOrigin(origins = "*")
@RestController
@Transactional(rollbackOn=Exception.class)
@RequestMapping("/v1/lookup/statuses")
public class StatusController {
	
//	private final StatusService statusService;
//	
//	@Autowired
//	public StatusController(StatusService statusService) {
//		this.statusService = statusService;
//	}
//	
//	@GetMapping
//	public ResponseEntity<List<StatusResponseDto>> getAllStatuses() {
//		return ResponseEntity.ok(statusService.getAllStatuses());
//	}
//	
//	@GetMapping("/{id}")
//	public ResponseEntity<StatusResponseDto> getAllStatusByyId(@PathVariable("id") int statusId) {
//		return ResponseEntity.ok(statusService.getStatusById(statusId));
//	}
//	
//	@GetMapping("/inst")
//	public ResponseEntity<List<StatusResponseDto>> getStatusesByInstId(HttpServletRequest request) {
//		return ResponseEntity.ok(statusService.getStatusByInstId(Integer.parseInt(request.getHeader("instId"))));
//	}
}