package com.mdsl.repository;

import java.util.Date;
import java.util.List;

import com.mdsl.model.dto.response.TransactionMerchantNamesListDto;
import org.postgresql.core.NativeQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mdsl.model.entity.TransactionCurrent;

@Repository
public interface TransactionCurrentRepository extends JpaRepository<TransactionCurrent, Integer> {
	@Query(value = "SELECT DISTINCT TO_CHAR(tc.MERCH_SETTL_DATE, 'DD/MM/YYYY HH:MI:SS.FF6 AM') AS MERCH_SETTL_DATE " +
            "FROM MD_TRANSACTION_CURRENT tc " +
            "WHERE tc.ACQ_INST_ID = :acqInstId " +
            "AND tc.MERCH_SETTL_DATE NOT IN " +
            "( " +
            "  SELECT DISTINCT alc.MERCH_SETTL_DATE " +
            "  FROM MD_ACCOUNTING_LEDGER alc " +
            "  WHERE alc.ACCOUNT_ORIGIN = 'I' AND alc.INSTITUTION_ID = tc.ACQ_INST_ID " +
            "  UNION " +
            "  SELECT DISTINCT alh.MERCH_SETTL_DATE " +
            "  FROM MD_ACCOUNTING_LEDGER_HIST alh " +
            "  WHERE alh.ACCOUNT_ORIGIN = 'I' AND alh.INSTITUTION_ID = tc.ACQ_INST_ID " +
            ")", nativeQuery = true)
	List<String> findMerchSettlementDatesNotInAccountingLedger(@Param("acqInstId") String acqInstId);
	
	@Query(value = "SELECT DISTINCT TO_CHAR(tc.MERCH_PAYMENT_DATE, 'DD/MM/YYYY HH:MI:SS.FF6 AM') AS MERCH_PAYMENT_DATE " +
            "FROM MD_TRANSACTION_CURRENT tc " +
            "WHERE tc.ACQ_INST_ID = :acqInstId " +
            "AND tc.MERCH_PAYMENT_DATE NOT IN " +
            "( " +
            "  SELECT DISTINCT alc.MERCH_PAYMENT_DATE " +
            "  FROM MD_ACCOUNTING_LEDGER alc " +
            "  WHERE alc.ACCOUNT_ORIGIN = 'I' AND alc.INSTITUTION_ID = tc.ACQ_INST_ID " +
            "  UNION " +
            "  SELECT DISTINCT alh.MERCH_PAYMENT_DATE " +
            "  FROM MD_ACCOUNTING_LEDGER_HIST alh " +
            "  WHERE alh.ACCOUNT_ORIGIN = 'I' AND alh.INSTITUTION_ID = tc.ACQ_INST_ID " +
            ") AND tc.MERCH_PAYMENT_DATE IS NOT NULL", nativeQuery = true)
	List<String> findMerchPaymentDatesNotInAccountingLedger(@Param("acqInstId") String acqInstId);
	
