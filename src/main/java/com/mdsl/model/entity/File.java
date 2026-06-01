package com.mdsl.model.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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
@Entity(name="MD_CFG_FILES")
public class File {
	@Id
	@Column(name = "FILEID", nullable = false)			
	private Integer fileId;
	
	@Column(name="FILENAME")
	private String fileName;
	
	@ManyToOne
    @JoinColumn(name = "FILETYPE")
	private FileType fileType;
	
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