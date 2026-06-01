package com.mdsl.service;

import java.util.List;

import com.mdsl.model.dto.request.PaginationRequestDto;
import com.mdsl.model.dto.response.PaginationApiResponseDto;
import com.mdsl.model.dto.response.PaginationResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.PaginationCommonRequestDto;
import com.mdsl.model.dto.request.SpecificSortPaginationRequestDto;
import com.mdsl.model.dto.response.PaginationCommonResponseDto;
import com.mdsl.model.entity.Institution;
import com.mdsl.repository.SystemCodeRepository;
import com.mdsl.utils.ResponseCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommonService {

	public final SystemCodeRepository systemCodeRepository;
	
	public PaginationCommonResponseDto getPaginationCommonResponseDto(Object paginationRequestDto, Page<?> pagesResult) {
		PaginationCommonResponseDto paginationResponseDto = new PaginationCommonResponseDto();

		if (paginationRequestDto instanceof  PaginationCommonRequestDto) {
			PaginationCommonRequestDto request = (PaginationCommonRequestDto) paginationRequestDto;
			paginationResponseDto.setAsc(request.getAsc());
			paginationResponseDto.setPageNumber(request.getOffset());
			paginationResponseDto.setPageSize(request.getPageSize());
			paginationResponseDto.setSortBy(request.getSortBy());

		} else if (paginationRequestDto instanceof SpecificSortPaginationRequestDto) {
			SpecificSortPaginationRequestDto request = (SpecificSortPaginationRequestDto) paginationRequestDto;
			paginationResponseDto.setPageNumber(request.getOffset());
			paginationResponseDto.setPageSize(request.getPageSize());
		}
		long totalElements = pagesResult.getTotalElements();
		paginationResponseDto.setTotalNumberOfRecords((int)totalElements);
		return paginationResponseDto;
	}

	public PaginationCommonResponseDto getPaginationCommonResponseDto(Object paginationRequestDto, List<?> pagesResult, int countNb) {
		PaginationCommonResponseDto paginationResponseDto = new PaginationCommonResponseDto();

		if (paginationRequestDto instanceof  PaginationCommonRequestDto) {
			PaginationCommonRequestDto request = (PaginationCommonRequestDto) paginationRequestDto;
			paginationResponseDto.setAsc(request.getAsc());
			paginationResponseDto.setPageNumber(request.getOffset());
			paginationResponseDto.setPageSize(request.getPageSize());
			paginationResponseDto.setSortBy(request.getSortBy());

		} else if (paginationRequestDto instanceof SpecificSortPaginationRequestDto) {
			SpecificSortPaginationRequestDto request = (SpecificSortPaginationRequestDto) paginationRequestDto;
			paginationResponseDto.setPageNumber(request.getOffset());
			paginationResponseDto.setPageSize(request.getPageSize());
		}
		paginationResponseDto.setTotalNumberOfRecords(countNb);
		return paginationResponseDto;
	}
	
	public String getKeyValue (String prefix, String suffix, Institution institution) {
		return systemCodeRepository.findByCodePrefixAndCodeSuffixAndInstitution(prefix, suffix, institution)
				.orElseThrow(()-> new BusinessException(ResponseCode.MISSING_CONFIGURATION, HttpStatus.NOT_FOUND)).getCodeValue();
	}
	
	public UserDetailsImpl getLoggedInUser() {
		return (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	public PaginationApiResponseDto getPaginationResponseDto(Object paginationRequestDto, Page<?> pagesResult) {
		PaginationApiResponseDto paginationResponseDto = new PaginationApiResponseDto();

		if (paginationRequestDto instanceof PaginationRequestDto) {
			PaginationRequestDto request = (PaginationRequestDto) paginationRequestDto;
			paginationResponseDto.setAsc(request.getAsc());
			paginationResponseDto.setPageNumber(request.getOffset());
			paginationResponseDto.setPageSize(request.getPageSize());
			paginationResponseDto.setSortBy(request.getSortBy());

		} else if (paginationRequestDto instanceof SpecificSortPaginationRequestDto) {
			SpecificSortPaginationRequestDto request = (SpecificSortPaginationRequestDto) paginationRequestDto;
			paginationResponseDto.setPageNumber(request.getOffset());
			paginationResponseDto.setPageSize(request.getPageSize());
		}
		long totalElements = pagesResult.getTotalElements();
		paginationResponseDto.setTotalNumberOfRecords((int)totalElements);
		return paginationResponseDto;
	}
}
