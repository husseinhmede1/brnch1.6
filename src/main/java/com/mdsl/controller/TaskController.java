package com.mdsl.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mdsl.model.dto.response.TaskResponseDto;
import com.mdsl.service.TaskService;

@CrossOrigin(origins = "*")
@RestController
//@Transactional(rollbackOn = Exception.class)
@RequestMapping("/task")
public class TaskController {
	
	private final TaskService taskService;
	
	@Autowired
	public TaskController(TaskService taskService) {
		this.taskService = taskService;
	}
	
	@GetMapping
	public ResponseEntity<List<TaskResponseDto>> getAllTasks() {
		return ResponseEntity.ok(this.taskService.getAllTasks());
	}
	
	@GetMapping("/inst/{instId}")
	public ResponseEntity<List<TaskResponseDto>> getAllTasksByInstitutionId(@PathVariable("instId") String instId) {
		return ResponseEntity.ok(this.taskService.getAllTasksByInstitutionId(instId));
	}

}
