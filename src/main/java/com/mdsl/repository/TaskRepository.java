package com.mdsl.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mdsl.model.entity.Task;

@Repository
public abstract interface TaskRepository extends JpaRepository<Task, Integer>
{
  public abstract Optional<Task> findByInstitutionIdAndTaskName(String paramString1, String paramString2);
  
  List<Task> findByInstitutionIdOrInstitutionId(String systemInst, String instId);
}
