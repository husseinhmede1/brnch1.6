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
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties
@Entity(name = "MD_TASK_PARAMETERS")
public class TaskParameter {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TASK_PARAM_ID")
	@SequenceGenerator(name = "TASK_PARAM_ID", sequenceName = "MD_TASK_PARAMETERS_SEQ", allocationSize = 1)
	@Column(name="TASK_PARAM_ID")
	private Integer taskParamId;
	
	@Column(name="TASK_ID")
	private Integer taskId;
	
	@Column(name="PARAMETER")
	private String parameter;
	
	@Column(name="VALIDITY")
	private Character validity;
	
	@Column(name="CONDITION")
	private String condition;
	
	@Column(name="PARAMETER_TYPE")
	private String parameterType;
	
	@Column(name="SEQUENCE")
	private Integer sequence;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY")
	private User createdBy;
	
	@Column(name="CREATED_DATE", columnDefinition = "DATE DEFAULT CURRENT_TIMESTAMP")
	private Timestamp createdDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UPDATED_BY")
	private User updatedBy;
	
	@Column(name="UPDATED_DATE", columnDefinition = "DATE DEFAULT CURRENT_TIMESTAMP")
	private Timestamp updatedDate;

}
