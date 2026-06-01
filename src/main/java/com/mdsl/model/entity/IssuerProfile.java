package com.mdsl.model.entity;

import java.io.Serializable;
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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties
@Entity(name = "MD_ISSUER_ACQ_PROFILE")
public class IssuerProfile implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROFILE_ID")
	@SequenceGenerator(name = "PROFILE_ID", sequenceName = "MD_ISSUER_SEQ", allocationSize = 1)
	@Column(name="PROFILE_ID")
	private Integer profileId;
	
	@Column(name="PROFILE_DESCRIPTION")
	private String profileDescription;
	
	@Column(name="ISSUER_ACQ_PROFILE")
	private String issuerAcqProfile;
	
	@Column(name="INSTITUTION_ID")
	private String institutionId;
	
	@Column(name="CREATED_BY")
	private Integer createdBy;

	@Column(name="CREATED_DATE", columnDefinition="DATE DEFAULT CURRENT_TIMESTAMP")
	private Timestamp createdDate;

	@Column(name="UPDATED_BY")
	private Integer updatedBy;

	@Column(name="UPDATED_DATE", columnDefinition="DATE DEFAULT CURRENT_TIMESTAMP")
	private Timestamp updatedDate;
	
}
