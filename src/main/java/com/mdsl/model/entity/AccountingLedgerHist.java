package com.mdsl.model.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Entity(name = "MD_ACCOUNTING_LEDGER_HIST")
public class AccountingLedgerHist implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ACCOUNTING_LEDGER_ID")
	private Integer accountingLedgerId;
	
	@Column(name = "INSTITUTION_ID")
	private String institutionId;
	
	@Column(name = "PDATE")
	private Timestamp pDate;
	
	@Column(name = "MERCH_SETTL_DATE")
	private Timestamp merchSettlementDate;
	
	@Column(name = "DBCR_EFFECTIVE_DATE")
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
	
	@Column(name = "DESTINATION_INSTITUTION")
	private String destinationInstitution;
	
	@Column(name = "ACCOUNT_TYPE")
	private String accountType;
	
	@Column(name = "AMOUNT_TYPE")
	private String amountType;
	
	@Column(name = "PERCENTAGE_APPLIED")
	private Double percentageApplied;
	
	@Column(name = "SIGN_FLAG")
	private String signFlag;
	
	@Column(name = "LINE_DESCRIPTION")
	private String lineDescription;
	
	@Column(name = "CARD_SCHEME_ID")
	private String cardSchemeId;
	
	@Column(name = "ISSUER_ACQ_PROFILE")
	private String issuerAcqProfile;
	
	@Column(name = "BANK_CODE")
	private String bankCode;
	
	@Column(name = "ACCOUNT_NUMBER")
	private String accountNumber;
	
	@Column(name = "CURRENCY_CODE")
	private String currencyCode;
	
	@Column(name = "IBAN")
	private String iban;
	
	@Column(name = "AMOUNT")
	private Double amount;
	
	@Column(name = "RELATED_TRANSACTION")
	private String relatedTransaction;
	
	@Column(name = "CREATED_BY")
	@NotNull
	private Integer createdBy;
	
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(name = "CREATED_DATE")
	@NotNull
	private Date createdDate;
	
	@Column(name = "UPDATED_BY")
	private Integer updatedBy;
	
	@Column(name = "UPDATED_DATE")
	private Date updatedDate;
}
