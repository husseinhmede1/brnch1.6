package com.mdsl.model.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

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
@Entity(name = "MD_ISSUER_ACQ_RELATION")
public class IssuerRelation {
	@Id
	@Column(name = "RECORD_SEQ_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RECORD_SEQ_ID")
	@SequenceGenerator(name = "RECORD_SEQ_ID", sequenceName = "MD_ISSUER_RELATION_SEQ", allocationSize = 1)
	private Integer recordSeqId;
	
	@Column(name = "INSTITUTION_ID")
	private String institutionId;
	
	@Column(name = "ISSUER_ACQ_PROFILE")
	private String issuerAcqProfile;
	
	@Column(name = "PAN_RANGE_FROM")
	private String panRangeFrom;
	
	@Column(name = "PAN_RANGE_TO")
	private String panRangeTo;
	
	@Column(name="CNTRY_CODE")
	private String cntryCode;
	
	@Column(name="CREATED_BY")
	private Integer createdBy;

	@Column(name="CREATED_DATE", columnDefinition="DATE DEFAULT CURRENT_TIMESTAMP")
	private Timestamp createdDate;

	@Column(name="UPDATED_BY")
	private Integer updatedBy;

	@Column(name="UPDATED_DATE", columnDefinition="DATE DEFAULT CURRENT_TIMESTAMP")
	private Timestamp updatedDate;
}