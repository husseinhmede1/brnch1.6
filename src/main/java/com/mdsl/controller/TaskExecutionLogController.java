package com.mdsl.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mdsl.model.dto.request.TaskExecutionLogRequestDto;
import com.mdsl.model.dto.response.PageableTaskExecutionLogResponseDto;
import com.mdsl.service.TaskExecutionLogService;
import com.mdsl.utils.Validations;

@CrossOrigin(origins={"*"})
@RestController
@RequestMapping({"/task-execution-log"})
public class TaskExecutionLogController {
	
	@Autowired
	private TaskExecutionLogService taskExecutionLogService;
	
	@PostMapping({"/filter"})
	public ResponseEntity<PageableTaskExecutionLogResponseDto> getTaskExecutionLogs(@Valid @RequestBody TaskExecutionLogRequestDto taskExecutionLogRequestDto, BindingResult bindingResult, HttpServletRequest request) {
		Validations.validate(bindingResult);
		return ResponseEntity.ok(this.taskExecutionLogService.getTaskExecutionLogs(taskExecutionLogRequestDto));
	}

}
