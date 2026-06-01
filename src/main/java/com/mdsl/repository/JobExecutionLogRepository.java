package com.mdsl.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mdsl.model.entity.Job;
import com.mdsl.model.entity.JobExecutionLog;

@Repository
public interface JobExecutionLogRepository extends JpaRepository<JobExecutionLog, Integer> {
	Boolean existsByJob (Job job);
	
	@Query(value="SELECT * FROM MD_JOB_EXECUTION_LOG WHERE JOB_ID = :jobId AND (:fromStartDate IS NULL OR TO_DATE(START_DATE) BETWEEN :fromStartDate AND :toStartDate)",nativeQuery=true)
	Page<JobExecutionLog> findByJob(Integer jobId, Date fromStartDate, Date toStartDate, Pageable pageable);
	
	@Modifying
	@Query(value="UPDATE MD_JOB_EXECUTION_LOG SET END_DATE=SYSDATE , EXECUTION_DETAILS=:executionDetails ,STATUS=:status WHERE JOB_ID=:jobId AND EXEC_ID IN (SELECT MAX(EXEC_ID) FROM MD_JOB_EXECUTION_LOG WHERE JOB_ID=:jobId ) ",nativeQuery=true)
	void updateJobEndDateAndDetailsAndStatusByJobIdAndStatus(String executionDetails, char status, Integer jobId);

	@Query("select to_char(START_DATE, 'dd/mm/yyyy') as start_date from MD_JOB_EXECUTION_LOG where SERVICE_ID = :taskId AND END_DATE IS NULL AND STATUS IN ('0', '1')")
	String checkTaskIfRunning(int taskId);
}