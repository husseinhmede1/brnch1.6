package com.mdsl.model.dto.response;

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
public class FileResponseDto {
	private Integer fileId;
	private String fileName;
	private Integer fileTypeId;
	private String fileTypeCode;
	private String status;
}