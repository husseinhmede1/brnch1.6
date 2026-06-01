package com.mdsl.controller;

import com.mdsl.model.dto.response.ModuleActivityResponseDto;
import com.mdsl.service.CommonService;
import com.mdsl.service.ModuleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api("Module Controller")
@RestController
@Transactional
@RequestMapping("/v1/lookup/modules")
@RequiredArgsConstructor
public class ModuleController {
	
	private final ModuleService moduleService;
	private final CommonService commonService;

//	@GetMapping("/{id}")
//	@ApiOperation(value = "Get Module by ID")
//	public ResponseEntity<ModuleResponseDto> getModuleById(@PathVariable("id") int id) {
//		return ResponseEntity.ok(moduleService.getModuleById(id));
//	}
//
//	@GetMapping
//	@ApiOperation(value = "Get all Modules",response = ModuleResponseDto.class)
//	public ResponseEntity<List<ModuleResponseDto>> getAllModules() {
//		return ResponseEntity.ok(moduleService.getAllModules());
//	}
//
//	@GetMapping("/active")
//	@ApiOperation(value = "Get all Active Modules",response = ModuleResponseDto.class)
//	public ResponseEntity<List<ModuleResponseDto>> getActiveModules() {
//		return ResponseEntity.ok(moduleService.getActiveModules());
//	}
//
//	@GetMapping("/activity")
//	@ApiOperation(value = "Get all Module Activities",response = ModuleActivityResponseDto.class)
//	public ResponseEntity<List<ModuleActivityResponseDto>> getModulesActivities(HttpServletRequest request) {
//		return ResponseEntity.ok(moduleService.getModulesActivities(Integer.parseInt(request.getHeader("instId"))));
//	}
	
	@GetMapping("/user")
	@ApiOperation(value = "Get all Module Activities by User",response = ModuleActivityResponseDto.class)
	public ResponseEntity<byte[]> getModulesActivitiesByUser(HttpServletRequest request) {
		return ResponseEntity.ok(moduleService.getModulesActivitiesByUser(Integer.parseInt(request.getHeader("instId")),commonService.getLoggedInUser().getId()));
	}
}