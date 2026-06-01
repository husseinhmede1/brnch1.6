package com.mdsl.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class BlockedIpPaginationResponseDto {
    private List<BlockedIpResponseDto> blockedIpResponseDto;
    private Integer totalCount;
}