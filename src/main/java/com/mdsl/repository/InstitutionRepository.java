package com.mdsl.repository;


import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import com.mdsl.model.entity.Institution;

@Repository
public interface InstitutionRepository extends JpaRepository<Institution, String>{
	

	
	List<Institution> findByStatus(char value,Sort sort);

	List<Institution> findByInstitutionNameIgnoreCase(String institutionName);
	
	List<Institution> findByInstitutionNameEqualsIgnoreCaseAndInstitutionId(String institutionName,String institutionId);
	
	Optional<Institution> findByInstitutionId(String institutionId);
	
	@Query("from MD_INSTITUTION inst order by inst.dateCreate desc")
	List<Institution> findAllByOrderByDateCreateDesc();

	List<Institution> findByUserCreate(String string);

//	List<Institution> findByInstitutionIdIgnoreCaseAndInstitutionNameIgnoreCaseAndRecord_seq_id(String instId,
//			String instName, Integer recordSeqId);

	List<Institution> findByInstitutionIdIgnoreCaseAndInstitutionNameIgnoreCaseAndRecordSeqId(String instId,
			String instName, Integer recordSeqId);

	List<Institution> findByInstitutionIdIgnoreCaseOrInstitutionNameIgnoreCase(String instId, String instName);

	List<Institution> findByInstitutionIdIgnoreCaseOrInstitutionNameIgnoreCaseAndRecordSeqIdNot(String instId,
			String instName, Integer recordSeqId);

	List<Institution> findByInstitutionNameIgnoreCaseAndRecordSeqIdNot(String instName,
			Integer recordSeqId);
	
	@Query(value = "from MD_INSTITUTION ins where ins.recordSeqId != :recordSeqId and ( lower(ins.institutionId) = :instName or lower(ins.institutionName) = :instName) ")
	List<Institution> findInstitutionExistsFromRecordSeqIdAndInstitutionIdOrInstitutionName(@Param(value="recordSeqId") Integer recordSeqId, @Param(value="instName") String instName);
	
	@Query(value = "from MD_INSTITUTION ins where (lower(ins.institutionId) = :instId or lower(ins.institutionId) = :instName) or (lower(ins.institutionName) = :instId or lower(ins.institutionName) = :instName) ")
	List<Institution> findInstitutionExistsFromInstitutionIdOrInstitutionName(@Param(value="instId") String instId, @Param(value="instName") String instName);
	
//	@Transactional
//	@Query(value = "SELECT MD_INSTITUTION_SEQ.nextval from DUAL",nativeQuery = true)
//	int findNextRecordSequence();
//	
//	
//    
//	@Modifying
//	@Transactional
//	@Query(value="ALTER SEQUENCE MD_INSTITUTION_SEQ INCREMENT BY 1",nativeQuery = true)
//	int updateRecordSequence();
	
//	@Query(value = "from Institution institution where institution.institutionId=:institutionId")
//	List<Institution> findByInstitutionId(String institutionId);
}