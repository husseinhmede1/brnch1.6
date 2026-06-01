package com.mdsl.model.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties
@Getter
@Setter
@Builder
@AllArgsConstructor 
@NoArgsConstructor 
@Entity(name="MD_ACCOUNTING_TEMPLATE_DTLS")
public class AccountingTemplateDetails{

  @Id
  @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ACCT_TEMPLATE_DTL_ID")
  @SequenceGenerator(name="ACCT_TEMPLATE_DTL_ID", sequenceName="MD_ACCOUNTING_TEMPLATE_DTLS_SEQ", allocationSize=1)
  @Column(name="ACCT_TEMPLATE_DTL_ID")
  private Integer acctTemplateDtlId;

  @Column(name="INSTITUTION_ID")
  private String institutionId;

  @Column(name="ACCT_TEMPLATE_HDR_SUB_ID")
  private Integer acctTemplateHdrSubId;

  @Column(name="TRANS_ID")
  private String transId;

  @Column(name="ACCOUNT_ORIGIN")
  private String accountOrigin;

  @Column(name="DESTINATION_INSTITUTION")
  private String destinationInstitution;

  @Column(name="ACCOUNT_TYPE")
  private String accountType;

  @Column(name="AMOUNT_TYPE")
  private String amountType;

  @Column(name="PERCENTAGE_APPLIED")
  private Double percentageApplied;

  @Column(name="PERCENT_SRC")
  private String percentSrc;

  @Column(name="SIGN_FLAG")
  private String signFlag;

  @Column(name="LINE_DESCRIPTION")
  private String lineDescription;
  
  @Column(name="SHOW")
  private Integer show;

  @Column(name="CREATED_BY")
  private Integer createdBy;

  @Column(name="CREATED_DATE", columnDefinition="DATE DEFAULT CURRENT_TIMESTAMP")
  private Timestamp createdDate;

  @Column(name="UPDATED_BY")
  private Integer updatedBy;

  @Column(name="UPDATED_DATE", columnDefinition="DATE DEFAULT CURRENT_TIMESTAMP")
  private Timestamp updatedDate;
}