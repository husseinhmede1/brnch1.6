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
@Entity(name="MD_BKD_PARAMETERS_SERVICE")
public class BKDParameterService {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PARAMETERS_SERVICE_ID")
	@SequenceGenerator(name="PARAMETERS_SERVICE_ID", sequenceName = "MD_BKD_PARAMETERS_SERVICE_SEQ", allocationSize = 1)
	@Column(name = "PARAMETERS_SERVICE_ID", nullable = false) 		
	private Integer parametersServiceId;
	
	@Column(name = "SERVICE_ID") 		
	private Integer serviceId;
	
	@Column(name = "PARAMETER_ID") 		
	private Integer parameterId;
	
	@Column(name = "IS_MANDATORY") 		
	private String isMandatory;
	
	@Column(name="CREATED_DATE", columnDefinition = "DATE DEFAULT CURRENT_TIMESTAMP")
	private Timestamp createdDate;
	
	@Column(name = "CREATED_BY")
	private Integer createdBy;
	
	@Column(name="UPDATED_DATE", columnDefinition = "DATE DEFAULT CURRENT_TIMESTAMP")
	private Timestamp updatedDate;
	
	@Column(name = "UPDATED_BY")
	private Integer updatedBy;
}
