package com.mdsl.model.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"createdBy", "updatedBy"})
@Entity(name = "MD_ENT_PASS_POLICY")
public class PasswordPolicy {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POLICY_ID")
	@SequenceGenerator(name = "POLICY_ID", sequenceName = "MD_ENT_PASS_POLICY_SEQ", allocationSize = 1)
	@Column(name="POLICY_ID")
	private int policyId;
	
	@ManyToOne
    @JoinColumn(name = "INST_ID")
	private Institution institution; 
	
	@Column(name="PASSWORD_HIST")
	private Integer passwordHistory; 
	
	@Column(name="PASSWORD_LENGTH")
	private Integer passwordLength; 
	
	@Column(name="CREATED_DATE", columnDefinition = "DATE DEFAULT CURRENT_TIMESTAMP")
	private Timestamp createdDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY")
	private User createdBy;
	
	@Column(name="UPDATED_DATE", columnDefinition = "DATE DEFAULT CURRENT_TIMESTAMP")
	private Timestamp updatedDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UPDATED_BY")
	private User updatedBy;
}