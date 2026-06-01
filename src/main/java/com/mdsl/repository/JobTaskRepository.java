package com.mdsl.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mdsl.model.dto.response.BKDParameterResponseDto;
import com.mdsl.model.entity.JobTask;

@Repository
public interface JobTaskRepository extends JpaRepository<JobTask, Integer> {
	List<JobTask> findAllByOrderByTaskName();
	
	@Query(name="job_task_parameters", nativeQuery=true)
	List<BKDParameterResponseDto> findJobTaskparameters(@Param("taskId") Integer taskId);
	@Query(
		    value = "SELECT T.TASK_ID, P.PARAMETER_ID, SP.PARAMETERS_SERVICE_ID, P.PARAMETER_NAME, SP.IS_MANDATORY " +
		            "FROM MD_JOB_TASK T " +
		            "JOIN MD_BKD_SERVICE S ON T.SERVICE_ID = S.SERVICE_ID " +
		            "JOIN MD_BKD_PARAMETERS_SERVICE SP ON SP.SERVICE_ID = S.SERVICE_ID " +
		            "JOIN MD_BKD_PARAMETERS P ON SP.PARAMETER_ID = P.PARAMETER_ID " +
		            "WHERE T.TASK_ID IN (:taskIds)",
		    nativeQuery = true
		)
		List<Object[]> findJobTaskparametersAll(List<Integer> taskIds);

	@Query(value="SELECT COUNT(*) FROM MD_BKD_JOB_TASK_PARAM WHERE PARAMETERS_SERVICE_ID IN (SELECT PARAMETERS_SERVICE_ID "
			+ "FROM MD_BKD_PARAMETERS_SERVICE "
			+ "WHERE PARAMETER_ID = (SELECT PARAMETER_ID FROM MD_BKD_PARAMETERS WHERE PARAMETER_NAME= :parameterName)) "
			+ "AND PARAMETER_VALUE= :channelCode", nativeQuery=true)
	Integer exitsInJobTaskparameters(String parameterName, String channelCode);
	
	@Query(value="SELECT COUNT(*) FROM MD_BKD_JOB_TASK_PARAM WHERE PARAMETERS_SERVICE_ID IN (SELECT PARAMETERS_SERVICE_ID FROM MD_BKD_PARAMETERS_SERVICE WHERE PARAMETER_ID IN "
			+ "(SELECT PARAMETER_ID FROM MD_BKD_PARAMETERS WHERE PARAMETER_NAME='Layout')) AND PARAMETER_VALUE= :layout", nativeQuery=true)
	Integer existsByLayout(String layout);
}