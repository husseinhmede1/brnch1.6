package com.mdsl.model.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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
@JsonInclude(Include.NON_DEFAULT)
public class ActivityResponseDto {
	private Integer activityId;
	private String activityCode;
	private String activityDesc;
	private Integer parentActivityId;
	private String parentActivityDesc;
	private String instId;
	private String instName;
	private Character isMenu;
	private Character hasScreen;
	private Character activityType;
	private Character activityMode;
	private Character accessView;
	private Character accessAdd;
	private Character accessUpdate;
	private Character accessDelete;
	private List<ActivityApiResponseDto> activityApi;
}