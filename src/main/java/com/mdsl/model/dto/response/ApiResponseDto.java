package com.mdsl.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_DEFAULT)
public class ApiResponseDto {
	private int apiId;
	private String apiCode;
	private String apiUrl;
	private String apiObject;
	private String apiFunction;
	private String apiDesc;
	private char enabled;
	private Integer instId;
	private Character stp;
}