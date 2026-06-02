package com.mdsl.model.dto.request;

import javax.validation.constraints.Pattern;

import com.mdsl.utils.ResponseCode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangeStatusRequestDto {
	private Integer id;
	@Pattern(regexp = "[0-1]", message = ResponseCode.CFG_INVALID_STATUS)
	private String status;
	private String idString;
	private String changeAllFlag;
	private String instId;
	private String remoteAddress;
}
