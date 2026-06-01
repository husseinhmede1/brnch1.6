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
public class BkdServiceResponseDto {
	private Integer serviceId;
	private String serviceName;
	private String serviceMode;
	private String className;
	private String srcFolder;
	private String destFolder;
	private String filter;
	private Integer batchSize;
}
