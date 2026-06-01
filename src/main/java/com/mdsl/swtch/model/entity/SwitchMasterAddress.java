package com.mdsl.swtch.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "MASTER_ADDRESS")
@IdClass(SwitchMasterAddressPK.class) 
public class SwitchMasterAddress {
	
	@Id
	@Column(name="ADDRESS_ID")
	private Integer addressId;
	
	@Id
	@Column(name="INSTITUTION_ID")
	private String institutionId;
	
	@Column(name="ADDRESS1")
	private String address1;
	
	@Column(name="ADDRESS2")
	private String address2;
	
	@Column(name="CITY")
	private String city;
	
	@Column(name="PROV_STATE_ABBREV")
	private Character provStateAbbrev;
	
	@Column(name="PROV_STATE_NUM_CD")
	private Integer provStateNumCd;
	
	@Column(name="PROVINCE_STATE")
	private String provinceState;
	
	@Column(name="COUNTY_CD")
	private Integer countyCd;
	
	@Column(name="COUNTY")
	private String county;
	
	@Column(name="POSTAL_CD_ZIP")
	private String postalCdZip;
	
	@Column(name="GEOCODE")
	private String geocode;
	
	@Column(name="CNTRY_CODE")
	private String cntryCode;
	
	@Column(name="COUNTRY")
	private String country;
	
	@Column(name="PHONE1")
	private String phone1;
	
	@Column(name="PHONE2")
	private String phone2;
	
	@Column(name="FAX")
	private String fax;
	
	@Column(name="EMAIL_ADDRESS")
	private String emailAddress;
	
	@Column(name="URL")
	private String url;
	
	@Column(name="ADDRESS3")
	private String address3;
	
	@Column(name="ADDRESS4")
	private String address4;
	
	@Column(name="REGION_CODE")
	private String regionCode;
	
	@Column(name="CED_MAPPING")
	private String cedMapping;

}
