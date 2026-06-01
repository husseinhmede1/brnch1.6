package com.mdsl.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({ "record_seq_id", "date" })
@Entity(name = "MD_GENERAL_ADDRESS")
public class Address implements Cloneable,Serializable {


	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ADDRESS_ID")
	@SequenceGenerator(name = "ADDRESS_ID", sequenceName = "MD_ADDRESS_SEQ", allocationSize = 1)
	@Column(name="ADDRESS_ID")
	private Integer addressId;

	@ManyToOne
	@JoinColumn(name = "INSTITUTION_ID")
	private Institution institution;

	@ManyToOne
	@JoinColumn(name = "CNTRY_CODE", referencedColumnName = "CNTRY_CODE")
	private Country cntryCode;

	@ManyToOne
	@JoinColumn(name="CITY_CODE", referencedColumnName = "CITY_ABBREV")
	private City cityCode;

	@Column(name = "ADDRESS1", columnDefinition = "varchar(50)")
	private String address1;

	@Column(name = "ADDRESS2", columnDefinition = "varchar(50)")
	private String address2;

	@Column(name = "ADDRESS3", columnDefinition = "varchar(50)")
	private String address3;

	@Column(name = "ADDRESS4", columnDefinition = "varchar(50)")
	private String address4;

	@Column(name = "POSTAL_CODE_ZIP", columnDefinition = "varchar(20)")
	private String postalCodeZip;

	@Column(name = "GEO_CODE", columnDefinition = "varchar(16)")
	private String geoCode;

	@Column(name = "PHONE1", columnDefinition = "varchar(20)")
	private String phone1;

	@Column(name = "PHONE_EXTRENAL", columnDefinition = "varchar(10)")
	private String phoneExternal;

	@Column(name = "PHONE2", columnDefinition = "varchar(20)")
	private String phone2;

	@Column(name = "MOBILE1", columnDefinition = "varchar(20)")
	private String mobile1;

	@Column(name = "FAX", columnDefinition = "varchar(30)")
	private String fax;

	@Column(name = "EMAIL_ADDRESS1", columnDefinition = "varchar(100)")
	private String emailAddress1;

	@Column(name = "EMAIL_ADDRESS2", columnDefinition = "varchar(100)")
	private String emailAddress2;

	@Column(name = "URL", columnDefinition = "varchar(255)")
	private String url;

	@Column(name = "MOBILE2", columnDefinition = "varchar(20)")
	private String mobile2;

	@Column(name = "FREE_TEXT1", columnDefinition = "varchar(100)")
	private String freeText1;

	@Column(name = "FREE_TEXT2", columnDefinition = "varchar(100)")
	private String freeText2;

    @Column(name = "ENTITY_ID")
    private String entities;
	
	@ManyToOne
	@JoinColumns({
	    @JoinColumn(name = "ENTITY_ID", referencedColumnName = "ENTITY_ID", insertable = false, updatable = false),
	    @JoinColumn(name = "INSTITUTION_ID", referencedColumnName = "INSTITUTION_ID", insertable = false, updatable = false)
	})
	private Entities entitiesObject;

	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(nullable = false, name = "date_create")
	private Date date;

	@NotNull
	@Column(name = "user_create", columnDefinition = "varchar(40)")
	private String userCreate;

	@NotNull
	@Column
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int record_seq_id;

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
