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
@Entity(name="MD_JOB_PROCESSED_FILES")
public class JobProcessedFiles {
	
	@Id
	@Column(name="JOB_PROCESSED_FILES_ID")
	private Integer jobProcessedFilesId;
	
	@Column(name="PROC_FILE_NAME")
	private String procFileName;
	
	@Column(name="PROC_FILE_DATE", columnDefinition = "DATE DEFAULT CURRENT_TIMESTAMP")
	private Timestamp procFileDate;
	
	@Column(name="PROC_FILE_STATUS")
	private char procFileStatus;
	
	@Column(name="CREATED_DATE", columnDefinition = "DATE DEFAULT CURRENT_TIMESTAMP")
	private Timestamp createdDate;
	
	@Column(name = "CREATED_BY")
	private Integer createdBy;
	
	@Column(name="UPDATED_DATE", columnDefinition = "DATE DEFAULT CURRENT_TIMESTAMP")
	private Timestamp updatedDate;
	
	@Column(name = "UPDATED_BY")
	private Integer updatedBy;
	
}
