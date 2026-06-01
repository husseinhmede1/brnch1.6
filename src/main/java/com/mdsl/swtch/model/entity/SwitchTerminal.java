package com.mdsl.swtch.model.entity;

import java.util.Date;

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
@Entity(name = "TERMINAL")
public class SwitchTerminal {
	
	@Id
	@Column(name = "TERM_RECORD_SEQ")
	private Integer termRecordSeq;
	
	@Column(name = "INSTITUTION_ID")
	private String institutionId;
	
	@Column(name = "TERMINAL_ID_LOW")
	private String terminalIdLow;
	
	@Column(name = "TERMINAL_ID_HIGH")
	private String terminalIdHigh;
	
	@Column(name = "ALLW_ID")
	private Integer allwId;
	
	@Column(name = "BAR_ACCESS")
	private Character barAccess;
	
	@Column(name = "HOUR_ID")
	private Integer hourId;
	
	@Column(name = "ACTUAL_START_DATE")
	private Date actualStartDate;
	
	@Column(name = "TERMINATION_DATE")
	private Date termninationDate;
	
	@Column(name = "TERMINAL_ID_NR")
	private Integer terminalIdNr;
	
	@Column(name = "POS_TERMINAL_TYPE")
	private String posTerminalType;
	
	@Column(name = "POS_MSG_BRAND")
	private String posMsgBrand;
	
	@Column(name = "OWNER_ENTITY_ID")
	private String ownerEntityId;
	
	@Column(name = "DEFAULT_CURR_CD")
	private String defaultCurrCd;
	
	@Column(name = "CPS_ELIGIBLE")
	private Character cpsEligible;
	
	@Column(name = "DEFAULT_MCC")
	private String defaultMcc;
	
	@Column(name = "CAPTURE_TYPE")
	private Character captureType;
	
	@Column(name = "SERIAL_NUMBER")
	private String serialNumber;
	
	@Column(name = "TERMINAL_ENTITY_ID")
	private String terminalEntityId;

}
