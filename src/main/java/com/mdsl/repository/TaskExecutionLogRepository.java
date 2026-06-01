package com.mdsl.repository;

import java.sql.Timestamp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.mdsl.model.entity.TaskExecutionLog;

@Repository
public interface TaskExecutionLogRepository extends JpaRepository<TaskExecutionLog, Integer> {
    
    @Query("SELECT t FROM MD_TASK_EXECUTION_LOG t " +
           "WHERE t.institutionId = :institutionId " +
           "AND (:taskId = 0 OR t.taskId = :taskId) " +
           "AND (:startDate IS NULL OR t.startDatetime >= :startDate) " +
           "AND (:endDate IS NULL OR t.endDatetime <= :endDate)")
    Page<TaskExecutionLog> findByInstitutionIdTaskIdAndDateRange(
            @Param("institutionId") String institutionId,
            @Param("taskId") int taskId,
            @Param("startDate") Timestamp startDate,
            @Param("endDate") Timestamp endDate,
            Pageable pageable
    );
}
