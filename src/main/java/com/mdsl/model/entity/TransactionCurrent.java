package com.mdsl.model.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

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
@Entity
@Table(name = "MD_TRANSACTION_CURRENT")
public class TransactionCurrent implements Serializable {
	
	private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "RECORD_SEQ_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "record_seq_id")
    @SequenceGenerator(name = "record_seq_id", sequenceName = "MD_RECORD_SEQ", allocationSize = 1)
    private Integer recordSeqId;

    @Column(name = "INSTITUTION_ID")
    private String institutionId;

    @Column(name = "OUTLET_CODE")
    private String outletCode;

    @Column(name = "MERCHANT_NAME")
    private String merchantName;
    
    @Column(name = "MERCHANT_CATEGORY")
    private String merchantCategory;
    
    @Column(name = "MERCHANT_BANK")
    private String merchantBank;

    @Column(name = "MERCHANT_ACCOUNT_NUMBER")
    private String merchantAccountNumber;
    
    @Column(name = "MERCHANT_IBAN")
    private String merchantIBAN;

    @Column(name = "MERCHANT_ACCOUNT_CURR")
    private String merchantAccountCurrency;
    
    @Column(name = "MERCHANT_BRANCH")
    private String merchantBranch;
    
    @Column(name = "MERCHANT_BANK_SWIFT")
    private String merchantBankSwift;
    
    @Column(name = "MERCHANT_BENEFICIARY_NAME")
    private String merchantBeneficiaryName;

    @Column(name = "MERCH_PAYMENT_DATE")
    private Timestamp merchPaymentDate;

    @Column(name = "TERMINAL_ID")
    private String terminalId;

    @Column(name = "TERMINAL_LOCATION")
    private String terminalLocation;

    @Column(name = "SOURCE_REFERENCE")
    private String sourceReference;

    @Column(name = "PAN")
    private String pan;

    @Column(name = "CARD_NUMBER")
    private String cardNumber;

    @Column(name = "MASK_PAN")
    private String maskPan;

    @Column(name = "CARD_SEQ_NBR")
    private Integer cardSeqNumber;

    @Column(name = "EXPIRY_DATE")
    private Timestamp expiryDate;

    @Column(name = "MICROFILM_REF_NBR")
    private String microfilmRefNumber;

    @Column(name = "REF_NBR_SEQ")
    private Integer refNumberSeq;

    @Column(name = "ISSUER_ACQ_PROFILE")
    private String issuerAcqProfile;

    @Column(name = "CARD_SCHEME")
    private String cardScheme;

    @Column(name = "LINKUP_CODE")
    private String linkupCode;

    @Column(name = "PROCESSING_CODE")
    private Integer processingCode;

    @Column(name = "TRANS_ID")
    private String transId;

    @Column(name = "REVERSAL_FLAG")
    private String reversalFlag;

    @Column(name = "SOURCE_AMOUNT")
    private Float sourceAmount;

    @Column(name = "SOURCE_CURRENCY")
    private String sourceCurrency;

    @Column(name = "BILLING_AMOUNT")
    private Double billingAmount;

    @Column(name = "BILLING_CURRENCY")
    private String billingCurrency;

    @Column(name = "SETTLEMENT_AMOUNT")
    private Double settlementAmount;

    @Column(name = "SETTLEMENT_CURRENCY")
    private String settlementCurrency;

    @Column(name = "LOCAL_AMOUNT")
    private Double localAmount;

    @Column(name = "LOCAL_CURRENCY")
    private String localCurrency;

    @Column(name = "TIPS_AMOUNT")
    private Double tipsAmount;

    @Column(name = "TIPS_SETTL_AMOUNT")
    private Double tipsSettlementAmount;

    @Column(name = "TIPS_CURRENCY")
    private String tipsCurrency;

    @Column(name = "DCC_AMOUNT")
    private Double dccAmount;

    @Column(name = "DCC_CURRENCY")
    private String dccCurrency;

    @Column(name = "DCC_MERCHANT_AMOUNT")
    private Double dccMerchantAmount;

    @Column(name = "DCC_MERCHANT_SETTL_AMOUNT")
    private Double dccMerchantSettlementAmount;

    @Column(name = "DCC_MERCHANT_SETTL_AMOUNT_CURR")
    private String dccMerchantSettlementCurrency;

    @Column(name = "MERCHANT_COMMISSION")
    private Double merchantCommission;

    @Column(name = "MERCH_MARKUP")
    private Double merchMarkup;

    @Column(name = "CH_MARKUP")
    private Double chMarkup;

