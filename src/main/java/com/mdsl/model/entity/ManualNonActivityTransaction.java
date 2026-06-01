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
@Entity(name = "MD_NON_ACTIVITY_MANUAL")
public class ManualNonActivityTransaction {

	@Id
	@Column(name = "record_seq_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "record_seq_id")
	@SequenceGenerator(name = "record_seq_id", sequenceName = "MD_NON_ACTIVITY_TRANSACTION_SEQ", allocationSize = 1)
	private Integer manualNonActivityTransactionId;
	
	@ManyToOne
	@JoinColumn(name = "ACQ_INST_ID",referencedColumnName = "INSTITUTION_ID")
	private Institution institution;
	
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

	@Column(name="TRANSACTION_AMOUNT",columnDefinition = "Decimal(26,8)")
	private Float transactionAmount;
	
	@ManyToOne
	@JoinColumn(name="TRANSACTION_CURRENCY", referencedColumnName = "CURRENCY_CODE")
	private Currency transactionCurrency;
	
	@Column(name="Transaction_date")
	private Date transactionDate;
	
	@NotNull
	@Column(name = "user_create",columnDefinition = "varchar(40)")
	private String userCreate;
	
	@NotNull
	@Column(name = "date_create")
	private Date dateCreate;
	
}
