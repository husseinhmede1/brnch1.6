package com.mdsl.model.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Entity(name="MD_BKD_SERVICE")
public class BKDService {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SERVICE_ID")
	@SequenceGenerator(name="SERVICE_ID", sequenceName = "MD_SERVICE_SEQ", allocationSize = 1)
	@Column(name = "SERVICE_ID", nullable = false) 		
	private Integer serviceId;
	
	@Column(name = "SERVICE_NAME")
	private String serviceName;
	
	@Column(name = "SERVICE_MODE")
	private String serviceMode;
	
	@Column(name = "CLASS_NAME")
	private String className;
	
	@Column(name = "SRCFOLDER")
	private String srcFolder;
	
	@Column(name = "DESTFOLDER")
	private String destFolder;
	
	@Column(name = "FILTER")
	private String filter;
	
	@Column(name = "BATCHSIZE")
	private Integer batchSize	;
	
	@Column(name="CREATED_DATE", columnDefinition = "DATE DEFAULT CURRENT_TIMESTAMP")
	private Timestamp createdDate;
	
	@Column(name = "CREATED_BY")
	private Integer createdBy;
	
	@Column(name="UPDATED_DATE", columnDefinition = "DATE DEFAULT CURRENT_TIMESTAMP")
	private Timestamp updatedDate;
	
	@Column(name = "UPDATED_BY")
	private Integer updatedBy;
}