package com.mdsl.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.CardSchemeRequestDto;
import com.mdsl.model.dto.request.ChangeStatusRequestDto;
import com.mdsl.model.dto.response.CardSchemeResponseDto;
import com.mdsl.model.entity.CardScheme;
import com.mdsl.model.mapper.CardSchemeMapper;
import com.mdsl.repository.CardSchemeRepository;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.enumerations.StatusEnum;

@Service
public class CardSchemeService {


	@Autowired
	private CardSchemeRepository cardSchemeRepository;

	@Autowired
	private CardSchemeMapper cardSchemeMapper;

	public List<CardSchemeResponseDto> fetchAllCardScheme() {

		List<CardScheme> cardScheme = cardSchemeRepository.findAll(Sort.by(Sort.Direction.ASC, "cardSchemeId"));
		List<CardSchemeResponseDto> dto = new ArrayList<CardSchemeResponseDto>();
		for (CardScheme temp : cardScheme) {
			CardSchemeResponseDto dtoTemp = cardSchemeMapper.toDto(temp);
			dto.add(dtoTemp);
		}
		return dto;

	}

	public CardSchemeResponseDto fetchCardSchemeById(String id) {

		CardScheme cardScheme = cardSchemeRepository.findById(id)
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_CARDSCHEME_NOT_FOUND, HttpStatus.NOT_FOUND));
		CardSchemeResponseDto dto1 = cardSchemeMapper.toDto(cardScheme);
		return dto1;

	}

	public CardSchemeResponseDto saveOrUpdateCardScheme(@Valid CardSchemeRequestDto request) {
		if (request == null) {
			throw new BusinessException(ResponseCode.VAL_ERROR_OCCURRED, HttpStatus.BAD_REQUEST);
		}

		try {
			if (request.getCardSchemeId() == null || request.getCardSchemeId().trim().isEmpty()) {
				throw new BusinessException(ResponseCode.CFG_CARDSCHEME_NOT_FOUND, HttpStatus.BAD_REQUEST);
			}

			if (request.getStatus() == null || request.getStatus().isEmpty()) {
				throw new BusinessException(ResponseCode.VAL_ERROR_OCCURRED, HttpStatus.BAD_REQUEST);
			}

			UserDetailsImpl userDetails = null;
			try {
				userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			} catch (Exception e) {
			}

			CardScheme cardScheme;

			Optional<CardScheme> existing = cardSchemeRepository.findByCardSchemeId(request.getCardSchemeId());

			if (existing.isPresent()) {
				cardScheme = existing.get();
				boolean duplicate = cardSchemeRepository.existsByCardSchemeIdAndRecordSequenceNumberNot(
						request.getCardSchemeId(), request.getRecordSequenceNumber()
				);

				if (duplicate) {
					throw new BusinessException(ResponseCode.CFG_CARDSCHEME_ALREADY_EXISTS, HttpStatus.CONFLICT);
				}

				cardScheme.setCardSchemeId(request.getCardSchemeId());
				cardScheme.setCardSchemeName(request.getCardSchemeName());
				cardScheme.setCardSchemeSpecific(request.getCardSchemeSpecific());
				cardScheme.setStatus(request.getStatus().charAt(0));
			} else {
				if (cardSchemeRepository.existsByCardSchemeId(request.getCardSchemeId())) {
					throw new BusinessException(ResponseCode.CFG_CARDSCHEME_ALREADY_EXISTS, HttpStatus.CONFLICT);
				}

				cardScheme = cardSchemeMapper.toEntity(request);
				cardScheme.setStatus(request.getStatus().charAt(0));
				cardScheme.setCreatedDate(new Date());
				cardScheme.setRecordSequenceNumber(null);

				if (userDetails != null) {
					cardScheme.setCreatedBy(String.valueOf(userDetails.getId()));
				}
			}

			CardScheme saved = cardSchemeRepository.save(cardScheme);
			return cardSchemeMapper.toDto(saved);

		} catch (BusinessException e) {
			throw new BusinessException (e.getMessage(), e.getHttpStatus());
		}
	}

	public void deleteCardSchemeById(String id) throws Exception {
		cardSchemeRepository.findById(id)
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_CARDSCHEME_NOT_FOUND, HttpStatus.BAD_REQUEST));
		cardSchemeRepository.deleteById(id);
	}

	public String changeStatus(@Valid ChangeStatusRequestDto changeStatusRequestDto) {
		CardScheme cardScheme = cardSchemeRepository.findById(String.valueOf(changeStatusRequestDto.getIdString()))
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_CARDSCHEME_NOT_FOUND, HttpStatus.NOT_FOUND));
		cardScheme.setStatus(changeStatusRequestDto.getStatus().charAt(0));
		cardSchemeRepository.save(cardScheme);

		return "Card scheme status changed successfully";
	}

	public List<CardSchemeResponseDto> getActiveCardScheme() {

		List<CardScheme> cardScheme = cardSchemeRepository.findByStatus(StatusEnum.ENABLED.getValue(),
				Sort.by(Sort.Direction.ASC, "cardSchemeId"));
		List<CardSchemeResponseDto> dto = new ArrayList<CardSchemeResponseDto>();
		for (CardScheme temp : cardScheme) {
			CardSchemeResponseDto dtoTemp = cardSchemeMapper.toDto(temp);
			dto.add(dtoTemp);
		}
		return dto;

	}

}
