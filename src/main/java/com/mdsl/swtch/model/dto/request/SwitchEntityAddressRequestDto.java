package com.mdsl.swtch.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SwitchEntityAddressRequestDto {
	private String institutionId;
	private String entityId;
	private Integer addressId;
}
