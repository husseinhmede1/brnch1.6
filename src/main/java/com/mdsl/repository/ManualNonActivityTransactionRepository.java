package com.mdsl.repository;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mdsl.model.entity.ManualNonActivityTransaction;

@Repository
public interface ManualNonActivityTransactionRepository extends JpaRepository<ManualNonActivityTransaction, Integer> {

//	@Query("FROM MD_NON_ACTIVITY_MANUAL WHERE INSTITUTION_ID = :instId")
//	List<ManualNonActivityTransaction> findByInstitutionId(int instId, Sort by);
//	Optional<ManualNonActivityTransaction> findByManualNonActivityTransactionId(int id);

	Page<ManualNonActivityTransaction> findByInstitution_InstitutionId(PageRequest pageRequest, String institutionId);

	Page<ManualNonActivityTransaction> findByInstitution_InstitutionIdAndEntitiesObject_EntityIdAndTransactionDateBetween(
			PageRequest pageRequest, String institutionId, String outletId, Date fromTransactionDate, Date toTransactionDate);

	Page<ManualNonActivityTransaction> findByInstitution_InstitutionIdAndEntitiesObject_EntityIdAndTransactionEntity_TransactionIdAndTransactionDateBetween(
			PageRequest pageRequest, String institutionId, String outletId, String transactionId, Date fromTransactionDate,
			Date toTransactionDate);

	Page<ManualNonActivityTransaction> findByInstitution_InstitutionIdAndTransactionDateBetween(PageRequest pageRequest,
			String institutionId, Date fromTransactionDate, Date toTransactionDate);

	List<ManualNonActivityTransaction> findByUserCreate(String string);

}
