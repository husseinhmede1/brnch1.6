package com.mdsl.model.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.transaction.Transactional;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties
@Transactional
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name="MD_BANK_FILES_OUTPUTS")
public class BankFilesOutput {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="BANK_FILES_OUTPUT_ID")
	@SequenceGenerator(name="BANK_FILES_OUTPUT_ID", sequenceName="MD_BANK_FILES_OUTPUTS_SEQ", allocationSize=1)
	@Column(name="BANK_FILES_OUTPUT_ID")
	private Integer bankFilesOutputId;
	
	@Column(name="INSTITUTION_ID")
	private String institutionId;
	
	@Column(name="BANK_CODE")
	private String bankCode;
	
	@Column(name="OUTPUT_FILE_TYPE")
	private String outputFileType;
	
	@Column(name="OUTPUT_FILE_TYPE_ABBR")
	private String outputFileTypeAbbr;
	
	@Column(name="OUTPUT_TEMPLATE_HDR_ID")
	private int outputTemplateHdrId;
	
	@Column(name="CREATED_BY")
	private Integer createdBy;

	@Column(name="CREATED_DATE", columnDefinition="DATE DEFAULT CURRENT_TIMESTAMP")
	private Timestamp createdDate;

	@Column(name="UPDATED_BY")
	private Integer updatedBy;

	@Column(name="UPDATED_DATE", columnDefinition="DATE DEFAULT CURRENT_TIMESTAMP")
	private Timestamp updatedDate;

}
