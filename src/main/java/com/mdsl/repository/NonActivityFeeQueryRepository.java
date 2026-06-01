package com.mdsl.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mdsl.model.entity.NonActivityFeeQuery;

@Repository
public interface NonActivityFeeQueryRepository extends JpaRepository<NonActivityFeeQuery, Integer>{

	Page<NonActivityFeeQuery> findByInstitution_InstitutionId(PageRequest pageRequest, String institutionId);

	Page<NonActivityFeeQuery> findByInstitution_InstitutionIdAndEntitiesObject_EntityIdAndProcessingDateBetween(
			PageRequest pageRequest, String institutionId, String entityId, Date fromProcessingDate, Date toProcessingDate);

	Page<NonActivityFeeQuery> findByInstitution_InstitutionIdAndEntitiesObject_EntityIdAndProcessingDateBetweenAndTransactionEntity_TransactionId(
			PageRequest pageRequest, String institutionId, String entityId, Date fromProcessingDate, Date toProcessingDate,
			String transactionId);

	Page<NonActivityFeeQuery> findByInstitution_InstitutionIdAndProcessingDateBetween(PageRequest pageRequest,
			String institutionId, Date fromProcessingDate, Date toProcessingDate);

	List<NonActivityFeeQuery> findByUserCreate(String string);
}