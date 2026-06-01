package com.mdsl.repository;

import com.mdsl.model.entity.AcquiringTransactionHold;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface AcquiringTransactionHoldRepository extends JpaRepository<AcquiringTransactionHold, Integer> {

    @Modifying
    @Transactional
    @Query(value ="Insert into MD_TRANSACTION_CURRENT_HOLD_TMP SELECT * from MD_TRANSACTION_CURRENT  Where MICROFILM_REF_NBR = :microfilmRefNbr " +
            "And outlet_code = :entityId " +
            "And terminal_id = :terminalId " +
            "And NVL(BATCH_ID,'NULL') =NVL(:batchId,'NULL') " +
            "And LINKUP_CODE = :linkupCode " +
            "And PROCESSING_REF_NBR5 = :processingRefNbr5 " +
            "And SETTL_PROCESSING_FLAG = :settleProcessionFlag " +
            "And SETTL_MERCH_HALT = :haltMerchPay " +
            "AND TRANS_ID in (select distinct trans_id from MD_DEFAULT_TRANS_ID where TRANS_USAGE ='FEE')", nativeQuery = true)
    void insertTempData(String microfilmRefNbr, String entityId, String terminalId, String batchId, String linkupCode,
                        String processingRefNbr5, Character settleProcessionFlag, Character haltMerchPay);
}
