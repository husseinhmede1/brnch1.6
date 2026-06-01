package com.mdsl.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mdsl.model.entity.FileElement;

@Repository 
public interface FileElementRepository extends JpaRepository<FileElement, Integer>{
	List<FileElement> findByFileId(int fileId);
	
	@Query(value="SELECT ELEMENT_ID FROM MD_CFG_FILE_ELEMENTS WHERE FILEID = :fileId AND IS_MANDATORY = :isMandatory", nativeQuery=true)
	List<Integer> findByFileIdAndIsMandatory(int fileId, String isMandatory);

	@Query(value="SELECT * FROM MD_CFG_FILE_ELEMENTS WHERE FILEID = :fileId AND IS_MANDATORY = :isMandatory", nativeQuery=true)
	List<FileElement> findAllByFileIdAndIsMandatory(int fileId, String isMandatory);
	
	Optional<FileElement> findByElementIdAndFileId(int elementId, int fileId);
}
