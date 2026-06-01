package com.mdsl.swtch.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "INSTITUTION")
public class SwitchInstitution {
	
	@Id
	@Column(name = "INSTITUTION_ID")
	private String institutionId;
	
	@Column(name = "INST_TYPE")
	private String instType;
	
	@Column(name = "INST_NAME")
	private String instName;
	
	@Column(name = "INST_FYEAR_ST_DT")
	private String instFyearStDt;
	
	@Column(name = "LANGUAGE_CODE")
	private String languageCode;
	
	@Column(name = "SECONDARY_LANG_CD")
	private String secondaryLangCd;
	
	@Column(name = "INST_CURR_CODE")
	private String instCurrCode;
	
	@Column(name = "CED_MAPPING")
	private String cedMapping;

}
