package com.mdsl.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mdsl.model.entity.IssuerRelation;

@Repository
public interface IssuerRelationRepository extends JpaRepository<IssuerRelation, Integer> {
	@Query("FROM MD_ISSUER_ACQ_RELATION WHERE issuer_acq_profile = :issuerId")
	List<IssuerRelation> findAllByIssuerId(int issuerId,Sort sort);
	
	Page<IssuerRelation> findByIssuerAcqProfile(String issuerAcqProfile, Pageable pageable);
	
	
	@Query("SELECT i FROM MD_ISSUER_ACQ_RELATION i WHERE i.issuerAcqProfile = :issuerAcqProfile AND (i.panRangeFrom = :panRange OR i.panRangeTo = :panRange)")
	Page<IssuerRelation> findByIssuerAcqProfileAndPanRangeFromOrPanRangeTo(String issuerAcqProfile, String panRange, Pageable pageable);
	
	@Query("SELECT i FROM MD_ISSUER_ACQ_RELATION i WHERE i.issuerAcqProfile = :issuerAcqProfile AND (i.panRangeFrom LIKE CONCAT('%', :panRange, '%') OR i.panRangeTo LIKE CONCAT('%', :panRange, '%'))")
	Page<IssuerRelation> findByIssuerAcqProfileAndLikePanRangeFromOrPanRangeTo(String issuerAcqProfile, String panRange, Pageable pageable);
	
	@Query("SELECT i FROM MD_ISSUER_ACQ_RELATION i WHERE i.issuerAcqProfile = :issuerAcqProfile AND institutionId = :instId AND (i.panRangeFrom = :panRange OR i.panRangeTo = :panRange)")
	Page<IssuerRelation> findByIssuerAcqProfileAndInstitutionIdAndPanRangeFromOrPanRangeTo(String issuerAcqProfile,String instId, String panRange, Pageable pageable);
	@Query("SELECT i FROM MD_ISSUER_ACQ_RELATION i WHERE i.issuerAcqProfile = :issuerAcqProfile AND institutionId = :instId AND (i.panRangeFrom LIKE CONCAT('%', :panRange, '%') OR i.panRangeTo LIKE CONCAT('%', :panRange, '%'))")
	Page<IssuerRelation> findByIssuerAcqProfileAndInstitutionIdAndLikePanRangeFromOrPanRangeTo(String issuerAcqProfile,String instId, String panRange, Pageable pageable);
	
	List<IssuerRelation> findByIssuerAcqProfile(String issuerAcqProfile, Sort sort);
	
	List<IssuerRelation>findByIssuerAcqProfileAndInstitutionId(String issuerAcqProfile,String instId, Sort sort);
	Page<IssuerRelation>findByIssuerAcqProfileAndInstitutionId(String issuerAcqProfile,String instId, Pageable pageable);
	List<IssuerRelation>findByIssuerAcqProfileAndInstitutionId(String issuerAcqProfile,String instId);

	List<IssuerRelation> findByIssuerAcqProfile(String issuerAcqProfile);
	
	List<IssuerRelation> findByInstitutionIdAndPanRangeFromAndPanRangeToAndCntryCode(String institutionId, String panRangeFrom, String panRangeTo, String cntryCode);
	
	List<IssuerRelation> findByInstitutionIdAndPanRangeFromAndPanRangeTo(String institutionId, String panRangeFrom, String panRangeTo);
	
	boolean existsByIssuerAcqProfile(String issuerAcqProfile);
	
	void deleteByIssuerAcqProfile(String issuerAcqProfile);
}
