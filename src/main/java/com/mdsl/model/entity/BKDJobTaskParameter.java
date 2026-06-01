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

@Builder
@AllArgsConstructor 
@NoArgsConstructor
@Getter
@Setter
@Entity(name="MD_BKD_JOB_TASK_PARAM")
public class BKDJobTaskParameter {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "JOB_TASK_PARAM_ID")
	@SequenceGenerator(name="JOB_TASK_PARAM_ID", sequenceName = "MD_BKD_JOB_TASK_PARAM_SEQ", allocationSize = 1)
	@Column(name = "JOB_TASK_PARAM_ID", nullable = false) 
	private Integer jobTaskParamId;
	
	@Column(name = "JOB_TASK_ID") 
	private Integer jobTaskId;

	@Column(name = "PARAMETERS_SERVICE_ID") 
	private Integer parametersServiceId;
	
	@Column(name = "PARAMETER_VALUE") 
	private String parameterValue;
	
	@Column(name="CREATED_DATE", columnDefinition = "DATE DEFAULT CURRENT_TIMESTAMP")
	private Timestamp createdDate;
	
	@Column(name = "CREATED_BY")
	private Integer createdBy;
	
	@Column(name="UPDATED_DATE", columnDefinition = "DATE DEFAULT CURRENT_TIMESTAMP")
	private Timestamp updatedDate;
	
	@Column(name = "UPDATED_BY")
	private Integer updatedBy;
}
