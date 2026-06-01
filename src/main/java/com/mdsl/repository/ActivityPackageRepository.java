package com.mdsl.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mdsl.model.entity.ActivityPackage;
import com.mdsl.model.entity.Entities;

@Repository
public interface ActivityPackageRepository extends JpaRepository<ActivityPackage, Integer>{
	@Query("FROM MD_ACQ_ACTIVITY_PKG WHERE INSTITUTION_ID = :instId")
	List<ActivityPackage> findActivityPackageByInstitutionId(@Param("instId")String instId, Sort sort);
	
	List<ActivityPackage> findByStatus(char value,Sort sort);
	
	@Query("FROM MD_ACQ_ENTITY WHERE ACTIVITY_FEE_PKG = :id")
	List<Entities> findEntity(int id);
	
	List<ActivityPackage> findByInstitution_InstitutionIdAndStatus(String id, char value, Sort by);

	Optional<ActivityPackage> findByPackageId(String activityPackageId);

	List<ActivityPackage> findByUserCreate(String string);

	@Query(value = "select * from MD_ACQ_ACTIVITY_PKG where pkg_id = :packageId and institution_id = :institutionId", nativeQuery = true)
	Optional<ActivityPackage> findByPackageIdAndInstitution_InstitutionId(@Param("packageId") String packageId, @Param("institutionId") String institutionId);

	@Modifying
	@Query(value = "DELETE from MD_ACQ_ACTIVITY_PKG where RECORD_SEQ_ID = :recordId", nativeQuery = true)
	void deleteActivityPackage(Integer recordId);

	
}
