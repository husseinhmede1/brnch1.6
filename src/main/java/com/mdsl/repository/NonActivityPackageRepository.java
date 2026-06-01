package com.mdsl.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mdsl.model.entity.Entities;
import com.mdsl.model.entity.NonActivityPackage;

@Repository
public interface NonActivityPackageRepository extends JpaRepository<NonActivityPackage, Integer>{
	Optional<NonActivityPackage> findByPackageId(int id);

	@Query("FROM MD_ACQ_NON_ACTIVITY_PKG WHERE INSTITUTION_ID = :instId")
	List<NonActivityPackage> findNonActivityPackageByInstitutionId(@Param("instId") String instId, Sort sort);

	List<NonActivityPackage> findByStatus(char charAt, Sort sort);

	@Query("FROM MD_ACQ_ENTITY WHERE NON_ACTIVITY_FEE_PKG = :id")
	List<Entities> findEntity(int id);

	List<NonActivityPackage> findByInstitution_InstitutionIdAndStatus(String id, char charAt, Sort by);

	Optional<NonActivityPackage> findByPackageId(String nonActivityPackageId);

	List<NonActivityPackage> findByUserCreate(String string);

	@Query(value = "SELECT * FROM MD_ACQ_NON_ACTIVITY_PKG WHERE PKG_ID = :nonActivityPackageId and institution_id = :instId", nativeQuery = true)
	Optional<NonActivityPackage> findByPackageIdAndInstitution_institutionId(@Param("nonActivityPackageId") String nonActivityPackageId,@Param("instId") String instId);

	@Modifying
	@Transactional
	@Query(value = "DELETE FROM MD_ACQ_NON_ACTIVITY_PKG WHERE PKG_ID = :pkgId AND INSTITUTION_ID = :instId", nativeQuery = true)
	void deleteByPackageIdAndInstitutionId(String pkgId, String instId);
	
	
	//	@Transactional
//	@Query(value = "SELECT MD_NON_ACT_PKG_SEQ.nextval from DUAL", nativeQuery = true)
//	int findNextRecordSequence();
}
