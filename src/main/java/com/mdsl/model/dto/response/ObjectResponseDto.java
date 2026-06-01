package com.mdsl.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.mdsl.model.entity.Institution;
import lombok.*;

@Data
@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_DEFAULT)
public class ObjectResponseDto {
	private int objectId;
	private String objectName; 
	private int scopeId;
	private String scopeAbbrev;
	private String cardProduct; 
	private Institution institution; 
}