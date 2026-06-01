package com.mdsl.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

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
@Entity(name = "MD_CURRENCY_TABLE")
public class Currency implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="record_seq_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "record_seq_id")
	@SequenceGenerator(name = "record_seq_id", sequenceName = "MD_CFG_CURRENCY_SEQ", allocationSize = 1)
	private Integer currencyId;

	@Column(name = "CURRENCY_CODE", unique = true,columnDefinition = "varchar(3)")
	private String currencyCode;
	
	@Column(name = "CURR_NAME", unique = true,columnDefinition = "varchar(50)")
	private String currencyName;
	
	@Column(name = "CURR_CODE_ALPHA2",columnDefinition = "varchar(2)")
	private String currCodeALPHA2;
	
	@Column(name = "CURR_CODE_ALPHA3",columnDefinition = "varchar(3)")
	private String currCodeALPHA3;
	
	@Column(name = "CURR_EXPONENT",columnDefinition = "varchar(1)")
	private String currExponent;

	@NotNull
	@Column(name = "user_create",columnDefinition = "varchar(40)")
	private String userCreate;
	
	@NotNull
	@Column(name = "date_create")
	private Date dateCreate;
	
	@Column(name = "status")
	private Character status;
}