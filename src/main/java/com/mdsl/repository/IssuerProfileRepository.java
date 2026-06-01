package com.mdsl.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mdsl.model.entity.IssuerProfile;

@Repository
public interface IssuerProfileRepository extends JpaRepository<IssuerProfile, Integer>{
	Optional<IssuerProfile> findByIssuerAcqProfile(String issuerAcqProfile);
	
	boolean existsByIssuerAcqProfileAndInstitutionIdAndProfileIdNot(String issuerAcqProfile, String institutionId, Integer profileId);
	boolean existsByIssuerAcqProfileAndInstitutionId(String issuerAcqProfile, String institutionId);
	Optional<IssuerProfile> findByIssuerAcqProfileAndInstitutionId(String issuerAcqProfile,String instId);
	List<IssuerProfile> findByInstitutionId(String institutionId);
}
