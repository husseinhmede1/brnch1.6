package com.mdsl.model.dto.response;

import com.mdsl.model.dto.request.DefaultTransactionIdRequestDto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class DefaultTransactionIdResponseDto {
	private String transactionId;
	private String description;
	private Character signFlag;
	private String transUsage;
	private Integer usageSystemCodeId;
	private String usageDescription; 
	private String institutionId;
	private String institutionName;
	private Character status;
}