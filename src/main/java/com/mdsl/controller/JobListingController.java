package com.mdsl.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mdsl.model.dto.request.ChangeStatusRequestDto;
import com.mdsl.model.dto.request.DeleteJobRequestDto;
import com.mdsl.model.dto.request.JobRequestDto;
import com.mdsl.model.dto.request.ScheduleJobRequestDto;
import com.mdsl.model.dto.response.JobResponseDto;
import com.mdsl.model.dto.response.JobScheduledMonitoringResponseDto;
import com.mdsl.service.JobService;
import com.mdsl.utils.Validations;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*")
@Api("Job Controller")
@RestController
@Transactional
@RequestMapping("/config/jobs")
public class JobListingController {

	private final JobService jobService;

	@Autowired
	public JobListingController(JobService jobService) {
		super();
		this.jobService = jobService;
	}

	@GetMapping
	@ApiOperation(value = "Get all Jobs",response = JobResponseDto.class)
	public ResponseEntity<List<JobResponseDto>> getAllJobs(HttpServletRequest request) {
		return ResponseEntity.ok(jobService.getAllJobs(String.valueOf(request.getHeader("instId"))));
	}
	
	@GetMapping("/active")
	@ApiOperation(value = "Get all Active Jobs",response = JobResponseDto.class)
	public ResponseEntity<List<JobResponseDto>> getAllActiveJobs(HttpServletRequest request) {
		return ResponseEntity.ok(jobService.getAllActiveJobs(String.valueOf(request.getHeader("instId"))));
	}

	@GetMapping("/{id}")
	@ApiOperation(value = "Get Job by ID")
	public ResponseEntity<JobResponseDto> getJobById(@PathVariable("id") int jobId, HttpServletRequest request) {
		return ResponseEntity.ok(jobService.getJobById(jobId, String.valueOf(request.getHeader("instId"))));
	}
	
	@GetMapping("execution-status/{id}")
	@ApiOperation(value = "Get Job execution status by ID")
	public ResponseEntity<String> getJobExecutionStatusById(@PathVariable("id") int jobId, HttpServletRequest request) {
		return ResponseEntity.ok(jobService.getJobExecutionStatusById(jobId, String.valueOf(request.getHeader("instId"))));
	}
	
	@PostMapping
	@ApiOperation(value = "Save Job")
	public ResponseEntity<JobResponseDto> saveJob(@Valid @RequestBody JobRequestDto jobRequestDto, BindingResult bindingResult, HttpServletRequest request) {
		Validations.validate(bindingResult);
		jobRequestDto.setInstId(String.valueOf(request.getHeader("instId")));
		jobRequestDto.setRemoteAddress(request.getRemoteAddr());
		return ResponseEntity.ok(jobService.saveJob(jobRequestDto));
	}

	@PostMapping("/schedule")
	@ApiOperation(value = "Schedule Job")
	public ResponseEntity<JobResponseDto> scheduleJob(@Valid @RequestBody ScheduleJobRequestDto scheduleJobRequestDto, BindingResult bindingResult, HttpServletRequest request) {
		Validations.validate(bindingResult);
		scheduleJobRequestDto.setInstId(String.valueOf(request.getHeader("instId")));
		scheduleJobRequestDto.setRemoteAddress(request.getRemoteAddr());
		return ResponseEntity.ok(jobService.scheduleJob(scheduleJobRequestDto));
	}
	
	@PostMapping("/start/{jobId}")
	@ApiOperation(value = "Start Job Execution")
	public void startJobExecution(@PathVariable("jobId") int jobId, HttpServletRequest request) {
		jobService.startJobExecution(jobId, String.valueOf(request.getHeader("instId")), request.getRemoteAddr(), request.getLocale());
	}
	
	@PostMapping("/stop/{jobId}")
	@ApiOperation(value = "Stop Job Execution")
	public void stopJobExecution(@PathVariable("jobId") int jobId, HttpServletRequest request) {
		jobService.stopJobExecution(jobId, String.valueOf(request.getHeader("instId")), request.getRemoteAddr());
	}
	
	@PostMapping("/status-change")
	@ApiOperation(value = "Enable/Disable Job")
	public void enableDisableJob(@Valid @RequestBody ChangeStatusRequestDto changeJobStatusRequestDTO, BindingResult bindingResult, HttpServletRequest request) {
		Validations.validate(bindingResult);
		changeJobStatusRequestDTO.setInstId(String.valueOf(request.getHeader("instId")));
		changeJobStatusRequestDTO.setRemoteAddress(request.getRemoteAddr());
		jobService.enableDisableJob(changeJobStatusRequestDTO);
	}
	
	@DeleteMapping("/{id}")
	@ApiOperation(value = "Delete Job")
	public void deleteJob(@PathVariable("id") int jobId, HttpServletRequest request) {

	    DeleteJobRequestDto dto = new DeleteJobRequestDto();
	    dto.setJobId(jobId);
	    dto.setInstId(request.getHeader("instId"));
	    dto.setRemoteAddress(request.getRemoteAddr());

	    jobService.deleteJob(dto);
	}
	@GetMapping("/job-monitoring")
	@ApiOperation(value = "Job Monitoring ",response = JobScheduledMonitoringResponseDto.class)
	public ResponseEntity<List<JobScheduledMonitoringResponseDto>> getJobMonitoring(HttpServletRequest request) {
		return ResponseEntity.ok(jobService.getJobMonitoring(String.valueOf(request.getHeader("instId"))));
	}
}