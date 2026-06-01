package com.mdsl.service;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.JobExecutionLogRequestDto;
import com.mdsl.model.dto.response.JobExecutionLogResponseDto;
import com.mdsl.model.dto.response.PageableJobExecutionLogResponseDto;
import com.mdsl.model.entity.Institution;
import com.mdsl.model.entity.Job;
import com.mdsl.model.entity.JobExecutionLog;
import com.mdsl.model.mapper.JobExecutionLogMapper;
import com.mdsl.repository.InstitutionRepository;
import com.mdsl.repository.JobExecutionLogRepository;
import com.mdsl.repository.JobRepository;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.Validations;
import com.mdsl.utils.enumerations.SystemCodePrefixEnum;
import com.mdsl.utils.enumerations.SystemCodeSuffixEnum;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JobExecutionLogService {

	private final JobExecutionLogRepository jobExecutionLogRepository;
	private final JobExecutionLogMapper jobExecutionLogMapper;
	private final JobRepository jobRepository;
	private final CommonService commonService;
	private final InstitutionRepository institutionRepository;

	/*
	 * Returns all logs from table MD_JOB_EXECUTION_LOG for a specific job id 
	 */
	public PageableJobExecutionLogResponseDto getAllLogsByJobId(JobExecutionLogRequestDto jobExecutionLogRequestDto, String instId) {
		List<JobExecutionLogResponseDto> listOfJobExecutionLogResponseDTO;

		if ((jobExecutionLogRequestDto.getFromDate().equals("") && !jobExecutionLogRequestDto.getToDate().equals("")) || 
				(!jobExecutionLogRequestDto.getFromDate().equals("") && jobExecutionLogRequestDto.getToDate().equals(""))) {
			throw new BusinessException(ResponseCode.VAL_FROM_TO_DATE_AVAILABLE, HttpStatus.NOT_FOUND);
		}
		
		Date fromDate=null, toDate=null;
		if(!jobExecutionLogRequestDto.getFromDate().equals("") && !jobExecutionLogRequestDto.getFromDate().equals("")) {
			fromDate = Date.valueOf(jobExecutionLogRequestDto.getFromDate()); 
			toDate = Date.valueOf(jobExecutionLogRequestDto.getToDate());
			Validations.dateValidations(fromDate, toDate); 
			Institution institution = institutionRepository.findById(String.valueOf(instId)).orElseThrow(() -> new BusinessException(ResponseCode.INVALID_INSTITUTION_ID, HttpStatus.BAD_REQUEST));
			String dateRange = commonService.getKeyValue(SystemCodePrefixEnum.DATE_RANGE.getValue(), SystemCodeSuffixEnum.DATE_RANGE.getValue(), institution); 
			Validations.isValidDateRange (fromDate, toDate, Integer.parseInt(dateRange));
		}
		
		Job job = jobRepository.findByInstitutionAndJobId(instId, jobExecutionLogRequestDto.getJobId()).orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_JOB, HttpStatus.NOT_FOUND));

		PageableJobExecutionLogResponseDto pageableJobExecutionLogResponseDto = new PageableJobExecutionLogResponseDto();
		Pageable pageable = PageRequest.of(jobExecutionLogRequestDto.getPaginationRequestDto().getOffset(), jobExecutionLogRequestDto.getPaginationRequestDto().getPageSize(),  Sort.by("START_DATE").descending());
		
		Page<JobExecutionLog> jobExecutionLogs = jobExecutionLogRepository.findByJob(job.getJobId(),fromDate,toDate, pageable);

        listOfJobExecutionLogResponseDTO = jobExecutionLogs.stream().map(this.jobExecutionLogMapper::toDto).collect(Collectors.toList());
		pageableJobExecutionLogResponseDto.setJobExecutionLogResponseDto(listOfJobExecutionLogResponseDTO);
		pageableJobExecutionLogResponseDto.setPaginationCommonResponseDto(this.commonService.getPaginationCommonResponseDto(jobExecutionLogRequestDto.getPaginationRequestDto(), jobExecutionLogs));
		
		Validations.isEmpty(listOfJobExecutionLogResponseDTO);
		return pageableJobExecutionLogResponseDto;
	}
}