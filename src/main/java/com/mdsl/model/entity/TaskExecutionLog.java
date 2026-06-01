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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties
@Entity(name = "MD_TASK_EXECUTION_LOG")
public class TaskExecutionLog {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TASK_EXECUTION_LOG_ID")
	@SequenceGenerator(name = "TASK_EXECUTION_LOG_ID", sequenceName = "MD_TASK_EXECUTION_LOG_SEQ", allocationSize = 1)
	@Column(name="TASK_EXECUTION_LOG_ID")
	private Integer taskExecutionLogId;
	
	@Column(name="INSTITUTION_ID")
	private String institutionId;
	
	@Column(name="TASK_ID")
	private Integer taskId;
	
	@Column(name="TASK_DETAILS")
	private String taskDetails;
	
	@Column(name="START_DATETIME", columnDefinition = "DATE DEFAULT CURRENT_TIMESTAMP")
	private Timestamp startDatetime;
	
	@Column(name="END_DATETIME", columnDefinition = "DATE DEFAULT CURRENT_TIMESTAMP")
	private Timestamp endDatetime;
	
	@Column(name = "CREATED_BY")
	private Integer createdBy;
	
	@Column(name="CREATED_DATE", columnDefinition = "DATE DEFAULT CURRENT_TIMESTAMP")
	private Timestamp createdDate;
	
	@Column(name = "UPDATED_BY")
	private Integer updatedBy;
	
	@Column(name="UPDATED_DATE", columnDefinition = "DATE DEFAULT CURRENT_TIMESTAMP")
	private Timestamp updatedDate;

}
