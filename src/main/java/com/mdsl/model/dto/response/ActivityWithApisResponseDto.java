package com.mdsl.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ActivityWithApisResponseDto {
    private int     activityId;
    private String  activityCode;
    private String  activityDesc;
    private String  isMenu;
    private String  hasScreen;
    private String  accessView;
    private String  accessAdd;
    private String  accessUpdate;
    private String  accessDelete;
    private String  accessChecker;
    private List<ActivityApiDto> apis;
}
