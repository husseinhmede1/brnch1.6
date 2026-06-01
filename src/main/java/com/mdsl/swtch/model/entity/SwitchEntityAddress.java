package com.mdsl.swtch.model.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "ACQ_ENTITY_ADDRESS")
public class SwitchEntityAddress {
	
	@Id
	@Column(name = "ENTITY_ID", nullable = false,columnDefinition = "varchar(30)")
	private String entityId;
	
	@Column(name = "INSTITUTION_ID")
	private String institutionId;
	
	@Column(name = "ADDRESS_ROLE")
	private String addressRole;
	
	@Column(name = "EFFECTIVE_DATE")
	private Date effectiveDate;
	
	@Column(name = "ADDRESS_ID")
	private Integer addressId;
	
	@Column(name = "CED_MAPPING")
	private String cedMapping;

}
