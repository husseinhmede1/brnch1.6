package com.mdsl.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mdsl.model.dto.request.JobExecutionLogRequestDto;
import com.mdsl.model.dto.response.PageableJobExecutionLogResponseDto;
import com.mdsl.service.JobExecutionLogService;
import com.mdsl.utils.Validations;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("Job Execution Log")
@CrossOrigin(origins = "*")
@RestController
@Transactional
@RequestMapping("/config/job-execution-logs")
public class JobExecutionLogController {

	private JobExecutionLogService jobExecutionLogService;

	@Autowired
	public JobExecutionLogController(JobExecutionLogService jobExecutionLogService) {
		super();
		this.jobExecutionLogService = jobExecutionLogService;
	}

	@PostMapping("/job")
	@ApiOperation(value = "Get all Logs by Job ID")
	public ResponseEntity<PageableJobExecutionLogResponseDto> getAllLogsByJobId(@Valid @RequestBody JobExecutionLogRequestDto jobExecutionLogRequestDto, BindingResult bindingResult, HttpServletRequest request) {
		Validations.validate(bindingResult);
		return ResponseEntity.ok(jobExecutionLogService.getAllLogsByJobId(jobExecutionLogRequestDto, String.valueOf(request.getHeader("instId"))));
	}
}