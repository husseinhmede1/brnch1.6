package com.mdsl.model.entity;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "MD_TRANSACTION_CURRENT")
public class AcquiringTransaction implements Cloneable {	
	@Id
	@Column(name = "RECORD_SEQ_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "record_seq_id")
	@SequenceGenerator(name = "record_seq_id", sequenceName = "MD_RECORD_SEQ", allocationSize = 1)
	private Integer acquiringTransactionId;

	@ManyToOne
	@JoinColumn(name = "INSTITUTION_ID")
	private Institution institution;

	@Column(name = "OUTLET_CODE")
    private String entities;
	
	@ManyToOne
	@JoinColumns({
	    @JoinColumn(name = "OUTLET_CODE", referencedColumnName = "ENTITY_ID", insertable = false, updatable = false),
	    @JoinColumn(name = "INSTITUTION_ID", referencedColumnName = "INSTITUTION_ID", insertable = false, updatable = false)
	})
	private Entities entitiesObject;
	
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

    @ManyToOne
	@JoinColumn(name = "MERCHANT_ACCOUNT_CURR", referencedColumnName = "CURRENCY_CODE")
	private Currency merchantAccountCurr;
    
    @Column(name = "MERCHANT_BRANCH")
    private String merchantBranch;
    
    @Column(name = "MERCHANT_BANK_SWIFT")
    private String merchantBankSwift;
    
    @Column(name = "MERCHANT_BENEFICIARY_NAME")
    private String merchantBeneficiaryName;

    @Column(name = "MERCH_PAYMENT_DATE")
    private Timestamp merchPaymentDate;

 	@Column(name = "TERMINAL_ID")
	private String terminal;
	
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
	private Integer cardSeqNbr;
    
    @Column(name = "EXPIRY_DATE")
    private Timestamp expiryDate;

    @Column(name = "MICROFILM_REF_NBR")
    private String microfilmRefNbr;
    
    @Column(name = "REF_NBR_SEQ")
    private Integer refNumberSequence;

    @Column(name = "ISSUER_ACQ_PROFILE")
    private String issuerAcqProfile;

    @ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CARD_SCHEME", referencedColumnName = "CARD_SCHEME_ID")
	private CardScheme cardScheme;

    @Column(name = "LINKUP_CODE")
    private String linkupCode;

    @Column(name = "PROCESSING_CODE")
    private Integer processingCode;

    @ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "TRANS_ID",referencedColumnName = "TRANS_ID")
	private DefaultTransactionId transactionId;
    
    @Column(name = "REVERSAL_FLAG")
    private String reversalFlag;

    @Column(name = "SOURCE_AMOUNT")
    private Float sourceAmount;

    @ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SOURCE_CURRENCY", referencedColumnName = "CURRENCY_CODE")
	private Currency sourceCurrency;

    @Column(name = "BILLING_AMOUNT")
    private Double billingAmount;

