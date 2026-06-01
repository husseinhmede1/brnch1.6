package com.mdsl.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mdsl.model.entity.Institution;
import com.mdsl.model.entity.NonActivityPackage;
import com.mdsl.model.entity.NonActivityPackageDetails;

@Repository
public interface NonActivityPackageDetailsRepository extends JpaRepository<NonActivityPackageDetails, Integer> {

	@Query("FROM MD_ACQ_NON_ACTIVITY_PKG_DTL WHERE INSTITUTION_ID = :instId")
	List<NonActivityPackageDetails> findNonActivityPackageDetailsByInstitutionId(String instId, Sort sort);

	List<NonActivityPackageDetails> findByNonActivityPackageEntityAndInstitution(NonActivityPackage nonActivityPackage, Institution institution);

	@Query(value = "SELECT * FROM MD_ACQ_NON_ACTIVITY_PKG_DTL WHERE PKG_ID = :nonActivityPackageId AND INSTITUTION_ID = :institutionId", nativeQuery = true)
	List<NonActivityPackageDetails> findByNonActivityPackageAndInstitution(String nonActivityPackageId, String institutionId);

	List<NonActivityPackageDetails> findByStatus(char charAt, Sort sort);

	@Query("FROM MD_ACQ_NON_ACTIVITY_PKG_DTL WHERE PKG_ID = :id and INSTITUTION_ID = :instId")
	List<NonActivityPackageDetails> findByPackageIdAndInstitution_institutionId(@Param("id") String id,@Param("instId") String instId,Sort sort);

	@Query("FROM MD_ACQ_NON_ACTIVITY_PKG_DTL WHERE INSTITUTION_ID = :instId AND PKG_ID = :pkgId")
	List<NonActivityPackageDetails> findNonActivityPackageDetailsByInstitutionIdAndPackageId(@Param("instId") String instId,@Param("pkgId") String pkgId,
																							 Sort sort);

	@Query("FROM MD_ACQ_NON_ACTIVITY_PKG_DTL WHERE INSTITUTION_ID = :instId AND PKG_ID = :pkgId AND status = :status ")
	List<NonActivityPackageDetails> findActiveNonActivityPackageDetailsByInstitutionIdAndPackageId(String instId,
			String pkgId, char status, Sort sort);

	List<NonActivityPackageDetails> findByUserCreate(String string);

	List<NonActivityPackageDetails> findByTerminalTypes_TerminalType(String terminalType);

	@Query(value = "select MD_NON_ACTIVITY_PKG_DTL_SEQ.nextval from dual", nativeQuery = true)
	Integer findNonActivityPackageDetailsSeqNextValue();

	@Modifying
	@Transactional
	@Query(value = "DELETE FROM MD_ACQ_NON_ACTIVITY_PKG_DTL d WHERE d.PKG_ID = :pkgId AND d.INSTITUTION_ID = :institution", nativeQuery = true)
	void deleteByNonActivityPackageAndInstitution(String pkgId, String institution);
	
	
}
