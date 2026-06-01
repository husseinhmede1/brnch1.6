package com.mdsl.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mdsl.model.dto.response.JobScheduledMonitoringResponseDto;
import com.mdsl.model.entity.Job;

@Repository
public interface JobRepository extends JpaRepository<Job, Integer> {
	Boolean existsByInstitutionAndJobNameIgnoreCase(String institution, String jobName);
	
	Optional<Job> findByInstitutionAndJobId(String institution, int jobId);
	
	List<Job> findByInstitutionOrderByJobName(String institution);
	 
	List<Job> findByInstitutionAndStatusOrderByJobName (String institution, char status); 
	
	List<Job> findByStatusAndFrequency (String status, Integer frequency); 
	
	@Query(name = "job_monitoring_query" , nativeQuery = true)
	List<JobScheduledMonitoringResponseDto> getJobMonitoring(@Param("instId") String instId);
	
	@Transactional
	@Modifying
	@Query("UPDATE MD_JOB_DEFINITION SET STATUS = :status, UPDATED_DATE = SYSDATE, UPDATED_BY = :userId WHERE JOB_ID = :jobId AND INST_ID = :instId")
	void updateJobStatus(int jobId, char status, String instId, Integer userId);
	
	@Transactional
	@Modifying
	@Query("UPDATE MD_JOB_DEFINITION SET ENABLED = :status, UPDATED_DATE = SYSDATE, UPDATED_BY = :userId WHERE JOB_ID = :jobId AND INST_ID = :instId")
	void updateJobEnabledFlag(Integer jobId, char status, String instId, Integer userId);

	@Transactional
	@Modifying
	@Query("UPDATE MD_JOB_DEFINITION SET ENABLED = '0', UPDATED_DATE = SYSDATE, UPDATED_BY = :userId, " +
	"LAST_RUN_RESULT = :status WHERE JOB_ID = :jobId AND INST_ID = :instId")
	void setJobDone(Integer jobId, char status, String instId, int userId);

}