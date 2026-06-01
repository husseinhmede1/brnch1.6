package com.mdsl.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_DEFAULT)
public class ApiListResponseDto {
	private ApiResponseDto api;
	private int instId;
	private String instName;
	private String apiFunction; 
	private ScopeResponseDto scope; 
	private String apiObject; 
	private String apiDesc;
	private String apiUrl;
	private String stp;
}