    @Column(name = "FEE_AMOUNT1")
    private Double feeAmount1;

    @Column(name = "FEE_AMOUNT1_CURRENCY")
    private String feeAmount1Currency;

    @Column(name = "FEE_AMOUNT2")
    private Double feeAmount2;

    @Column(name = "FEE_AMOUNT2_CURRENCY")
    private String feeAmount2Currency;

    @Column(name = "TRANSACTION_AMOUNT")
    private Double transactionAmount;

    @Column(name = "TRANS_CURRENCY")
    private String transCurrency;

    @Column(name = "TRANSACTION_DATE")
    private Timestamp transactionDate;

    @Column(name = "TRANS_TIME")
    private Timestamp transTime;

    @Column(name = "AUTHORIZATION_NUMBER")
    private String authorizationNumber;

    @Column(name = "BILLING_PROCESSING_FLAG")
    private Character billingProcessingFlag;

    @Column(name = "SETTL_PROCESSING_FLAG")
    private Character settlProcessingFlag;

    @Column(name = "SETTL_PROCESSING_NBR")
    private Integer settlProcessingNumber;

    @Column(name = "SETTL_MERCH_HALT")
    private Character settlMerchHalt;

    @Column(name = "BATCH_ID")
    private String batchId;

    @Column(name = "MERCHANT_COUNTRY")
    private String merchantCountry;

    @Column(name = "ACQUIRER_ID")
    private String acquirerId;

    @Column(name = "ISSUER_ID")
    private String issuerId;

    @Column(name = "ISSUER_REF_NBR")
    private String issuerRefNumber;

    @Column(name = "ACQ_INST_ID")
    private String acqInstId;

    @Column(name = "ISSUER_INST_ID")
    private String issuerInstId;

    @Column(name = "ACQUIRER_DATA")
    private String acquirerData;

    @Column(name = "ISSUER_DATA")
    private String issuerData;

    @Column(name = "TERMINAL_DATA")
    private String terminalData;

    @Column(name = "ECOMMERCE_FLAG")
    private Character ecommerceFlag;

    @Column(name = "ORIGIN_NETWORK")
    private String originNetwork;

    @Column(name = "DESTINATION_NETWORK")
    private String destinationNetwork;

    @Column(name = "PROCESSING_DATE")
    private Timestamp processingDate;

    @Column(name = "MERCH_SETTL_DATE")
    private Timestamp merchSettlementDate;

    @Column(name = "REVERSAL_REASON")
    private String reversalReason;

    @Column(name = "REVERSAL_COMMENT")
    private String reversalComment;

    @Column(name = "MANUAL_ENTRY")
    private Character manualEntry;

    @Column(name = "MANUAL_COMMENT")
    private String manualComment;

    @Column(name = "CHARGEBACK_COMMENT")
    private String chargebackComment;

    @Column(name = "CHERGEBACK_REASON")
    private String chargebackReason;

    @Column(name = "REPRESENTMENT_COMMENT")
    private String representmentComment;

    @Column(name = "REPRESENTMENT_REASON")
    private String representmentReason;

    @Column(name = "CONFIRM_STOPPING_PAYMENT")
    private Character confirmStoppingPayment;

    @Column(name = "TO_BE_PAID_TO_MERCHANT")
    private Character toBePaidToMerchant;

    @Column(name = "PAY_HALT_COMMENT")
    private String payHaltComment;

    @Column(name = "PAY_HALT_STATUS")
    private String payHaltStatus;

    @Column(name = "USAGE_CODE")
    private Integer usageCode;

    @Column(name = "REASON_CODE")
    private Integer reasonCode;

    @Column(name = "ACQUIRER_RECORD_TO_APPEAR")
    private Character acquirerRecordToAppear;

    @Column(name = "ISSUER_RECORD_TO_APPEAR")
    private Character issuerRecordToAppear;

    @Column(name = "PROCESSING_REF_NBR1")
    private String processingRefNumber1;

    @Column(name = "PROCESSING_REF_NBR2")
    private String processingRefNumber2;

    @Column(name = "PROCESSING_REF_NBR3")
    private String processingRefNumber3;

    @Column(name = "PROCESSING_REF_NBR4")
    private String processingRefNumber4;

    @Column(name = "PROCESSING_REF_NBR5")
    private String processingRefNumber5;

    @Column(name = "USER_CREATE", nullable = false)
    private String userCreate;

    @Column(name = "DATE_CREATE", nullable = false)
    private Timestamp dateCreate;
}
