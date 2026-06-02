package com.mdsl.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.MCCListRequestDto;
import com.mdsl.model.dto.response.MCCListResponseDto;
import com.mdsl.model.dto.response.PaginationResponseDto;
import com.mdsl.model.entity.CardScheme;
import com.mdsl.model.entity.Entities;
import com.mdsl.model.entity.MCCList;
import com.mdsl.model.entity.MerchantType;
import com.mdsl.model.entity.SystemCode;
import com.mdsl.model.entity.Terminal;
import com.mdsl.model.mapper.MCCListMapper;
import com.mdsl.repository.CardSchemeRepository;
import com.mdsl.repository.EntitiesRepository;
import com.mdsl.repository.MCCListRepository;
import com.mdsl.repository.MCCListRepositoryImpl;
import com.mdsl.repository.MerchantTypeRepository;
import com.mdsl.repository.SystemCodeRepository;
import com.mdsl.repository.TerminalRepository;
import com.mdsl.utils.MakerCheckerEngine;
import com.mdsl.utils.PaginationCommonCode;
import com.mdsl.utils.ResponseCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MCCListService {
    private final MakerCheckerEngine makerCheckerEngine ;

	@Autowired
	private CardSchemeRepository cardSchemeRepository;

	@Autowired
	private MerchantTypeRepository merchantTypeRepository;

	@Autowired
	private EntitiesRepository entitiesRepository;

	@Autowired
	private TerminalRepository terminalRepository;

	@Autowired
	private MCCListRepository mccListRepository;

	@Autowired
	private MCCListMapper mccListMapper;

	@Autowired
	private MCCListRepositoryImpl mccListRepositoryImpl;

	@Autowired
	private SystemCodeRepository systemCodeRepository;
	
	

	public MCCListResponseDto saveOrUpdateMccList(MCCListRequestDto mccListRequestDto) {
		MCCList mccList;
		MCCList finalList;
		
		if(mccListRequestDto.getDescription()!=null) {
			mccListRequestDto.setDescription(mccListRequestDto.getDescription().trim());
		}
		
		if(mccListRequestDto.getCardSchemeId()!=null) {
			mccListRequestDto.setCardSchemeId(mccListRequestDto.getCardSchemeId().trim());
		}
		
		if(mccListRequestDto.getMcc()!=null) {
			mccListRequestDto.setMcc(mccListRequestDto.getMcc().trim());
		}
		
		
		UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();

		SystemCode merchantType = systemCodeRepository.findById(mccListRequestDto.getMerchantTypeId())
				.orElseThrow(() -> new BusinessException(ResponseCode.SYS_CODE_ID_NOT_FOUND, HttpStatus.NOT_FOUND));

		CardScheme cardScheme = cardSchemeRepository.findById(mccListRequestDto.getCardSchemeId())
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_CARDSCHEME_NOT_FOUND, HttpStatus.NOT_FOUND));

		if (Objects.isNull(mccListRequestDto.getMccId()) || mccListRequestDto.getMccId() == 0) {
			List<MCCList> mccLists=mccListRepository.findByCardSchemeTypeMapping_CardSchemeIdEqualsIgnoreCaseAndMccEqualsIgnoreCaseAndMerchantType_SystemCodeIdAndDescriptionEqualsIgnoreCase(mccListRequestDto.getCardSchemeId(),mccListRequestDto.getMcc(),mccListRequestDto.getMerchantTypeId(),mccListRequestDto.getDescription());
			if(mccLists.size()>0) {
				throw new BusinessException(ResponseCode.MCC_ALREADY_EXISTS, HttpStatus.NOT_FOUND);
			}
			List<MCCList> mccLists2 = mccListRepository.findByMccEqualsIgnoreCaseAndCardSchemeTypeMapping_CardSchemeIdEqualsIgnoreCase(mccListRequestDto.getMcc(),mccListRequestDto.getCardSchemeId());
			if(mccLists2.size()>0){
				throw new BusinessException(ResponseCode.MCC_ALREADY_EXISTS, HttpStatus.NOT_FOUND);
			}
			mccList = mccListMapper.toEntity(mccListRequestDto);
			mccList.setMerchantType(merchantType);
			mccList.setCardSchemeTypeMapping(cardScheme);
			mccList.setCreatedBy("Harshil");
			mccList.setCreatedDate(new Date());
			if(userDetails!=null) {
				mccList.setCreatedBy(Integer.valueOf(userDetails.getId()).toString());
			}
		} else {
			List<MCCList> mccLists=mccListRepository.findByCardSchemeTypeMapping_CardSchemeIdEqualsIgnoreCaseAndMccEqualsIgnoreCaseAndMerchantType_SystemCodeIdAndDescriptionEqualsIgnoreCaseAndMccIdNot(mccListRequestDto.getCardSchemeId(),mccListRequestDto.getMcc(),mccListRequestDto.getMerchantTypeId(),mccListRequestDto.getDescription(),mccListRequestDto.getMccId());
			if(mccLists.size()>0) {
				throw new BusinessException(ResponseCode.MCC_ALREADY_EXISTS, HttpStatus.NOT_FOUND);
			}
			mccList = (mccListRepository.findById(mccListRequestDto.getMccId()))
					.orElseThrow(() -> new BusinessException(ResponseCode.MCC_NOT_FOUND, HttpStatus.NOT_FOUND));
			mccList.setMcc(mccListRequestDto.getMcc());
			mccList.setMerchantType(merchantType);
			mccList.setCardSchemeTypeMapping(cardScheme);
			mccList.setDescription(mccListRequestDto.getDescription());
		//	mccList.setCreatedBy(Integer.valueOf(userDetails.getId()).toString());
		}
		if (makerCheckerEngine.processIfRequired(mccListRequestDto, MCCListService.class.getName(), "saveOrUpdateMccList", "")) {
			return null;
		}
		finalList = mccListRepository.save(mccList);
		MCCListResponseDto dto = mccListMapper.toDto(finalList);
		return dto;
	}

	public List<MCCListResponseDto> fetchAllMccList() {
		List<MCCList> mccList = mccListRepository.findAll(Sort.by(Sort.Direction.ASC, "mccId"));
		List<MCCListResponseDto> dto = new ArrayList<MCCListResponseDto>();
		for (MCCList temp : mccList) {
			MCCListResponseDto dto1 = mccListMapper.toDto(temp);
			dto.add(dto1);
		}
		return dto;
	}

	public ResponseEntity<PaginationResponseDto> fetchAllMccList(MCCListRequestDto mccListRequestDto) {
		Page<MCCList> page = null;

		PaginationCommonCode paginationCommonCode = new PaginationCommonCode();

		PageRequest pageRequest = paginationCommonCode.getPageRequestForPagination(mccListRequestDto.getSort(), "mcc",
				mccListRequestDto.getPageNo(), mccListRequestDto.getPageSize());
		// List<MCCList> mccList = mccListRepository.findAll(Sort.by(Sort.Direction.ASC,
		// "mccId"));

		page = mccListRepository.findAll(pageRequest);
		List<MCCListResponseDto> dto = new ArrayList<MCCListResponseDto>();

		page.getContent().stream().forEach((temp) -> {

			MCCListResponseDto dto1 = mccListMapper.toDto(temp);
			dto.add(dto1);

		});
//		for(MCCList temp: mccListRequestDto.getSort())
//		{
//			MCCListResponseDto dto1=mccListMapper.toDto(temp);
//			dto.add(dto1);
//		}
		return new ResponseEntity<PaginationResponseDto>(
				new PaginationResponseDto(true, null, dto, page.getTotalPages(), page.getTotalElements()),
				HttpStatus.OK);
	}

	public MCCListResponseDto fetchMccListById(int id) {
		MCCList mccList = mccListRepository.findById(id)
				.orElseThrow(() -> new BusinessException(ResponseCode.MCC_NOT_FOUND, HttpStatus.NOT_FOUND));
		MCCListResponseDto dto1 = mccListMapper.toDto(mccList);
		return dto1;
	}

	public void deleteMccListById(int id) throws Exception {
		MCCList mccList = mccListRepository.findById(id)
				.orElseThrow(() -> new BusinessException(ResponseCode.MCC_NOT_FOUND, HttpStatus.NOT_FOUND));
		List<Terminal> terminals = terminalRepository.findByMccList(mccList.getMcc());
		List<Entities> entities = entitiesRepository.findByDefaultMCC(mccList.getMcc());
		if ((terminals.isEmpty()) && (entities.isEmpty())) {
			if (makerCheckerEngine.processIfRequired(id, MCCListService.class.getName(), "deleteMccListById", "")) {
				return;
			}
			mccListRepository.deleteById(id);
		} else {
			throw new BusinessException(ResponseCode.CFG_REFERENCE_EXISTS, HttpStatus.NOT_FOUND);
		}
	}

