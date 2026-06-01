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
@Entity(name="MD_CFG_LAYOUT")
public class Layout {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LAYOUT_ID")
	@SequenceGenerator(name="LAYOUT_ID", sequenceName = "MD_LAYOUT_SEQ", allocationSize = 1)
	@Column(name = "LAYOUT_ID", nullable = false)			
	private Integer layoutId;
	
	@Column(name="LAYOUT_NAME")
	private String layoutName;
	
	@Column(name = "LAYOUT_FORMAT")			
	private String layoutFormat;

	@Column(name = "LAYOUT_SEPARATOR")			
	private String layoutSeparator;

	@Column(name = "INCLUDES_HEADER")
	private boolean includesHeader;
	
	@Column(name = "FILEID")			
	private Integer fileId;
	
	@Column(name = "INST_ID")			
	private Integer instId;
	
	@Column(name="STATUS")
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