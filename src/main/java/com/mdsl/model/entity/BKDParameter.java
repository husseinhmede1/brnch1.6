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

@Builder
@AllArgsConstructor 
@NoArgsConstructor
@Getter
@Setter
@Entity(name="MD_BKD_PARAMETERS")
public class BKDParameter {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PARAMETER_ID")
	@SequenceGenerator(name="PARAMETER_ID", sequenceName = "MD_BKD_PARAMETERS_SEQ", allocationSize = 1)
	@Column(name = "PARAMETER_ID", nullable = false) 		
	private Integer parameterId;
	
	@Column(name = "PARAMETER_NAME")
	private String parameterName;
	
	@Column(name="CREATED_DATE", columnDefinition = "DATE DEFAULT CURRENT_TIMESTAMP")
	private Timestamp createdDate;
	
	@Column(name = "CREATED_BY")
	private Integer createdBy;
	
	@Column(name="UPDATED_DATE", columnDefinition = "DATE DEFAULT CURRENT_TIMESTAMP")
	private Timestamp updatedDate;
	
	@Column(name = "UPDATED_BY")
	private Integer updatedBy;
}
