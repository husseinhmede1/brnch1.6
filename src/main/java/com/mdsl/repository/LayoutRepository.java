package com.mdsl.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mdsl.model.entity.Layout;

@Repository 
public interface LayoutRepository extends JpaRepository<Layout, Integer>{
	Boolean existsByInstIdAndLayoutNameIgnoreCase(String instId, String layoutName);
	
	Boolean existsByInstIdAndLayoutNameIgnoreCaseAndLayoutIdNotIn(String instId, String layoutName, List<Integer> layoutId);

	List<Layout> findByInstIdAndStatus(int instId, String status);

	Page<Layout> findByInstId(int instId, Pageable pageable);
	
	List<Layout> findByInstIdAndFileId(int instId, Integer fileId);
	
	@Query(value="SELECT * FROM MD_CFG_LAYOUT WHERE INST_ID = :instId AND FILEID IN (SELECT FILEID FROM MD_CFG_FILES WHERE FILETYPE IN (SELECT FILE_TYPE_ID FROM MD_LKU_FILE_TYPES WHERE FILE_TYPE_CODE = :fileType))", nativeQuery=true)
	List<Layout> findByInstIdAndFileType(int instId, String fileType);
	
	Optional<Layout> findByInstIdAndLayoutId(String instId, Integer layoutId);
	
	@Transactional
	@Modifying
	@Query("UPDATE MD_CFG_LAYOUT SET STATUS = :status, UPDATED_DATE = SYSDATE, UPDATED_BY =:userId WHERE LAYOUT_ID = :layoutId")
	void updateLayoutStatus(int layoutId, char status, int userId);
}