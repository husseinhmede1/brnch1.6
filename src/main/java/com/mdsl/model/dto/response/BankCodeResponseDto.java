package com.mdsl.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankCodeResponseDto {
	private Integer bankCodeId;
	private String institutionName;
	private String institutionId;
	private String bankCode;
	private String bankName;
	private String altBankName;
	private String swiftCode;
}
