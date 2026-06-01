package com.mdsl.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mdsl.model.entity.BKDJobTaskParameter;

@Repository
public interface BKDJobTaskParameterRepository extends JpaRepository<BKDJobTaskParameter, Integer>{
	Boolean existsByJobTaskIdAndParametersServiceId(int jobTaskId, int parametersServiceId);
	
	List<BKDJobTaskParameter> findByJobTaskId(Integer jobTaskId);
	
	@Transactional
	@Modifying
	void deleteAllByJobTaskId(Integer jobTaskId);
	
	@Transactional
	@Modifying
	@Query(value="DELETE FROM MD_BKD_JOB_TASK_PARAM WHERE JOB_TASK_ID IN (SELECT JOB_TASK_ID FROM MD_JOB_DEF_TASK WHERE JOB_ID = :jobId)",nativeQuery=true)
	void deleteAllByJobId(Integer jobId);
	
	List<BKDJobTaskParameter> findByJobTaskIdIn(List<Integer> jobTaskIds);

}