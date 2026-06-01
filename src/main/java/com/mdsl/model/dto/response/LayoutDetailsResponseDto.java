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
public class LayoutDetailsResponseDto {
	private Integer detailsId;
	private Integer elementId;
	private String elementName;
	private Integer elementLength;
	private String elementPaddingType;
	private String elementPaddingValue;
	private String elementSection;
	private Integer elementOrder;
	private Integer elementParentId;
	private String elementParentName;
}