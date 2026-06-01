package com.mdsl.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mdsl.model.entity.JobThreadTracker;


@Repository
public interface JobThreadTrackerRepository extends JpaRepository<JobThreadTracker , Integer> {
	
	List<JobThreadTracker> findByJob(Integer jobId);
	
	@Query(value="SELECT * FROM MD_JOB_THREAD_TRACKER WHERE LOWER(STATUS)=LOWER(:status) AND JOB_ID=:jobId",nativeQuery=true)
	List<JobThreadTracker> getAllRunningThreads(Integer jobId,String status);
}