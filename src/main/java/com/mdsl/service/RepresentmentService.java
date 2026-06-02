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

	@Autowired
	private RepresentmentRepository representmentRepository;

	@Autowired
	private RepresentmentMapper representmentMapper;
    private final MakerCheckerEngine makerCheckerEngine;

	public RepresentmentResponseDto saveOrUpdateRepresentment(RepresentmentRequestDto representmentRequestDto) {
		// TODO Auto-generated methoRd stub
		Representment representment = new Representment();

		RepresentmentResponseDto representmentResponseDto = new RepresentmentResponseDto();

		if (representmentRequestDto.getRePresentmentId() != 0) {

			representment = representmentRepository.findById(representmentRequestDto.getRePresentmentId()).get();
		}

		if (representment != null && representmentResponseDto.getRePresentmentId() != 0) {

			representment = representmentMapper.toEntity(representmentRequestDto);
	   		if (makerCheckerEngine.processIfRequired(representmentRequestDto, RepresentmentService.class.getName(), "saveOrUpdateRepresentment", "")) {
				return null;
			}
			representmentRepository.save(representment);

			representmentResponseDto = representmentMapper.toDto(representment);

//				
			return representmentResponseDto;
		} else {
			Representment representment1 = new Representment();

			representment1 = representmentMapper.toEntity(representmentRequestDto);
	   		if (makerCheckerEngine.processIfRequired(representmentRequestDto, RepresentmentService.class.getName(), "saveOrUpdateRepresentment", "")) {
				return null;
			}
			representmentRepository.save(representment1);
			representmentResponseDto = representmentMapper.toDto(representment1);

			return representmentResponseDto;
		}
	}

	public List<RepresentmentResponseDto> viewRepresentment() {
		// TODO Auto-generated method stub

		List<Representment> representments = representmentRepository.findAll();

		List<RepresentmentResponseDto> representmentResponseDtos = new ArrayList();

		representments.stream().forEach((representment) -> {

			representmentResponseDtos.add(representmentMapper.toDto(representment));
		});

		return representmentResponseDtos;
	}

//
	public RepresentmentResponseDto getRepresentment(int representmentId) {
		// TODO Auto-generated method stub

		Representment representment = representmentRepository.findById(representmentId).orElseThrow(
				() -> new BusinessException(ResponseCode.CFG_REPRESENTMENT_NOT_FOUND, HttpStatus.NOT_FOUND));

		if (representment != null) {
			return representmentMapper.toDto(representment);
		} else {
			throw new BusinessException(ResponseCode.CFG_REPRESENTMENT_NOT_FOUND, HttpStatus.NOT_FOUND);
		}

	}


	public void deleteRepresentment(int representmentId) throws Exception {
		representmentRepository.findById(representmentId).orElseThrow(
				() -> new BusinessException(ResponseCode.CFG_REPRESENTMENT_NOT_FOUND, HttpStatus.NOT_FOUND));
   		if (makerCheckerEngine.processIfRequired(representmentId, RepresentmentService.class.getName(), "deleteRepresentment", "")) {
			return;
		}
		representmentRepository.deleteById(representmentId);

	}

}
