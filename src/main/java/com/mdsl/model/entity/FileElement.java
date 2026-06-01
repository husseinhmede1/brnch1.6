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
@Entity(name="MD_CFG_FILE_ELEMENTS")
public class FileElement {
	@Id
	@Column(name = "ELEMENT_ID", nullable = false)			
	private Integer elementId;
	
	@Column(name="ELEMENT_NAME")
	private String elementName;
	
	@Column(name="ELEMENT_SECTION")
	private String elementSection;
	
	@Column(name = "FILEID")			
	private Integer fileId;

	@Column(name="IS_REPEATED")
	private String isRepeated;
	
	@Column(name="VALIDATION_REQUIRED")
	private String validationRequired;
	
	@Column(name="IS_MANDATORY")
	private String isMandatory;
	
	@Column(name="VALIDATION_LENGTH")
	private Integer validationLength;
	
	@Column(name="VALIDATION_FORMAT")
	private String validationFormat;
	
	@Column(name="CREATED_DATE", columnDefinition = "DATE DEFAULT CURRENT_TIMESTAMP")
	private Timestamp createdDate;

	@Column(name = "CREATED_BY")
	private Integer createdBy;
	
	@Column(name="UPDATED_DATE", columnDefinition = "DATE DEFAULT CURRENT_TIMESTAMP")
	private Timestamp updatedDate;
	
	@Column(name = "UPDATED_BY")
	private Integer updatedBy;
}