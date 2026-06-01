package com.mdsl.model.entity;

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
@Entity(name = "MD_BUSINESS_TYPE")
public class BusinessType {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BUSINESS_TYPE_ID")
	@SequenceGenerator(name = "BUSINESS_TYPE_ID", sequenceName = "MD_BUSINESS_TYPE_SEQ", allocationSize = 1)
	@Column(name = "BUSINESS_TYPE_ID")
	private Integer businessTypeId;
	
	@Column(name = "BUSINESS_TYPE")
	private String businessType;
	
	@Column(name="DESCRIPTION")
	private String discription;

}
