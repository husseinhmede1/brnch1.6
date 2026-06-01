package com.mdsl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mdsl.model.entity.JobProcessedFiles;

@Repository
public interface JobProcessedFilesRepository extends JpaRepository<JobProcessedFiles, Integer>{

}