package com.mdsl.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.RepresentmentRequestDto;
import com.mdsl.model.dto.response.RepresentmentResponseDto;
import com.mdsl.model.entity.Representment;
import com.mdsl.model.mapper.RepresentmentMapper;
import com.mdsl.repository.RepresentmentRepository;
import com.mdsl.utils.MakerCheckerEngine;
import com.mdsl.utils.ResponseCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RepresentmentService {
	private final RepresentmentRepository representmentRepository;
	private final RepresentmentMapper representmentMapper;
    private final MakerCheckerEngine makerCheckerEngine;

	public RepresentmentResponseDto saveOrUpdateRepresentment(RepresentmentRequestDto representmentRequestDto) {
		Representment representment = representmentMapper.toEntity(representmentRequestDto);

		if (makerCheckerEngine.processIfRequired(representmentRequestDto, this.getClass().getName(), new Object() {}.getClass().getEnclosingMethod().getName(), "")) {
			return null;
		}
		representmentRepository.save(representment);

		return representmentMapper.toDto(representment);
	}

	public List<RepresentmentResponseDto> viewRepresentment() {
		// TODO Auto-generated method stub

		List<Representment> representments = representmentRepository.findAll();

		List<RepresentmentResponseDto> representmentResponseDtos = new ArrayList<>();

		representments.forEach((representment) -> {

			representmentResponseDtos.add(representmentMapper.toDto(representment));
		});

		return representmentResponseDtos;
	}

//
	public RepresentmentResponseDto getRepresentment(int representmentId) {
		// TODO Auto-generated method stub
		Representment representment = representmentRepository.findById(representmentId).orElseThrow(
				() -> new BusinessException(ResponseCode.CFG_REPRESENTMENT_NOT_FOUND, HttpStatus.NOT_FOUND));
		return representmentMapper.toDto(representment);
	}


	public void deleteRepresentment(int representmentId) throws Exception {
		representmentRepository.findById(representmentId).orElseThrow(
				() -> new BusinessException(ResponseCode.CFG_REPRESENTMENT_NOT_FOUND, HttpStatus.NOT_FOUND));
   		if (makerCheckerEngine.processIfRequired(representmentId, this.getClass().getName(), new Object() {}.getClass().getEnclosingMethod().getName(), "")) {
			return;
		}
		representmentRepository.deleteById(representmentId);

	}

}
