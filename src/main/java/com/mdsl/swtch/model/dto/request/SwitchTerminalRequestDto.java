package com.mdsl.swtch.model.dto.request;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SwitchTerminalRequestDto {
	private String ownerEntityId;
	private String defaultCurrCd;
	private Date termninationDate;
	private String terminalId;
	private String posTerminalType;
	private String defaultMcc;
	private String institutionId;
	private Date actualStartDate;
	private String serialNumber;
	private String terminalEntityId;
}
