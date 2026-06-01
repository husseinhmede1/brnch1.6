package com.mdsl.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mdsl.model.entity.InstitutionControl;

@Repository
public interface InstitutionControlRepository extends JpaRepository<InstitutionControl, Integer>{
	
	boolean existsByInstitutionId(String instId);
	
	boolean existsByInstitutionIdAndRecordSeqIdNot(String instId, int recordSeqId);
	
	Optional<InstitutionControl> findByInstitutionId(String instId);
}