    @ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "BILLING_CURRENCY", referencedColumnName = "CURRENCY_CODE")
	private Currency billingCurrency;

    @Column(name = "SETTLEMENT_AMOUNT")
    private Double settlementAmount;

    @ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SETTLEMENT_CURRENCY", referencedColumnName = "CURRENCY_CODE")
	private Currency settlementCurrency;

    @Column(name = "LOCAL_AMOUNT")
    private Double localAmount;

    @ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "LOCAL_CURRENCY", referencedColumnName = "CURRENCY_CODE")
	private Currency localCurrency;

    @Column(name = "TIPS_AMOUNT")
    private Double tipsAmount;

    @Column(name = "TIPS_SETTL_AMOUNT")
    private Double tipsSettlementAmount;

    @ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "TIPS_CURRENCY", referencedColumnName = "CURRENCY_CODE")
	private Currency tipsCurrency;

    @Column(name = "DCC_AMOUNT")
    private Double dccAmount;

    @ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "DCC_CURRENCY", referencedColumnName = "CURRENCY_CODE")
	private Currency dccCurrency;
    
    @Column(name = "DCC_MERCHANT_AMOUNT")
    private Double dccMerchantAmount;

    @Column(name = "DCC_MERCHANT_SETTL_AMOUNT")
    private Double dccMerchantSettlAmount;

    @ManyToOne
	@JoinColumn(name = "DCC_MERCHANT_SETTL_AMOUNT_CURR", referencedColumnName = "CURRENCY_CODE")
	private Currency dccMerchantSettlAmountCurrency;

    @Column(name = "MERCHANT_COMMISSION")
	private Float merchantCommisionNumber;

    @Column(name = "MERCH_MARKUP")
	private Float merchantMarkUpNumber;

    @Column(name = "CH_MARKUP")
	private Float chMarkUpNumber;

    @Column(name = "FEE_AMOUNT1")
    private Double feeAmount1;

    @ManyToOne
	@JoinColumn(name = "FEE_AMOUNT1_CURRENCY", referencedColumnName = "CURRENCY_CODE")
	private Currency feeAmount1Currency;

    @Column(name = "FEE_AMOUNT2")
    private Double feeAmount2;

    @ManyToOne
	@JoinColumn(name = "FEE_AMOUNT2_CURRENCY", referencedColumnName = "CURRENCY_CODE")
	private Currency feeAmount2Currency;

    @Column(name = "TRANSACTION_AMOUNT")
    private Double transactionAmount;

    @ManyToOne
	@JoinColumn(name = "TRANS_CURRENCY", referencedColumnName = "CURRENCY_CODE")
	private Currency transactionCurrency;

    @Column(name = "TRANSACTION_DATE")
    private Timestamp transactionDate;

    @Column(name = "TRANS_TIME")
	private Date transactionTime;
    
    @Column(name = "AUTHORIZATION_NUMBER")
    private String authorizationCode;

    @Column(name = "BILLING_PROCESSING_FLAG")
    private Character billingProcessingFlag;

    @Column(name = "SETTL_PROCESSING_FLAG")
    private Character settlProcessingFlag;

    @Column(name = "SETTL_PROCESSING_NBR")
    private Integer settlProcessingNbr;

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
    private String issuerRefNum;

    @ManyToOne
	@JoinColumn(name = "ACQ_INST_ID")
	private Institution acqInstitutionId;

    @Column(name = "ISSUER_INST_ID")
    private String issuerInstitutionId;

    @Column(name = "ACQUIRER_DATA")
    private String acquirerData;

    @Column(name = "ISSUER_DATA")
    private String issuerData;
    
    @Column(name = "TERMINAL_DATA")
    private String terminalData;

    @Column(name = "ECOMMERCE_FLAG")
    private Character eCommerceFlag;

    @Column(name = "ORIGIN_NETWORK")
    private String originNetwork;

    @Column(name = "DESTINATION_NETWORK")
    private String destinationNetwork;

    @Column(name = "PROCESSING_DATE")
    private Date processingDate;

    @Column(name = "MERCH_SETTL_DATE")
    private Date merchantSettlDate;

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
    private String chergebackReason;

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

    @ManyToOne
	@JoinColumn(name = "REASON_CODE")
	private SystemCode reasonCode;

    @Column(name = "ACQUIRER_RECORD_TO_APPEAR")
    private Character accquierRecordToAppear;

    @Column(name = "ISSUER_RECORD_TO_APPEAR")
    private Character issueRecordToAppear;

    @Column(name = "PROCESSING_REF_NBR1")
    private String processingRefNbr1;

    @Column(name = "PROCESSING_REF_NBR2")
    private String processingRefNbr2;

    @Column(name = "PROCESSING_REF_NBR3")
    private String processingRefNbr3;

    @Column(name = "PROCESSING_REF_NBR4")
    private String processingRefNbr4;

    @Column(name = "PROCESSING_REF_NBR5")
    private String processingRefNbr5;

    @Column(name = "USER_CREATE", nullable = false)
    private String userCreate;

    @Column(name = "DATE_CREATE", nullable = false)
    private Date dateCreate;

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public AcquiringTransaction() {
	}

	public AcquiringTransaction(Institution institution, Entities entities, Date processingDate,
			Character manualEntry) {
		super();
		this.institution = institution;
		this.entitiesObject = entities;
		this.processingDate = processingDate;
		this.manualEntry = manualEntry;

	}

}
