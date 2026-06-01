package com.mdsl.swtch.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mdsl.swtch.model.entity.SwitchSeqCtrl;

@Repository
public interface SwitchSeqCtrlRepository extends JpaRepository<SwitchSeqCtrl, String>{
	
	@Query(value="SELECT * FROM SEQ_CTRL WHERE INSTITUTION_ID= :institutionId AND SEQ_NAME = :seqName", nativeQuery=true)
	Optional<SwitchSeqCtrl> findBySeqNameAndInstitutionId(String seqName, String institutionId);
}
