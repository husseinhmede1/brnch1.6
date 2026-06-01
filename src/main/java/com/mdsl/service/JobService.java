/*
 * Caching was removed from this service since it is not accessed frequently
 * Moreover, changes are applied by the engine. Therefore, there is no way of clearing the cache to keep it updated.
 */

package com.mdsl.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.ChangeStatusRequestDto;
import com.mdsl.model.dto.request.JobDefinitionTaskRequestDto;
import com.mdsl.model.dto.request.JobRequestDto;
import com.mdsl.model.dto.request.JobTaskParametersRequestDto;
import com.mdsl.model.dto.request.ScheduleJobRequestDto;
import com.mdsl.model.dto.response.JobDefinitionTaskResponseDto;
import com.mdsl.model.dto.response.JobResponseDto;
import com.mdsl.model.dto.response.JobScheduledMonitoringResponseDto;
import com.mdsl.model.dto.response.JobTaskParametersResponseDto;
import com.mdsl.model.entity.BKDJobTaskParameter;
import com.mdsl.model.entity.BKDParameter;
import com.mdsl.model.entity.BKDParameterService;
import com.mdsl.model.entity.Institution;
import com.mdsl.model.entity.Job;
import com.mdsl.model.entity.JobDefinitionTask;
import com.mdsl.model.entity.JobScheduled;
import com.mdsl.model.entity.JobTask;
import com.mdsl.model.entity.JobThreadTracker;
import com.mdsl.model.entity.JobValidator;
import com.mdsl.model.entity.SystemCode;
import com.mdsl.model.entity.User;
import com.mdsl.model.mapper.BKDJobTaskParameterMapper;
import com.mdsl.model.mapper.JobMapper;
import com.mdsl.repository.BKDJobTaskParameterRepository;
import com.mdsl.repository.BKDParameterRepository;
import com.mdsl.repository.BKDParameterServiceRepository;
import com.mdsl.repository.InstitutionRepository;
import com.mdsl.repository.JobDefinitionTaskRepository;
import com.mdsl.repository.JobExecutionLogRepository;
import com.mdsl.repository.JobRepository;
import com.mdsl.repository.JobScheduledRepository;
import com.mdsl.repository.JobTaskRepository;
import com.mdsl.repository.JobThreadTrackerRepository;
import com.mdsl.repository.JobValidatorRepository;
import com.mdsl.repository.SystemCodeRepository;
import com.mdsl.repository.UserRepository;
import com.mdsl.utils.CronJob;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.Validations;
import com.mdsl.utils.enumerations.JobFrequencyEnum;
import com.mdsl.utils.enumerations.JobTaskExecutionStatusEnum;
import com.mdsl.utils.enumerations.StatusEnum;
import com.mdsl.utils.enumerations.SystemCodePrefixEnum;
import com.mdsl.utils.enumerations.SystemCodeSuffixEnum;
import com.mdsl.utils.enumerations.ThreadStatusEnum;

//import lb.com.mdsl.etl.Base24ETLEngineLauncher;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JobService {
	private final JobRepository jobRepository;
	private final JobTaskRepository jobTaskRepository;
	private final JobExecutionLogRepository jobExecutionLogRepository;
	private final JobScheduledRepository jobScheduledRepository;
	private final BKDJobTaskParameterRepository bkdJobTaskParameterRepository;
	private final BKDParameterServiceRepository bkdParameterServiceRepository;
	private final BKDParameterRepository bkdParameterRepository;
	private final UserRepository userRepository;
	private final JobValidatorRepository jobValidatorRepository;
	private final SystemCodeRepository systemCodeRepository;
	private final JobThreadTrackerRepository jobThreadTrackerRepository;
	private final JobDefinitionTaskRepository jobDefinitionTaskRepository;
	private final InstitutionRepository institutionRepository;


	private final UserService userService;
	private final CommonService commonService;
	//private final BackEndLogService backEndLogService;

	private final JobMapper jobMapper;
	private final BKDJobTaskParameterMapper jobTaskParameterMapper;

	private static final Logger logger = LoggerFactory.getLogger(JobService.class);
