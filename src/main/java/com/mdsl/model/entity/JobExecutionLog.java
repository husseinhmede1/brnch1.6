package com.mdsl.model.entity;

import java.sql.Timestamp;
import java.util.Date;

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
@Entity(name = "MD_JOB_EXECUTION_LOG")
public class JobExecutionLog {
	
//	public JobExecutionLog(Object object, Job job, JobScheduled jobScheduler, Date startDate, char status, Date endDate,
//			String executionDetails, Timestamp createdDate, int createdBy, Timestamp updatedDate, int updatedBy) {
//		this.job = job;
//		this.jobScheduled = jobScheduler;
//		this.startDate = startDate;
//		this.endDate = endDate;
//		this.status = status;
//		this.executionDetails = executionDetails;
//		this.createdDate = createdDate;
//		this.createdBy = createdBy;
//		this.createdBy = createdBy;
//		this.updatedBy = updatedBy;
//	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EXEC_ID")
	@SequenceGenerator(name = "EXEC_ID", sequenceName = "MD_JOB_EXECUTION_LOG_SEQ", allocationSize = 1)
	@Column(name = "EXEC_ID", nullable = false)
	private Integer execId;
	
	@ManyToOne
	@JoinColumn(name = "SCHEDULE_EXEC_ID")
	private JobScheduled jobScheduled;
	
	@ManyToOne
	@JoinColumn(name = "JOB_ID")
	private Job job;
	
	@ManyToOne
	@JoinColumn(name = "SERVICE_ID")
	private BKDService bkdService;
	
	@Column(name = "START_DATE")
	private Date startDate;
	
	@Column(name = "END_DATE")
	private Date endDate;
	
	@Column(name = "EXECUTION_DETAILS")
	private String executionDetails;
	
	@Column(name = "PROC_ARGS")
	private String procArgs;
	
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