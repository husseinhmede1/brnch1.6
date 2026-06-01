package com.mdsl.service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.PaginatedRequestDto;
import com.mdsl.model.dto.request.ProcessingEventsRequestDto;
import com.mdsl.model.dto.response.FileDirectoryResponseDto;
import com.mdsl.model.dto.response.PageableProcessingEventsResponseDto;
import com.mdsl.model.dto.response.PaginatedResponseDto;
import com.mdsl.model.dto.response.ProcessingEventsResponseDto;
import com.mdsl.model.dto.response.RunTaskResponseDto;
import com.mdsl.model.entity.Institution;
import com.mdsl.model.entity.ProcessingEvents;
import com.mdsl.model.entity.Task;
import com.mdsl.model.entity.TaskExecutionLog;
import com.mdsl.model.entity.TaskParameter;
import com.mdsl.model.mapper.ProcessingEventsMapper;
import com.mdsl.repository.InstitutionRepository;
import com.mdsl.repository.ProcessingEventsRepository;
import com.mdsl.repository.TaskExecutionLogRepository;
import com.mdsl.repository.TaskParametersRepository;
import com.mdsl.repository.TaskRepository;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.enumerations.TaskSuccesResultEnum;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProcessingEventsService {
	
	private final ProcessingEventsRepository processingEventsRepository;
	private final InstitutionRepository institutionRepository;
	private final TaskRepository taskRepository;
	private final TaskExecutionLogRepository taskExecutionLogRepository;
	private final TaskParametersRepository taskParametersRepository;
	
	private final ProcessingEventsMapper processingEventsMapper;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	 
	@Autowired
	@Qualifier("appJdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	public PageableProcessingEventsResponseDto getAllProcessingEventsByInstitutionAndTaskExecutionLogId(String instId,int taskExecutionLogId, PaginatedRequestDto paginationRequestDto) {
		Institution institution = institutionRepository.findById(instId)
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_INST, HttpStatus.NOT_FOUND));
		TaskExecutionLog taskExecutionLog = this.taskExecutionLogRepository.findById(taskExecutionLogId)
				.orElseThrow(() -> new BusinessException(ResponseCode.TSK_TASK_NOT_FOUND, HttpStatus.NOT_FOUND));
		Pageable pageable = PageRequest.of(paginationRequestDto.getOffset(), paginationRequestDto.getPageSize(), paginationRequestDto.getAsc().trim().equalsIgnoreCase("true") ? Sort.by(paginationRequestDto.getSortBy()).ascending(): Sort.by(paginationRequestDto.getSortBy()).descending());
		PageableProcessingEventsResponseDto pageableProcessingEventsResponseDto = new PageableProcessingEventsResponseDto();
		Page<ProcessingEvents> allProcessingEvents = processingEventsRepository.findByInstitutionIdAndTaskExecutionLogId(institution.getInstitutionId(), taskExecutionLog.getTaskExecutionLogId(), pageable);
		List<ProcessingEventsResponseDto> allProcessingEventsDtos = new ArrayList<ProcessingEventsResponseDto>();
		
		pageableProcessingEventsResponseDto.setPaginatedResponseDto(getPaginationResponseDto(paginationRequestDto, allProcessingEvents));

		allProcessingEvents.stream().forEach((processingEvents) -> {
			ProcessingEventsResponseDto processingEventsResponseDto = processingEventsMapper.toDto(processingEvents);
			
			TaskSuccesResultEnum result = TaskSuccesResultEnum.valueOf(String.valueOf(processingEvents.getSuccessResult()));
	        processingEventsResponseDto.setSuccessResult(result.getValue());
			
			allProcessingEventsDtos.add(processingEventsResponseDto);
		});
		pageableProcessingEventsResponseDto.setProcessingEventsResponseDto(allProcessingEventsDtos);
		return pageableProcessingEventsResponseDto;
	}
	
	public List<FileDirectoryResponseDto> getFilesFromDirectory(String instId, String scope) {
		Institution institution = this.institutionRepository.findById(instId)
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_INST, HttpStatus.NOT_FOUND));
		try {
	        String result;

	        result = jdbcTemplate.execute(connection -> {
	            CallableStatement callableStatement = connection.prepareCall("{ call MD_PKG_UTILITIES.GET_INPUT_FILENAMES(?, ?, ?) }");
	            
	            callableStatement.setString(1, institution.getInstitutionId());
				callableStatement.setString(2, scope);
	            callableStatement.registerOutParameter(3, Types.VARCHAR);

	            return callableStatement;
	        }, (CallableStatement cs) -> {
	            cs.execute();

	            return cs.getString(3); // Return the result from the procedure
	        });

	        if (result != null && !result.isEmpty()) {
	        	List<String> files = new ArrayList<>(List.of(result.split("\\|")));
	        	List<FileDirectoryResponseDto> fileDirectoryResponseDtos = new ArrayList<FileDirectoryResponseDto>();
	        	
	        	for(String file : files) {
	        		FileDirectoryResponseDto fileDirectoryResponseDto = new FileDirectoryResponseDto();
	        		fileDirectoryResponseDto.setFileName(file);
	        		fileDirectoryResponseDto.setDisplayFileName(file);
	        		fileDirectoryResponseDtos.add(fileDirectoryResponseDto);
	        	}
	        	
	        	return fileDirectoryResponseDtos; 
	        } else {
	        	throw new BusinessException(ResponseCode.CFG_NO_FILES_FOUND, HttpStatus.NOT_FOUND);
	        }

	    } catch (ArithmeticException e) {
	    	e.printStackTrace();
	    	throw new BusinessException(ResponseCode.CFG_NO_FILES_FOUND, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	
	public RunTaskResponseDto runTask(ProcessingEventsRequestDto processingEventsRequestDto) {
	    UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    RunTaskResponseDto runTaskResponseDto = new RunTaskResponseDto();
	    
	    TaskExecutionLog saveTaskExecutionLog = new TaskExecutionLog();
        TaskExecutionLog savedTaskExecutionLog = new TaskExecutionLog();
	    
	    Connection conn = null;
	    try {
	        conn = this.jdbcTemplate.getDataSource().getConnection();
	        if (conn == null) {
	            this.logger.debug("Could not get a connection.");
	            runTaskResponseDto.setResult(-1);
	        }
	        Institution institution = this.institutionRepository.findById(processingEventsRequestDto.getInstitutionId()).orElseThrow(() -> new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
	        Task task = this.taskRepository.findById(processingEventsRequestDto.getTaskId()).orElseThrow(() -> new BusinessException(ResponseCode.TSK_TASK_NOT_FOUND, HttpStatus.NOT_FOUND));
	        List<TaskParameter> allTaskParameters = this.taskParametersRepository.findByTaskIdOrderBySequence(task.getTaskId());
	        String command = "{call " + task.getTaskPackage() + "." + task.getTaskProcedure() + "(?, ";
	        
	        for (int i = 0; i < allTaskParameters.size(); i++) {
	            command += "?, ";
	        }

	        command += "?, ?)}";			//Set the out parameters
	        
	        saveTaskExecutionLog.setTaskId(task.getTaskId());
	        saveTaskExecutionLog.setTaskDetails(task.getTaskName());
	        saveTaskExecutionLog.setStartDatetime(new Timestamp(System.currentTimeMillis()));
	        saveTaskExecutionLog.setCreatedBy(userDetails.getId());
	        saveTaskExecutionLog.setCreatedDate(new Timestamp(System.currentTimeMillis()));
	        saveTaskExecutionLog.setInstitutionId(institution.getInstitutionId());
	        savedTaskExecutionLog = this.taskExecutionLogRepository.save(saveTaskExecutionLog);

	        int counter = 1;
	        CallableStatement cstmt = conn.prepareCall(command);
	        cstmt.setInt(counter++, savedTaskExecutionLog.getTaskExecutionLogId());

	        for (int i = 0; i < allTaskParameters.size(); i++) {
	            if (allTaskParameters.get(i).getParameterType().equals("VARCHAR2")) {
	                String value = processingEventsRequestDto.getTaskParameters().get(allTaskParameters.get(i).getTaskParamId());
	                cstmt.setString(counter++, value);
	            } else if (allTaskParameters.get(i).getParameterType().equals("NUMBER")) {
	                Integer value = Integer.valueOf(processingEventsRequestDto.getTaskParameters().get(allTaskParameters.get(i).getTaskParamId()));
	                cstmt.setInt(counter++, value);
	            } else if (allTaskParameters.get(i).getParameterType().equals("DATE")) {
	                String value = processingEventsRequestDto.getTaskParameters().get(allTaskParameters.get(i).getTaskParamId());
	                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	                Date date = sdf.parse(value);
	                Timestamp timestamp = new Timestamp(date.getTime());
	                cstmt.setTimestamp(counter++, timestamp);
	            }
	        }
	        cstmt.registerOutParameter(counter++, Types.INTEGER);
	        cstmt.registerOutParameter(counter++, Types.VARCHAR);
	        cstmt.executeUpdate();
	        cstmt.close();

	        savedTaskExecutionLog.setEndDatetime(new Timestamp(System.currentTimeMillis()));

	        this.taskExecutionLogRepository.save(savedTaskExecutionLog);
	    } catch (SQLException s) {
	        this.logger.debug(s.getMessage());
	        runTaskResponseDto.setResult(-2);
	    } catch (Exception e) {
	        e.printStackTrace();
	        runTaskResponseDto.setResult(-3);
	    } finally {
	        try {
	            if (conn != null) {
	                conn.close();
	            }
	        } catch (SQLException l) {
	            this.logger.debug(l.getMessage());
	        }
	    }
	    runTaskResponseDto.setResult(0);
	    runTaskResponseDto.setTaskExecutionLogId(savedTaskExecutionLog.getTaskExecutionLogId());
	    
	    return runTaskResponseDto;
	}
	
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
