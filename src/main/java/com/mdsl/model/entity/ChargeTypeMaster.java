package com.mdsl.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties
@Entity(name = "CHARGE_TYPE_MASTER")
public class ChargeTypeMaster implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CHARGE_TYPE_MASTER_ID")
	@SequenceGenerator(name="CHARGE_TYPE_MASTER_ID", sequenceName = "MD_CHARGE_TYPE_MASTER_SEQ", allocationSize = 1)
	@Column(name="CHARGE_TYPE_MASTER_ID")
	private Integer chargeTypeMasterId;
	
	@Column(name="CHARGE_TYPE_MASTER_NAME")
	private String chargeTypeMasterName;
	
}