	@Query(value = "SELECT * FROM MD_TRANSACTION_CURRENT a " +
			"       WHERE a.ACQ_INST_ID = :institutionId " +
			"       AND a.SETTL_MERCH_HALT = :settlMerchHalt " +
			"       AND a.OUTLET_CODE = :entityId " +
			"       AND a.transaction_date BETWEEN TO_DATE(:fromDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"       AND TO_DATE(:toDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"      	AND  TRANS_ID in (select distinct TRANS_ID from MD_DEFAULT_TRANS_ID where TRANS_USAGE ='TRANS') " +
			"       AND ( settl_processing_flag ='N' or " +
			"       (settl_processing_flag ='Y'" +
			"			 AND record_seq_id not in" +
			"		(select distinct record_seq_id from MD_ACCOUNTING_LEDGER UNION select distinct record_seq_id from MD_ACCOUNTING_LEDGER_HIST))) ", nativeQuery = true)
		Page<TransactionCurrent> findTransactions(
				PageRequest pageRequest, @Param("institutionId") String institutionId,
		    @Param("entityId") String entityId,
		    @Param("fromDate") String fromDate,
		    @Param("toDate") String toDate,
		    @Param("settlMerchHalt") char settlMerchHalt
				);

	@Query(value = "SELECT * FROM MD_TRANSACTION_CURRENT a " +
			"       WHERE a.ACQ_INST_ID = :institutionId " +
			"       AND a.SETTL_MERCH_HALT = :settlMerchHalt " +
			"       AND a.OUTLET_CODE = :entityId " +
			"       AND a.transaction_date BETWEEN TO_DATE(:fromDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"       AND TO_DATE(:toDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"		AND terminal_id = :terminalId"+
			"      	AND  TRANS_ID in (select distinct TRANS_ID from MD_DEFAULT_TRANS_ID where TRANS_USAGE ='TRANS') " +
			"       AND ( settl_processing_flag ='N' or " +
			"       (settl_processing_flag ='Y'" +
			"			 AND record_seq_id not in" +
			"		(select distinct record_seq_id from MD_ACCOUNTING_LEDGER UNION select distinct record_seq_id from MD_ACCOUNTING_LEDGER_HIST))) ", nativeQuery = true)
		Page<TransactionCurrent> findTransactions(PageRequest pageRequest, @Param("institutionId") String institutionId, @Param("entityId") String entityId,
												  @Param("fromDate") String fromDate, @Param("toDate") String toDate, @Param("settlMerchHalt") char settlMerchHalt,
												  @Param("terminalId") Integer terminalId);

	@Query(value = "SELECT * FROM MD_TRANSACTION_CURRENT a " +
			"       WHERE a.ACQ_INST_ID = :institutionId " +
			"       AND a.SETTL_MERCH_HALT = :settlMerchHalt " +
			"       AND a.OUTLET_CODE = :entityId " +
			"       AND a.transaction_date BETWEEN TO_DATE(:fromDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"       AND TO_DATE(:toDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"		AND terminal_id = :terminalId"+
			"		AND pan like %:cardNumber% "+
			"      	AND  TRANS_ID in (select distinct TRANS_ID from MD_DEFAULT_TRANS_ID where TRANS_USAGE ='TRANS') " +
			"       AND ( settl_processing_flag ='N' or " +
			"       (settl_processing_flag ='Y'" +
			"			 AND record_seq_id not in" +
			"		(select distinct record_seq_id from MD_ACCOUNTING_LEDGER UNION select distinct record_seq_id from MD_ACCOUNTING_LEDGER_HIST))) ", nativeQuery = true)
	Page<TransactionCurrent> findByAcqInstIdAndOutletCodeAndTerminalIdAndCardNumberIgnoreCaseContainingAndProcessingDateBetweenAndSettlMerchHalt(
			PageRequest pageRequest, @Param("institutionId") String institutionId, @Param("entityId") String entityId,@Param("terminalId") int terminalId,
			@Param("cardNumber") String cardNumber,  @Param("fromDate") String fromDate, @Param("toDate") String toDate,@Param("settlMerchHalt") char settlMerchHalt);


	@Query(value = "SELECT * FROM MD_TRANSACTION_CURRENT a " +
			"       WHERE a.ACQ_INST_ID = :institutionId " +
			"       AND a.SETTL_MERCH_HALT = :settlMerchHalt " +
			"       AND a.OUTLET_CODE = :entityId " +
			"       AND a.transaction_date BETWEEN TO_DATE(:fromDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"       AND TO_DATE(:toDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"		AND terminal_id = :terminalId"+
			"		AND pan like %:cardNumber% "+
			"		AND a.MANUAL_ENTRY = :manualEntry"+
			"      	AND  TRANS_ID in (select distinct TRANS_ID from MD_DEFAULT_TRANS_ID where TRANS_USAGE ='TRANS') " +
			"       AND ( settl_processing_flag ='N' or " +
			"       (settl_processing_flag ='Y'" +
			"			 AND record_seq_id not in" +
			"		(select distinct record_seq_id from MD_ACCOUNTING_LEDGER UNION select distinct record_seq_id from MD_ACCOUNTING_LEDGER_HIST))) ", nativeQuery = true)
	Page<TransactionCurrent> findByAcqInstIdAndOutletCodeAndTerminalIdAndCardNumberIgnoreCaseContainingAndProcessingDateBetweenAndManualEntryAndSettlMerchHalt(
			PageRequest pageRequest, @Param("institutionId") String institutionId, @Param("entityId") String entityId, @Param("terminalId") int terminalId,
			@Param("cardNumber") String cardNumber,  @Param("fromDate") String fromDate, @Param("toDate") String toDate, @Param("manualEntry") char manualEntry,
			@Param("settlMerchHalt") char settlMerchHalt);


	@Query(value = "SELECT * FROM MD_TRANSACTION_CURRENT a " +
			"       WHERE a.ACQ_INST_ID = :institutionId " +
			"       AND a.OUTLET_CODE = :entityId " +
			"       AND a.transaction_date BETWEEN TO_DATE(:fromDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"       AND TO_DATE(:toDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"		AND terminal_id = :terminalId"+
			"		AND pan like %:cardNumber% "+
			"		AND a.MANUAL_ENTRY = :manualEntry"+
			" 		AND TRANS_ID IN (SELECT DISTINCT TRANS_ID " +
			"		FROM MD_DEFAULT_TRANS_ID " +
			"		WHERE TRANS_USAGE = 'TRANS')" +
			"       AND settl_processing_flag = 'P' ", nativeQuery = true)
	Page<TransactionCurrent> findByAcqInstIdAndOutletCodeAndTerminalIdAndCardNumberIgnoreCaseContainingAndProcessingDateBetweenAndManualEntryAndSettlMerchHaltIsNo(
			PageRequest pageRequest, @Param("institutionId") String institutionId, @Param("entityId") String entityId, @Param("terminalId") int terminalId,
			@Param("cardNumber") String cardNumber,  @Param("fromDate") String fromDate, @Param("toDate") String toDate, @Param("manualEntry") char manualEntry);

	@Query(value = "SELECT * FROM MD_TRANSACTION_CURRENT a " +
			"       WHERE a.ACQ_INST_ID = :institutionId " +
			"       AND a.OUTLET_CODE = :entityId " +
			"       AND a.transaction_date BETWEEN TO_DATE(:fromDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"       AND TO_DATE(:toDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"		AND terminal_id = :terminalId"+
			"		AND pan like %:cardNumber% "+
			" 		AND TRANS_ID IN (SELECT DISTINCT TRANS_ID " +
			"		FROM MD_DEFAULT_TRANS_ID " +
			"		WHERE TRANS_USAGE = 'TRANS')" +
			"       AND settl_processing_flag = 'P' ", nativeQuery = true)
	Page<TransactionCurrent> findByAcqInstIdAndOutletCodeAndTerminalIdAndCardNumberIgnoreCaseContainingAndProcessingDateBetweenAndSettlMerchHaltIsNo(
			PageRequest pageRequest, @Param("institutionId") String institutionId, @Param("entityId") String entityId,@Param("terminalId") int terminalId,
			@Param("cardNumber") String cardNumber,  @Param("fromDate") String fromDate, @Param("toDate") String toDate);

	@Query(value = "SELECT * FROM MD_TRANSACTION_CURRENT a " +
			"       WHERE a.ACQ_INST_ID = :institutionId " +
			"       AND a.OUTLET_CODE = :entityId " +
			"       AND a.transaction_date BETWEEN TO_DATE(:fromDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"       AND TO_DATE(:toDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"		AND terminal_id = :terminalId "+
			" 		AND TRANS_ID IN (SELECT DISTINCT TRANS_ID " +
			"		FROM MD_DEFAULT_TRANS_ID " +
			"		WHERE TRANS_USAGE = 'TRANS')" +
			"       AND settl_processing_flag = 'P' ", nativeQuery = true)
	Page<TransactionCurrent> findTransactionsSettlMerchHaltIsNo(PageRequest pageRequest, @Param("institutionId") String institutionId, @Param("entityId") String entityId,
											  @Param("fromDate") String fromDate, @Param("toDate") String toDate, @Param("terminalId") Integer terminalId);

	@Query(value = "SELECT * FROM MD_TRANSACTION_CURRENT a " +
			"       WHERE a.ACQ_INST_ID = :institutionId " +
			"       AND a.OUTLET_CODE = :entityId " +
			"       AND a.transaction_date BETWEEN TO_DATE(:fromDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"       AND TO_DATE(:toDate, 'YYYY-MM-DD HH24:MI:SS') " +
			" 		AND TRANS_ID IN (SELECT DISTINCT TRANS_ID " +
			"		FROM MD_DEFAULT_TRANS_ID " +
			"		WHERE TRANS_USAGE = 'TRANS')" +
			"       AND settl_processing_flag = 'P' ", nativeQuery = true)
	Page<TransactionCurrent> findByAcqInstIdAndOutletCodeAndTerminalIdAndProcessingDateBetweenEntryAndSettlMerchHaltIsNoUpdated(
			PageRequest pageRequest,@Param("institutionId") String institutionId, @Param("entityId") String entityId
			,@Param("fromDate") String fromDate, @Param("toDate") String toDate);

	@Query(value = "SELECT * FROM MD_TRANSACTION_CURRENT a " +
			"       WHERE a.ACQ_INST_ID = :institutionId " +
			"       AND a.SETTL_MERCH_HALT = :settlMerchHalt " +
			"       AND a.OUTLET_CODE = :entityId " +
			"       AND a.transaction_date BETWEEN TO_DATE(:fromDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"       AND TO_DATE(:toDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"      	AND  TRANS_ID in (select distinct TRANS_ID from MD_DEFAULT_TRANS_ID where TRANS_USAGE ='TRANS') " +
			"       AND ( settl_processing_flag ='N' or " +
			"       (settl_processing_flag ='Y'" +
			"			 AND record_seq_id not in" +
			"		(select distinct record_seq_id from MD_ACCOUNTING_LEDGER UNION select distinct record_seq_id from MD_ACCOUNTING_LEDGER_HIST))) ", nativeQuery = true)
	Page<TransactionCurrent> findByAcqInstIdAndOutletCodeAndTerminalIdAndProcessingDateBetweenEntryAndSettlMerchHaltUpdated(
			PageRequest pageRequest,@Param("institutionId") String institutionId, @Param("entityId") String entityId
			,@Param("fromDate") String fromDate, @Param("toDate") String toDate,
			@Param("settlMerchHalt") char settlMerchHalt);

	@Query(value = "SELECT * FROM MD_TRANSACTION_CURRENT a " +
			"       WHERE a.ACQ_INST_ID = :institutionId " +
			"       AND a.SETTL_MERCH_HALT = :settlMerchHalt " +
			"       AND a.OUTLET_CODE = :entityId " +
			"		AND a.MANUAL_ENTRY = :manualEntry"+
			"       AND a.transaction_date BETWEEN TO_DATE(:fromDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"       AND TO_DATE(:toDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"      	AND  TRANS_ID in (select distinct TRANS_ID from MD_DEFAULT_TRANS_ID where TRANS_USAGE ='TRANS') " +
			"       AND ( settl_processing_flag ='N' or " +
			"       (settl_processing_flag ='Y'" +
			"			 AND record_seq_id not in" +
			"		(select distinct record_seq_id from MD_ACCOUNTING_LEDGER UNION select distinct record_seq_id from MD_ACCOUNTING_LEDGER_HIST))) ", nativeQuery = true)
	Page<TransactionCurrent> findByAcqInstIdAndOutletCodeAndTerminalIdAndProcessingDateBetweenAndManualEntryAndSettlMerchHaltUpdated(
			PageRequest pageRequest,@Param("institutionId") String institutionId, @Param("entityId") String entityId
			,@Param("fromDate") String fromDate, @Param("toDate") String toDate,
			@Param("manualEntry") char manualEntry, @Param("settlMerchHalt") char settlMerchHalt);

	@Query(value = "SELECT * FROM MD_TRANSACTION_CURRENT a " +
			"       WHERE a.ACQ_INST_ID = :institutionId " +
			"       AND a.OUTLET_CODE = :entityId " +
			"		AND a.MANUAL_ENTRY = :manualEntry"+
			"       AND a.transaction_date BETWEEN TO_DATE(:fromDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"       AND TO_DATE(:toDate, 'YYYY-MM-DD HH24:MI:SS') " +
			" 		AND TRANS_ID IN (SELECT DISTINCT TRANS_ID " +
			"		FROM MD_DEFAULT_TRANS_ID " +
			"		WHERE TRANS_USAGE = 'TRANS')" +
			"       AND settl_processing_flag = 'P' ", nativeQuery = true)
	Page<TransactionCurrent> findByAcqInstIdAndOutletCodeAndTerminalIdAndProcessingDateBetweenAndManualEntryAndSettlMerchHaltIsNoUpdated(
			PageRequest pageRequest,@Param("institutionId") String institutionId, @Param("entityId") String entityId
			,@Param("fromDate") String fromDate, @Param("toDate") String toDate,
			@Param("manualEntry") char manualEntry);

	@Query(value = "SELECT * FROM MD_TRANSACTION_CURRENT a " +
			"       WHERE a.ACQ_INST_ID = :institutionId " +
			"       AND a.SETTL_MERCH_HALT = :settlMerchHalt " +
			"       AND a.OUTLET_CODE = :entityId " +
			"		AND a.MANUAL_ENTRY = :manualEntry"+
			"       AND a.transaction_date BETWEEN TO_DATE(:fromDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"       AND TO_DATE(:toDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"		AND terminal_id = :terminalId"+
			"      	AND  TRANS_ID in (select distinct TRANS_ID from MD_DEFAULT_TRANS_ID where TRANS_USAGE ='TRANS') " +
			"       AND ( settl_processing_flag ='N' or " +
			"       (settl_processing_flag ='Y'" +
			"			 AND record_seq_id not in" +
			"		(select distinct record_seq_id from MD_ACCOUNTING_LEDGER UNION select distinct record_seq_id from MD_ACCOUNTING_LEDGER_HIST))) ", nativeQuery = true)
	Page<TransactionCurrent> findByAcqInstIdAndOutletCodeAndTerminalIdAndProcessingDateBetweenAndManualEntryAndSettlMerchHaltNative(
			PageRequest pageRequest, @Param("institutionId")String institutionId,@Param("entityId") String entityId,@Param("terminalId") int terminalId,
			@Param("fromDate") String fromDate, @Param("toDate") String toDate,@Param("manualEntry") char manualEntry, @Param("settlMerchHalt") char settlMerchHalt);

	Page<TransactionCurrent> findByAcqInstIdAndProcessingDateBetweenAndSettlMerchHalt(
			PageRequest pageRequest, String acqInstId, Date fromProcessingDate, Date toProcessingDate, char settlMerchHalt);
	
	Page<TransactionCurrent> findByAcqInstIdAndProcessingDateBetweenAndManualEntryAndSettlMerchHalt(
			PageRequest pageRequest, String acqInstId, Date fromProcessingDate, Date toProcessingDate,
			char manualEntry, char settlMerchHalt);
	
	Page<TransactionCurrent> findByAcqInstIdAndSettlMerchHalt(PageRequest pageRequest, String institutionId, char settlMerchHalt);


	@Query(value = "SELECT * FROM MD_TRANSACTION_CURRENT a " +
			"       WHERE a.ACQ_INST_ID = :acqInstId " +
			"       AND a.SETTL_MERCH_HALT = :settlMerchHalt " +
			"       AND a.OUTLET_CODE = :outletCode " +
			"       AND a.transaction_date BETWEEN TO_DATE(:fromProcessingDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"       AND TO_DATE(:toProcessingDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"		AND manual_Entry = :manualEntry "+
			"		AND terminal_id = :terminalId "+
			"     	AND SETTL_MERCH_HALT = :settlMerchHalt "+
			"      	AND  TRANS_ID in (select distinct TRANS_ID from MD_DEFAULT_TRANS_ID where TRANS_USAGE ='TRANS') " +
			"       AND ( settl_processing_flag ='N' or " +
			"       (settl_processing_flag ='Y'" +
			"			 AND record_seq_id not in" +
			"		(select distinct record_seq_id from MD_ACCOUNTING_LEDGER UNION select distinct record_seq_id from MD_ACCOUNTING_LEDGER_HIST))) ", nativeQuery = true)
	Page<TransactionCurrent> findByAcqInstIdAndOutletCodeAndTerminalIdAndProcessingDateBetweenAndManualEntryAndSettlMerchHalt(
			PageRequest pageRequest,  @Param("acqInstId") String acqInstId, @Param("outletCode") String outletCode,@Param("terminalId") int terminalId,
			@Param("fromProcessingDate") String fromProcessingDate,
			@Param("toProcessingDate") String toProcessingDate, @Param("manualEntry") int manualEntry,@Param("settlMerchHalt") char settlMerchHalt);

	@Query(value = "SELECT * FROM MD_TRANSACTION_CURRENT a " +
			"       WHERE a.ACQ_INST_ID = :acqInstId " +
			"       AND a.OUTLET_CODE = :outletCode " +
			"       AND a.transaction_date BETWEEN TO_DATE(:fromProcessingDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"       AND TO_DATE(:toProcessingDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"		AND manual_Entry = :manualEntry "+
			"		AND terminal_id = :terminalId "+
			" 		AND TRANS_ID IN (SELECT DISTINCT TRANS_ID " +
			"		FROM MD_DEFAULT_TRANS_ID " +
			"		WHERE TRANS_USAGE = 'TRANS')" +
			"       AND settl_processing_flag = 'P' ", nativeQuery = true)
	Page<TransactionCurrent> findByAcqInstIdAndOutletCodeAndTerminalIdAndProcessingDateBetweenAndManualEntryAndSettlMerchHaltIsNo(
			PageRequest pageRequest,  @Param("acqInstId") String acqInstId, @Param("outletCode") String outletCode,@Param("terminalId") int terminalId,
			@Param("fromProcessingDate") String fromProcessingDate,
			@Param("toProcessingDate") String toProcessingDate, @Param("manualEntry") int manualEntry);

	@Query(value = "SELECT * FROM MD_TRANSACTION_CURRENT a " +
			"       WHERE a.ACQ_INST_ID = :acqInstId " +
			"       AND a.SETTL_MERCH_HALT = :settlMerchHalt " +
			"       AND a.OUTLET_CODE = :outletCode " +
			"       AND a.transaction_date BETWEEN TO_DATE(:fromProcessingDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"       AND TO_DATE(:toProcessingDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"		AND manual_Entry = :manualEntry "+
			"     	AND SETTL_MERCH_HALT = :settlMerchHalt "+
			"      	AND  TRANS_ID in (select distinct TRANS_ID from MD_DEFAULT_TRANS_ID where TRANS_USAGE ='TRANS') " +
			"       AND ( settl_processing_flag ='N' or " +
			"       (settl_processing_flag ='Y'" +
			"			 AND record_seq_id not in" +
			"		(select distinct record_seq_id from MD_ACCOUNTING_LEDGER UNION select distinct record_seq_id from MD_ACCOUNTING_LEDGER_HIST))) ", nativeQuery = true)
	Page<TransactionCurrent> findByAcqInstIdAndOutletCodeAndProcessingDateBetweenAndManualEntryAndSettlMerchHalt(
			PageRequest pageRequest, @Param("acqInstId") String acqInstId, @Param("outletCode") String outletCode ,@Param("fromProcessingDate") String fromProcessingDate,
			@Param("toProcessingDate") String toProcessingDate, @Param("manualEntry") int manualEntry,@Param("settlMerchHalt") char settlMerchHalt);

	@Query(value = "SELECT * FROM MD_TRANSACTION_CURRENT a " +
			"       WHERE a.ACQ_INST_ID = :acqInstId " +
			"       AND a.SETTL_MERCH_HALT = :settlMerchHalt " +
			"       AND a.OUTLET_CODE = :outletCode " +
			"       AND a.transaction_date BETWEEN TO_DATE(:fromDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"       AND TO_DATE(:toDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"		AND TRANS_ID = :transId"+
			"		AND manual_Entry = :manualEntry "+
			"      	AND  TRANS_ID in (select distinct TRANS_ID from MD_DEFAULT_TRANS_ID where TRANS_USAGE ='TRANS') " +
			"       AND ( settl_processing_flag ='N' or " +
			"       (settl_processing_flag ='Y'" +
			"			 AND record_seq_id not in" +
			"		(select distinct record_seq_id from MD_ACCOUNTING_LEDGER UNION select distinct record_seq_id from MD_ACCOUNTING_LEDGER_HIST))) ", nativeQuery = true)
	Page<TransactionCurrent> findByAcqInstIdAndOutletCodeAndProcessingDateBetweenAndTransIdAndSettlMerchHaltNative(
			PageRequest pageRequest, @Param("acqInstId") String acqInstId,@Param("outletCode") String outletCode,@Param("fromDate") String fromDate,
			@Param("toDate") String toDate,@Param("transId") String transId, @Param("settlMerchHalt") char settlMerchHalt,  @Param("manualEntry") int manualEntry);

	@Query(value = "SELECT * FROM MD_TRANSACTION_CURRENT a " +
			"       WHERE a.ACQ_INST_ID = :acqInstId " +
			"       AND a.OUTLET_CODE = :outletCode " +
			"       AND a.transaction_date BETWEEN TO_DATE(:fromDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"       AND TO_DATE(:toDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"		AND TRANS_ID = :transId"+
			"       AND a.SETTL_MERCH_HALT = :settlMerchHalt " +
			"      	AND  TRANS_ID in (select distinct TRANS_ID from MD_DEFAULT_TRANS_ID where TRANS_USAGE ='TRANS') " +
			"       AND ( settl_processing_flag ='N' or " +
			"       (settl_processing_flag ='Y'" +
			"			 AND record_seq_id not in" +
			"		(select distinct record_seq_id from MD_ACCOUNTING_LEDGER UNION select distinct record_seq_id from MD_ACCOUNTING_LEDGER_HIST))) ", nativeQuery = true)
	Page<TransactionCurrent> findByAcqInstIdAndOutletCodeAndProcessingDateBetweenAndTransIdNative(
			PageRequest pageRequest, @Param("acqInstId") String acqInstId,@Param("outletCode") String outletCode,@Param("fromDate") String fromDate,
			@Param("toDate") String toDate,@Param("transId") String transId, @Param("settlMerchHalt") char settlMerchHalt);

	@Query(value = "SELECT * FROM MD_TRANSACTION_CURRENT a " +
			"       WHERE a.ACQ_INST_ID = :acqInstId " +
			"       AND a.OUTLET_CODE = :outletCode " +
			"       AND a.transaction_date BETWEEN TO_DATE(:fromDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"       AND TO_DATE(:toDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"		AND TRANS_ID = :transId"+
			" 		AND TRANS_ID IN (SELECT DISTINCT TRANS_ID " +
			"		FROM MD_DEFAULT_TRANS_ID " +
			"		WHERE TRANS_USAGE = 'TRANS')" +
			"       AND settl_processing_flag = 'P' ", nativeQuery = true)
	Page<TransactionCurrent> findByAcqInstIdAndOutletCodeAndProcessingDateBetweenAndTransIdNativeAndMerchSettlIsNo(
			PageRequest pageRequest, @Param("acqInstId") String acqInstId,@Param("outletCode") String outletCode,@Param("fromDate") String fromDate,
			@Param("toDate") String toDate,@Param("transId") String transId);

	@Query(value = "SELECT * FROM MD_TRANSACTION_CURRENT a " +
			"       WHERE a.ACQ_INST_ID = :acqInstId " +
			"       AND a.OUTLET_CODE = :outletCode " +
			"       AND a.transaction_date BETWEEN TO_DATE(:fromDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"       AND TO_DATE(:toDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"		AND TRANS_ID = :transId"+
			"		AND manual_Entry = :manualEntry "+
			" 		AND TRANS_ID IN (SELECT DISTINCT TRANS_ID " +
			"		FROM MD_DEFAULT_TRANS_ID " +
			"		WHERE TRANS_USAGE = 'TRANS')" +
			"       AND settl_processing_flag = 'P' ", nativeQuery = true)
	Page<TransactionCurrent> findByAcqInstIdAndOutletCodeAndProcessingDateBetweenAndTransIdAndSettlMerchHaltNativeAndMerchSettleIsNo(
			PageRequest pageRequest, @Param("acqInstId") String acqInstId,@Param("outletCode") String outletCode,@Param("fromDate") String fromDate,
			@Param("toDate") String toDate,@Param("transId") String transId, @Param("manualEntry") char manualEntry);

	@Query(value = "SELECT * FROM MD_TRANSACTION_CURRENT a " +
			"       WHERE a.ACQ_INST_ID = :acqInstId " +
			"       AND a.OUTLET_CODE = :outletCode " +
			"       AND a.SETTL_MERCH_HALT = :settlMerchHalt " +
			"       AND a.transaction_date BETWEEN TO_DATE(:fromDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"       AND TO_DATE(:toDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"		AND pan like %:cardNumber% "+
			"      	AND  TRANS_ID in (select distinct TRANS_ID from MD_DEFAULT_TRANS_ID where TRANS_USAGE ='TRANS') " +
			"       AND ( settl_processing_flag ='N' or " +
			"       (settl_processing_flag ='Y'" +
			"			 AND record_seq_id not in" +
			"		(select distinct record_seq_id from MD_ACCOUNTING_LEDGER UNION select distinct record_seq_id from MD_ACCOUNTING_LEDGER_HIST))) ", nativeQuery = true)
	Page<TransactionCurrent> findByAcqInstIdAndOutletCodeAndCardNumberIgnoreCaseContainingAndProcessingDateBetweenNative(
			PageRequest pageRequest, @Param("acqInstId") String acqInstId,@Param("outletCode") String outletCode,
			@Param("fromDate") String fromDate, @Param("toDate") String toDate,@Param("cardNumber") String cardNumber, @Param("settlMerchHalt") char settlMerchHalt);

	@Query(value = "SELECT * FROM MD_TRANSACTION_CURRENT a " +
			"       WHERE a.ACQ_INST_ID = :acqInstId " +
			"       AND a.OUTLET_CODE = :outletCode " +
			"       AND a.transaction_date BETWEEN TO_DATE(:fromDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"       AND TO_DATE(:toDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"		AND pan like %:cardNumber% "+
			" 		AND TRANS_ID IN (SELECT DISTINCT TRANS_ID " +
			"		FROM MD_DEFAULT_TRANS_ID " +
			"		WHERE TRANS_USAGE = 'TRANS')" +
			"       AND settl_processing_flag = 'P' ", nativeQuery = true)
	Page<TransactionCurrent> findByAcqInstIdAndOutletCodeAndCardNumberIgnoreCaseContainingAndProcessingDateBetweenNativeAndSettleIsNo(
			PageRequest pageRequest, @Param("acqInstId") String acqInstId,@Param("outletCode") String outletCode,
			@Param("fromDate") String fromDate, @Param("toDate") String toDate,@Param("cardNumber") String cardNumber);

	@Query(value = "SELECT * FROM MD_TRANSACTION_CURRENT a " +
			"       WHERE a.ACQ_INST_ID = :acqInstId " +
			"       AND a.OUTLET_CODE = :outletCode " +
			"       AND a.SETTL_MERCH_HALT = :settlMerchHalt " +
			"       AND a.transaction_date BETWEEN TO_DATE(:fromDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"       AND TO_DATE(:toDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"		AND pan like %:cardNumber% "+
			"		AND manual_Entry = :manualEntry "+
			"      	AND  TRANS_ID in (select distinct TRANS_ID from MD_DEFAULT_TRANS_ID where TRANS_USAGE ='TRANS') " +
			"       AND ( settl_processing_flag ='N' or " +
			"       (settl_processing_flag ='Y'" +
			"			 AND record_seq_id not in" +
			"		(select distinct record_seq_id from MD_ACCOUNTING_LEDGER UNION select distinct record_seq_id from MD_ACCOUNTING_LEDGER_HIST))) ", nativeQuery = true)
	Page<TransactionCurrent> findByAcqInstIdAndOutletCodeAndCardNumberIgnoreCaseContainingAndProcessingDateBetweenAndSettlMerchHaltNative(
			PageRequest pageRequest, @Param("acqInstId") String acqInstId,@Param("outletCode") String outletCode,
			@Param("fromDate") String fromDate, @Param("toDate") String toDate,@Param("cardNumber") String cardNumber,
			@Param("settlMerchHalt") char settlMerchHalt,  @Param("manualEntry") int manualEntry);

	@Query(value = "SELECT * FROM MD_TRANSACTION_CURRENT a " +
			"       WHERE a.ACQ_INST_ID = :acqInstId " +
			"       AND a.OUTLET_CODE = :outletCode " +
			"       AND a.transaction_date BETWEEN TO_DATE(:fromDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"       AND TO_DATE(:toDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"		AND pan like %:cardNumber% "+
			"		AND manual_Entry = :manualEntry "+
			" 		AND TRANS_ID IN (SELECT DISTINCT TRANS_ID " +
			"		FROM MD_DEFAULT_TRANS_ID " +
			"		WHERE TRANS_USAGE = 'TRANS')" +
			"       AND settl_processing_flag = 'P' ", nativeQuery = true)
	Page<TransactionCurrent> findByAcqInstIdAndOutletCodeAndCardNumberIgnoreCaseContainingAndProcessingDateBetweenAndSettlMerchHaltNativeAndsettleIsNo(
			PageRequest pageRequest, @Param("acqInstId") String acqInstId,@Param("outletCode") String outletCode,
			@Param("fromDate") String fromDate, @Param("toDate") String toDate,@Param("cardNumber") String cardNumber, @Param("manualEntry") int manualEntry);

	@Query(value = "SELECT * FROM MD_TRANSACTION_CURRENT a " +
			"       WHERE a.ACQ_INST_ID = :acqInstId " +
			"       AND a.OUTLET_CODE = :outletCode " +
			"       AND a.SETTL_MERCH_HALT = :settlMerchHalt " +
			"       AND a.transaction_date BETWEEN TO_DATE(:fromDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"       AND TO_DATE(:toDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"		AND pan like %:cardNumber% "+
			"		AND TRANS_ID = :transId"+
			"      	AND  TRANS_ID in (select distinct TRANS_ID from MD_DEFAULT_TRANS_ID where TRANS_USAGE ='TRANS') " +
			"       AND ( settl_processing_flag ='N' or " +
			"       (settl_processing_flag ='Y'" +
			"			 AND record_seq_id not in" +
			"		(select distinct record_seq_id from MD_ACCOUNTING_LEDGER UNION select distinct record_seq_id from MD_ACCOUNTING_LEDGER_HIST))) ", nativeQuery = true)
	Page<TransactionCurrent> findByAcqInstIdAndOutletCodeAndCardNumberIgnoreCaseContainingAndTransactionIdAndProcessingDateBetweenNative(
			PageRequest pageRequest, @Param("acqInstId") String acqInstId,@Param("outletCode") String outletCode,
			@Param("fromDate") String fromDate, @Param("toDate") String toDate,@Param("cardNumber") String cardNumber,
			@Param("transId") String transId , @Param("settlMerchHalt") char settlMerchHalt);

	@Query(value = "SELECT * FROM MD_TRANSACTION_CURRENT a " +
			"       WHERE a.ACQ_INST_ID = :acqInstId " +
			"       AND a.OUTLET_CODE = :outletCode " +
			"       AND a.transaction_date BETWEEN TO_DATE(:fromDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"       AND TO_DATE(:toDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"		AND pan like %:cardNumber% "+
			"		AND TRANS_ID = :transId"+
			" 		AND TRANS_ID IN (SELECT DISTINCT TRANS_ID " +
			"		FROM MD_DEFAULT_TRANS_ID " +
			"		WHERE TRANS_USAGE = 'TRANS')" +
			"       AND settl_processing_flag = 'P' ", nativeQuery = true)
	Page<TransactionCurrent> findByAcqInstIdAndOutletCodeAndCardNumberIgnoreCaseContainingAndTransactionIdAndProcessingDateBetweenNativeAndSettlIsNo(
			PageRequest pageRequest, @Param("acqInstId") String acqInstId,@Param("outletCode") String outletCode,
			@Param("fromDate") String fromDate, @Param("toDate") String toDate,@Param("cardNumber") String cardNumber,
			@Param("transId") String transId);

	@Query(value = "SELECT * FROM MD_TRANSACTION_CURRENT a " +
			"       WHERE a.ACQ_INST_ID = :acqInstId " +
			"       AND a.OUTLET_CODE = :outletCode " +
			"       AND a.transaction_date BETWEEN TO_DATE(:fromDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"       AND TO_DATE(:toDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"		AND pan like %:cardNumber% "+
			"		AND manual_Entry = :manualEntry "+
			"		AND TRANS_ID = :transId"+
			"       AND a.SETTL_MERCH_HALT = :settlMerchHalt " +
			"      	AND  TRANS_ID in (select distinct TRANS_ID from MD_DEFAULT_TRANS_ID where TRANS_USAGE ='TRANS') " +
			"       AND ( settl_processing_flag ='N' or " +
			"       (settl_processing_flag ='Y'" +
			"			 AND record_seq_id not in" +
			"		(select distinct record_seq_id from MD_ACCOUNTING_LEDGER UNION select distinct record_seq_id from MD_ACCOUNTING_LEDGER_HIST))) ", nativeQuery = true)
	Page<TransactionCurrent> findByAcqInstIdAndOutletCodeAndCardNumberIgnoreCaseContainingAndTransactionIdAndProcessingDateBetweenAndSettlMerchHaltNative(
			PageRequest pageRequest, @Param("acqInstId") String acqInstId,@Param("outletCode") String outletCode,
			@Param("fromDate") String fromDate, @Param("toDate") String toDate,@Param("cardNumber") String cardNumber,
			@Param("transId") String transId, @Param("settlMerchHalt") char settlMerchHalt, @Param("manualEntry") int manualEntry);

	@Query(value = "SELECT * FROM MD_TRANSACTION_CURRENT a " +
			"       WHERE a.ACQ_INST_ID = :acqInstId " +
			"       AND a.OUTLET_CODE = :outletCode " +
			"       AND a.transaction_date BETWEEN TO_DATE(:fromDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"       AND TO_DATE(:toDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"		AND pan like %:cardNumber% "+
			"		AND manual_Entry = :manualEntry "+
			"		AND TRANS_ID = :transId"+
			"      	AND  TRANS_ID in (select distinct TRANS_ID from MD_DEFAULT_TRANS_ID where TRANS_USAGE ='TRANS') " +
			"       AND ( settl_processing_flag ='N' or " +
			"       (settl_processing_flag ='Y'" +
			"			 AND record_seq_id not in" +
			"		(select distinct record_seq_id from MD_ACCOUNTING_LEDGER UNION select distinct record_seq_id from MD_ACCOUNTING_LEDGER_HIST))) ", nativeQuery = true)
	Page<TransactionCurrent> findByAcqInstIdAndOutletCodeAndCardNumberIgnoreCaseContainingAndTransactionIdAndProcessingDateBetweenAndSettlMerchHaltIsNoNative(
			PageRequest pageRequest, @Param("acqInstId") String acqInstId,@Param("outletCode") String outletCode,
			@Param("fromDate") String fromDate, @Param("toDate") String toDate,@Param("cardNumber") String cardNumber,
			@Param("transId") String transId, @Param("manualEntry") int manualEntry);

	@Query(value = "SELECT * FROM MD_TRANSACTION_CURRENT a " +
			"       WHERE a.ACQ_INST_ID = :acqInstId " +
			"       AND a.OUTLET_CODE = :outletCode " +
			"       AND a.SETTL_MERCH_HALT = :settlMerchHalt " +
			"       AND a.transaction_date BETWEEN TO_DATE(:fromDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"       AND TO_DATE(:toDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"		AND pan like %:cardNumber% "+
			"		AND TRANS_ID = :transId"+
			"		AND terminal_id = :terminalId"+
			"      	AND  TRANS_ID in (select distinct TRANS_ID from MD_DEFAULT_TRANS_ID where TRANS_USAGE ='TRANS') " +
			"       AND ( settl_processing_flag ='N' or " +
			"       (settl_processing_flag ='Y'" +
			"			 AND record_seq_id not in" +
			"		(select distinct record_seq_id from MD_ACCOUNTING_LEDGER UNION select distinct record_seq_id from MD_ACCOUNTING_LEDGER_HIST))) ", nativeQuery = true)
	Page<TransactionCurrent> findByAcqInstIdAndOutletCodeAndCardNumberIgnoreCaseContainingAndTransactionIdAndTerminalidAndProcessingDateBetweenNative(
			PageRequest pageRequest, @Param("acqInstId") String acqInstId,@Param("outletCode") String outletCode,
			@Param("fromDate") String fromDate, @Param("toDate") String toDate,@Param("cardNumber") String cardNumber,
			@Param("transId") String transId, @Param("terminalId") int terminalId,@Param("settlMerchHalt") char settlMerchHalt);

	@Query(value = "SELECT * FROM MD_TRANSACTION_CURRENT a " +
			"       WHERE a.ACQ_INST_ID = :acqInstId " +
			"       AND a.OUTLET_CODE = :outletCode " +
			"       AND a.transaction_date BETWEEN TO_DATE(:fromDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"       AND TO_DATE(:toDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"		AND pan like %:cardNumber% "+
			"		AND TRANS_ID = :transId"+
			"		AND terminal_id = :terminalId"+
			" 		AND TRANS_ID IN (SELECT DISTINCT TRANS_ID " +
			"		FROM MD_DEFAULT_TRANS_ID " +
			"		WHERE TRANS_USAGE = 'TRANS')" +
			"       AND settl_processing_flag = 'P' ", nativeQuery = true)
	Page<TransactionCurrent> findByAcqInstIdAndOutletCodeAndCardNumberIgnoreCaseContainingAndTransactionIdAndTerminalidAndProcessingDateBetweenAndSettlIsNoNative(
			PageRequest pageRequest, @Param("acqInstId") String acqInstId,@Param("outletCode") String outletCode,
			@Param("fromDate") String fromDate, @Param("toDate") String toDate,@Param("cardNumber") String cardNumber,
			@Param("transId") String transId, @Param("terminalId") int terminalId);

	@Query(value = "SELECT * FROM MD_TRANSACTION_CURRENT a " +
			"       WHERE a.ACQ_INST_ID = :acqInstId " +
			"       AND a.OUTLET_CODE = :outletCode " +
			"       AND a.transaction_date BETWEEN TO_DATE(:fromDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"       AND TO_DATE(:toDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"		AND pan like %:cardNumber% "+
			"		AND TRANS_ID = :transId"+
			"		AND terminal_id = :terminalId"+
			"		AND manual_Entry = :manualEntry "+
			"       AND a.SETTL_MERCH_HALT = :settlMerchHalt " +
			"      	AND  TRANS_ID in (select distinct TRANS_ID from MD_DEFAULT_TRANS_ID where TRANS_USAGE ='TRANS') " +
			"       AND ( settl_processing_flag ='N' or " +
			"       (settl_processing_flag ='Y'" +
			"			 AND record_seq_id not in" +
			"		(select distinct record_seq_id from MD_ACCOUNTING_LEDGER UNION select distinct record_seq_id from MD_ACCOUNTING_LEDGER_HIST))) ", nativeQuery = true)
	Page<TransactionCurrent> findByAcqInstIdAndOutletCodeAndCardNumberIgnoreCaseContainingAndTransactionIdAndTerminalidAndProcessingDateBetweenAndSettlMerchHaltNative(
			PageRequest pageRequest, @Param("acqInstId") String acqInstId,@Param("outletCode") String outletCode,
			@Param("fromDate") String fromDate, @Param("toDate") String toDate,@Param("cardNumber") String cardNumber,
			@Param("transId") String transId,@Param("terminalId") int terminalId, @Param("settlMerchHalt") char settlMerchHalt,
			@Param("manualEntry") int manualEntry);


	@Query(value = "SELECT * FROM MD_TRANSACTION_CURRENT a " +
			"       WHERE a.ACQ_INST_ID = :acqInstId " +
			"       AND a.OUTLET_CODE = :outletCode " +
			"       AND a.transaction_date BETWEEN TO_DATE(:fromDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"       AND TO_DATE(:toDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"		AND pan like %:cardNumber% "+
			"		AND TRANS_ID = :transId"+
			"		AND terminal_id = :terminalId"+
			"		AND manual_Entry = :manualEntry "+
			"      	AND  TRANS_ID in (select distinct TRANS_ID from MD_DEFAULT_TRANS_ID where TRANS_USAGE ='TRANS') " +
			"       AND ( settl_processing_flag ='N' or " +
			"       (settl_processing_flag ='Y'" +
			"			 AND record_seq_id not in" +
			"		(select distinct record_seq_id from MD_ACCOUNTING_LEDGER UNION select distinct record_seq_id from MD_ACCOUNTING_LEDGER_HIST))) ", nativeQuery = true)
	Page<TransactionCurrent> findByAcqInstIdAndOutletCodeAndCardNumberIgnoreCaseContainingAndTransactionIdAndTerminalidAndProcessingDateBetweenAndSettlMerchHaltIsNoNative(
			PageRequest pageRequest, @Param("acqInstId") String acqInstId,@Param("outletCode") String outletCode,
			@Param("fromDate") String fromDate, @Param("toDate") String toDate,@Param("cardNumber") String cardNumber,
			@Param("transId") String transId,@Param("terminalId") int terminalId, @Param("manualEntry") int manualEntry);


	@Query(value = "SELECT * FROM MD_TRANSACTION_CURRENT a " +
			"       WHERE a.ACQ_INST_ID = :acqInstId " +
			"       AND a.OUTLET_CODE = :outletCode " +
			"       AND a.SETTL_MERCH_HALT = :settlMerchHalt " +
			"       AND a.transaction_date BETWEEN TO_DATE(:fromDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"       AND TO_DATE(:toDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"		AND TRANS_ID = :transId"+
			"		AND terminal_id = :terminalId"+
			"      	AND  TRANS_ID in (select distinct TRANS_ID from MD_DEFAULT_TRANS_ID where TRANS_USAGE ='TRANS') " +
			"       AND ( settl_processing_flag ='N' or " +
			"       (settl_processing_flag ='Y'" +
			"			 AND record_seq_id not in" +
			"		(select distinct record_seq_id from MD_ACCOUNTING_LEDGER UNION select distinct record_seq_id from MD_ACCOUNTING_LEDGER_HIST))) ", nativeQuery = true)
	Page<TransactionCurrent> findByAcqInstIdAndOutletCodeAndTerminalIdAndProcessingDateBetweenAndTransIdAndSettlMerchHalt(
			PageRequest pageRequest, @Param("acqInstId") String acqInstId,@Param("outletCode") String outletCode, @Param("terminalId") int terminalId,
			@Param("fromDate") String fromDate, @Param("toDate") String toDate, @Param("transId") String transId,  @Param("settlMerchHalt") char settlMerchHalt);

	@Query(value = "SELECT * FROM MD_TRANSACTION_CURRENT a " +
			"       WHERE a.ACQ_INST_ID = :acqInstId " +
			"       AND a.OUTLET_CODE = :outletCode " +
			"       AND a.transaction_date BETWEEN TO_DATE(:fromDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"       AND TO_DATE(:toDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"		AND TRANS_ID = :transId"+
			"		AND terminal_id = :terminalId"+
			" 		AND TRANS_ID IN (SELECT DISTINCT TRANS_ID " +
			"		FROM MD_DEFAULT_TRANS_ID " +
			"		WHERE TRANS_USAGE = 'TRANS')" +
			"       AND settl_processing_flag = 'P' ", nativeQuery = true)
	Page<TransactionCurrent> findByAcqInstIdAndOutletCodeAndTerminalIdAndProcessingDateBetweenAndTransIdAndSettlMerchHaltIsNo(
			PageRequest pageRequest, @Param("acqInstId") String acqInstId,@Param("outletCode") String outletCode, @Param("terminalId") int terminalId,
			@Param("fromDate") String fromDate, @Param("toDate") String toDate, @Param("transId") String transId);

	@Query(value = "SELECT * FROM MD_TRANSACTION_CURRENT a " +
			"       WHERE a.ACQ_INST_ID = :acqInstId " +
			"       AND a.OUTLET_CODE = :outletCode " +
			"       AND a.transaction_date BETWEEN TO_DATE(:fromDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"       AND TO_DATE(:toDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"		AND TRANS_ID = :transId"+
			"		AND terminal_id = :terminalId"+
			"		AND manual_Entry = :manualEntry "+
			" 		AND TRANS_ID IN (SELECT DISTINCT TRANS_ID " +
			"		FROM MD_DEFAULT_TRANS_ID " +
			"		WHERE TRANS_USAGE = 'TRANS')" +
			"       AND settl_processing_flag = 'P' ", nativeQuery = true)
	Page<TransactionCurrent> findByAcqInstIdAndOutletCodeAndTerminalIdAndProcessingDateBetweenAndTransIdAndSettlMerchHaltIsNoAndManualEntry(
			PageRequest pageRequest, @Param("acqInstId") String acqInstId,@Param("outletCode") String outletCode, @Param("terminalId") int terminalId,
			@Param("fromDate") String fromDate, @Param("toDate") String toDate, @Param("transId") String transId, @Param("manualEntry") char manualEntry);

	@Query(value = "SELECT * FROM MD_TRANSACTION_CURRENT a " +
			"       WHERE a.ACQ_INST_ID = :acqInstId " +
			"       AND a.OUTLET_CODE = :outletCode " +
			"       AND a.SETTL_MERCH_HALT = :settlMerchHalt " +
			"       AND a.transaction_date BETWEEN TO_DATE(:fromDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"       AND TO_DATE(:toDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"		AND TRANS_ID = :transId"+
			"		AND terminal_id = :terminalId"+
			"		AND manual_Entry = :manualEntry "+
			"      	AND  TRANS_ID in (select distinct TRANS_ID from MD_DEFAULT_TRANS_ID where TRANS_USAGE ='TRANS') " +
			"       AND ( settl_processing_flag ='N' or " +
			"       (settl_processing_flag ='Y'" +
			"			 AND record_seq_id not in" +
			"		(select distinct record_seq_id from MD_ACCOUNTING_LEDGER UNION select distinct record_seq_id from MD_ACCOUNTING_LEDGER_HIST))) ", nativeQuery = true)
	Page<TransactionCurrent> findByAcqInstIdAndOutletCodeAndTerminalIdAndProcessingDateBetweenAndTransIdAndManualEntryAndSettlMerchHalt(
			PageRequest pageRequest, @Param("acqInstId") String acqInstId,@Param("outletCode") String outletCode, @Param("terminalId") int terminalId,
			@Param("fromDate") String fromDate, @Param("toDate") String toDate, @Param("transId") String transId,@Param("manualEntry") char manualEntry,
			@Param("settlMerchHalt") char settlMerchHalt);

	Page<TransactionCurrent> findByAcqInstIdAndOutletCodeAndProcessingDateBetweenAndTransIdAndSettlMerchHalt(
			PageRequest pageRequest, String acqInstId, String outletCode, Date fromProcessingDate,
			Date toProcessingDate, String transId, char settlMerchHalt);
	
	Page<TransactionCurrent> findByAcqInstIdAndOutletCodeAndProcessingDateBetweenAndTransIdAndManualEntryAndSettlMerchHalt(
			PageRequest pageRequest, String acqInstId, String outletCode, Date fromProcessingDate,
			Date toProcessingDate, String transId, char manualEntry , char settlMerchHalt);
	
	Page<TransactionCurrent> findByAcqInstIdAndOutletCodeAndTerminalIdAndCardNumberIgnoreCaseContainingAndProcessingDateBetweenAndTransIdAndSettlMerchHalt(
			PageRequest pageRequest, String acqInstId, String outletCode, int terminalId, String cardNumber,
			Date fromProcessingDate, Date toProcessingDate, String transId, char settlMerchHalt);
	
	Page<TransactionCurrent> findByAcqInstIdAndOutletCodeAndTerminalIdAndCardNumberIgnoreCaseContainingAndProcessingDateBetweenAndTransIdAndManualEntryAndSettlMerchHalt(
			PageRequest pageRequest, String acqInstId, String outletCode, int terminalId, String cardNumber,
			Date fromProcessingDate, Date toProcessingDate, String transId, char manualEntry, char settlMerchHalt);
	
	Page<TransactionCurrent> findByAcqInstIdAndOutletCodeAndCardNumberIgnoreCaseContainingAndProcessingDateBetweenAndTransIdAndSettlMerchHalt(
			PageRequest pageRequest, String acqInstId, String outletCode, String cardNumber,
			Date fromProcessingDate, Date toProcessingDate, String transId, char settlMerchHalt);

	Page<TransactionCurrent> findByAcqInstIdAndOutletCodeAndCardNumberIgnoreCaseContainingAndProcessingDateBetweenAndTransIdAndManualEntryAndSettlMerchHalt(
			PageRequest pageRequest, String acqInstId, String outletCode, String cardNumber,
			Date fromProcessingDate, Date toProcessingDate, String transId, char manualEntry, char settlMerchHalt);
	
	List<TransactionCurrent> findByMicrofilmRefNumberAndTransIdAndSourceAmountAndUsageCode(
			 String microfilmRefNbr, String transId, Float sourceAmount, int usageCode);
	
	List<TransactionCurrent> findByMicrofilmRefNumberAndTransIdAndSourceAmountAndReversalFlag(
			String microfilmRefNbr, String transactionId, Float sourceAmount, String reversalFlag);


	@Query(value = "select distinct TO_CHAR(merch_settl_date, 'DD/MM/YYYY HH:MI:SS.FF6 AM') merch_settl_date from MD_TRANSACTION_CURRENT where " +
			"SETTL_PROCESSING_FLAG ='Y' AND " +
			"MERCH_PAYMENT_DATE NOT IN " +
			"(SELECT DISTINCT MERCH_PAYMENT_DATE FROM MD_ACCOUNTING_LEDGER where institution_id = :acqInstId " +
			"	UNION " +
			"SELECT DISTINCT MERCH_PAYMENT_DATE FROM MD_ACCOUNTING_LEDGER_HIST where institution_id = :acqInstId )", nativeQuery = true)
	List<String> getMerchSettlDateRevert(@Param("acqInstId") String acqInstId);


	@Query(value = "select * from MD_TRANSACTION_CURRENT WHERE merch_Payment_Date IS NULL ", nativeQuery = true)
	Page<TransactionCurrent> findAllNative(PageRequest pageRequest);


	@Query(value = "SELECT * FROM MD_TRANSACTION_CURRENT " +
			"WHERE ACQ_INST_ID = :acqInstId " +
			"AND (:outletCode IS NULL OR OUTLET_CODE = :outletCode) " +
			"AND PROCESSING_DATE BETWEEN TO_DATE(:fromDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"AND TO_DATE(:toDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"AND (:transId IS NULL OR TRANS_ID = :transId) " +
			"AND (:cardNumber IS NULL OR pan = :cardNumber) " +
			"AND (:terminalId IS NULL OR terminal_id = :terminalId) " +
			"AND (:batchId IS NULL OR BATCH_ID = :batchId) "+
			"AND (:authorizationNumber IS NULL OR AUTHORIZATION_NUMBER = :authorizationNumber) "+
			"AND (:manualEntry IS NULL OR  MANUAL_ENTRY = :manualEntry) "+
			"AND TRANS_ID NOT IN (SELECT DISTINCT TRANS_ID FROM MD_DEFAULT_TRANS_ID WHERE TRANS_USAGE = 'FEE') " +
			"AND settl_processing_flag <> 'P' " +
			"AND ((:merchantName IS NULL) OR MERCHANT_NAME=:merchantName) " +
			"AND (:merchantAccountNumber IS NULL OR MERCHANT_ACCOUNT_NUMBER = :merchantAccountNumber)", nativeQuery = true)
	Page<TransactionCurrent> findTransactionSettlMerchHaltYes(
			PageRequest pageRequest,
			@Param("acqInstId") String acqInstId,
			@Param("outletCode") String outletCode,
			@Param("fromDate") String fromDate,
			@Param("toDate") String toDate,
			@Param("cardNumber") String cardNumber,
			@Param("transId") String transId,
			@Param("terminalId") String terminalId,
			@Param("batchId") String batchId,
			@Param("authorizationNumber") String authorizationNumber,
			@Param("manualEntry") Character manualEntry,
			@Param("merchantName") String merchantName,
			@Param("merchantAccountNumber") String merchantAccountNumber
			);


	@Query(value = "SELECT * FROM MD_TRANSACTION_CURRENT " +
			"WHERE ACQ_INST_ID = :acqInstId " +
			"AND (:outletCode IS NULL OR OUTLET_CODE = :outletCode) " +
			"AND PROCESSING_DATE BETWEEN TO_DATE(:fromDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"AND TO_DATE(:toDate, 'YYYY-MM-DD HH24:MI:SS') " +
			"AND (:cardNumber IS NULL OR pan = :cardNumber) " +
			"AND (:transId IS NULL OR TRANS_ID = :transId) " +
			"AND (:terminalId IS NULL OR terminal_id = :terminalId) " +
			"AND (:batchId IS NULL OR BATCH_ID = :batchId) "+
			"AND (:authorizationNumber IS NULL OR AUTHORIZATION_NUMBER = :authorizationNumber) "+
			"AND (:manualEntry IS NULL OR  MANUAL_ENTRY = :manualEntry) "+
			"AND  TRANS_ID NOT IN (select distinct TRANS_ID from MD_DEFAULT_TRANS_ID where TRANS_USAGE ='FEE')  " +
			"AND settl_processing_flag = 'P' " +
			"AND ((:merchantName IS NULL) OR MERCHANT_NAME=:merchantName) " +
			"AND (:merchantAccountNumber IS NULL OR MERCHANT_ACCOUNT_NUMBER = :merchantAccountNumber)", nativeQuery = true)
	Page<TransactionCurrent> findTransactionSettlMerchHaltNo(
			PageRequest pageRequest,
			@Param("acqInstId") String acqInstId,
			@Param("outletCode") String outletCode,
			@Param("fromDate") String fromDate,
			@Param("toDate") String toDate,
			@Param("cardNumber") String cardNumber,
			@Param("transId") String transId,
			@Param("terminalId") String terminalId,
			@Param("batchId") String batchId,
			@Param("authorizationNumber") String authorizationNumber,
			@Param("manualEntry") Character manualEntry,
			@Param("merchantName") String merchantName,
			@Param("merchantAccountNumber") String merchantAccountNumber
	);

	@Query(value="SELECT DISTINCT MERCHANT_NAME FROM MD_TRANSACTION_CURRENT WHERE INSTITUTION_ID IS NULL OR INSTITUTION_ID = :instId",nativeQuery = true)
	List<String> getTransactionMerchantNames(String instId);
}

