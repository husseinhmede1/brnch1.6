package com.mdsl.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.mdsl.model.objects.FrontUrl;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(Include.NON_DEFAULT)
public class ActivityModuleResponseDto {
	private int activityId;
	private String subMenu;
	private String activityCode;
	private String activityDesc;
	private char isMenu;
	private char hasScreen;
	private List<FrontUrl> url;
	private char accessView;
	private char accessAdd;
	private char accessUpdate;
	private char accessDelete;
	private char accessChecker;
}