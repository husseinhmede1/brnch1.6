package com.mdsl.model.dto.request;

import javax.validation.constraints.NotEmpty;

import com.mdsl.utils.ResponseCode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntityLevelsRequestDto {
	@NotEmpty(message = ResponseCode.CFG_INVALID_INSTITUTION_ID)
	private String institutionId;
	private int entityLevelId;
	private int hierarchyLevel;
	private String typeDescription;
	private int idLength;
	private int generationMethod;
	private char statementFlag;
}
