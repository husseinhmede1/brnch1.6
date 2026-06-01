package com.mdsl.model.dto.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AcquiringTransactionHaltResponseDto {
	private List<AcquiringTransactionResponseDto> acquiringTransactionResponseDto;
	private List<String> errorMessages;
}