//	public ResponseEntity<PaginationResponseDto> getMCCBySearch(MCCListRequestDto mccListRequestDto) {
//		Page<MCCList> page = null;
//
//		PaginationCommonCode paginationCommonCode = new PaginationCommonCode();
//
//		PageRequest pageRequest = paginationCommonCode.getPageRequestForPagination(mccListRequestDto.getSort(), "mccId",
//				mccListRequestDto.getPageNo(), mccListRequestDto.getPageSize());
//
//		page = mccListRepository
//				.findByMccIgnoreCaseContainingOrDescriptionIgnoreCaseContainingOrCardSchemeTypeMapping_CardSchemeIdIgnoreCaseContainingOrCardSchemeTypeMapping_CardSchemeNameIgnoreCaseContainingOrMerchantTypeMapping_MerchantTypeNameIgnoreCaseContaining(
//						pageRequest, mccListRequestDto.getSearch(), mccListRequestDto.getSearch(),
//						mccListRequestDto.getSearch(), mccListRequestDto.getSearch(), mccListRequestDto.getSearch());
//
//		List<MCCListResponseDto> dtos = new ArrayList<MCCListResponseDto>();
//
//		page.getContent().stream().forEach((mcc) -> {
//
//			MCCListResponseDto dto = mccListMapper.toDto(mcc);
//
//			dto.setPageNo(mccListRequestDto.getPageNo());
//			dto.setPageSize(mccListRequestDto.getPageSize());
//
//			dtos.add(dto);
//		});
//
//		return new ResponseEntity<PaginationResponseDto>(
//				new PaginationResponseDto(true, null, dtos, page.getTotalPages(), page.getTotalElements()),
//				HttpStatus.OK);
//	}

	public ResponseEntity<PaginationResponseDto> getMCCBySearch(MCCListRequestDto mccListRequestDto) {
		Page<MCCList> page = null;

		if(mccListRequestDto.getDescription()!=null) {
			mccListRequestDto.setDescription(mccListRequestDto.getDescription().trim());
		}
		
		if(mccListRequestDto.getCardSchemeId()!=null) {
			mccListRequestDto.setCardSchemeId(mccListRequestDto.getCardSchemeId().trim());
		}
		
		if(mccListRequestDto.getMcc()!=null) {
			mccListRequestDto.setMcc(mccListRequestDto.getMcc().trim());
		}
		
		PaginationCommonCode paginationCommonCode = new PaginationCommonCode();

//		MerchantType merchantType = merchantTypeRepository.findById(mccListRequestDto.getMerchantTypeId())
//				.orElseThrow(() -> new BusinessException(ResponseCode.INVALID_MCCLIST_ID, HttpStatus.NOT_FOUND)); 

		PageRequest pageRequest = paginationCommonCode.getPageRequestForPagination(mccListRequestDto.getSort(), "mccId",
				mccListRequestDto.getPageNo(), mccListRequestDto.getPageSize());

		page = mccListRepositoryImpl
				.findByMccIgnoreCaseContainingOrDescriptionIgnoreCaseContainingOrCardSchemeTypeMapping_CardSchemeIdIgnoreCaseContainingOrCardSchemeTypeMapping_CardSchemeNameIgnoreCaseContainingOrMerchantType_CodeValueIgnoreCaseContaining(
						pageRequest, mccListRequestDto.getMcc(), mccListRequestDto.getDescription(),
						mccListRequestDto.getCardSchemeId(), mccListRequestDto.getMerchantTypeId());

		List<MCCListResponseDto> dtos = new ArrayList<MCCListResponseDto>();

		page.getContent().stream().forEach((mcc) -> {

			MCCListResponseDto dto = mccListMapper.toDto(mcc);

			dto.setPageNo(mccListRequestDto.getPageNo());
			dto.setPageSize(mccListRequestDto.getPageSize());

			dtos.add(dto);
		});

		return new ResponseEntity<PaginationResponseDto>(
				new PaginationResponseDto(true, null, dtos, page.getTotalPages(), page.getTotalElements()),
				HttpStatus.OK);
	}

}
