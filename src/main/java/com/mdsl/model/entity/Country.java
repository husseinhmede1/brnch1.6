package com.mdsl.model.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.NaturalId;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties({"record_seq_id", "date","cntryStatus"})
@Entity(name="MD_COUNTRY")
public class Country implements Serializable{

private static final long serialVersionUID = 1L;

	@Id
	@Column(name="record_seq_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "record_seq_id")
	@SequenceGenerator(name = "record_seq_id", sequenceName = "MD_CNTRY_SEQ", allocationSize = 1)
	private Integer cntryId;

	@NotNull
	@NaturalId
	@Column(name="CNTRY_CODE",columnDefinition = "varchar(3)")
	private String cntryCode;

	@Column(name="CNTRY_NAME",columnDefinition = "varchar(50)")
	private String cntryName;

	@Column(name="CNTRY_NAME_ALT",columnDefinition = "varchar(100)")
	private String cntryNameAlt;

	@Column(name="CNTRY_CODE_ALPHA2",columnDefinition = "varchar(2)")
	private String cntryCodeAlpha2;

	@Column(name="CNTRY_CODE_ALPHA3",columnDefinition = "varchar(3)")
	private String cntryCodeAlpha3;

	@ManyToOne
	@JoinColumn(name="CURR_CODE", referencedColumnName = "CURRENCY_CODE")
	private Currency currency;

	@Column(name="CURR_PATTERN",columnDefinition = "varchar(20)")
	private String currPattern;

	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(name="DATE_PATTERN",columnDefinition = "varchar(20)")
	private String datePattern;

	@Column(name="ECONOMIC_AREA_IND",columnDefinition = "varchar(4)")
	private String economicAreaInd;

	@Column(name="CNTRY_STATUS")
	private Character cntryStatus;

	@DateTimeFormat(pattern = "dd-MM-yyyy")
    @Column(nullable=false,name = "date_create")
    private Date date;
	
	@Column(name = "user_create",columnDefinition = "varchar(40)")
	private String userCreate;
	
}
