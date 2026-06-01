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
public class SwitchEntitiesRequestDto {
	private String entityId;
	private String entityName;
	private String institutionId;
	private String defaultMcc;
	private Integer entityLevel;
	private String entityDbaName;
	private char entityStatus;
	private String parentEntityId;
	private Date creationCreate;
	private Date actualStartDate;
	private Date terminationDate;
}
