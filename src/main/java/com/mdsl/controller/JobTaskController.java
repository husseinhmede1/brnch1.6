package com.mdsl.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mdsl.model.dto.response.JobTaskResponseDto;
import com.mdsl.service.JobTaskService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*")
@Api("Job Task Controller")
@RestController
@Transactional
@RequestMapping("/config/job-tasks")
public class JobTaskController {

	private JobTaskService jobTaskService;

	@Autowired
	public JobTaskController(JobTaskService jobTaskService) {
		super();
		this.jobTaskService = jobTaskService;
	}

	@GetMapping
	@ApiOperation(value = "Get all Job Tasks",response = JobTaskResponseDto.class)
	public ResponseEntity<List<JobTaskResponseDto>> findAllJobTasks(HttpServletRequest request) {
		return ResponseEntity.ok(jobTaskService.findAllJobTasks());
	}
	
	@GetMapping("/{id}")
	@ApiOperation(value = "Get Job Task by ID")
	public ResponseEntity<JobTaskResponseDto> findJobTaskById(@PathVariable("id") int taskId, HttpServletRequest request) {
		return ResponseEntity.ok(jobTaskService.findJobTaskById(taskId));
	}
}