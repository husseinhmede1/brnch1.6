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
@Entity(name = "MERCHANT_TYPE")
public class MerchantType implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MERCHANT_TYPE_ID")
	@SequenceGenerator(name="MERCHANT_TYPE_ID", sequenceName = "MD_MERCHANT_TYPE_SEQ", allocationSize = 1)
	@Column(name="MERCHANT_TYPE_ID")
	private Integer merchantTypeId;
	
	@Column(name="MERCHANT_TYPE_NAME",columnDefinition = "varchar(4)")
	private String merchantTypeName;
}
