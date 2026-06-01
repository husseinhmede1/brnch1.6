package com.mdsl.model.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter 
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(Include.NON_DEFAULT)
public class ModuleActivityResponseDto {
	private int moduleId;
	private String moduleDesc;
	private List<ActivityModuleResponseDto> activities;
	private List<ModuleActivityResponseDto> subModule;
}