package com.mdsl.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mdsl.model.entity.ProcessingEvents;

@Repository
public interface ProcessingEventsRepository extends JpaRepository<ProcessingEvents, String>
{
	Page<ProcessingEvents> findByInstitutionIdAndTaskExecutionLogId(String institutionId, int taskExecutionLogId, Pageable pageable);
}
