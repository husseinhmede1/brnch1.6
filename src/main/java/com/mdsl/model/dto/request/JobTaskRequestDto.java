package com.mdsl.model.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobTaskRequestDto {


	private Integer jobTaskId;
	private String taskName;
	private String taskDescription;
	private Integer serviceId;
}
