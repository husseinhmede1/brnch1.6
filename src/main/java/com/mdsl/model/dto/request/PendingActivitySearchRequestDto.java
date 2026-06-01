package com.mdsl.model.dto.request;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PendingActivitySearchRequestDto {

    @Min(value = 0, message = "Invalid offset")
    @Max(value = 999999, message = "Invalid offset")
    private int offset;

    @Min(value = 1, message = "Invalid page size")
    @Max(value = 100, message = "Invalid page size")
    private int pageSize;

    private String status;    // optional: PENDING | APPROVED | DECLINED | PROCESSING
    private Integer apiId;    // optional
    private String fromDate;  // optional: YYYY-MM-DD
    private String toDate;    // optional: YYYY-MM-DD
}
