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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
@JsonIgnoreProperties
@Entity
@Table(name = "MD_ACQ_ENTITY_PAYMENT_ACCOUNTS")
public class PaymentAccount implements Cloneable{

	@Id
	@Column(name = "record_seq_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "record_seq_id")
	@SequenceGenerator(name = "record_seq_id", sequenceName = "MD_PAYMENT_ACCOUNT_SEQ", allocationSize = 1)
	private Integer paymentAccountId;
	
	@Column(name = "ACCOUNT_NUMBER",columnDefinition = "varchar(30)")
	private String accountNumber;
	
	@Column(name="IBAN",columnDefinition = "varchar(30)")
	private String iban;
	
	@Column(name="CONVERSION_RATE_MARKUP",columnDefinition = "Decimal(6,3)")
	private double currencyMarkup;

	@NotNull
	@Column(name="user_create",columnDefinition = "varchar(40)")
	private String createdBy;
	
	@NotNull
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(name = "date_create")
	private Date createdDate;
	
//	@OneToOne(cascade = CascadeType.PERSIST)
//	@JoinColumn(name = "BANK_CODE", referencedColumnName = "BANK_CODE")
//	private BankCode bankCode;
	
	@Column(name = "BANK_CODE", columnDefinition = "varchar(30)")
	private String bankCode;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumns({
	    @JoinColumn(name = "BANK_CODE", referencedColumnName = "BANK_CODE", insertable = false, updatable = false),
	    @JoinColumn(name = "INSTITUTION_ID", referencedColumnName = "INSTITUTION_ID", insertable = false, updatable = false)
	})
	private BankCode bankCodeEntity;
	
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "TRANS_CURRENCY_CODE", referencedColumnName = "CURRENCY_CODE")
	private Currency transferCurrency;
	

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "SETTL_CURRENCY_CODE", referencedColumnName = "CURRENCY_CODE")
	private Currency settlementCurrency;
	
	@Column(name="BENEFICIARY_NAME",columnDefinition = "varchar(50)")
	private String beneficiaryName;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "INSTITUTION_ID")
	private Institution institution;
	
    @Column(name = "ENTITY_ID")
    private String entity;
	
	@ManyToOne
	@JoinColumns({
	    @JoinColumn(name = "ENTITY_ID", referencedColumnName = "ENTITY_ID",insertable=false,updatable=false),
	    @JoinColumn(name = "INSTITUTION_ID", referencedColumnName = "INSTITUTION_ID",insertable=false,updatable=false)
	})
	private Entities entityObject;
	
	@Column(name="BRANCH")
	private String branch;

	public PaymentAccount(PaymentAccount paymentAccount) {
		this.paymentAccountId = null;
        this.accountNumber = paymentAccount.getAccountNumber();
        this.iban = paymentAccount.getIban();
        this.currencyMarkup = paymentAccount.getCurrencyMarkup();
        this.createdBy = paymentAccount.getCreatedBy();
        this.createdDate = paymentAccount.getCreatedDate();
        this.bankCode = paymentAccount.getBankCode();
        this.transferCurrency = paymentAccount.getTransferCurrency();
        this.settlementCurrency = paymentAccount.getSettlementCurrency();
        this.beneficiaryName = paymentAccount.getBeneficiaryName();
        this.institution = paymentAccount.getInstitution();
        this.entity = paymentAccount.getEntity();
        this.branch = paymentAccount.getBranch();
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
}
