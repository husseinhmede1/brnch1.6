package com.mdsl.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.response.TaskResponseDto;
import com.mdsl.model.entity.Institution;
import com.mdsl.model.entity.Task;
import com.mdsl.model.mapper.TaskMapper;
import com.mdsl.repository.InstitutionRepository;
import com.mdsl.repository.TaskRepository;
import com.mdsl.utils.ResponseCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {
	
	private final TaskRepository taskRepository;
	private final InstitutionRepository institutionRepository;
	
	private final TaskMapper taskMapper;
	
	public List<TaskResponseDto> getAllTasks() {
		List<TaskResponseDto> taskResponseDto = new ArrayList<TaskResponseDto>();
		List<Task> allTasks = taskRepository.findAll(Sort.by(Sort.Direction.ASC, "recordSeqId"));
		allTasks.stream().forEach((task) -> {
			taskResponseDto.add(taskMapper.toDto(task));
		});
		
//		if(taskResponseDto.isEmpty()) {
//			throw new BusinessException(ResponseCode.CFG_NO_DATA_FOUND, HttpStatus.NOT_FOUND);
//		}
		
		return taskResponseDto;
	}
	
	public List<TaskResponseDto> getAllTasksByInstitutionId(String instId) {
		List<TaskResponseDto> allTasksDto = new ArrayList<TaskResponseDto>();
		Institution institution = this.institutionRepository.findById(instId)
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
		List<Task> allTasks = this.taskRepository.findByInstitutionIdOrInstitutionId("SYSTEM", institution.getInstitutionId());
		
		allTasks.stream().forEach((task) -> {
			allTasksDto.add(taskMapper.toDto(task));
		});
		
		return allTasksDto;
	}

}
