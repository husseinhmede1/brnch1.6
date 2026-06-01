package com.mdsl.model.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Entity(name = "MD_JOB_DEF_TASK")
public class JobDefinitionTask {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "JOB_TASK_ID")
	@SequenceGenerator(name = "JOB_TASK_ID", sequenceName = "MD_JOB_DEF_TASK_SEQ", allocationSize = 1)
	@Column(name = "JOB_TASK_ID", nullable = false)
	private Integer jobTaskId;

	@Column(name = "JOB_ID")
	private Integer job;
	
	@ManyToOne
	@JoinColumn(name = "TASK_ID")
	private JobTask task;

	@Column(name = "PRIORITY")
	private Integer priority;

	@Column(name = "CREATED_DATE", columnDefinition = "DATE DEFAULT CURRENT_TIMESTAMP")
	private Timestamp createdDate;
	
	@Column(name = "CREATED_BY")
	private Integer createdBy;
	
	@Column(name = "UPDATED_DATE", columnDefinition = "DATE DEFAULT CURRENT_TIMESTAMP")
	private Timestamp updatedDate;
	
	@Column(name = "UPDATED_BY")
	private Integer updatedBy;
}