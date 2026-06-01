package com.mdsl.repository;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mdsl.model.entity.DefaultTransactionId;
import com.mdsl.model.entity.Institution;

public interface DefaultTransactionIdRepository extends JpaRepository<DefaultTransactionId, Integer> {

	@Query(value = "SELECT * FROM MD_DEFAULT_TRANS_ID WHERE UPPER(TRANS_ID) = UPPER(:transactionId) AND INSTITUTION_ID = :institutionId AND ROWNUM = 1", nativeQuery = true)
	Optional<DefaultTransactionId> findFirstByTransactionIdAndInstitutionId(@Param("transactionId") String transactionId, @Param("institutionId") String institutionId);

	List<DefaultTransactionId> findByTransUsageIgnoreCase(String usage, Sort by);

	//Optional<DefaultTransactionId> findByTransactionId(String defTransactionId);
	Optional<DefaultTransactionId> findByTransactionIdAndInstitution(String defTransactionId,Institution institution);
	Optional<DefaultTransactionId> findByTransactionIdAndInstitution_InstitutionId(String defTransactionId,String instId);

  //13-06-2023: This function was amended to return the data related to the selected instituion only not the system institution as well
  @Query("from DefaultTransactionId d where d.institution.institutionId=:institutionId1 OR d.institution.institutionId=:institutionId2")
  public abstract List<DefaultTransactionId> findByInstitutionOrInstitution(@Param("institutionId1") String institutionId1, @Param("institutionId2") String institutionId2, Sort paramSort);
  
  @Query("from DefaultTransactionId d where d.institution.institutionId=:institutionId OR d.institution.institutionId='SYSTEM' ")
//List<Terminal> findByInstitution(String institutionId, Sort by);
  List<DefaultTransactionId> findByInstitutionUnion(String institutionId, Sort by);
  
	List<DefaultTransactionId> findByTransUsageAndInstitution_InstitutionId(String usage, String instId,
			Sort by);

    @Query("from DefaultTransactionId d where (d.institution.institutionId=:institutionId OR d.institution.institutionId='SYSTEM') AND d.status = :status AND trans_usage in :transUsage")
    List<DefaultTransactionId> findByTransUsageAndInstitution(@Param("institutionId") String institutionId,@Param("status") Character status,@Param("transUsage") List<String> transUsage, Sort by);

	List<DefaultTransactionId> findByUserCreate(String string);

	List<DefaultTransactionId> findByTransactionIdIgnoreCaseAndInstitution_InstitutionId(@Valid String transId,String instId);

	List<DefaultTransactionId> findByTransactionIdIgnoreCaseAndInstitution_InstitutionIdAndRecordSequenceIdNot(@Valid String transId, String instId, Integer recordSeqId);

	@Query(value = "SELECT TRANS_ID FROM MD_DEFAULT_TRANS_ID WHERE INSTITUTION_ID = :institutionId",nativeQuery = true)
	List<String> findAllTransId(@Param("institutionId") String institutionId);

	boolean existsByTransactionIdAndInstitution_InstitutionId(String transactionId, String institutionId);

}