package com.mdsl.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mdsl.model.dto.response.TaskParameterResponseDto;
import com.mdsl.service.TaskParameterService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/task-parameter")
public class TaskParameterController {
	
	@Autowired
	private TaskParameterService taskParameterService;
	
	@GetMapping("/task/{id}")
	public ResponseEntity<List<TaskParameterResponseDto>> getTaskParametersByTaskId(@PathVariable("id") int id) {
		return ResponseEntity.ok(taskParameterService.getTaskParametersByTaskId(id));
	}

}
