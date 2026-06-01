package com.mdsl.model.entity;

import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Md_transaction_manual")
public class ManualMerchantTransaction {

	@Id
	@Column(name="record_seq_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "record_seq_id")
	@SequenceGenerator(name = "record_seq_id", sequenceName = "MD_MER_TRANS_SEQ", allocationSize = 1)
	private Integer merchantTransactionId;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "ACQ_INST_ID",referencedColumnName = "INSTITUTION_ID")
	private Institution institution;

	@Column(name = "PAN",columnDefinition = "varchar(19)")
	private String pan;

	@Column(name = "CARD_SEQ_NBR")
	private Integer cardSeqNbr;
	
	@Column(name = "CARD_NBR",columnDefinition = "varchar(19)")
	private String cardNumber;

	@Column(name = "TRANS_ID")
	private String transaction;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumns({
			@JoinColumn(name = "TRANS_ID", referencedColumnName = "TRANS_ID", insertable = false, updatable = false),
			@JoinColumn(name = "ACQ_INST_ID", referencedColumnName = "INSTITUTION_ID", insertable = false, updatable = false)
	})
	private DefaultTransactionId transactionEntity;

	@Column(name = "REVERSAL_FLAG",columnDefinition = "varchar(2)")
	private String reversalFlag;

	@ManyToOne
	@JoinColumn(name = "REASON_CODE")
	private SystemCode reasonCode;
	
	@Column(name = "COMMENTS",columnDefinition = "varchar(100)")
	private String comments;

    @Column(name = "OUTLET_CODE")
    private String entities;
	
	@ManyToOne
	@JoinColumns({
	    @JoinColumn(name = "OUTLET_CODE", referencedColumnName = "ENTITY_ID", insertable = false, updatable = false),
	    @JoinColumn(name = "ACQ_INST_ID", referencedColumnName = "INSTITUTION_ID", insertable = false, updatable = false)
	})
	private Entities entitiesObject;

    @Column(name = "TERMINAL_ID")
	private String terminal;
	
	@ManyToOne
	@JoinColumns({
	    @JoinColumn(name = "TERMINAL_ID", referencedColumnName = "TERMINAL_ID", insertable = false, updatable = false),
	    @JoinColumn(name = "ACQ_INST_ID", referencedColumnName = "INSTITUTION_ID", insertable = false, updatable = false)
	})
	private Terminal terminalEntity;
	
	
	@Column(name="TRANSACTION_AMOUNT",columnDefinition = "Decimal(26,8)")
	private Float transactionAmount;
	
	@ManyToOne
	@JoinColumn(name="TRANSACTION_CURRENCY", referencedColumnName = "CURRENCY_CODE")
	private Currency transactionCurrency;
	
	@Column(name="TIPS_AMOUNT",columnDefinition = "Decimal(26,8)")
	private Float tipsAmount;
	
	@ManyToOne
	@JoinColumn(name="TIPS_CURRENCY", referencedColumnName = "CURRENCY_CODE")
	private Currency tipsCurrency;
	
	@Column(name="Transaction_date")
	private Date transactionDate;

	@Column(name="Expiry_date")
	private Date expiryDate;
	
	@Column(name = "Authorization_number",columnDefinition = "varchar(6)")
	private String authorizationNumber;

	@NotNull
	@Column(name = "user_create",columnDefinition = "varchar(40)")
	private String userCreate;
	
	@NotNull
	@Column(name = "date_create")
	private Date dateCreate;
}
