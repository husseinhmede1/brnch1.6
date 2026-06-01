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
public class PageablePendingActivityResponseDto {
    private List<PendingActivityResponseDto> pendingActivities;
    private PaginationApiResponseDto         paginationResponseDto;
}
