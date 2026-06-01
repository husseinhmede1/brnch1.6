package com.mdsl.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mdsl.model.entity.TaskParameter;

@Repository
public interface TaskParametersRepository extends JpaRepository<TaskParameter, Integer> {
	List<TaskParameter> findByTaskIdOrderBySequence(int taskId);
	List<TaskParameter> findByTaskId(int taskId);
}
