package com.mdsl.model.dto.response;

import lombok.*;

@Data
@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScopeResponseDto {
	private int scopeId;
	private String scopeDesc;
	private String scopeAbbrev;
	private char scopeStatus;
}