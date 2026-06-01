package com.mdsl.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.response.BKDParameterResponseDto;
import com.mdsl.model.dto.response.JobTaskResponseDto;
import com.mdsl.model.entity.JobTask;
import com.mdsl.model.mapper.JobTaskMapper;
import com.mdsl.repository.JobTaskRepository;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.Validations;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JobTaskService {
	private final JobTaskRepository jobTaskRepository;
	private final JobTaskMapper jobTaskMapper;

	/*
	 * Returns all tasks from table MD_JOB_TASK 
	 */
	public List<JobTaskResponseDto> findAllJobTasks() {
	    List<JobTask> jobTasks = jobTaskRepository.findAllByOrderByTaskName();
	    if (jobTasks.isEmpty()) {
	        Validations.isEmpty(jobTasks);
	        return Collections.emptyList();
	    }
	    List<Integer> taskIds = jobTasks.stream().map(JobTask::getTaskId).collect(Collectors.toList());
	    Map<Integer, List<BKDParameterResponseDto>> taskParametersMap =  new HashMap<>();
	    for (Object[] row : jobTaskRepository.findJobTaskparametersAll(taskIds)) {
	        Integer taskId = ((Number) row[0]).intValue();
	        BKDParameterResponseDto dto = new BKDParameterResponseDto();
	        dto.setParameterId(((Number) row[1]).intValue());
	        dto.setParametersServiceId(((Number) row[2]).intValue());
	        dto.setParameterName((String) row[3]);
	        dto.setIsMandatory(row[4] == null ? null : String.valueOf(row[4]));
	        taskParametersMap.computeIfAbsent(taskId, k -> new ArrayList<>()).add(dto);
	    }
	    List<JobTaskResponseDto> allJobTasks = jobTasks.stream().map(jobTask -> convertToJobTaskResponseDtoForAll(jobTask, taskParametersMap)).collect(Collectors.toList());

	    Validations.isEmpty(allJobTasks);
	    return allJobTasks;
	}
	
	/*
	 * Returns a task from table MD_JOB_TASK based on the task id 
	 */
	public JobTaskResponseDto findJobTaskById(int taskId) {
		JobTask jobTask = jobTaskRepository.findById(taskId).orElseThrow(()-> new BusinessException(ResponseCode.CFG_INVALID_JOB_TASK_ID, HttpStatus.NOT_FOUND)); 
		return this.convertToJobTaskResponseDto(jobTask);
	}
	
	public JobTaskResponseDto convertToJobTaskResponseDto(JobTask jobTask) {
		JobTaskResponseDto response = jobTaskMapper.toDto(jobTask);
		List<BKDParameterResponseDto> parametersList = this.jobTaskRepository.findJobTaskparameters(jobTask.getTaskId());
		response.setParameterResponseDto(parametersList);
		response.setServiceMode(jobTask.getService().getServiceMode());
		return response;
	}
	
	private JobTaskResponseDto convertToJobTaskResponseDtoForAll(JobTask jobTask,Map<Integer, List<BKDParameterResponseDto>> taskParametersMap) {
	    JobTaskResponseDto response = jobTaskMapper.toDto(jobTask);
	    List<BKDParameterResponseDto> parametersList = taskParametersMap.getOrDefault(jobTask.getTaskId(), Collections.emptyList());
	    response.setParameterResponseDto(parametersList);
	    response.setServiceMode(jobTask.getService().getServiceMode());
	    return response;
	}
}