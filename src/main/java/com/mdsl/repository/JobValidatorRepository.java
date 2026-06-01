package com.mdsl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mdsl.model.entity.JobValidator;

@Repository
public interface JobValidatorRepository extends JpaRepository<JobValidator, String>{

}