//	private static Base24ETLEngineLauncher base24ETLEngineLauncher = new Base24ETLEngineLauncher();

	@Bean("cronJob")
	public CronJob createCronJob() {
		return new CronJob();
	}

	/*
	 * Returns a list of all jobs from table MD_JOB_DEFINITION for a specific institution id
	 */
	public List<JobResponseDto> getAllJobs(String instId) {
	    List<Job> jobs = jobRepository.findByInstitutionOrderByJobName(instId);
	    if (jobs.isEmpty()) {
	        Validations.isEmpty(jobs);
	        return Collections.emptyList();
	    }
	    List<Integer> jobTaskIds = jobs.stream()
	            .flatMap(job -> job.getJobDefinitionTask().stream())
	            .map(JobDefinitionTask::getJobTaskId)
	            .collect(Collectors.toList());

	    List<BKDJobTaskParameter> allParams = jobTaskIds.isEmpty()
	            ? Collections.emptyList()
	            : bkdJobTaskParameterRepository.findByJobTaskIdIn(jobTaskIds);

	    Set<Integer> allParameterServiceIds = allParams.stream().map(BKDJobTaskParameter::getParametersServiceId).collect(Collectors.toSet());
	    System.out.println("allParameterServiceIds>>>>"+allParameterServiceIds.toString());
	    Map<Integer, BKDParameterService> parameterServiceMap =bkdParameterServiceRepository.findAllById(allParameterServiceIds).stream().collect(Collectors.toMap(BKDParameterService::getParametersServiceId, Function.identity()));
	    System.out.println("parameterServiceMap>>>>"+parameterServiceMap.toString());
	    Set<Integer> allParameterIds = parameterServiceMap.values().stream().map(BKDParameterService::getParameterId).collect(Collectors.toSet());
	    Map<Integer, String> parameterNameMap = bkdParameterRepository.findByParameterIdIn(allParameterIds).stream().collect(Collectors.toMap(BKDParameter::getParameterId, BKDParameter::getParameterName));
	    Map<Integer, List<BKDJobTaskParameter>> taskParamsByJobTaskId = allParams.stream().collect(Collectors.groupingBy(BKDJobTaskParameter::getJobTaskId));
	    List<JobResponseDto> allJobs = jobs.stream().map(job -> convertToJobResponseDtoForAll(job, taskParamsByJobTaskId, parameterServiceMap, parameterNameMap, false)).collect(Collectors.toList());

	    Validations.isEmpty(allJobs);
	    return allJobs;
	}

	private JobResponseDto convertToJobResponseDtoForAll(Job job,Map<Integer, List<BKDJobTaskParameter>> taskParamsByJobTaskId,Map<Integer, BKDParameterService> parameterServiceMap,Map<Integer, String> parameterNameMap,boolean convertCodeToId) {

	    JobResponseDto response = jobMapper.toDto(job);
	    if (response.getJobDefinitionTask() != null) {
	        Map<Integer, String> taskIdToServiceMode = job.getJobDefinitionTask().stream()
	                .filter(jdt -> jdt.getTask() != null && jdt.getTask().getTaskId() != null)
	                .collect(Collectors.toMap(
	                        jdt -> jdt.getTask().getTaskId(),
	                        jdt -> jdt.getTask().getService().getServiceMode(),
	                        (existing, replacement) -> existing
	                ));

	        response.getJobDefinitionTask().forEach(dto -> {
	            if (dto.getTask() != null) {
	                dto.getTask().setServiceMode(taskIdToServiceMode.get(dto.getTask().getTaskId()));
	            }
	        });
	    }

	    for (JobDefinitionTaskResponseDto jobDefinitionTask : response.getJobDefinitionTask()) {
	        List<BKDJobTaskParameter> jobTaskParameters = taskParamsByJobTaskId.getOrDefault(jobDefinitionTask.getJobTaskId(), Collections.emptyList());
	        String bin = null;
	        String cardTypes = null;
	        List<JobTaskParametersResponseDto> jobTaskParamsResponseDtos = new ArrayList<>(jobTaskParameters.size());
	        for (BKDJobTaskParameter taskParameter : jobTaskParameters) {
	            JobTaskParametersResponseDto dto = jobTaskParameterMapper.toDto(taskParameter);
	            BKDParameterService ps = parameterServiceMap.get(taskParameter.getParametersServiceId());
	            if (ps == null) throw new BusinessException(ResponseCode.CFG_INVALID_SERVICE_PARAMETER, HttpStatus.NOT_FOUND);
	            String parameterName = parameterNameMap.get(ps.getParameterId());
//	            if (ParametersEnum.BIN.getValue().equalsIgnoreCase(parameterName)) {
//	                bin = taskParameter.getParameterValue();
//	            } else if (ParametersEnum.CARD_TYPE.getValue().equalsIgnoreCase(parameterName)) {
//	                cardTypes = taskParameter.getParameterValue();
//	            }
	            dto.setParameterId(ps.getParameterId());
	            dto.setParameterName(parameterName);
	            jobTaskParamsResponseDtos.add(dto);
	        }
//	        if (convertCodeToId && bin != null && cardTypes != null) {
//	            List<String> trimmedCodes = Arrays.stream(cardTypes.split(","))
//	                    .map(String::trim)
//	                    .collect(Collectors.toList());
//	            List<CardType> cardTypesList = cardTypeRepository.findByCardTypeCodeInAndBinValue(trimmedCodes, bin.trim());
//	            Map<String, Integer> codeToIdMap = cardTypesList.stream()
//	                    .collect(Collectors.toMap(CardType::getCardTypeCode, CardType::getCardtypeId));
//	            String cardTypeIds = trimmedCodes.stream()
//	                    .map(codeToIdMap::get)
//	                    .filter(Objects::nonNull)
//	                    .map(String::valueOf)
//	                    .collect(Collectors.joining(","));
//	            if (!cardTypeIds.isEmpty()) {
//	                jobTaskParamsResponseDtos.stream()
//	                        .filter(p -> ParametersEnum.CARD_TYPE.getValue().equalsIgnoreCase(p.getParameterName()))
//	                        .findFirst()
//	                        .ifPresent(p -> p.setParameterValue(cardTypeIds));
//	            }
//	        }
	        jobDefinitionTask.setJobTaskParametersResponseDto(jobTaskParamsResponseDtos);
	    }
	    return response;
	}

	
	/*
	 * Returns a list of all active jobs (status = 1) from table MD_JOB_DEFINITION for a specific institution id
	 */
	public List<JobResponseDto> getAllActiveJobs(String instId) {
		List<JobResponseDto> allJobs= new ArrayList<JobResponseDto>();
		List<Job> jobs = jobRepository.findByInstitutionAndStatusOrderByJobName(instId, StatusEnum.ENABLED.getValue());

		jobs.forEach((job) -> {
			allJobs.add(this.convertToJobResponseDto(job,null,false));
		});
		Validations.isEmpty(allJobs);
		return allJobs;
	}

	/*
	 * Returns a job from table MD_JOB_DEFINITION based on the job id and the institution id sent in the request
	 */
	public JobResponseDto getJobById(int jobId, String instId) {
		Job job = jobRepository.findByInstitutionAndJobId(instId, jobId).orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_JOB, HttpStatus.NOT_FOUND));
		return this.convertToJobResponseDto(job,null,true);
	}

	/*
	 * Returns a the job enabled flag from table MD_JOB_DEFINITION based on the job id and the institution id sent in the request
	 */
	public String getJobExecutionStatusById(int jobId, String instId) {
		Job job = jobRepository.findByInstitutionAndJobId(instId, jobId).orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_JOB, HttpStatus.NOT_FOUND));
		return job.getEnabled();
	}

	/*
	 * Saves or created a job based on the job id in tables MD_JOB_DEFINITION and MD_JOB_DEF_TASK
	 * If job id is not available or is equal to 0, we create
	 * If job id is available we update
	 * The job name is unique per institution
	 * The transactions are logged in table MD_ADT_BKD_LOG
	 */
	public JobResponseDto saveJob(JobRequestDto jobRequestDto, String instId,  String remoteAddress) {
		String action = "";
		String description = "";
		Job saveJob;
		Job savedJob;
		List<JobDefinitionTask> listOfJobDefinitionTask = new ArrayList<>();
		UserDetailsImpl userDetails = commonService.getLoggedInUser();

		if (Objects.isNull(jobRequestDto.getJobDefinitionTask()) || jobRequestDto.getJobDefinitionTask().isEmpty()) {
			throw new BusinessException(ResponseCode.CFG_JOB_TASK_NOT_EMPTY,HttpStatus.BAD_REQUEST);
		} else {
			List<JobDefinitionTaskRequestDto> jobDefinitionTasks = jobRequestDto.getJobDefinitionTask();
			jobDefinitionTasks.forEach((task)->{
				//Some tasks do not have parameters
				if(Objects.nonNull(task.getJobTaskParamtersRequestDto())) {
					List<JobTaskParametersRequestDto> jobTaskParameters = task.getJobTaskParamtersRequestDto();
					jobTaskParameters.forEach((parameter)->{
						BKDParameterService parameterService = this.bkdParameterServiceRepository.findById(parameter.getParametersServiceId()).orElseThrow(()-> new BusinessException(ResponseCode.CFG_INVALID_SERVICE_PARAMETER,HttpStatus.NOT_FOUND));
						if(parameterService.getIsMandatory().equalsIgnoreCase(String.valueOf(StatusEnum.ENABLED.getValue())) && (Objects.isNull(parameter.parameterValue) || parameter.parameterValue.equalsIgnoreCase(""))){
							throw new BusinessException(ResponseCode.CFG_MANDATORY_PARAMETER_NO_EMPTY,HttpStatus.BAD_REQUEST);
						}
					});
				}
			});
		}

		if((Character.compare(jobRequestDto.getAlertSuccess().charAt(0), StatusEnum.ENABLED.getValue())==0) &&
				(Objects.isNull(jobRequestDto.getSuccessEmail()) || jobRequestDto.getSuccessEmail().equals(""))) {
			throw new BusinessException(ResponseCode.CFG_INVALID_EMAIL_SUCCESS,HttpStatus.BAD_REQUEST);
		}

		if((Character.compare(jobRequestDto.getAlertFailure().charAt(0), StatusEnum.ENABLED.getValue())==0) &&
				(Objects.isNull(jobRequestDto.getFailEmail()) || jobRequestDto.getFailEmail().equals(""))) {
			throw new BusinessException(ResponseCode.CFG_INVALID_EMAIL_FAILURE, HttpStatus.BAD_REQUEST);
		}

		if (Objects.isNull(jobRequestDto.getJobId()) || jobRequestDto.getJobId() == 0) {// create job definition
			if (jobRepository.existsByInstitutionAndJobNameIgnoreCase(instId, jobRequestDto.getJobName())) {
				throw new BusinessException(ResponseCode.VAL_JOB_NAME_EXISTS, HttpStatus.CONFLICT);
			}

			saveJob = jobMapper.toEntity(jobRequestDto);
			saveJob.setInstitution(instId);
			saveJob.setStatus(jobRequestDto.getStatus());
			saveJob.setEnabled(String.valueOf(StatusEnum.DISABLED.getValue()));
			saveJob.setCreatedBy(userDetails.getId());
			saveJob.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			saveJob = jobRepository.save(saveJob);

			action = "create";
			description = "Created Job: [" + jobRequestDto.getJobName() + " - " + jobRequestDto.getJobDescription() + "]";
		} else {// update
			Job job = jobRepository.findByInstitutionAndJobId(instId, jobRequestDto.getJobId()).orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_JOB, HttpStatus.NOT_FOUND));
			if (job.getEnabled().equalsIgnoreCase(String.valueOf(StatusEnum.ENABLED.getValue()))) {
				throw new BusinessException(ResponseCode.CFG_JOB_RUNNING, HttpStatus.BAD_REQUEST);
			}
			saveJob = jobMapper.toEntity(jobRequestDto);
			saveJob.setInstitution(instId);
			saveJob.setEnabled(String.valueOf(StatusEnum.DISABLED.getValue()));
			saveJob.setStatus(jobRequestDto.getStatus());
			saveJob.setLastExecId(job.getLastExecId());
			saveJob.setLastRunResult(job.getLastRunResult());
			saveJob.setFrequency(job.getFrequency());
			saveJob.setStartDate(job.getStartDate());
			saveJob.setEndDate(job.getEndDate());
			saveJob.setRecurring(job.getRecurring());
			saveJob.setRecurringFreq(job.getRecurringFreq());
			saveJob.setMonthDay(job.getMonthDay());
			saveJob.setMaxExceTime(job.getMaxExceTime());
			saveJob.setCreatedBy(job.getCreatedBy());
			saveJob.setCreatedDate(job.getCreatedDate());
			saveJob.setUpdatedBy(userDetails.getId());
			saveJob.setUpdatedDate(new Timestamp(System.currentTimeMillis()));

			//This is added for maker checker
			int jobId=saveJob.getJobId();
			saveJob = jobRepository.save(saveJob);
			saveJob.setJobId(jobId);

			bkdJobTaskParameterRepository.deleteAllByJobId(saveJob.getJobId());
			bkdJobTaskParameterRepository.flush();
			jobDefinitionTaskRepository.deleteByJob(saveJob.getJobId());
			jobDefinitionTaskRepository.flush();

			action = "update";
			description = "Update Job: [" + jobRequestDto.getJobName() + " - " + jobRequestDto.getJobDescription() + "]";
		}

		List<BKDJobTaskParameter> listOfJobTaskParameters = new ArrayList<BKDJobTaskParameter>();
		if (Objects.nonNull(jobRequestDto.getJobDefinitionTask())) {
			List<Integer> elementsOrder = new ArrayList<Integer>();
			savedJob = saveJob;

			jobRequestDto.getJobDefinitionTask().forEach(jobTask ->{
				elementsOrder.add(jobTask.getPriority());
			});

			Collections.sort(elementsOrder);

			if(!Validations.areAllUnique(elementsOrder)) {
				throw new BusinessException (ResponseCode.CFG_INVALID_PRIORITY, HttpStatus.BAD_REQUEST);
			}

			if (!Validations.allPositionsExist(elementsOrder)) {
				throw new BusinessException (ResponseCode.CFG_INVALID_PRIORITY, HttpStatus.BAD_REQUEST);
			}

			jobRequestDto.getJobDefinitionTask().forEach(task -> {
				if (Objects.nonNull(task.getTaskId())) {
					JobTask jobTask = jobTaskRepository.findById(task.getTaskId()).orElseThrow(()-> new BusinessException (ResponseCode.CFG_INVALID_TASK_ID, HttpStatus.NOT_FOUND));
					JobDefinitionTask jobDefinitonTask = new JobDefinitionTask();
					jobDefinitonTask.setJob(savedJob.getJobId());
					jobDefinitonTask.setTask(jobTask);
					jobDefinitonTask.setPriority(task.getPriority());
					jobDefinitonTask.setCreatedBy(userDetails.getId());
					jobDefinitonTask.setCreatedDate(new Timestamp(System.currentTimeMillis()));
					jobDefinitonTask = jobDefinitionTaskRepository.save(jobDefinitonTask);
					listOfJobDefinitionTask.add(jobDefinitonTask);

					if (Objects.nonNull(task.getJobTaskParamtersRequestDto())) {

						if(Objects.nonNull(jobDefinitonTask.getJobTaskId())) {
							bkdJobTaskParameterRepository.deleteAllByJobTaskId(jobDefinitonTask.getJobTaskId());
							bkdJobTaskParameterRepository.flush();
						}


						for(JobTaskParametersRequestDto parameter: task.getJobTaskParamtersRequestDto()) {
							if(Objects.nonNull(jobDefinitonTask.getJobTaskId()) && this.bkdJobTaskParameterRepository.existsByJobTaskIdAndParametersServiceId(jobDefinitonTask.getJobTaskId(), parameter.getParametersServiceId())) {
								throw new BusinessException(ResponseCode.CFG_PARAMETER_SERVICE_EXISTS_FOR_JOB_TASK,HttpStatus.CONFLICT);
							}

							BKDJobTaskParameter jobTaskParameter = this.jobTaskParameterMapper.toEntity(parameter);
							jobTaskParameter.setJobTaskId(jobDefinitonTask.getJobTaskId());
							jobTaskParameter.setParameterValue(parameter.getParameterValue());
							jobTaskParameter.setCreatedBy(userDetails.getId());
							jobTaskParameter.setCreatedDate(new Timestamp(System.currentTimeMillis()));
							listOfJobTaskParameters.add(jobTaskParameter);
							bkdJobTaskParameterRepository.save(jobTaskParameter);
						}
					}
				}
			});
		}

		saveJob.setJobDefinitionTask(listOfJobDefinitionTask);
		//Logs the activity in MD_ADT_BKD_LOG
		//backEndLogService.logBackEndActivity(userService.getEntityUserById(userDetails.getId()), LoggingCategoriesEnum.JOBS.getValue(), ("Job-" + action), description, remoteAddress, jobRequestDto.toString(), instId, branchId);

		return this.convertToJobResponseDto(saveJob,listOfJobTaskParameters,false);
	}

	/*
	 * Enables/disables a job by changing the status
	 * Logs the transaction in table MD_ADT_BKD_LOG
	 */
	public void enableDisableJob(ChangeStatusRequestDto changeJobStatusRequestDto, String remoteAddress, String instId) {

		UserDetailsImpl userDetails = commonService.getLoggedInUser();
		Job job = jobRepository.findByInstitutionAndJobId(instId, changeJobStatusRequestDto.getId()).orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_JOB, HttpStatus.NOT_FOUND));

		if (job.getEnabled().equalsIgnoreCase(String.valueOf(StatusEnum.ENABLED.getValue()))) {
			throw new BusinessException(ResponseCode.CFG_JOB_RUNNING, HttpStatus.BAD_REQUEST);
		}

		if (changeJobStatusRequestDto.getStatus().equalsIgnoreCase(String.valueOf(job.getStatus()))) {
			throw new BusinessException(ResponseCode.CFG_STATUS_NOT_CHANGED, HttpStatus.CONFLICT);
		}

		jobRepository.updateJobStatus(changeJobStatusRequestDto.getId(), changeJobStatusRequestDto.getStatus().charAt(0), instId, userDetails.getId());

		if (job.getEnabled().equalsIgnoreCase(String.valueOf(StatusEnum.ENABLED.getValue()))) {
			job.setEnabled(String.valueOf(StatusEnum.DISABLED.getValue()));
		}

		//Case of disable - delete all future scheduled jobs
		if(changeJobStatusRequestDto.getStatus().equalsIgnoreCase(String.valueOf(StatusEnum.DISABLED.getValue()))) {
			this.deleteFutureScheduledByJobId(job.getJobId());
		}

		if (Objects.nonNull(job.getFrequency())) {
			this.processJobSchedule(job, false);
		}

		//Logs the activity in MD_ADT_BKD_LOG
		//backEndLogService.logBackEndActivity(userService.getEntityUserById(userDetails.getId()), LoggingCategoriesEnum.JOBS.getValue(), ("Job-status-change"), "Status changed for job: [" + job.getJobName() + " - " + job.getJobDescription() + "] to " + (changeJobStatusRequestDto.getStatus().equalsIgnoreCase("0")? "Disabled": "Enabled"), remoteAddress, changeJobStatusRequestDto.toString(), instId, branchId);
	}

	/*
	 * Deletes a job from tables MD_JOB_DEFINITION, MD_JOB_DEF_TASK, MD_JOB_SCHEDULED_EXEC  based on the job id
	 * If records are available in MD_JOB_EXECUTION_LOG or MD_JOB_UNDER_EXEC, job cannot be deleted
	 * Logs the transaction in table MD_ADT_BKD_LOG
	 */
	public void deleteJob(int jobId, String instId, String remoteAddress) {

		UserDetailsImpl userDetails = commonService.getLoggedInUser();
		Job job = jobRepository.findByInstitutionAndJobId(instId, jobId).orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_JOB, HttpStatus.NOT_FOUND));

		if (job.getEnabled().equalsIgnoreCase(String.valueOf(StatusEnum.ENABLED.getValue()))) {
			throw new BusinessException(ResponseCode.CFG_JOB_RUNNING, HttpStatus.BAD_REQUEST);
		}

		if (jobExecutionLogRepository.existsByJob(job)) {
			throw new BusinessException (ResponseCode.VAL_CANNOT_DELETE_JOB, HttpStatus.NOT_ACCEPTABLE);
		}

		try {
			jobScheduledRepository.deleteByJobId(jobId);

			job.getJobDefinitionTask().forEach( (jobDefinitionTask) -> {
				bkdJobTaskParameterRepository.deleteAllByJobTaskId(jobDefinitionTask.getJobTaskId());
			});
			jobDefinitionTaskRepository.deleteByJob(job.getJobId());
			jobRepository.deleteById(jobId);
		} catch (Exception e) {
			logger.error("@JobService#deleteJob: " + e.getMessage());
			throw new BusinessException (ResponseCode.VAL_JOB_EXECUTING_CANNOT_DELETE, HttpStatus.NOT_ACCEPTABLE);
		}

		//Logs the activity in MD_ADT_BKD_LOG
		//backEndLogService.logBackEndActivity(userService.getEntityUserById(userDetails.getId()), LoggingCategoriesEnum.JOBS.getValue(), ("Job-delete"), "Deleted Job: [" + job.getJobName() + " - " + job.getJobDescription() + "]", remoteAddress, "", instId, branchId);
	}

	/*
	 * Schedules an existing job based on the job id by updating the corresponding data in table MD_JOB_DEFINITION
	 * Inserts the corresponding records in table MD_JOB_SCHEDULED_EXEC
	 * * * For Once: inserts one record
	 * * * For Daily: inserts records for one month ahead
	 * * * For monthly: inserts records for one month ahead
	 * The transaction is logged in table MD_ADT_BKD_LOG
	 */
	public JobResponseDto scheduleJob (ScheduleJobRequestDto scheduleJobRequestDto, String instId, String remoteAddress) {
		UserDetailsImpl userDetails = commonService.getLoggedInUser();

		Job job = jobRepository.findByInstitutionAndJobId(instId, scheduleJobRequestDto.getJobId()).orElseThrow(()-> new BusinessException(ResponseCode.CFG_INVALID_JOB, HttpStatus.NOT_FOUND));
		Job cloneJob = this.jobMapper.clone(job);	//This is for maker checker

		if(job.getStatus().equalsIgnoreCase(String.valueOf(StatusEnum.DISABLED.getValue()))) {
			throw new BusinessException(ResponseCode.VAL_JOB_DISABLED,HttpStatus.CONFLICT);
		}
		cloneJob.setFrequency(Integer.parseInt(scheduleJobRequestDto.getFrequency()));

		//Date Format validations
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date parsedStartDateTime = null;
		Date parsedEndDateTime = null;
		String startDateTime = "";
		if (Objects.isNull(scheduleJobRequestDto.getStartDateTime()) || scheduleJobRequestDto.getStartDateTime().equals("")) {
			startDateTime = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		} else {
			startDateTime = scheduleJobRequestDto.getStartDateTime();
		}

		String endDateTime = scheduleJobRequestDto.getEndDateTime() == null ? "" : scheduleJobRequestDto.getEndDateTime().trim();

		Validations.dateFormatValidation(Integer.parseInt(startDateTime.substring(8,10)), Integer.parseInt(startDateTime.substring(5,7)), Integer.parseInt(startDateTime.substring(0,4)));

		if (!endDateTime.equalsIgnoreCase("")) {
			Validations.dateFormatValidation(Integer.parseInt(endDateTime.substring(8,10)), Integer.parseInt(endDateTime.substring(5,7)), Integer.parseInt(endDateTime.substring(0,4)));
		}

		try {
			parsedStartDateTime = sdf1.parse(startDateTime);
			if (!endDateTime.equalsIgnoreCase("")) {
				parsedEndDateTime = sdf1.parse(endDateTime);
				Validations.dateValidationsScheduled(new java.sql.Date(parsedStartDateTime.getTime()), new java.sql.Date(parsedEndDateTime.getTime()));
			}
			if (parsedStartDateTime != null && !new Timestamp(parsedStartDateTime.getTime()).equals(job.getStartDate())) {
				Validations.fromDateSysdateValidation(startDateTime.substring(0,10));
			}
			if (!endDateTime.equalsIgnoreCase("")) {
				Validations.fromDateSysdateValidation(endDateTime.substring(0,10));
			}
		} catch (BusinessException e) {
			logger.error("@JobService#scheduleJob: " + e.getMessage());
			throw new BusinessException (e.getMessage(), e.getHttpStatus());
		} catch (Exception e) {
			logger.error("@JobService#scheduleJob: " + e.getMessage());
			throw new BusinessException(ResponseCode.CFG_INVALID_DATE, HttpStatus.BAD_REQUEST);
		}
		//End Date Format validations

		//Set the start and end date
		cloneJob.setStartDate(new Timestamp(parsedStartDateTime.getTime()));

		if (!endDateTime.equalsIgnoreCase("")) {
			cloneJob.setEndDate(new Timestamp(parsedEndDateTime.getTime()));
		} else { //set the end date one month from the system date in case of daily and monthly frequencies
			cloneJob.setEndDate(null);
		}
		//End Set the start and end date
		cloneJob.setRecurring(Objects.isNull(scheduleJobRequestDto.getRepeatTaskFlag())? "0" : scheduleJobRequestDto.getRepeatTaskFlag().equalsIgnoreCase("") ? "0" : scheduleJobRequestDto.getRepeatTaskFlag());

		//Calculate the recurring frequency in seconds
		Integer recurringFrequency = null;
		if (scheduleJobRequestDto.getRepeatTaskFlag().trim().equalsIgnoreCase(String.valueOf(StatusEnum.ENABLED.getValue()))
				&& (Objects.nonNull(scheduleJobRequestDto.getRepeatTaskHour()) || Objects.nonNull(scheduleJobRequestDto.getRepeatTaskMinute()))) {
			recurringFrequency = scheduleJobRequestDto.getRepeatTaskHour();
		}
		cloneJob.setRecurringFreq(recurringFrequency);

		cloneJob.setMonthDay(Objects.nonNull(scheduleJobRequestDto.getMonthDay()) ? scheduleJobRequestDto.getMonthDay() : null);

		//Calculate the stop time in minutes
		if (Objects.nonNull(scheduleJobRequestDto.getStopTaskFlag())
				&& scheduleJobRequestDto.getStopTaskFlag().equalsIgnoreCase("1")
				&& (Objects.nonNull(scheduleJobRequestDto.getStopTaskHour()) || Objects.nonNull(scheduleJobRequestDto.getStopTaskMinute()))) {
			Integer stopTime = (scheduleJobRequestDto.getStopTaskHour() * 60) + scheduleJobRequestDto.getStopTaskMinute();
			cloneJob.setMaxExceTime(stopTime);
		}

		cloneJob.setUpdatedBy(userDetails.getId());
		cloneJob.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
		jobRepository.save(cloneJob);
		try {
			this.processJobSchedule(cloneJob, false);
		} catch (BusinessException e) {
			logger.error(e.getMessage());
			throw new BusinessException (e.getMessage(), e.getHttpStatus());
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new BusinessException(ResponseCode.AN_ERROR_OCCURRED, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		//Logs the activity in MD_ADT_BKD_LOG
		int frequency = Integer.parseInt(scheduleJobRequestDto.getFrequency());
		DateTimeFormatter inputFormatter =DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		DateTimeFormatter outputFormatter =DateTimeFormatter.ofPattern("dd-MM-yyyy h:mm a");
		String formattedStartDate="";
		String formattedEndDate="";
		if(scheduleJobRequestDto.getStartDateTime()!=null && !scheduleJobRequestDto.getStartDateTime().equalsIgnoreCase("")) {
			LocalDateTime dateTime = LocalDateTime.parse(scheduleJobRequestDto.getStartDateTime(), inputFormatter);
			formattedStartDate = dateTime.format(outputFormatter);
		}
		if(scheduleJobRequestDto.getEndDateTime()!=null && !scheduleJobRequestDto.getEndDateTime().equalsIgnoreCase("")) {
			LocalDateTime dateTime = LocalDateTime.parse(scheduleJobRequestDto.getEndDateTime(), inputFormatter);
			formattedEndDate = dateTime.format(outputFormatter);
		}
		StringBuilder description = new StringBuilder();
		description.append("Schedule Job [").append(job.getJobName()).append("] with the following [");
		switch (frequency) {
		    case 0:
		        description.append("One time run with a Start Time Date of: ").append(formattedStartDate);
		        break;
		    case 1:
		        description.append("Daily run time with a Start Time Date of: ").append(formattedStartDate);
		        Integer repeatHours = scheduleJobRequestDto.getRepeatTaskHour();
		        if (repeatHours != null && repeatHours > 0) {
		            description.append(" with a repeat time interval of: ").append(repeatHours).append(" Hours");
		        }
		        String dailyEndDate = scheduleJobRequestDto.getEndDateTime();
		        if (dailyEndDate != null && !dailyEndDate.isBlank()) {
		            description.append(" and an Expiry Date of: ").append(formattedEndDate);
		        }
		        break;
		    case 2:
		        description.append("Monthly run time with a Start Time Date of: ").append(formattedStartDate).append(" with a rerun on the ").append(scheduleJobRequestDto.getMonthDay()).append(" of every month");
		        String monthlyEndDate = scheduleJobRequestDto.getEndDateTime();
		        if (monthlyEndDate != null && !monthlyEndDate.isBlank()) {
		            description.append(" and an Expiry Date of: ").append(formattedEndDate);
		        }
		        break;
		    case 3:
		        description.append("Continuous run time");
		        break;
		    default:
		        description.append("Unknown scheduling configuration");
		        break;
		}
		description.append("]");
		String scheduledDescription = description.toString();
		//backEndLogService.logBackEndActivity(userService.getEntityUserById(userDetails.getId()), LoggingCategoriesEnum.JOBS.getValue(), ("Job-Scheduling"), scheduledDescription, remoteAddress, scheduleJobRequestDto.toString(), instId, branchId);

		return this.convertToJobResponseDto(cloneJob,null,false);
	}

	/*
	 * Generates records based on the frequency and saves them in table MD_JOB_SCHEDULED_EXEC
	 */
	public boolean processJobSchedule(Job job, boolean isNew) {

		if (String.valueOf(job.getStatus()).equals("0")) {
			return this.deleteFutureScheduledByJobId(job.getJobId());
		}

		List<JobScheduled> saveJobSchedule = new ArrayList<JobScheduled>();

		if (job.getFrequency().equals(JobFrequencyEnum.ONE_TIME.getValue())) {
			saveJobSchedule = this.prepareOneTimeFrequency(job);
		} else if (job.getFrequency().equals(JobFrequencyEnum.DAILY.getValue())) {
			saveJobSchedule = this.prepareDailyFrequency(job);
		} else if (job.getFrequency().equals(JobFrequencyEnum.MONTHLY.getValue())) {
			saveJobSchedule = this.prepareMonthlyFrequency(job);
		} else if (job.getFrequency().equals(JobFrequencyEnum.CONTINUOUS.getValue())) {
			return true;
		} else {
			throw new BusinessException (ResponseCode.CFG_INVALID_FREQUENCY, HttpStatus.NOT_FOUND);
		}

		if (saveJobSchedule != null && !saveJobSchedule.isEmpty()) {
			if (!isNew) {
				this.deleteFutureScheduledByJobId(job.getJobId());
			}
			saveJobSchedule.forEach(record->{
				jobScheduledRepository.save(record);
			});

			return true;
		}

		return false;
	}

	/*
	 * Deletes records from table MD_JOB_SCHEDULED_EXEC based on job id
	 */
	public boolean deleteFutureScheduledByJobId(final Integer jobId) {
		jobScheduledRepository.deleteFutureScheduledByJobId(jobId);
		return true;
	}

	/*
	 * Generates a record for table MD_JOB_SCHEDULED_EXEC in case where frequency is set to "One Time"
	 */
	private List<JobScheduled> prepareOneTimeFrequency(final Job jobDefinition) {
		List<JobScheduled> jobScheduledList = new ArrayList<>();
		JobScheduled jobScheduled = new JobScheduled(null, jobDefinition, jobDefinition.getStartDate(), StatusEnum.DISABLED.getValue(), new Timestamp(System.currentTimeMillis()), jobDefinition.getCreatedBy(), null, null);
		jobScheduledList.add(jobScheduled);
		return jobScheduledList ;
	}

	/*
	 * Generates records for table MD_JOB_SCHEDULED_EXEC  in case where frequency is set to "Daily"
	 * If the recurring is 0, there is only one record per day for a period of one month
	 * If the recurring is 1, we check the recurring frequency (in minutes) and insert a record for each frequency for a period of one month
	 */
	private List<JobScheduled> prepareDailyFrequency(final Job jobDefinition) {
		List<JobScheduled> jobScheduledList = new ArrayList<>();
		Date today = new Date();
		Calendar iterateDate = getCalendar(jobDefinition.getStartDate(), null, null);
		Institution institution = institutionRepository.findById(jobDefinition.getInstitution().trim()).orElseThrow(() -> new BusinessException(ResponseCode.INVALID_INSTITUTION_ID, HttpStatus.BAD_REQUEST));
		Calendar endDate = getCalendar(jobDefinition.getStartDate(), null, null);
		endDate.add(Calendar.DAY_OF_MONTH, Integer.valueOf(commonService.getKeyValue(SystemCodePrefixEnum.JOB.getValue(), SystemCodeSuffixEnum.JOB_MAX_SCHEDULE.getValue(), institution)));
		//Set end time to 12 am
		endDate.set(Calendar.HOUR_OF_DAY, 0);
		endDate.set(Calendar.MINUTE, 0);
		endDate.set(Calendar.SECOND, 0);
		endDate.set(Calendar.MILLISECOND, 0);

		Date nextMonth = endDate.getTime();

		if (Objects.nonNull(jobDefinition.getEndDate()) && !jobDefinition.getEndDate().after(new Timestamp(nextMonth.getTime()))) {
			nextMonth = jobDefinition.getEndDate();
			//Set end time to 12 am if end date is greater by 1 day
			if(isEndDateNextDay(jobDefinition.getStartDate(),jobDefinition.getEndDate())) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(nextMonth);
				calendar.set(Calendar.HOUR_OF_DAY, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				nextMonth = calendar.getTime();
			}
		}
		Integer frequency = null;
		if (Objects.nonNull(jobDefinition.getRecurring()) && jobDefinition.getRecurring().equalsIgnoreCase(String.valueOf(StatusEnum.ENABLED.getValue()))
				&& Objects.nonNull(jobDefinition.getRecurringFreq()) && jobDefinition.getRecurringFreq() != 0) {
			frequency = jobDefinition.getRecurringFreq();
		} else {
			frequency = 24;
		}

		while (nextMonth.after(today) && iterateDate.getTime().before(nextMonth)) {
			JobScheduled jobScheduled = new JobScheduled(null, jobDefinition, new Timestamp(iterateDate.getTimeInMillis()), StatusEnum.DISABLED.getValue(),
					new Timestamp(System.currentTimeMillis()), jobDefinition.getCreatedBy(), null, null);
			jobScheduledList.add(jobScheduled);
			iterateDate.add(Calendar.HOUR, frequency);
			iterateDate.set(Calendar.MILLISECOND, 0);
			endDate.set(Calendar.MILLISECOND, 0);
		}
		return jobScheduledList;
	}

	/*
	 * Generates records for table MD_JOB_SCHEDULES_EXEC in case where frequency is set to "Monthly"
	 * The Month Day is the day of the month in which the record should be executed
	 */
	private List<JobScheduled> prepareMonthlyFrequency(final Job jobDefinition) {
		List<JobScheduled> jobScheduledList = new ArrayList<>();
		Date today = new Date();
		Calendar iterateDate = getCalendar(jobDefinition.getStartDate(), null, null);
		Institution institution = institutionRepository.findById(jobDefinition.getInstitution().trim()).orElseThrow(() -> new BusinessException(ResponseCode.INVALID_INSTITUTION_ID, HttpStatus.BAD_REQUEST));

		if (Objects.nonNull(jobDefinition.getMonthDay()) && jobDefinition.getMonthDay() != 0) {
			iterateDate = getCalendar(jobDefinition.getStartDate(), jobDefinition.getMonthDay(), null);
		}

		Calendar endDate = getCalendar(jobDefinition.getStartDate(), null, null);
		endDate.add(Calendar.MONTH, Integer.valueOf(commonService.getKeyValue(SystemCodePrefixEnum.JOB.getValue(), SystemCodeSuffixEnum.JOB_MAX_SCHEDULE_MONTH.getValue(), institution)));
		Date nextMonth = endDate.getTime();

		if (Objects.nonNull(jobDefinition.getEndDate()) && jobDefinition.getEndDate().before(new Timestamp(nextMonth.getTime()))) {
			nextMonth = jobDefinition.getEndDate();
		}

		while (nextMonth.after(today) && iterateDate.getTime().before(nextMonth)) {
			JobScheduled jobScheduled = new JobScheduled(null, jobDefinition, new Timestamp(iterateDate.getTimeInMillis()), StatusEnum.DISABLED.getValue(),
					new Timestamp(System.currentTimeMillis()), jobDefinition.getCreatedBy(), null, null);
			jobScheduledList.add(jobScheduled);
			iterateDate.add(Calendar.MONTH, 1);
		}
		return jobScheduledList;
	}

	/*
	 * Returns a calendar object having the first date set as per the configured params
	 */
	private Calendar getCalendar(Date date, Integer dayOfMonth, Integer month) {
		Calendar calendar = Calendar.getInstance();
		Calendar startDate = Calendar.getInstance();
		if (date != null) {
			calendar.setTime(date);
			startDate.setTime(date);
		}

		if (dayOfMonth != null) {
			calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			if (calendar.before(startDate)) {
				calendar.add(Calendar.MONTH, 1);
			}
		}

		if (month != null) {
			calendar.set(Calendar.MONTH, month);
		}

		return calendar;
	}

	/*
	 * Start the Job Execution based on the job id
	 * Runs the next available job from the queue in table MD_JOB_SCHEDULED_EXEC
	 */
	public void startJobExecution(int jobId, String instId, String remoteAddress, Locale locale) {
		UserDetailsImpl userDetails = commonService.getLoggedInUser();
		Job job = jobRepository.findByInstitutionAndJobId(instId, jobId).orElseThrow(()-> new BusinessException(ResponseCode.CFG_INVALID_JOB, HttpStatus.NOT_FOUND));

		if(job.getStatus().equalsIgnoreCase(String.valueOf(StatusEnum.DISABLED.getValue()))) {
			throw new BusinessException(ResponseCode.VAL_JOB_DISABLED,HttpStatus.CONFLICT);
		}

		if (String.valueOf(job.getEnabled()).equalsIgnoreCase(String.valueOf(StatusEnum.ENABLED.getValue()))) {
			throw new BusinessException(ResponseCode.CFG_JOB_RUNNING, HttpStatus.BAD_REQUEST);
		}

		runJob(job, userDetails.getId(), instId, null);

//		backEndLogService.logBackEndActivity(userService.getEntityUserById(userDetails.getId()),
//				LoggingCategoriesEnum.JOBS.getValue(), ("Job-Run"),
//				"Job Name: "+job.getJobName() + ": job Id: " + jobId, remoteAddress,
//				job.getJobName() + ": job Id: " + jobId, instId, branchId);
	}

	/*
	 * PS THIS NEEDS TO BE ENHANCED. JOB SHOULD BE STOPPED FROM ENGINE AS WELL AS FROM APPLICATION
	 * Stops the Job Execution based on the job id
	 */
	@Transactional
	public void stopJobExecution(int jobId, String instId, String remoteAddress) {
		UserDetailsImpl userDetails = commonService.getLoggedInUser();

		Job job = jobRepository.findByInstitutionAndJobId(instId, jobId)
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_JOB, HttpStatus.NOT_FOUND));
		Institution institution = institutionRepository.findById(instId.trim()).orElseThrow(() -> new BusinessException(ResponseCode.INVALID_INSTITUTION_ID, HttpStatus.BAD_REQUEST));

		// 1. Get all running threads from DB
		List<JobThreadTracker> runningThreads = jobThreadTrackerRepository.getAllRunningThreads(jobId,ThreadStatusEnum.RUNNING.getValue());

		// 2. Try to interrupt each thread by its stored threadId
		for (JobThreadTracker tracker : runningThreads) {
			long threadId = tracker.getThreadId();

			Thread targetThread = findThreadById(threadId);
			if (targetThread!=null && targetThread.isAlive()) {
				targetThread.interrupt();
				logger.info("Thread with ID " + threadId + " was interrupted.");
			} else {
				logger.warn("Thread with ID " + threadId + " not found or already terminated.");
			}

			// tracker.setStatus(ThreadStatusEnum.	TERMINATED_BY_FORCE.getValue());
			// tracker.setUpdatedBy(userDetails.getId());
			// tracker.setUpdatedDate(new Date());
			jobThreadTrackerRepository.delete(tracker);
		}

		// 4. Mark job as disabled
		SystemCode execMessage = systemCodeRepository .findByCodePrefixAndCodeSuffixAndInstitution(SystemCodePrefixEnum.JOB.getValue(), SystemCodeSuffixEnum.JOB_CANCEL_EXEC_DETAILS.getValue(), institution).orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_GLOBALPROPERTY, HttpStatus.NOT_FOUND));
		jobRepository.updateJobEnabledFlag(job.getJobId(), StatusEnum.DISABLED.getValue(), instId, userDetails.getId());

		jobExecutionLogRepository.updateJobEndDateAndDetailsAndStatusByJobIdAndStatus(execMessage.getCodeValue(),JobTaskExecutionStatusEnum.FORCE_STOP.getValue(), job.getJobId());
	}

	public static Thread findThreadById(long threadId) {
		for (Thread t : Thread.getAllStackTraces().keySet()) {
			if (t.getId() == threadId) return t;
		}
		return null;
	}


	/*
	 * This function runs once a day at midnight
	 * It gathers the list of jobs to be scheduled and prepare them to be ran
	 */
	//@Scheduled(cron = "0 0/10 * * * *")
	public void createCronList () {
		long millis=System.currentTimeMillis();
		java.sql.Date date = new java.sql.Date(millis);

		List<JobScheduled> scheduledJobs = jobScheduledRepository.findAllJobScheduledForNextDay(StatusEnum.DISABLED.getValue(), date, date);
		for (JobScheduled scheduledJob : scheduledJobs) {
			Timestamp startDate = scheduledJob.getStartDate();
			LocalDateTime dateTime = startDate.toLocalDateTime();
			String dayOfMonth = "";
			if (Objects.nonNull(scheduledJob.getJob().getMonthDay()) && scheduledJob.getJob().getMonthDay() != 0) {
				dayOfMonth = String.valueOf(scheduledJob.getJob().getMonthDay());
			} else {
				dayOfMonth = String.valueOf(dateTime.getDayOfMonth());
			}
			String cronString = CronJob.toCron(String.valueOf(dateTime.getSecond()), String.valueOf(dateTime.getMinute()), String.valueOf(dateTime.getHour()), dayOfMonth, String.valueOf(dateTime.getMonth().getValue()));

			try {
				if (jobValidatorRepository.existsById(scheduledJob.getJob().getJobId()+"-"+cronString+"-"+scheduledJob.getScheduleExecId())) {
					logger.debug("inside eixsts for crong " + scheduledJob.getJob().getJobId()+"-"+cronString+"-"+scheduledJob.getScheduleExecId());
					continue;
				} else {
					JobValidator jobValidator = new JobValidator();
					jobValidator.setConString(scheduledJob.getJob().getJobId()+"-"+cronString+"-"+scheduledJob.getScheduleExecId());
					logger.debug("Crong String unique " + scheduledJob.getJob().getJobId()+"-"+cronString+"-"+scheduledJob.getScheduleExecId());

					jobValidatorRepository.save(jobValidator);
					logger.debug("After Save" );
				}
			} catch (Exception e) {
				logger.debug("Error inserting the cron job in the table ");
				continue;
			}

			try {
				JobDataMap data = new JobDataMap();
				data.put("job", scheduledJob.getJob());
				data.put("cronString", cronString);
				data.put("scheduledJob", scheduledJob.getScheduleExecId());

				JobDetail jobs = JobBuilder.newJob(com.mdsl.utils.CronJobJob.class).usingJobData(data).build();
				Trigger trigger = TriggerBuilder.newTrigger().withSchedule(CronScheduleBuilder.cronSchedule(cronString)).build();

				SchedulerFactory schedulerFactory = new StdSchedulerFactory();
				Scheduler scheduler = schedulerFactory.getScheduler();
				scheduler.start();
				scheduler.scheduleJob(jobs, trigger);
			} catch (Exception e) {
				logger.error("@JobService#createCronList: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	/*
	 * Generates the arguments needed for the ETL engine
	 */
	private static String[] getEtlEngineArguments (Integer scheduledJob, JobDefinitionTask jobTask, String instId, int userId) {
		String mode = jobTask.getTask().getService().getServiceMode();
		String serviceId = jobTask.getTask().getService().getServiceId().toString();
		String institution = String.valueOf(instId);
		String scheduleExecId = Objects.nonNull(scheduledJob)?scheduledJob.toString():"";
		String jobIdArg = jobTask.getJob().toString();
		String userIdArg = String.valueOf(userId);

		String [] args = new String[6];
		args[0] = mode;
		args[1] = serviceId;
		args[2] = institution;
		args[3] = scheduleExecId;
		args[4] = jobIdArg;
		args[5] = userIdArg;
		return args;
	}

	/*
	 * Returns the list of scheduled jobs
	 */
	public List<JobScheduledMonitoringResponseDto> getJobMonitoring(String instId) {
		List<JobScheduledMonitoringResponseDto> jobMonitoringList =	jobRepository.getJobMonitoring(instId);
		Validations.isEmpty(jobMonitoringList);
		return jobMonitoringList;
	}

	/*
	 * Converts Job to Job Response Dto by setting the Job Task Parameters
	 * The list of parameters is sent as variable to cater for maker checker
	 * This function was adjusted by addign param "convertCodeToId" to return the ID of the card types instead of the code in case of get by id to adjust the filtering on the front end
	 */
	public JobResponseDto convertToJobResponseDto(Job job,List<BKDJobTaskParameter> listOfJobTaskParameters,Boolean convertCodeToId) {
	    JobResponseDto response = this.jobMapper.toDto(job);
	    if (Objects.nonNull(response.getJobDefinitionTask())) {
	        Map<Integer, String> taskIdToServiceMode =
	                job.getJobDefinitionTask().stream()
	                        .filter(jdt -> Objects.nonNull(jdt.getTask())
	                                && Objects.nonNull(jdt.getTask().getTaskId())
	                                && Objects.nonNull(jdt.getTask().getService()))
	                        .collect(Collectors.toMap(
	                                jdt -> jdt.getTask().getTaskId(),
	                                jdt -> jdt.getTask().getService().getServiceMode(),
	                                (existing, replacement) -> existing
	                        ));
	        response.getJobDefinitionTask().forEach(dto -> {
	            if (Objects.nonNull(dto.getTask())) {
	                String serviceMode =
	                        taskIdToServiceMode.get(dto.getTask().getTaskId());
	                if (Objects.nonNull(serviceMode)) {
	                    dto.getTask().setServiceMode(serviceMode);
	                }
	            }
	        });
	    }
	    Set<Integer> allParameterServiceIds = new HashSet<>();
	    final Map<Integer, List<BKDJobTaskParameter>> taskParamsByJobTaskId;

	    if (Objects.isNull(listOfJobTaskParameters)) {
	        List<Integer> jobTaskIds = response.getJobDefinitionTask().stream()
	                .map(JobDefinitionTaskResponseDto::getJobTaskId)
	                .collect(Collectors.toList());
	        List<BKDJobTaskParameter> allParams =this.bkdJobTaskParameterRepository.findByJobTaskIdIn(jobTaskIds);
	        taskParamsByJobTaskId =allParams.stream().collect(Collectors.groupingBy(BKDJobTaskParameter::getJobTaskId));
	        allParams.forEach(p ->allParameterServiceIds.add(p.getParametersServiceId()));
	    } else {
	        listOfJobTaskParameters.forEach(p ->
	                allParameterServiceIds.add(p.getParametersServiceId()));
	        taskParamsByJobTaskId =Collections.singletonMap(null, listOfJobTaskParameters);
	    }
	    Map<Integer, BKDParameterService> parameterServiceMap =this.bkdParameterServiceRepository.findAllById(allParameterServiceIds).stream().collect(Collectors.toMap(BKDParameterService::getParametersServiceId,Function.identity()));
	    Set<Integer> allParameterIds = parameterServiceMap.values().stream().map(BKDParameterService::getParameterId).collect(Collectors.toSet());
	    Map<Integer, String> parameterNameMap =bkdParameterRepository.findByParameterIdIn(allParameterIds).stream().collect(Collectors.toMap(BKDParameter::getParameterId,BKDParameter::getParameterName));
	    response.getJobDefinitionTask().forEach(jobDefinitionTask -> {
	        List<BKDJobTaskParameter> jobTaskParameters =
	                Objects.isNull(listOfJobTaskParameters)
	                        ? taskParamsByJobTaskId.getOrDefault(
	                                jobDefinitionTask.getJobTaskId(),
	                                Collections.emptyList())
	                        : listOfJobTaskParameters;

	        String bin = null;
	        String cardTypes = null;
	        List<JobTaskParametersResponseDto> jobTaskParamsResponseDtos =new ArrayList<>(jobTaskParameters.size());

	        for (BKDJobTaskParameter taskParameter : jobTaskParameters) {
	            JobTaskParametersResponseDto dto =this.jobTaskParameterMapper.toDto(taskParameter);
	            BKDParameterService ps =parameterServiceMap.get(taskParameter.getParametersServiceId());
	            if (Objects.isNull(ps)) {
	                throw new BusinessException(ResponseCode.CFG_INVALID_SERVICE_PARAMETER,HttpStatus.NOT_FOUND);
	            }
	            String parameterName =parameterNameMap.get(ps.getParameterId());
//	            if (ParametersEnum.BIN.getValue().equalsIgnoreCase(parameterName)) {
//	                bin = taskParameter.getParameterValue();
//	            } else if (ParametersEnum.CARD_TYPE.getValue().equalsIgnoreCase(parameterName)) {
//	                cardTypes = taskParameter.getParameterValue();
//	            }
	            dto.setParameterId(ps.getParameterId());
	            dto.setParameterName(parameterName);
	            jobTaskParamsResponseDtos.add(dto);
	        }

//	        if (convertCodeToId && Objects.nonNull(bin) && Objects.nonNull(cardTypes)) {
//	            List<String> trimmedCodes =Arrays.stream(cardTypes.split(",")).map(String::trim).collect(Collectors.toList());
//	            List<CardType> cardTypesList =cardTypeRepository.findByCardTypeCodeInAndBinValue(trimmedCodes, bin.trim());
//	            Map<String, Integer> codeToIdMap =cardTypesList.stream().collect(Collectors.toMap(CardType::getCardTypeCode,CardType::getCardtypeId));
//	            String cardTypeIds =trimmedCodes.stream()
//	                            .map(codeToIdMap::get)
//	                            .filter(Objects::nonNull)
//	                            .map(String::valueOf)
//	                            .collect(Collectors.joining(","));
//
//	            if (!cardTypeIds.isEmpty()) {
//	                jobTaskParamsResponseDtos.stream()
//	                        .filter(p ->ParametersEnum.CARD_TYPE.getValue().equalsIgnoreCase(p.getParameterName()))
//	                        .findFirst()
//	                        .ifPresent(p ->p.setParameterValue(cardTypeIds));}
//	        }
	        jobDefinitionTask.setJobTaskParametersResponseDto(jobTaskParamsResponseDtos);
	    });
	    return response;
	}




	//create a query to check the jobs that have the frequency continuous
	//@Scheduled(fixedRate = 5000)
	//@Scheduled(fixedDelay = 5000) //This ensures that there is a delay of 5 seconds after the task completes before it starts again, preventing overlapping tasks. (chatgpt)
	public void getContinuousJobs() {
		List<Job> jobs = jobRepository.findByStatusAndFrequency(String.valueOf(StatusEnum.ENABLED.getValue()), JobFrequencyEnum.CONTINUOUS.getValue());
		logger.debug("after fetching the jobs");
		jobs.forEach(job ->{
			logger.debug("Reaching inside the loop " + job.getJobDescription());
			if (String.valueOf(job.getEnabled()).equalsIgnoreCase(String.valueOf(StatusEnum.DISABLED.getValue()))) {
				logger.debug("Reaching inside the function for job " + job.getJobDescription());
				this.scheduleJobContinuously(job);
			}
		});
	}

	public void runJob(Job job, int userId, String instId, Integer scheduledJob) {
		Thread thread = new Thread(() -> {
			Map<String,Character> threadErrorMap= new HashMap<String,Character>();

			try {
				jobRepository.updateJobEnabledFlag(job.getJobId(), '1', instId, userId);
				for (JobDefinitionTask jobTask : job.getJobDefinitionTask()) {
					String startDate = jobExecutionLogRepository.checkTaskIfRunning(jobTask.getJobTaskId());
					if (startDate != null) {
						logger.info("Task already running");
						break;
					}
					String[] args = getEtlEngineArguments(scheduledJob, jobTask, instId, userId);
//					int resultCode = base24ETLEngineLauncher.mainWithResult(args);
//					if (resultCode != 0) {
//						//jobRepository.setJobDone(job.getJobId(), '4', instId, userId);
//						threadErrorMap.put(Thread.currentThread().getId()+"",'4');
//						logger.debug("@JobService#runJob: ETL executed with errors, returned result code: " + resultCode + " for task " + jobTask.getTask().getTaskId());
//						break;
//					} else {
//						threadErrorMap.put(Thread.currentThread().getId()+"",'3');
//						logger.debug("@JobService#runJob: ETL executed successfully for task " + jobTask.getTask().getTaskId());
//					}
				}
			} catch (Exception e) {
				threadErrorMap.put(Thread.currentThread().getId()+"",'4');
				logger.error("@JobService#runJob: Failed to execute job " + job.getJobId(), e);
			} finally {
				List<JobThreadTracker> runningThreads = jobThreadTrackerRepository.getAllRunningThreads(job.getJobId(), ThreadStatusEnum.RUNNING.getValue());
				runningThreads.forEach(jobThreadTrackerRepository::delete);
				if(Objects.nonNull(threadErrorMap.get(Thread.currentThread().getId()+""))){
					jobRepository.setJobDone(job.getJobId(), threadErrorMap.get(Thread.currentThread().getId()+""), instId, userId);
				} else{
					jobRepository.setJobDone(job.getJobId(),'3', instId, userId);
				}
				logger.info("@CommonService#executeJob: Job " + job.getJobId() + " has completed execution.");
			}
		});
		thread.setName("JOB-THREAD-" + job.getJobId());
		thread.start();

		long threadId = thread.getId();
		JobThreadTracker jobThreadTracker = new JobThreadTracker();
		jobThreadTracker.setJob(job.getJobId());
		jobThreadTracker.setThreadId(threadId);
		jobThreadTracker.setThreadName(thread.getName());
		jobThreadTracker.setStatus(ThreadStatusEnum.RUNNING.getValue());
		jobThreadTracker.setCreatedBy(userId);
		jobThreadTracker.setCreatedDate(new Date());
		jobThreadTrackerRepository.save(jobThreadTracker);
	}

	@Async
	public void scheduleJobContinuously(Job job) {
		// Check if the previous job is finished
		logger.debug("Reaching inside the function scheduleJobContinuously for job " + job.getJobDescription());
		Optional<User> user = userRepository.findByUsername("jobs");
		logger.debug("parameters  institution " + job.getInstitution() + "user " + user.get().getUserId());

		try {
			// Simulate a task that takes some time to complete
			runJob(job, user.get().getUserId(), job.getInstitution(), null);
			// Task finished
			System.out.println("Job completed!");
		} catch (Exception e) {
			System.err.println("Job failed: " + e.getMessage());
		} finally {
			// Reset the flag so the next job can start
		}
	}

	private boolean isEndDateNextDay(Date startDate, Date endDate) {
		LocalDate start = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate end = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		return end.isAfter(start); // Check if the end date is a later day
	}
}