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
@Entity(name = "MD_PROCESSING_EVENTS")
public class ProcessingEvents {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROCESSING_EVENTS_ID")
	@SequenceGenerator(name = "PROCESSING_EVENTS_ID", sequenceName = "MD_PROCESSING_EVENTS_SEQ", allocationSize = 1)
	@Column(name="PROCESSING_EVENTS_ID")
	private Integer processingEventsId;
	
	@Column(name="TASK_EXECUTION_LOG_ID")
	private Integer taskExecutionLogId;
	
	@Column(name="INSTITUTION_ID")
	private String institutionId;
	
	@Column(name="PROCESSING_PROGRAM")
	private String processingProgram;
	
	@Column(name="FILE_NAME")
	private String fileName;
	
	@Column(name="EXECUTION_TIME", columnDefinition = "DATE DEFAULT CURRENT_TIMESTAMP")
	private Timestamp executionTime;
	
	@Column(name="SUCCESS_RESULT")
	private Character successResult;
	
	@Column(name="REMARKS")
	private String remarks;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY")
	private User createdBy;
	
	@Column(name="CREATED_DATE", columnDefinition = "DATE DEFAULT CURRENT_TIMESTAMP")
	private Timestamp createdDate;

}
