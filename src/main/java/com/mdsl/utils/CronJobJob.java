package com.mdsl.utils;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.http.HttpStatus;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.service.JobService;
import com.mdsl.utils.enumerations.StatusEnum;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CronJobJob implements Job {
	private final JobService jobService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		com.mdsl.model.entity.Job job = (com.mdsl.model.entity.Job) dataMap.get("job");
		Integer scheduledJob = (Integer) dataMap.get("scheduledJob");

		if(job.getStatus().equalsIgnoreCase(String.valueOf(StatusEnum.DISABLED.getValue()))) {
			throw new BusinessException(ResponseCode.VAL_JOB_DISABLED,HttpStatus.CONFLICT);
		}

		if (String.valueOf(job.getEnabled()).equalsIgnoreCase(String.valueOf(StatusEnum.ENABLED.getValue()))) {
			throw new BusinessException(ResponseCode.CFG_JOB_RUNNING, HttpStatus.BAD_REQUEST);
		}
		jobService.runJob(job, 1, job.getInstitution(), scheduledJob);
	}
}