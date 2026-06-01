package com.mdsl.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties({ "record_seq_id", "date" })
@Entity(name = "MD_PROVINCE")
public class Province implements Serializable{

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "record_seq_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "record_seq_id")
	@SequenceGenerator(name = "record_seq_id", sequenceName = "MD_PROVINCE_SEQ", allocationSize = 1)
	private Integer provinceId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CNTRY_CODE", referencedColumnName = "CNTRY_CODE")
	private Country cntryCode;

	@NotNull
	@Column(name = "PROV_STATE_ABBREV ", columnDefinition = "varchar(3)")
	private String provStateAbbrev;

	@Column(name = "PROV_STATE_NUM_CD")
	private Integer provStateNumCd;

	@Column(name = "PROVINCE_STATE", columnDefinition = "varchar(50)")
	private String provinceState;

	@Column(name = "PROVINCE_STATE_ALT", columnDefinition = "varchar(50)")
	private String provinceStateAlt;

	@Column(name = "user_create", columnDefinition = "varchar(40)")
	private String userCreate;

	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(nullable = false, name = "date_create")
	private Date date;
}