package com.mdsl.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntityLevelsResponseDto {
	private int entityLevelId;
	private int hierarchyLevel;
	private String typeDescription;
	private int idLength;
	private int generationMethod;
	private char statementFlag;
}