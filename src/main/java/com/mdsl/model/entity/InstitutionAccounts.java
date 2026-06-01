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
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name="MD_INSTITUTION_ACCOUNTS")
public class InstitutionAccounts
{

  @Id
  @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="INSTITUTION_ACCTS_ID")
  @SequenceGenerator(name="INSTITUTION_ACCTS_ID", sequenceName="MD_INSTITUTION_ACCOUNTS_SEQ", allocationSize=1)
  @Column(name="INSTITUTION_ACCTS_ID")
  private Integer institutionAcctsId;

  @Column(name="INSTITUTION_ID")
  private String institutionId;

  @Column(name="ACCOUNT_TYPE")
  private String accountType;

  @Column(name="ACCOUNT_DESCRIPTION")
  private String accountDescription;

  @Column(name="CARD_SCHEME_ID")
  private String cardSchemeId;

  @Column(name="ISSUER_ACQ_PROFILE")
  private String issuerAcqProfile;

  @Column(name="CURRENCY_CODE")
  private String currencyCode;
  
  @Column(name="ACCOUNT_ORIGIN")
  private String accountOrigin;
  
  @Column(name="CHARGING_INSTITUTION")
  private String chargingInstitution;

  @Column(name="BANK_CODE")
  private String bankCode;

  @Column(name="ACCOUNT_NUMBER")
  private String accountNumber;

  @Column(name="IBAN")
  private String iban;
  
  @Column(name="BRANCH")
  private String branch;
  
  @Column(name="BENEFICIARY_NAME")
  private String beneficiaryName;

  @Column(name="CREATED_DATE", columnDefinition="DATE DEFAULT CURRENT_TIMESTAMP")
  private Timestamp createdDate;

  @Column(name="CREATED_BY")
  private Integer createdBy;

  @Column(name="UPDATED_DATE", columnDefinition="DATE DEFAULT CURRENT_TIMESTAMP")
  private Timestamp updatedDate;

  @Column(name="UPDATED_BY")
  private Integer updatedBy;
}