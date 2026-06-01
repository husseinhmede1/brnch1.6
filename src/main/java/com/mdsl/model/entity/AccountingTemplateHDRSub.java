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
@Entity(name="MD_ACCOUNTING_TEMPLATE_HDR_SUB")
public class AccountingTemplateHDRSub {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ACCT_TEMPLATE_HDR_SUB_ID")
	@SequenceGenerator(name="ACCT_TEMPLATE_HDR_SUB_ID", sequenceName="MD_ACCOUNTING_TEMPLATE_SUB_SEQ", allocationSize=1)
	@Column(name="ACCT_TEMPLATE_HDR_SUB_ID")
	private Integer acctTemplateHdrSubId;
	
	@Column(name="ACCT_TEMPLATE_HDR_ID")
	private Integer acctTemplateHdrId;

	@Column(name="INSTITUTION_ID")
	private String institutionId;
	
	@Column(name="BANK_CODE")
	private String bankCode;
	
	@Column(name="TEMPLATE_DESCRIPTION")
	private String tenplateDescription;
	
	@Column(name="CREATED_BY")
	private Integer createdBy;

	@Column(name="CREATED_DATE", columnDefinition="DATE DEFAULT CURRENT_TIMESTAMP")
	private Timestamp createdDate;

	@Column(name="UPDATED_BY")
	private Integer updatedBy;

	@Column(name="UPDATED_DATE", columnDefinition="DATE DEFAULT CURRENT_TIMESTAMP")
	private Timestamp updatedDate;

}
