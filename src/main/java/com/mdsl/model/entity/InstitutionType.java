package com.mdsl.model.entity;

import java.io.Serializable;

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
@Entity(name = "MD_INSTITUTION_TYPE")
public class InstitutionType implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INSTITUTION_Type_ID")
	@SequenceGenerator(name = "INSTITUTION_TYPE_ID", sequenceName = "INSTITUTION_TYPE_ID", allocationSize = 1)
	@Column(name = "INSTITUTION_TYPE_ID")
	private Integer institutionTypeId;
	
	@Column(name = "INST_TYPE" , columnDefinition = "varchar(4)")
	private String institutionType;
}
