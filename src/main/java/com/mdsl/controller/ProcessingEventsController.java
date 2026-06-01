package com.mdsl.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.PaginatedRequestDto;
import com.mdsl.model.dto.request.ProcessingEventsRequestDto;
import com.mdsl.model.dto.response.FileDirectoryResponseDto;
import com.mdsl.model.dto.response.PageableProcessingEventsResponseDto;
import com.mdsl.model.dto.response.RunTaskResponseDto;
import com.mdsl.service.ProcessingEventsService;
import com.mdsl.utils.Validations;

@CrossOrigin(origins={"*"})
@RestController
@RequestMapping({"/processing-events"})
@RequiredArgsConstructor
public class ProcessingEventsController{

  private final ProcessingEventsService processingEventsService;
  
  @GetMapping("/inst/{institutionId}/{scope}")
  public ResponseEntity<List<FileDirectoryResponseDto>> getFilesFromDirectory(@PathVariable("institutionId") String institutionId,
                                                                              @PathVariable("scope") String scope) {
	  return ResponseEntity.ok(this.processingEventsService.getFilesFromDirectory(institutionId, scope));
  }
  
  @PostMapping({"/task/{institutionId}/{taskExecutionLogId}"})
  public ResponseEntity<PageableProcessingEventsResponseDto> getAllProcessingEventsByInstitutionAndTaskExecutionLogId(@PathVariable("institutionId") String institutionId, @PathVariable("taskExecutionLogId") int taskExecutionLogId, @Valid @RequestBody PaginatedRequestDto paginatedRequestDto, BindingResult bindingResult, HttpServletRequest request) {
	Validations.validate(bindingResult);
    return ResponseEntity.ok(this.processingEventsService.getAllProcessingEventsByInstitutionAndTaskExecutionLogId(institutionId, taskExecutionLogId, paginatedRequestDto));
  }

  @PostMapping
  public ResponseEntity<RunTaskResponseDto> runTask(@Valid @RequestBody ProcessingEventsRequestDto processingEventsRequestDto, BindingResult bindingResult) {
    Validations.validate(bindingResult);
    RunTaskResponseDto result = this.processingEventsService.runTask(processingEventsRequestDto);
    if (result.getResult() == -1) {
      throw new BusinessException("CFG-280", HttpStatus.BAD_REQUEST);
    }
    if (result.getResult() == -2) {
      throw new BusinessException("CFG-282", HttpStatus.BAD_REQUEST);
    }
    if (result.getResult() == -3) {
      throw new BusinessException("CFG-280", HttpStatus.BAD_REQUEST);
    }

    return ResponseEntity.ok(result);
  }
}
