package com.mdsl.model.dto.response;

import java.util.List;

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
public class LayoutResponseDto {
	private Integer layoutId;
	private String layoutName;
	private FileResponseDto fileResponseDto;
	private String layoutFormat;
	private String layoutFormatDesc;
	private String layoutSeparator;
	private boolean includesHeader;
	private Integer instId;
	private String instName;		
	private String status;
	private List<LayoutDetailsResponseDto> listLayoutDetailsResponse;
}