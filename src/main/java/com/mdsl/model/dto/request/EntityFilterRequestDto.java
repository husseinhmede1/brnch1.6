package com.mdsl.model.dto.request;

import javax.validation.constraints.NotEmpty;

import com.mdsl.utils.ResponseCode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntityFilterRequestDto {
	@NotEmpty(message = ResponseCode.CFG_INVALID_INSTITUTION_ID)
	private String institutionId;
	private String entityId;
	private String parentId;
	private String entityName;
	private String mcc;
}