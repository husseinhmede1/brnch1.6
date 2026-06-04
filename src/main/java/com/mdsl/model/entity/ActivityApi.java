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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({ "activity", "service", "successAction", "createdBy", "updatedBy" })
@Entity(name = "MD_CFG_ACTIVITY_API")
public class ActivityApi {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACTIVITY_API_ID")
	@SequenceGenerator(name = "ACTIVITY_API_ID", sequenceName = "MD_ACTIVITY_API_SEQ", allocationSize = 1)
	@Column(name = "ACTIVITY_API_ID")
	private int activityApiId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ACTIVITY_ID")
	private Activity activity;

	@ManyToOne
	@JoinColumn(name = "API_ID")
	private Api api;

	@Column(name = "CUSTOM_VALIDATOR")
	private String customValidator;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SUCCESS_ACTION_ID")
	private Activity successAction;

	@Column(name = "IS_RESTRICTED")
	private String isRestricted;

	@Column(name = "STP")
	private char stp;

	@Column(name = "CREATED_DATE", columnDefinition = "DATE DEFAULT CURRENT_TIMESTAMP")
	private Timestamp createdDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY")
	private User createdBy;

	@Column(name = "UPDATED_DATE", columnDefinition = "DATE DEFAULT CURRENT_TIMESTAMP")
	private Timestamp updatedDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UPDATED_BY")
	private User updatedBy;
}