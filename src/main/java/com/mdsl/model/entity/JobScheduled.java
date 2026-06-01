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
@Entity(name = "MD_JOB_SCHEDULED_EXEC")
public class JobScheduled {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SCHEDULE_EXEC_ID")
	@SequenceGenerator(name = "SCHEDULE_EXEC_ID", sequenceName = "MD_JOB_SCHEDULED_EXEC_SEQ", allocationSize = 1)
	@Column(name = "SCHEDULE_EXEC_ID", nullable = false)
	private Integer scheduleExecId;
	
	@ManyToOne
	@JoinColumn(name = "JOB_ID")
	private Job job;
	
	@Column(name = "START_DATE")
	private Timestamp startDate;
	
	@Column(name = "STATUS")
	private char status;
	
	@Column(name = "CREATED_DATE", columnDefinition = "DATE DEFAULT CURRENT_TIMESTAMP")
	private Timestamp createdDate;
	
	@Column(name = "CREATED_BY")
	private Integer createdBy;
	
	@Column(name = "UPDATED_DATE", columnDefinition = "DATE DEFAULT CURRENT_TIMESTAMP")
	private Timestamp updatedDate;
	
	@Column(name = "UPDATED_BY")
	private Integer updatedBy;
}