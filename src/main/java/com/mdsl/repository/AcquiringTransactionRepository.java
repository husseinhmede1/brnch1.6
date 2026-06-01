package com.mdsl.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mdsl.model.entity.AccountingLedger;
import com.mdsl.model.entity.AcquiringTransaction;

@Repository
public interface AcquiringTransactionRepository extends JpaRepository<AcquiringTransaction, Integer> {

    Page<AcquiringTransaction> findAll(Pageable pageable);

    @Query("from AcquiringTransaction acq where acq.institution.institutionId=:institutionId")
    Page<AcquiringTransaction> findByInstitution(String institutionId, Pageable pageable);


    @Query("from AcquiringTransaction acq where acq.entitiesObject.entityId=:entitiesId")
    List<AcquiringTransaction> findByEntitiesObject(String entitiesId, Sort by);

    @Query("from AcquiringTransaction acq where acq.entitiesObject.entityId=:entitiesId And acq.institution.institutionId=:institutionId ")
    Page<AcquiringTransaction> findByEntitiesObjectAndInstitution(String entitiesId, String institutionId, Pageable pageable);

    Page<AcquiringTransaction> findByInstitution_InstitutionId(PageRequest pageRequest, String institutionId);


    List<AcquiringTransaction> findByUserCreate(String string);

    @Modifying
    @Transactional
    @Query(value = "update md_transaction_current SET" +
            " settl_processing_flag  = :settlProcessingFlag," +
            " settl_merch_halt = :haltMerchPay," +
            " SETTL_PROCESSING_NBR = NULL," +
            " PROCESSING_DATE = NULL," +
            " MERCHANT_COMMISSION = NULL," +
            " DCC_MERCHANT_SETTL_AMOUNT = NULL," +
            " SETTLEMENT_CURRENCY = NULL," +
            " SETTLEMENT_AMOUNT = NULL," +
            " DCC_MERCHANT_SETTL_AMOUNT_CURR = NULL," +
            " TIPS_SETTL_AMOUNT = NULL," +
            " MERCHANT_BANK = NULL," +
            " MERCHANT_ACCOUNT_NUMBER = NULL," +
            " MERCHANT_ACCOUNT_CURR = NULL," +
            " MERCHANT_IBAN = NULL," +
            " MERCHANT_BRANCH = NULL," +
            " MERCHANT_BANK_SWIFT = NULL," +
            " MERCHANT_BENEFICIARY_NAME = NULL," +
            " MERCH_PAYMENT_DATE = NULL," +
            " MERCH_SETTL_DATE = NULL, " +
            " PROCESSING_REF_NBR5 = :processingRefNbr5 " +
            " Where MICROFILM_REF_NBR = :microfilmRefNbr" +
            " And OUTLET_CODE = :outletCode" +
            " And TERMINAL_ID = :terminalId" +
            " And NVL(BATCH_ID,'NULL') =NVL(:batchId,'NULL')" +
            " And LINKUP_CODE = :linkupCode", nativeQuery = true)
    void haltTransaction(Character settlProcessingFlag, Character haltMerchPay, String processingRefNbr5, String microfilmRefNbr,
                         String outletCode, String terminalId, String batchId, String linkupCode);

    @Modifying
    @Transactional
    @Query(value = "Delete from MD_TRANSACTION_CURRENT  Where MICROFILM_REF_NBR = :microfilmRefNbr " +
            "              And outlet_code = :entityId " +
            "              And terminal_id = :terminalId " +
            "              And NVL(BATCH_ID,'NULL') = NVL(:batchId,'NULL')" +
            "              And LINKUP_CODE = :linkupCode " +
            "              And PROCESSING_REF_NBR5 = :processingRefNbr5 " +
            "              And SETTL_PROCESSING_FLAG = :settleProcessionFlag " +
            "              And SETTL_MERCH_HALT = :haltMerchPay " +
            "              AND TRANS_ID in (select distinct trans_id from MD_DEFAULT_TRANS_ID where TRANS_USAGE ='FEE')", nativeQuery = true)
    void deleteHoldTransaction(String microfilmRefNbr, String entityId, String terminalId,
                               String batchId, String linkupCode, String processingRefNbr5, Character settleProcessionFlag, Character haltMerchPay);

    @Query(value = "SELECT count(*) " +
                    "FROM MD_TRANSACTION_CURRENT T " +
                "WHERE T.record_seq_id = 2168 " +
                    "AND ( " +
                            "T.settl_processing_flag = 'N' " +
                            "OR ( " +
                                    "T.settl_processing_flag = 'Y' " +
                                    "AND T.record_seq_id NOT IN ( " +
                                        "SELECT DISTINCT RELATED_TRANSACTION " +
                                          "FROM MD_ACCOUNTING_LEDGER " +
                                        "UNION " +
                                        "SELECT DISTINCT RELATED_TRANSACTION " +
                                          "FROM MD_ACCOUNTING_LEDGER_HIST )))", nativeQuery = true)
    Integer findProcessed(Integer acquiringTransactionId);
    
    @Modifying
    @Query("UPDATE AcquiringTransaction SET settlProcessingFlag = 'N' , settlMerchHalt='N' WHERE acquiringTransactionId in (:acquiringTransactionId)")
    void UnhaltTransactions(@Param("acquiringTransactionId") List<Integer> acquiringTransactionId);
    
    @Query(value = 
    	    "SELECT (" +
    	    "  (SELECT COUNT(*) FROM MD_ACCOUNTING_LEDGER WHERE RELATED_TRANSACTION = :id) +" +
    	    "  (SELECT COUNT(*) FROM MD_ACCOUNTING_LEDGER_HIST WHERE RELATED_TRANSACTION = :id)" +
    	    ") AS total_count FROM dual",
    	    nativeQuery = true)
    	Integer validateAcountingLedger(@Param("id") String id);



}