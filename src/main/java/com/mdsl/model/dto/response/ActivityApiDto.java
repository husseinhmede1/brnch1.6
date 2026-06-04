package com.mdsl.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ActivityApiDto {
    private Integer apiId;
    private String  apiUrl;
    private String  apiFunction;
    private String  apiDesc;
}
