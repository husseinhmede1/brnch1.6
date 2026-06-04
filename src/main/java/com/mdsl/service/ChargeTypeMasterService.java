package com.mdsl.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import com.mdsl.utils.MakerCheckerEngine;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.ChargeTypeMasterRequestDto;
import com.mdsl.model.dto.response.ChargeTypeMasterResponseDto;
import com.mdsl.model.entity.ChargeTypeMaster;
import com.mdsl.model.mapper.ChargeTypeMasterMapper;
import com.mdsl.repository.ChargeTypeMasterRepository;
import com.mdsl.utils.ResponseCode;

@Service
@RequiredArgsConstructor
public class ChargeTypeMasterService {
    private final ChargeTypeMasterRepository chargeTypeMasterRepository;
    private final ChargeTypeMasterMapper chargeTypeMasterMapper;
    private final MakerCheckerEngine makerCheckerEngine;

    public List<ChargeTypeMasterResponseDto> fetchAllChargeTypeMaster() {
        List<ChargeTypeMaster> chargeTypeMaster = chargeTypeMasterRepository.findAll(Sort.by(Sort.Direction.ASC, "chargeTypeMasterId"));
        List<ChargeTypeMasterResponseDto> dto = new ArrayList<ChargeTypeMasterResponseDto>();
        for (ChargeTypeMaster temp : chargeTypeMaster) {
            ChargeTypeMasterResponseDto dtoTemp = chargeTypeMasterMapper.toDto(temp);
            dto.add(dtoTemp);
        }
        return dto;
    }

    public ChargeTypeMasterResponseDto fetchChargeTypeMasterById(int id) {
        ChargeTypeMaster chargeTypeMaster = chargeTypeMasterRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.CFG_CHARGE_TYPE_MASTER_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
        ChargeTypeMasterResponseDto dto = chargeTypeMasterMapper.toDto(chargeTypeMaster);
        return dto;
    }

    public void deleteChargeTypeMasterById(int id) {
        chargeTypeMasterRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.CFG_CHARGE_TYPE_MASTER_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
        if (makerCheckerEngine.processIfRequired(id, this.getClass().getName(), new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName(), "")) {
            return;
        }
        chargeTypeMasterRepository.deleteById(id);
    }

    public ChargeTypeMasterResponseDto saveOrUpdateChargeTypeMaster(@Valid ChargeTypeMasterRequestDto chargeTypeMasterRequestDto) {
        ChargeTypeMaster chargeTypeMaster;
        ChargeTypeMaster finalList;

        if (chargeTypeMasterRequestDto.getChargeTypeMasterId() == 0) {
            chargeTypeMaster = chargeTypeMasterMapper.toEntity(chargeTypeMasterRequestDto);
        } else {
            chargeTypeMaster = chargeTypeMasterRepository.findById(chargeTypeMasterRequestDto.getChargeTypeMasterId())
                    .orElseThrow(() -> new BusinessException(ResponseCode.CFG_CHARGE_TYPE_MASTER_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
            chargeTypeMaster.setChargeTypeMasterName(chargeTypeMasterRequestDto.getChargeTypeMasterName());
        }
        if (makerCheckerEngine.processIfRequired(chargeTypeMasterRequestDto, this.getClass().getName(), new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName(), "")) {
            return null;
        }
        finalList = chargeTypeMasterRepository.save(chargeTypeMaster);
        ChargeTypeMasterResponseDto dto = chargeTypeMasterMapper.toDto(finalList);
        return dto;
    }

}
