package com.mdsl.model.dto.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityEntityMappingRequestDto {
	private Integer id;
	private List<String> entities;
	private String instId;
}
