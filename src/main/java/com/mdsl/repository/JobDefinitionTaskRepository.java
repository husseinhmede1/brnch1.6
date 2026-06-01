package com.mdsl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mdsl.model.entity.JobDefinitionTask;

@Repository
public interface JobDefinitionTaskRepository extends JpaRepository<JobDefinitionTask, Integer> {
	@Transactional
	@Modifying
	void deleteByJob (Integer job);
}