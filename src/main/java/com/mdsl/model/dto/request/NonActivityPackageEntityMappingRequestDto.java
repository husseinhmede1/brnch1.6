package com.mdsl.model.dto.request;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.mdsl.model.entity.Entities;
import com.mdsl.utils.ResponseCode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NonActivityPackageEntityMappingRequestDto {

	@NotEmpty(message = ResponseCode.CFG_INVALID_ACTIVITY)
	private String id;

	private List<String> entities;

	private String instId;
}
