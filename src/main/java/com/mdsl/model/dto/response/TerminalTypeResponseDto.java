package com.mdsl.model.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TerminalTypeResponseDto {

	private Integer terminalTypesId;
	private String terminalType;
	private String makeName;
	private String makeModel;
	private String posCapability;
	private String freeText;
	private String status;
}
