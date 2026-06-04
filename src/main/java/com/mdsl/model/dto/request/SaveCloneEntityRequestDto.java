package com.mdsl.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaveCloneEntityRequestDto {
	private String clonedEntityId;
	private EntityRequestDto entityRequestDto;
}
