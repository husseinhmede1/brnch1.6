package com.mdsl.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mdsl.model.entity.IssuerProfile;

@Repository
public interface IssuerRepository extends JpaRepository<IssuerProfile, Integer>{
	
	@Query("FROM MD_ISSUER_ACQ_PROFILE WHERE INSTITUTION_ID = :instId")
	List<IssuerProfile> findAllByInstId(String instId,Sort sort);

}
