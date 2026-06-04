package com.mdsl.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.FrequencyMasterRequestDto;
import com.mdsl.model.dto.response.FrequencyMasterResponseDto;
import com.mdsl.model.entity.FrequencyMaster;
import com.mdsl.model.mapper.FrequencyMasterMapper;
import com.mdsl.repository.FrequencyMasterRepository;
import com.mdsl.utils.MakerCheckerEngine;
import com.mdsl.utils.ResponseCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FrequencyMasterService {
	@Autowired
	private FrequencyMasterRepository frequencyMasterRepository;

	@Autowired
	private FrequencyMasterMapper frequencyMasterMapper;

	@Autowired
	private MakerCheckerEngine makerCheckerEngine;

	public List<FrequencyMasterResponseDto> getAllFrequencies() {
		List<FrequencyMasterResponseDto> allFrequencyMasterCodesDto = new ArrayList<FrequencyMasterResponseDto>();
		List<FrequencyMaster> allFrequencyMasterCodes = frequencyMasterRepository.findAll();

		allFrequencyMasterCodes.stream().forEach((frequencyMasterCode) -> {
			FrequencyMasterResponseDto frequencyMasterCodeResponseDto = frequencyMasterMapper.toDto(frequencyMasterCode);
			allFrequencyMasterCodesDto.add(frequencyMasterCodeResponseDto);
		});

		return allFrequencyMasterCodesDto;
	}

	public FrequencyMasterResponseDto getFrequencyById(int id) {
		Optional<FrequencyMaster> frequencyMaster = frequencyMasterRepository.findById(id);
		FrequencyMaster frequency = frequencyMaster
				.orElseThrow(() -> new BusinessException(ResponseCode.FRQ_FREQUENCY_LIMIT_NOT_FOUND, HttpStatus.NOT_FOUND));
		return frequencyMasterMapper.toDto(frequency);
	}

	public FrequencyMasterResponseDto saveOrUpdateFrequency(FrequencyMasterRequestDto frequencyMasterRequestDto) {

		FrequencyMaster frequency = null;
		FrequencyMaster frequencyMaster;

		if (frequencyMasterRequestDto.getFrequencyId() != 0) {
			frequency = frequencyMasterRepository.findById(frequencyMasterRequestDto.getFrequencyId())
					.orElseThrow(() -> new BusinessException(ResponseCode.FRQ_FREQUENCY_LIMIT_NOT_FOUND, HttpStatus.NOT_FOUND));
		}

		if (makerCheckerEngine.processIfRequired(frequencyMasterRequestDto, this.getClass().getName(), new Object() {}.getClass().getEnclosingMethod().getName(), "")) {
			return null;
		}

		if (frequency == null) {
			frequencyMaster = frequencyMasterMapper.toEntity(frequencyMasterRequestDto);
			frequencyMaster = frequencyMasterRepository.save(frequencyMaster);
		}

		else {
			frequencyMaster = frequencyMasterRepository.save(frequency);
		}

		return frequencyMasterMapper.toDto(frequencyMaster);
	}

	public void deleteFrequency(int id) {
		FrequencyMaster frequencyMaster = frequencyMasterRepository.findById(id)
				.orElseThrow(() -> new BusinessException(ResponseCode.FRQ_FREQUENCY_LIMIT_NOT_FOUND, HttpStatus.NOT_FOUND));
		if (frequencyMaster != null) {
			if (makerCheckerEngine.processIfRequired(id, this.getClass().getName(), new Object() {}.getClass().getEnclosingMethod().getName(), "")) {
				return;
			}
			frequencyMasterRepository.deleteById(id);
		}
	}
}
