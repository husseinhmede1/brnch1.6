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
@Entity(name = "POS_TERM_TYPE")
public class SwitchPosTermType {
	
	@Id
	@Column(name = "POS_TERMINAL_TYPE")
	private String posTerminalType;
	
	@Column(name = "POS_TERM_MAKE")
	private String posTermMake;
	
	@Column(name = "POS_TERM_MODEL")
	private String posTermModel;
	
	@Column(name = "CARD_INPUT_CAPAB")
	private Character cardInputCapab;
	
	@Column(name = "CH_AUTH_CAPAB")
	private Character chAuthCapab;
	
	@Column(name = "CARD_CAP_CAPAB")
	private Character cardCapCapab;
	
	@Column(name = "OPER_ENV")
	private Character operEnv;
	
	@Column(name = "CVV_ELIGIBLE")
	private Character cvvEligible;
	
	@Column(name = "CARD_UPDATE")
	private Character cardUpdate;
	
	@Column(name = "TERMINAL_OUTPUT")
	private Character terminalOutput;
	
	@Column(name = "PIN_CAPTURE")
	private Character pinCapture;
	
	@Column(name = "CAT_LEVEL")
	private Character catLevel;
	
	@Column(name = "KEYS_LOAD_TYPE")
	private Character keysLoadType;
	
	@Column(name = "SRVRESTR_CD")
	private Character srvrEstrCd;
	
	@Column(name = "COND_CD")
	private Character condCd;
	
	@Column(name = "SENDS_BATCH_NR")
	private Character sendsBatchNr;
	
	@Column(name = "REVERSAL_IND")
	private Character reversalInd;
	
	@Column(name = "DEVICE_TYPE")
	private Character deviceType;
	
	@Column(name = "SENDS_320_TWICE")
	private Character sends320Twice;
	
	@Column(name = "SERVER_MBOX")
	private String serverMbox;
	
	@Column(name = "CAPABILITIES")
	private String capabilities;

}
