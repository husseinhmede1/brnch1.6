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
@Entity(name = "MD_ACQ_NON_ACTIVITY_PKG_DTL")
public class NonActivityPackageDetails {

	@Id
	@Column(name = "record_seq_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "record_seq_id")
	@SequenceGenerator(name = "record_seq_id", sequenceName = "MD_NON_ACTIVITY_PKG_DTL_SEQ", allocationSize = 1)
	private Integer recordSeqId;

	@Column(name = "PKG_ID")
	private String nonActivityPackage;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumns({
	    @JoinColumn(name = "PKG_ID", referencedColumnName = "PKG_ID", insertable = false, updatable = false),
	    @JoinColumn(name = "INSTITUTION_ID", referencedColumnName = "INSTITUTION_ID", insertable = false, updatable = false)
	})
	private NonActivityPackage nonActivityPackageEntity;

	@NotNull
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "CURRENCY_CODE", referencedColumnName = "CURRENCY_CODE")
	private Currency currency;

	@ManyToOne
	@JoinColumn(name = "INSTITUTION_ID")
	private Institution institution;

	@Column(name = "CHARGE_TYPE")
	private String chargeMaster;

	@Column(name = "CHARGE_COUNT",columnDefinition = "CHAR(1)")
	private String chargeCount;

	@ManyToOne
	@JoinColumn(name = "TERMINAL_TYPE", referencedColumnName = "TERMINAL_TYPE")
	private TerminalTypes terminalTypes;

	@ManyToOne
	@JoinColumn(name = "ASSIGNED_TRANS_ID", referencedColumnName = "trans_id")
	private DefaultTransactionId assignedTransaction; 

	@ManyToOne
	@JoinColumn(name = "SCHEME_CODE", referencedColumnName = "CARD_SCHEME_ID")
	private CardScheme cardScheme;

	@Column(name = "FREQUENCY")
	private String frequency;
	
	@Column(name = "NBR_OF_INSTALLMENTS")
	private Integer numberOfInstallments;
	
	@Column(name = "CHARGE_FIRST_TRANS",columnDefinition = "CHAR(1)")
	private String chargeFirstTransaction;
	
	@NotNull
	@Column(name = "AMOUNT",columnDefinition = "Decimal(15,3)")
	private Float amount;
	
	@NotNull
	@Column(name = "MAX_AMOUNT",columnDefinition = "Decimal(15,3)")
	private Float maxAmount;
	
	@NotNull
	@Column(name = "START_DATE")
	private Date startDate;
	
	@NotNull
	@Column(name = "END_DATE")
	private Date endDate;
	
	@Column(name = "user_create",columnDefinition = "varchar(40)")
	private String userCreate;
	
	@Column(name = "date_create")
	private Date dateCreate;
	
	@Column(name = "status")
	private Character status;	
}