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
@Entity(name="MD_LKU_FILE_TYPES")
public class FileType {
	@Id
	@Column(name = "FILE_TYPE_ID", nullable = false)			
	private Integer fileTypeId;
	
	@Column(name="FILE_TYPE_CODE")
	private String fileTypeCode;
	
	@Column(name="FILE_TYPE_STATUS")
	private String status;
	
	@Column(name="CREATED_DATE", columnDefinition = "DATE DEFAULT CURRENT_TIMESTAMP")
	private Timestamp createdDate;

	@Column(name = "CREATED_BY")
	private Integer createdBy;
	
	@Column(name="UPDATED_DATE", columnDefinition = "DATE DEFAULT CURRENT_TIMESTAMP")
	private Timestamp updatedDate;
	
	@Column(name = "UPDATED_BY")
	private Integer updatedBy;
}