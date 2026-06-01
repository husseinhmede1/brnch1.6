package com.mdsl.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.response.TaskParameterResponseDto;
import com.mdsl.model.entity.Task;
import com.mdsl.model.entity.TaskParameter;
import com.mdsl.model.mapper.TaskParameterMapper;
import com.mdsl.repository.TaskParametersRepository;
import com.mdsl.repository.TaskRepository;
import com.mdsl.utils.ResponseCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskParameterService {
	
	private final TaskParametersRepository taskParameterRepository;
	private final TaskRepository taskRepository;
	
	private final TaskParameterMapper taskParameterMapper;
	
	public List<TaskParameterResponseDto> getTaskParametersByTaskId(int taskId) {
		List<TaskParameterResponseDto> allTaskParametersDto = new ArrayList<TaskParameterResponseDto>();
		Task task = this.taskRepository.findById(taskId)
				.orElseThrow(() -> new BusinessException(ResponseCode.TSK_TASK_NOT_FOUND, HttpStatus.NOT_FOUND));
		List<TaskParameter> taskParameters = this.taskParameterRepository.findByTaskId(task.getTaskId());
		taskParameters.stream().forEach((taskParameter) -> {
			allTaskParametersDto.add(taskParameterMapper.toDto(taskParameter));
		});
		return allTaskParametersDto;
	}

}
