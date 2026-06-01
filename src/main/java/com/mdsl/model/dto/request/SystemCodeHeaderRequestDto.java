package com.mdsl.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemCodeHeaderRequestDto {

	private int systemCodeHeaderId;

	private String codePrefix;

	private char codePattern;

	private int maxSuffixLength;

	private String description;

	private char userFlag;
}