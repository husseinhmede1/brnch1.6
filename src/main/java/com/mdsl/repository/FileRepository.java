package com.mdsl.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mdsl.model.entity.File;

@Repository 
public interface FileRepository extends JpaRepository<File, Integer>{
	List<File> findByStatus(String status);
	
	Optional<File> findByFileIdAndStatus(Integer fileId, String status);
}