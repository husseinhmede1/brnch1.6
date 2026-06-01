package com.mdsl.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_DEFAULT)
public class ApiApiListResponseDto {
	private Integer apiId; 
	private Character loginRequired;
	private Character roleRequired; 
	private Integer apiListId; 
	private Character stp; 
	private String description; 
	private Integer scopeId; 
}
