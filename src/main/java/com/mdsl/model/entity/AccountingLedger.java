package com.mdsl.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "MD_ACCOUNTING_LEDGER")
public class AccountingLedger implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ACCOUNTING_LEDGER_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "accounting_ledger_id")
    @SequenceGenerator(name = "accounting_ledger_id", sequenceName = "MD_ACCOUNTING_LEDGER_SEQ", allocationSize = 1)
    private Integer accountingLedgerId;

    @Column(name = "INSTITUTION_ID", nullable = false)
    private String institutionId;

    @Column(name = "DBCR_EFFECTIVE_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dbcrEffectiveDate;

    @Column(name = "LINE_NUMBER")
    private Integer lineNumber;

    @Column(name = "RELATED_LINE_NUMBER")
    private Integer relatedLineNumber;

    @Column(name = "ACCT_TEMPLATE_DTL_ID")
    private Integer accountTemplateDetailId;

    @Column(name = "TRANS_ID")
    private String transId;

    @Column(name = "ACCOUNT_ORIGIN")
    private String accountOrigin;
    
    @Column(name = "MERCH_SETTL_DATE")
    private String merchSettlementDate;
    
    @Column(name = "MERCH_PAYMENT_DATE")
    private String merchPaymentDate;

    @Column(name = "DESTINATION_INSTITUTION")
    private String destinationInstitution;

    @Column(name = "ACCOUNT_TYPE")
    private String accountType;

    @Column(name = "AMOUNT_TYPE")
    private String amountType;

    @Column(name = "PERCENTAGE_APPLIED")
    private BigDecimal percentageApplied;

    @Column(name = "SIGN_FLAG")
    private String signFlag;

    @Column(name = "LINE_DESCRIPTION")
    private String lineDescription;

    @Column(name = "CARD_SCHEME_ID")
    private String cardSchemeId;

    @Column(name = "ISSUER_ACQ_PROFILE")
    private String issuerAcqProfile;

    @Column(name = "BANK_CODE", nullable = false)
    private String bankCode;

    @Column(name = "ACCOUNT_NUMBER", nullable = false)
    private String accountNumber;

    @Column(name = "CURRENCY_CODE", nullable = false)
    private String currencyCode;

    @Column(name = "IBAN", nullable = false)
    private String iban;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @Column(name="FILENAME")
    private String fileName;

    @Column(name = "RELATED_TRANSACTION")
    private String relatedTransaction;

    @Column(name = "CREATED_BY", nullable = false)
    private Integer createdBy;

    @CreationTimestamp
    @Column(name = "CREATED_DATE", nullable = false)
    private Date createdDate;

    @Column(name = "UPDATED_BY")
    private Integer updatedBy;

    @Column(name = "UPDATED_DATE")
    private Date updatedDate;
}