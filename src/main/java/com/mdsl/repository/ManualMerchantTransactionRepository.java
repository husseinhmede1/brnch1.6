package com.mdsl.repository;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mdsl.model.entity.ManualMerchantTransaction;

@Repository
public interface ManualMerchantTransactionRepository extends JpaRepository<ManualMerchantTransaction, Integer> {

	Boolean existsByInstitution_InstitutionIdAndTransactionEntity_TransactionId(String institutionId, String transactionId);

	Boolean existsByInstitution_InstitutionIdAndTransactionEntity_TransactionIdAndMerchantTransactionIdNotIn(String institutionId, String transactionId, List<Integer> MerchantTransactionIds);

	Page<ManualMerchantTransaction> findByInstitution_InstitutionId(PageRequest pageRequest, String institutionId);

	Page<ManualMerchantTransaction> findByInstitution_InstitutionIdAndEntitiesObject_EntityIdAndTerminalEntity_TerminalIdAndCardNumberIgnoreCaseAndTransactionDateBetweenAndTransactionEntity_TransactionId(
			PageRequest pageRequest, String institutionId, String outletId, String terminalId, String cardNumber, Date fromTransactionDate,
			Date toTransactionDate, String transactionId);

	Page<ManualMerchantTransaction> findByInstitution_InstitutionIdAndEntitiesObject_EntityIdAndTerminalEntity_TerminalIdAndCardNumberIgnoreCaseContainingAndTransactionDateBetweenAndTransactionEntity_TransactionId(
			PageRequest pageRequest, String institutionId, String outletId, String terminalId, String cardNumber, Date fromTransactionDate,
			Date toTransactionDate, String transactionId);

	Page<ManualMerchantTransaction> findByInstitution_InstitutionIdAndEntitiesObject_EntityIdAndTerminalEntity_TerminalIdAndTransactionDateBetween(
			PageRequest pageRequest, String institutionId, String outletId, String terminalId, Date fromTransactionDate, Date toTransactionDate);

	Page<ManualMerchantTransaction> findByInstitution_InstitutionIdAndEntitiesObject_EntityIdAndTransactionDateBetweenAndCardNumberIgnoreCaseAndTransactionEntity_TransactionId(
			PageRequest pageRequest, String institutionId, String outletId, Date fromTransactionDate, Date toTransactionDate, String cardNumber, String transactionId);

	Page<ManualMerchantTransaction> findByInstitution_InstitutionIdAndEntitiesObject_EntityIdAndTransactionDateBetweenAndTransactionEntity_TransactionId(
			PageRequest pageRequest, String institutionId, String outletId, Date fromTransactionDate, Date toTransactionDate, String transactionId);

	Page<ManualMerchantTransaction> findByInstitution_InstitutionIdAndEntitiesObject_EntityIdAndCardNumberIgnoreCaseAndTransactionDateBetween(
			PageRequest pageRequest, String institutionId, String outletId, String cardNumber, Date fromTransactionDate, Date toTransactionDate);

	Page<ManualMerchantTransaction> findByInstitution_InstitutionIdAndEntitiesObject_EntityIdAndCardNumberIgnoreCaseContainingAndTransactionDateBetween(
			PageRequest pageRequest, String institutionId, String outletId, String cardNumber, Date fromTransactionDate, Date toTransactionDate);

	Page<ManualMerchantTransaction> findByInstitution_InstitutionIdAndEntitiesObject_EntityIdAndTerminalEntity_TerminalIdAndTransactionDateBetweenAndTransactionEntity_TransactionId(
			PageRequest pageRequest, String institutionId, String outletId, String terminalId, Date fromTransactionDate, Date toTransactionDate,
			String transactionId);

	Page<ManualMerchantTransaction> findByInstitution_InstitutionIdAndEntitiesObject_EntityIdAndTerminalEntity_TerminalIdAndCardNumberIgnoreCaseAndTransactionDateBetween(
			PageRequest pageRequest, String institutionId, String outletId, String terminalId, String cardNumber, Date fromTransactionDate,
			Date toTransactionDate);

	Page<ManualMerchantTransaction> findByInstitution_InstitutionIdAndEntitiesObject_EntityIdAndTerminalEntity_TerminalIdAndCardNumberIgnoreCaseContainingAndTransactionDateBetween(
			PageRequest pageRequest, String institutionId, String outletId, String terminalId, String cardNumber, Date fromTransactionDate,
			Date toTransactionDate);

	Page<ManualMerchantTransaction> findByInstitution_InstitutionIdAndEntitiesObject_EntityIdAndTransactionDateBetween(
			PageRequest pageRequest, String institutionId, String outletId, Date fromTransactionDate, Date toTransactionDate);

	List<ManualMerchantTransaction> findByInstitution_InstitutionId(String institutionId, Sort by);



	Page<ManualMerchantTransaction> findByInstitution_InstitutionIdAndTransactionDateBetween(PageRequest pageRequest, String institutionId, Date fromTransactionDate, Date toTransactionDate);

	List<ManualMerchantTransaction> findByUserCreate(String string);

	
	
}
