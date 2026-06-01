package com.mdsl.service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.PaginatedRequestDto;
import com.mdsl.model.dto.request.TaskExecutionLogRequestDto;
import com.mdsl.model.dto.response.PageableTaskExecutionLogResponseDto;
import com.mdsl.model.dto.response.PaginatedResponseDto;
import com.mdsl.model.dto.response.TaskExecutionLogResponseDto;
import com.mdsl.model.entity.Institution;
import com.mdsl.model.entity.Task;
import com.mdsl.model.entity.TaskExecutionLog;
import com.mdsl.model.entity.User;
import com.mdsl.model.mapper.TaskExecutionLogMapper;
import com.mdsl.repository.InstitutionRepository;
import com.mdsl.repository.TaskExecutionLogRepository;
import com.mdsl.repository.TaskRepository;
import com.mdsl.repository.UserRepository;
import com.mdsl.utils.ResponseCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskExecutionLogService {
	
	private final TaskExecutionLogRepository taskExecutionLogRepository;
	private final TaskRepository taskRepository;
	private final UserRepository userRepository;
	private final InstitutionRepository institutionRepository;
	
	private final TaskExecutionLogMapper taskExecutionLogMapper;
	public PageableTaskExecutionLogResponseDto getTaskExecutionLogs(TaskExecutionLogRequestDto taskExecutionLogRequestDto) {		
	    Pageable pageable = PageRequest.of(
	        taskExecutionLogRequestDto.getPaginationRequestDto().getOffset(), 
	        taskExecutionLogRequestDto.getPaginationRequestDto().getPageSize(), 
	        taskExecutionLogRequestDto.getPaginationRequestDto().getAsc().trim().equalsIgnoreCase("true") 
	            ? Sort.by(taskExecutionLogRequestDto.getPaginationRequestDto().getSortBy()).ascending()
	            : Sort.by(taskExecutionLogRequestDto.getPaginationRequestDto().getSortBy()).descending()
	    );
	    
	    PageableTaskExecutionLogResponseDto pageableTaskExecutionLogResponseDto = new PageableTaskExecutionLogResponseDto();	
	    Page<TaskExecutionLog> allTaskExecutionLogs = null;
	    List<TaskExecutionLogResponseDto> allTaskExecutionLogsDtos = new ArrayList<TaskExecutionLogResponseDto>();
	    
	    Institution institution = institutionRepository.findById(taskExecutionLogRequestDto.getInstitutionId())
	            .orElseThrow(() -> new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
	    
	    Integer taskId = 0;
	    Timestamp startTimestamp = null;
	    Timestamp endTimestamp = null;
	    
	    // Validate and get taskId
	    if(taskExecutionLogRequestDto.getTaskId() != 0) {	
	        Task task = this.taskRepository.findById(taskExecutionLogRequestDto.getTaskId())
	                .orElseThrow(() -> new BusinessException(ResponseCode.TSK_TASK_NOT_FOUND, HttpStatus.NOT_FOUND));
	        taskId = task.getTaskId();
	    }
	    
	    // Parse start date
	    if(Objects.nonNull(taskExecutionLogRequestDto.getStartDatetime()) && !taskExecutionLogRequestDto.getStartDatetime().isEmpty()) {	
	        String startDatetime = taskExecutionLogRequestDto.getStartDatetime();
	        SimpleDateFormat dateFormat = startDatetime.length() > 10 
	            ? new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
	            : new SimpleDateFormat("yyyy-MM-dd");
	        
	        try {
	            java.util.Date parsedDate = dateFormat.parse(startDatetime);
	            startTimestamp = new Timestamp(parsedDate.getTime());
	        } catch (ParseException e) {
	            throw new BusinessException(ResponseCode.JOB_INVALID_DATE, HttpStatus.BAD_REQUEST);
	        }
	    }
	    
	    // Parse end date
	    if(Objects.nonNull(taskExecutionLogRequestDto.getEndDatetime()) && !taskExecutionLogRequestDto.getEndDatetime().isEmpty()) {	
	        String endDatetime = taskExecutionLogRequestDto.getEndDatetime();
	        SimpleDateFormat dateFormat;
	        
	        if(endDatetime.length() > 10) {
	            dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
	        } else {
	            // For date-only format, set to end of day (23:59:59.999)
	            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	        }
	        
	        try {
	            java.util.Date parsedDate = dateFormat.parse(endDatetime);
	            endTimestamp = new Timestamp(parsedDate.getTime());
	            
	            // If date-only format, add almost a full day to include entire day
	            if(endDatetime.length() <= 10) {
	                endTimestamp = new Timestamp(endTimestamp.getTime() + (24 * 60 * 60 * 1000) - 1); // Add 23:59:59.999
	            }
	        } catch (ParseException e) {
	            throw new BusinessException(ResponseCode.JOB_INVALID_DATE, HttpStatus.BAD_REQUEST);
	        }
	    }
	    
	    // Single repository call - no need for two different methods
	    allTaskExecutionLogs = this.taskExecutionLogRepository.findByInstitutionIdTaskIdAndDateRange(
	        institution.getInstitutionId(), 
	        taskId, 
	        startTimestamp, 
	        endTimestamp, 
	        pageable
	    );
	    
	    pageableTaskExecutionLogResponseDto.setPaginatedResponseDto(
	        getPaginationResponseDto(taskExecutionLogRequestDto.getPaginationRequestDto(), allTaskExecutionLogs)
	    );
	    
	    DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
	    
	    allTaskExecutionLogs.stream().forEach((taskExecutionLog) -> {
	        User user = this.userRepository.findById(taskExecutionLog.getCreatedBy())
	                .orElseThrow(() -> new BusinessException(ResponseCode.USR_USER_NOT_FOUND, HttpStatus.NOT_FOUND));

	        TaskExecutionLogResponseDto taskExecutionResponseDto = taskExecutionLogMapper.toDto(taskExecutionLog);
	        taskExecutionResponseDto.setUserName(user.getUsername());
	        
	        LocalDateTime startDatetime = taskExecutionLog.getStartDatetime().toLocalDateTime();
	        LocalDateTime endDatetime = taskExecutionLog.getEndDatetime().toLocalDateTime();
	        
	        String formattedStartDatetime = startDatetime.format(outputFormatter);
	        String formattedEndDatetime = endDatetime.format(outputFormatter);
	        
	        LocalDateTime parsedStartDatetime = LocalDateTime.parse(formattedStartDatetime, outputFormatter);
	        LocalDateTime parsedEndDatetime = LocalDateTime.parse(formattedEndDatetime, outputFormatter);
	        
	        taskExecutionResponseDto.setStartDatetime(parsedStartDatetime.toString());
	        taskExecutionResponseDto.setEndDatetime(parsedEndDatetime.toString());

	        allTaskExecutionLogsDtos.add(taskExecutionResponseDto);
	    });
	    
	    pageableTaskExecutionLogResponseDto.setTaskExecutionLogResponseDto(allTaskExecutionLogsDtos);
	    return pageableTaskExecutionLogResponseDto;
	}
//	public PageableTaskExecutionLogResponseDto getTaskExecutionLogs(TaskExecutionLogRequestDto taskExecutionLogRequestDto) {		
//		Pageable pageable = PageRequest.of(taskExecutionLogRequestDto.getPaginationRequestDto().getOffset(), taskExecutionLogRequestDto.getPaginationRequestDto().getPageSize(), taskExecutionLogRequestDto.getPaginationRequestDto().getAsc().trim().equalsIgnoreCase("true") ? Sort.by(taskExecutionLogRequestDto.getPaginationRequestDto().getSortBy()).ascending(): Sort.by(taskExecutionLogRequestDto.getPaginationRequestDto().getSortBy()).descending());
//		PageableTaskExecutionLogResponseDto pageableTaskExecutionLogResponseDto = new PageableTaskExecutionLogResponseDto();	
//		Page<TaskExecutionLog> allTaskExecutionLogs = null;
//		List<TaskExecutionLogResponseDto> allTaskExecutionLogsDtos = new ArrayList<TaskExecutionLogResponseDto>();
//		Institution institution = institutionRepository.findById(taskExecutionLogRequestDto.getInstitutionId())
//				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
//		
//		Integer taskId = 0;
//		String formattedStartDate = null;
//		String formattedEndDate = null;
//		
//		if(taskExecutionLogRequestDto.getTaskId() != 0)	{	
//			Task task = this.taskRepository.findById(taskExecutionLogRequestDto.getTaskId())
//					.orElseThrow(() -> new BusinessException(ResponseCode.TSK_TASK_NOT_FOUND, HttpStatus.NOT_FOUND));
//			taskId = task.getTaskId();
//		}
//		if(Objects.nonNull(taskExecutionLogRequestDto.getStartDatetime()) && !taskExecutionLogRequestDto.getStartDatetime().equals(""))	{	
//			// Check and validate the start date format
//		    String startDatetime = taskExecutionLogRequestDto.getStartDatetime();
//		    if (!startDatetime.isEmpty()) {
//		    	SimpleDateFormat dateFormat = null;
//		    	if(startDatetime.length() > 10) {
//		    		dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
//		    	}
//		    	else {
//		    		dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//		    	}
//		        try {
//		            dateFormat.parse(startDatetime);
//		            formattedStartDate = startDatetime;
//		        } catch (ParseException e) {
//		            throw new BusinessException(ResponseCode.JOB_INVALID_DATE, HttpStatus.BAD_REQUEST);
//		        }
//		    }
//		}
//		if(Objects.nonNull(taskExecutionLogRequestDto.getEndDatetime()) && !taskExecutionLogRequestDto.getEndDatetime().equals(""))	{	
//			// Check and validate the end date format
//		    String endDatetime = taskExecutionLogRequestDto.getEndDatetime();
//		    System.out.println(endDatetime);
//		    if (!endDatetime.isEmpty()) {
//		    	SimpleDateFormat dateFormat = null;
//		    	if(endDatetime.length() > 10) {
//		    		dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
//		    	}
//		    	else {
//		    		dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//		    	}
//		        try {
//		            dateFormat.parse(endDatetime);
//		            formattedEndDate = endDatetime;
//		        } catch (ParseException e) {
//		        	throw new BusinessException(ResponseCode.JOB_INVALID_DATE, HttpStatus.BAD_REQUEST);
//		        }
//		    }
//		}
//		
//		if(formattedStartDate.length() > 10 && formattedEndDate.length() > 10) {
//			allTaskExecutionLogs = this.taskExecutionLogRepository.getByInstitutionIdTaskIdAndStartDatetimeAndEndDatetime(institution.getInstitutionId(),taskId, formattedStartDate, formattedEndDate, pageable);
//		} else {
//			allTaskExecutionLogs = this.taskExecutionLogRepository.findByInstitutionIdTaskIdAndStartDatetimeAndEndDatetime(institution.getInstitutionId(),taskId, formattedStartDate, formattedEndDate, pageable);
//		}
//		
//		pageableTaskExecutionLogResponseDto.setPaginatedResponseDto(getPaginationResponseDto(taskExecutionLogRequestDto.getPaginationRequestDto(), allTaskExecutionLogs));
//		DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
//		
//		allTaskExecutionLogs.stream().forEach((taskExecutionLog) -> {
//		    User user = this.userRepository.findById(taskExecutionLog.getCreatedBy())
//		            .orElseThrow(() -> new BusinessException(ResponseCode.USR_USER_NOT_FOUND, HttpStatus.NOT_FOUND));
//
//		    TaskExecutionLogResponseDto taskExecutionResponseDto = taskExecutionLogMapper.toDto(taskExecutionLog);
//		    taskExecutionResponseDto.setUserName(user.getUsername());
//		    
//		    // Convert Timestamp to LocalDateTime
//		    LocalDateTime startDatetime = taskExecutionLog.getStartDatetime().toLocalDateTime();
//		    LocalDateTime endDatetime = taskExecutionLog.getEndDatetime().toLocalDateTime();
//		    // Format the LocalDateTime values
//		    String formattedStartDatetime = startDatetime.format(outputFormatter);
//		    String formattedEndDatetime = endDatetime.format(outputFormatter);
//		    // Parse the formatted strings back to LocalDateTime
//		    LocalDateTime parsedStartDatetime = LocalDateTime.parse(formattedStartDatetime, outputFormatter);
//		    LocalDateTime parsedEndDatetime = LocalDateTime.parse(formattedEndDatetime, outputFormatter);
//		    taskExecutionResponseDto.setStartDatetime(parsedStartDatetime.toString());
//		    taskExecutionResponseDto.setEndDatetime(parsedEndDatetime.toString());
//
//		    allTaskExecutionLogsDtos.add(taskExecutionResponseDto);
//		});
//		pageableTaskExecutionLogResponseDto.setTaskExecutionLogResponseDto(allTaskExecutionLogsDtos);
//		return pageableTaskExecutionLogResponseDto;
//		
//	}
//	
	public PaginatedResponseDto getPaginationResponseDto(Object paginatedRequestDto, Page<?> pagesResult) {
		PaginatedResponseDto paginationResponseDto = new PaginatedResponseDto();
		
		if (paginatedRequestDto instanceof  PaginatedRequestDto) {
			PaginatedRequestDto request = (PaginatedRequestDto) paginatedRequestDto;
			paginationResponseDto.setAsc(request.getAsc());
			paginationResponseDto.setPageNumber(request.getOffset());
			paginationResponseDto.setPageSize(request.getPageSize());
			paginationResponseDto.setSortBy(request.getSortBy());
		}

		long totalElements = pagesResult.getTotalElements();
		paginationResponseDto.setTotalNumberOfRecords((int)totalElements);
		return paginationResponseDto;
	} 

}
