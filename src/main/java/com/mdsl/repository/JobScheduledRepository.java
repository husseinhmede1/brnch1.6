package com.mdsl.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mdsl.model.entity.JobScheduled;

@Repository
public interface JobScheduledRepository extends JpaRepository<JobScheduled, Integer> {
	@Query(value = "Select * from MD_JOB_SCHEDULED_EXEC js left join MD_JOB_DEFINITION jd on jd.JOB_ID = js.JOB_ID where js.status = :status and to_date (js.start_date) between to_date (:fromDate) and to_date (:toDate)", nativeQuery = true)
	List<JobScheduled> findAllJobScheduledForNextDay (char status, Date fromDate, Date toDate);
	
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM MD_JOB_SCHEDULED_EXEC WHERE JOB_ID = :jobId AND status = '0'")
	void deleteFutureScheduledByJobId(int jobId);
	
	@Transactional
	@Modifying
	@Query(value = "DELETE FROM MD_JOB_SCHEDULED_EXEC WHERE JOB_ID = :jobId")
	void deleteByJobId(int jobId);
}