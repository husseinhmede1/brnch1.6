package com.mdsl.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mdsl.model.entity.TransactionGroup;

@Repository
public interface TransactionGroupRepository extends JpaRepository<TransactionGroup, Integer>{

	@Modifying
	@Query("UPDATE TransactionGroup tg SET tg.status = :status"
			+ " WHERE tg.transactionGroupId = :transactionGroupId")
	void updateStatus(@Param("transactionGroupId")int transactionGroupId, @Param("status") char status);
	
	List<TransactionGroup> findByStatus(char value, Sort by);

	List<TransactionGroup> findByInstitution_InstitutionId(String institutionId, Sort by);

	List<TransactionGroup> findByUserCreate(String string);

	List<TransactionGroup> findByTransactionGroupNameEqualsIgnoreCaseAndInstitution_InstitutionIdAndTransactionGroupIdNot(
			String upperCase, String institutionId, int transactionGroupId);

	List<TransactionGroup> findByTransactionGroupNameEqualsIgnoreCaseAndInstitution_InstitutionId(String upperCase,
			String institutionId);
}
