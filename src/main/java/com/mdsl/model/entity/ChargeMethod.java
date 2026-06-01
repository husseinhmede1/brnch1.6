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
@Entity(name = "MD_CHARGE_METHOD")
public class ChargeMethod implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CHARGE_METHOD_ID")
	@SequenceGenerator(name = "CHARGE_METHOD_ID", sequenceName = "MD_CHARGE_METHOD_SEQ", allocationSize = 1)
	@Column(name = "CHARGE_METHOD_ID")
	private Integer chargeMethodId;
	
	@Column(name = "CHARGE_METHOD",columnDefinition = "varchar(12)")
	private String chargeMethod;
	
}
