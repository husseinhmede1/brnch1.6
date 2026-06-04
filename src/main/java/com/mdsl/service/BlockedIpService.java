package com.mdsl.service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.PaginatedRequestDto;
import com.mdsl.model.dto.request.PaginationRequestDto;
import com.mdsl.model.dto.response.BlockedIpPaginationResponseDto;
import com.mdsl.model.dto.response.BlockedIpResponseDto;
import com.mdsl.model.entity.BlockedIp;
import com.mdsl.model.mapper.BlockedIpMapper;
import com.mdsl.repository.BlockedIpRepository;
import com.mdsl.utils.CommonServices;
import com.mdsl.utils.MakerCheckerEngine;
import com.mdsl.utils.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlockedIpService {

    private final BlockedIpRepository blockedIpRepository;
    private final CommonServices commonServices;
    private final BlockedIpMapper blockedIpMapper;
    private final MakerCheckerEngine makerCheckerEngine;

    public List<BlockedIpResponseDto> getAllBlockedIps(){
        List<BlockedIp> data = blockedIpRepository.findAll();
        return blockedIpMapper.toResponse(data);
    }

    public String unblockIp(String id){
        if (!id.matches("\\d+")) {
            throw new BusinessException(ResponseCode.CFG_INVALID_ID,HttpStatus.BAD_REQUEST);
        }

        BlockedIp blockedIp= blockedIpRepository.findById(Long.parseLong(id))
                .orElseThrow(()-> new BusinessException(ResponseCode.CFG_NO_DATA_FOUND , HttpStatus.BAD_REQUEST));
        if (makerCheckerEngine.processIfRequired(id, this.getClass().getName(), new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName(), "")) {
            return null;
        }
        blockedIpRepository.delete(blockedIp);
        return "IP Unblocked successfully";
    }
}