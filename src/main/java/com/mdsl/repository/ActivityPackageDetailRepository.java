package com.mdsl.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mdsl.model.entity.ActivityPackageDetail;
import com.mdsl.model.entity.Institution;
import com.mdsl.model.entity.IssuerProfile;

@Repository
public interface ActivityPackageDetailRepository extends JpaRepository<ActivityPackageDetail, Integer>{
	Boolean existsByIssuerAcqProfileEntityAndInstitution (IssuerProfile issuerAcqProfile,Institution institution);
	
	@Query("FROM MD_ACQ_ACTIVITY_PKG_DTL WHERE PKG_ID = :pkgId")
	List<ActivityPackageDetail> findByPkgId(@Param("pkgId") String pkgId, Sort sort);

	@Query(value="SELECT * FROM MD_ACQ_ACTIVITY_PKG_DTL WHERE pkg_id= :pkgId AND INSTITUTION_ID= :institution", nativeQuery = true)
	List<ActivityPackageDetail> findByPkgIdAndInstitution(String pkgId,String institution);
	
	List<ActivityPackageDetail> findByTranGroup(String transactionGroupName);
	List<ActivityPackageDetail> findByTranGroupAndInstitution(String transactionGroupName,Institution institution);
	List<ActivityPackageDetail> findByTranGroupAndInstitution_InstitutionId(String transactionGroupName,String institution);

	List<ActivityPackageDetail> findByUserCreate(String string);
}
