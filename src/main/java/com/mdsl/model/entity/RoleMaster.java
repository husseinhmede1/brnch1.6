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
@Entity(name = "MD_ROLE_MASTER")
public class RoleMaster implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROLE_ID")
	@SequenceGenerator(name = "ROLE_ID", sequenceName = "MD_ROLE_MASTER_SEQ", allocationSize = 1)
	@Column(name="ROLE_ID")
	private Integer roleId;
	
	@Column(name = "ROLE_CODE",columnDefinition = "varchar(5)")
	private String employeeRole;
	
	@Column(name="DESCRIPTION")
	private String discription;
}
