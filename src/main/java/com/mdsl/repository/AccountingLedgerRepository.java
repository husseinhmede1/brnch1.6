package com.mdsl.repository;

import com.mdsl.model.entity.AccountingLedger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountingLedgerRepository extends JpaRepository<AccountingLedger, Integer> {

    @Query(value = "SELECT DISTINCT TO_CHAR(MERCH_PAYMENT_DATE, 'DD/MM/YYYY HH:MI:SS.FF6 AM') MERCH_PAYMENT_DATE FROM MD_ACCOUNTING_LEDGER where institution_id = :acqInstId and EXPORT_STATUS ='0'" +
            "UNION " +
            "SELECT DISTINCT TO_CHAR(MERCH_PAYMENT_DATE, 'DD/MM/YYYY HH:MI:SS.FF6 AM') FROM MD_ACCOUNTING_LEDGER_HIST where institution_id = :acqInstId and EXPORT_STATUS ='0' ", nativeQuery = true)
    List<String> getMerchantPaymentDate(@Param("acqInstId") String acqInstId);

    @Query(value = "SELECT DISTINCT FILENAME " +
            "  FROM MD_ACCOUNTING_LEDGER " +
            " WHERE EXPORT_STATUS = 1 and institution_id = :institutionId " +
            "UNION " +
            "SELECT DISTINCT FILENAME " +
            "  FROM MD_ACCOUNTING_LEDGER_HIST " +
            " WHERE EXPORT_STATUS = 1 and institution_id = :institutionId",nativeQuery = true)
    List<String> getDistinctFileNames(@Param("institutionId") String institutionId);
}