package com.mdsl.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mdsl.model.entity.FileType;

@Repository
public interface FileTypeRepository extends JpaRepository<FileType, Integer>{
	
	Optional<FileType> findByFileTypeCode(String fileTypeCode);
}