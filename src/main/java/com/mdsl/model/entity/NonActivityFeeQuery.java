package com.mdsl.model.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
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
@Entity(name = "MD_NON_ACTIVITY_TRANSACTION")
public class NonActivityFeeQuery {

	@Id
	@Column(name = "record_seq_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "record_seq_id")
	@SequenceGenerator(name = "record_seq_id", sequenceName = "NON_ACTIVITY_FEE_QUERY_SEQ", allocationSize = 1)
	private Integer nonActivityFeeQueryId;

	@Column(name = "MICROFILM_REF_NBR",columnDefinition = "varchar(23)")
	private String microfilmRefNbr;

	@Column(name = "REF_NBR_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer refNbrSeq;

    @Column(name = "ENTITY_ID")
    private String entities;
	
	@ManyToOne
	@JoinColumns({
	    @JoinColumn(name = "ENTITY_ID", referencedColumnName = "ENTITY_ID", insertable = false, updatable = false),
	    @JoinColumn(name = "INSTITUTION_ID", referencedColumnName = "INSTITUTION_ID", insertable = false, updatable = false)
	})
	private Entities entitiesObject;

	@Column(name = "TRANS_ID")
	private String transaction;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumns({
			@JoinColumn(name = "TRANS_ID", referencedColumnName = "TRANS_ID", insertable = false, updatable = false),
			@JoinColumn(name = "INSTITUTION_ID", referencedColumnName = "INSTITUTION_ID", insertable = false, updatable = false)
	})
	private DefaultTransactionId transactionEntity;
	
	@Column(name = "Transaction_date")
	private Date transactionDate;

	@Column(name = "TRANSACTION_AMOUNT",columnDefinition = "Decimal(26,8)")
	private Float transactionAmount;

	@ManyToOne
	@JoinColumn(name = "TRANSACTION_CURRENCY", referencedColumnName = "CURRENCY_CODE")
	private Currency transactionCurrency;

	@ManyToOne
	@JoinColumn(name = "INSTITUTION_ID")
	private Institution institution;

	@Column(name = "REVERSAL_REASON",columnDefinition = "varchar(10)")
	private String reversalReason;

	@Column(name = "Manual_entry ",columnDefinition = "CHAR(1)")
	private Character manualEntry;

	@Column(name = "Trans_description",columnDefinition = "varchar(100)")
	private String transDesc;

	@Column(name = "Processing_date")
	private Date processingDate;

	@Column(name = "PROCESSING_REF_NBR",columnDefinition = "varchar(30)")
	private String processingRefNbr;

	@Column(name = "REASON")
	private String reason;

	@Column(name = "DESCRIPTION")
	private String description;

	@NotNull
	@Column(name = "user_create",columnDefinition = "varchar(40)")
	private String userCreate;

	@NotNull
	@Column(name = "date_create")
	private Date dateCreate;
	
	@Column(name = "REVERSAL_FLAG")
	private Character reversalFlag = "N".charAt(0);
}