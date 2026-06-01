package com.mdsl.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class PendingActivityResponseDto {
    private Integer pendingActivityId;
    private String  apiDesc;
    private String  apiUrl;
    private String  status;
    private String  notes;
    private String  instId;
    private String  clazz;
    private String  method;
    private String  createdDate;
    private Integer createdById;
    private String  createdByUsername;
}
