package com.mdsl.model.mapper;

import com.mdsl.model.dto.response.BlockedIpResponseDto;
import com.mdsl.model.entity.BlockedIp;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface BlockedIpMapper {
    BlockedIpResponseDto toResponse (BlockedIp blockedIp);
    List<BlockedIpResponseDto> toResponse (List<BlockedIp> blockedIp);
    BlockedIp clone(BlockedIp blockedIp);
}