package com.mdsl.model.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

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
@Entity(name="MD_JOB_FILE_PROCESSING_LOG")
public class JobFileProcessingLog {
	@Id
	@Column(name = "SERVICE_ID", nullable = false)
	private Integer serviceId;
	
	@Column(name = "FILE_NAME")
	private String fileName;
	
	@Column(name = "RECORD_NUMBER")
	private Integer recordNumber;
	
	@Column(name = "STATUS")
	private char status;
	
	@Column(name = "FAILURE_REASON")
	private String failureReason;
	
	@Column(name = "CREATED_DATE")
	private Timestamp createdDate;
	
	@Column(name="INSTITUTION_ID")
	private Integer institutionId;
	
	@Column(name="RECORD")
	private String record;
}