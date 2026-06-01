package com.mdsl.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Entity(name = "MD_CITY")
public class City implements Serializable{

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "record_seq_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "record_seq_id")
	@SequenceGenerator(name = "record_seq_id", sequenceName = "MD_CITY_SEQ", allocationSize = 1)
	private Integer cityId;

	@ManyToOne
	@JoinColumn(name = "CNTRY_ID", referencedColumnName = "CNTRY_CODE")
	private Country cntryCode;

	@ManyToOne
	@JoinColumn(name = "PROV_STATE_ABBREV",referencedColumnName = "PROV_STATE_ABBREV")
	private Province provStateAbbrev;

	@Column(name = "CITY_ABBREV", columnDefinition = "varchar(3)")
	private String cityAbbrev;

	@Column(name = "CITY_NAME", columnDefinition = "varchar(50)")
	private String cityName;

	@Column(name = "CITY_NAME_ALT", columnDefinition = "varchar(100)")
	private String cityNameAlt;

	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(nullable = false, name = "date_create")
	private Date date;

	@Column(name = "user_create", columnDefinition = "varchar(40)")
	private String userCreate;
}
