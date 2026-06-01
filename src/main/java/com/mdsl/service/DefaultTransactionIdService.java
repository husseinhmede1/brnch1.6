package com.mdsl.service;

import java.util.*;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.ChangeStatusRequestDto;
import com.mdsl.model.dto.request.DefaultTransactionIdRequestDto;
import com.mdsl.model.dto.response.DefaultTransactionIdResponseDto;
import com.mdsl.model.entity.DefaultTransactionId;
import com.mdsl.model.entity.Institution;
import com.mdsl.model.mapper.DefaultTransactionIdMapper;
import com.mdsl.repository.DefaultTransactionIdRepository;
import com.mdsl.repository.InstitutionRepository;
import com.mdsl.repository.SystemCodeRepository;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.enumerations.StatusEnum;

@Service
public class DefaultTransactionIdService {

	@Autowired
	private DefaultTransactionIdRepository defaultTransactionIdRepository;

	@Autowired
	private DefaultTransactionIdMapper defaultTransactionIdMapper;

	@Autowired
	private InstitutionRepository institutionRepository;

	public DefaultTransactionIdResponseDto saveOrUpdateDefaultTransactionId(DefaultTransactionIdRequestDto defaultTransactionIdRequestDto, String instId) {

		if(defaultTransactionIdRepository.existsByTransactionIdAndInstitution_InstitutionId(defaultTransactionIdRequestDto.getTransactionId(),instId) && !(String.valueOf(defaultTransactionIdRequestDto.getUpdateFlag()).equals("1"))){
			throw new BusinessException(ResponseCode.TRANSACTION_ID_ALREADY_USED,HttpStatus.BAD_REQUEST);
		}
		Institution institution = null;
		UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if ((defaultTransactionIdRequestDto.getInstitutionId() == null) || (defaultTransactionIdRequestDto.getInstitutionId().equals(""))) {
			defaultTransactionIdRequestDto.setInstitutionId(instId);
		} else {
			defaultTransactionIdRequestDto.setInstitutionId(defaultTransactionIdRequestDto.getInstitutionId());
		}

		DefaultTransactionIdResponseDto defaultTransactionIdResponseDto = new DefaultTransactionIdResponseDto();

		if (defaultTransactionIdRequestDto.getInstitutionId() != null) {
			institution = institutionRepository.findById(defaultTransactionIdRequestDto.getInstitutionId()).orElseThrow(
					() -> new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
		}
		DefaultTransactionId defaultTransactionId = new DefaultTransactionId();
		Optional<DefaultTransactionId> trans = defaultTransactionIdRepository
				.findByTransactionIdAndInstitution_InstitutionId(defaultTransactionIdRequestDto.getTransactionId(),instId);

//		if (defaultTransactionIdRequestDto.getTransactionId() != null) {
//		if (!(defaultTransactionIdRequestDto.getTransactionId().equals("0")
//				|| (defaultTransactionIdRequestDto.getTransactionId() != null))) {
//		if (!(defaultTransactionIdRequestDto.getTransactionId().trim().equals(""))) {
//
//			defaultTransactionId = defaultTransactionIdRepository
//					.findById(defaultTransactionIdRequestDto.getTransactionId()).get();
//		}

//		if (defaultTransactionId != null && defaultTransactionIdRequestDto.getTransactionId() != null) {

//		if (!(defaultTransactionIdRequestDto.getTransactionId().trim().equals(""))
//				&& !(defaultTransactionIdRequestDto.getTransactionId().equals(null))) {
		
		if (!(trans.isEmpty()) && (String.valueOf(defaultTransactionIdRequestDto.getUpdateFlag()).equals("1"))) {
//			if (checkTransactionIdOnUpdate(defaultTransactionIdRequestDto.getTransactionId(), instId, trans.get().getRecordSequenceId())) {
//				throw new BusinessException(ResponseCode.CFG_TRANS_ID_EXISTS_OR_FLAG_INVALID, HttpStatus.NOT_FOUND);
//			}		
			defaultTransactionId = defaultTransactionIdRepository.findByTransactionIdAndInstitution_InstitutionId(defaultTransactionIdRequestDto.getTransactionId(),instId).get();
			defaultTransactionId.setDateCreate(new Date());
			defaultTransactionId.setDescription(defaultTransactionIdRequestDto.getDescription());
			defaultTransactionId.setTransUsage(defaultTransactionIdRequestDto.getTransUsage());
			defaultTransactionId.setInstitution(institution);
			defaultTransactionId.setSignFlag(defaultTransactionIdRequestDto.getSignFlag());
			defaultTransactionIdRepository.save(defaultTransactionId);
			defaultTransactionIdResponseDto = defaultTransactionIdMapper.toDto(defaultTransactionId);		
			return defaultTransactionIdResponseDto;
		} else {		
			if (defaultTransactionIdRequestDto.getTransactionId().equals("0")) {
				throw new BusinessException(ResponseCode.CFG_DEFAULT_TRANSACTION_NOT_FOUND, HttpStatus.NOT_FOUND);
			} else {
				if (!(trans.isEmpty())
						|| (String.valueOf(defaultTransactionIdRequestDto.getUpdateFlag()).equals("1"))) {
					throw new BusinessException(ResponseCode.CFG_DEFAULT_TRANSACTION_NOT_FOUND, HttpStatus.NOT_FOUND);
				}
				if (!checkTransactionIdOnAdd(defaultTransactionIdRequestDto.getTransactionId(), instId)) {
					throw new BusinessException(ResponseCode.CFG_TRANS_ID_EXISTS_OR_FLAG_INVALID, HttpStatus.NOT_FOUND);
				}
				DefaultTransactionId defaultTransactionId1 = new DefaultTransactionId();
				String transName = defaultTransactionIdRequestDto.getDescription().replace(" ", "");

//				if (transName.length() > 4) {
//					String defTransactionId = transName.substring(0, 4);
//					boolean tempDefaultTransaction = defaultTransactionIdRepository
//							.findByTransactionId(defTransactionId).isEmpty();
//					if (tempDefaultTransaction == true) {
//						defaultTransactionIdRequestDto.setTransactionId(defTransactionId);
//
//					} else {
//						Random random = new Random();
//						boolean tempDefaultTransaction1 = defaultTransactionIdRepository
//								.findByTransactionId(transName.substring(0, 5)).isEmpty();
//						if (tempDefaultTransaction1 == true) {
//							defaultTransactionIdRequestDto.setTransactionId(transName.substring(0, 5));
//						} else {
//							defaultTransactionIdRequestDto
//									.setTransactionId(transName.substring(0, 5) + (random.nextInt(99999)));
//						}
//					}
//
//				} else {
//					if (transName.length() == 4) {
//						boolean tempDefaultTransaction = defaultTransactionIdRepository.findByTransactionId(transName)
//								.isEmpty();
//						if (tempDefaultTransaction == true) {
//							defaultTransactionIdRequestDto.setTransactionId(transName);
//						} else {
//							Random random = new Random();
//							defaultTransactionIdRequestDto.setTransactionId(transName + (random.nextInt(999999)));
//						}
//					} else {
//						boolean tempDefaultTransaction = defaultTransactionIdRepository.findByTransactionId(transName)
//								.isEmpty();
//						if (tempDefaultTransaction == true) {
//							defaultTransactionIdRequestDto.setTransactionId(transName);
//						} else {
//							Random random = new Random();
//							defaultTransactionIdRequestDto.setTransactionId(transName + (random.nextInt(9999999)));
//							// throw new BusinessException(ResponseCode.CFG_INVALID_DEFAULT_TRANSACTION_ID,
//							// HttpStatus.NOT_FOUND);
//						}
//					}
//				}

				defaultTransactionId1 = defaultTransactionIdMapper.toEntity(defaultTransactionIdRequestDto);
				defaultTransactionId1.setInstitution(institution);
				defaultTransactionId1.setStatus("1".charAt(0));
				defaultTransactionId1.setDateCreate(new Date());
				if(userDetails!=null) {
					defaultTransactionId1.setUserCreate(Integer.valueOf(userDetails.getId()).toString());
				}
				defaultTransactionId1.setTransUsage(defaultTransactionIdRequestDto.getTransUsage());
				defaultTransactionIdRepository.save(defaultTransactionId1);

				defaultTransactionIdResponseDto = defaultTransactionIdMapper.toDto(defaultTransactionId1);
				return defaultTransactionIdResponseDto;
			}
		}
	}

	public List<DefaultTransactionIdResponseDto> viewDefaultTransactionId() {
		// TODO Auto-generated method stub

		List<DefaultTransactionId> defaultTransactionIds = defaultTransactionIdRepository.findAll();

		List<DefaultTransactionIdResponseDto> defaultTransactionIdResponseDtos = new ArrayList();

		defaultTransactionIds.stream().forEach((defaultTransactionId) -> {
			
			DefaultTransactionIdResponseDto defaultTransactionIdResponseDto= defaultTransactionIdMapper.toDto(defaultTransactionId);
			//defaultTransactionIdResponseDto.setUsageDescription(defaultTransactionId.getDescription());
			defaultTransactionIdResponseDto.setTransUsage(defaultTransactionId.getTransUsage());
			defaultTransactionIdResponseDtos.add(defaultTransactionIdResponseDto);
		});

		return defaultTransactionIdResponseDtos;
	}

	public void deleteDefaultTransactionId(String defaultTransactionId,String instId) throws Exception {
		// TODO Auto-generated method stub
		DefaultTransactionId defaultTransactionId2=defaultTransactionIdRepository.findByTransactionIdAndInstitution_InstitutionId(defaultTransactionId,instId).orElseThrow(
				() -> new BusinessException(ResponseCode.CFG_DEFAULT_TRANSACTION_NOT_FOUND, HttpStatus.NOT_FOUND));
		defaultTransactionIdRepository.delete(defaultTransactionId2);
	}

	public DefaultTransactionIdResponseDto getDefaultTransactionId(String defaultTransactionId,String instId) {
		// TODO Auto-generated method stub

		DefaultTransactionId defaultTransactionIdEntity = defaultTransactionIdRepository.findByTransactionIdAndInstitution_InstitutionId(defaultTransactionId,instId)
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_DEFAULT_TRANSACTION_NOT_FOUND,
						HttpStatus.NOT_FOUND));

		if (defaultTransactionIdEntity != null) {
			
			DefaultTransactionIdResponseDto defaultTransactionIdResponseDto= defaultTransactionIdMapper.toDto(defaultTransactionIdEntity);
			
			defaultTransactionIdResponseDto.setTransUsage(defaultTransactionIdEntity.getTransUsage());
//			List<SystemCode> systemCode =systemCodeRepository.findByDescription(defaultTransactionIdEntity.getDescription());
//			defaultTransactionIdResponseDto.setUsageSystemCodeId(systemCode.get(0).getSystemCodeId());
//			defaultTransactionIdResponseDto.setUsageDescription(defaultTransactionIdEntity.getDescription());
			
			return defaultTransactionIdResponseDto;
		} else {
			throw new BusinessException(ResponseCode.CFG_DEFAULT_TRANSACTION_NOT_FOUND, HttpStatus.NOT_FOUND);
		}

	}

	public List<DefaultTransactionIdResponseDto> getDefaultTransactionIdByTransUsage(
			DefaultTransactionIdRequestDto requestDto, String institutionId) {
		List<DefaultTransactionIdResponseDto> defaultTransactionIdResponseDtos = new ArrayList<DefaultTransactionIdResponseDto>();

		String instId = "";
		if ((requestDto.getInstitutionId()==null)  || (requestDto.getInstitutionId().equals(""))) {
			instId = institutionId;
		} else {
			instId = requestDto.getInstitutionId();
		}

		
		List<DefaultTransactionId> transactions = defaultTransactionIdRepository
                .findByTransUsageAndInstitution(instId, StatusEnum.ENABLED.getValue(), requestDto.getTransUsage() == null || requestDto.getTransUsage().isEmpty() ? Collections.singletonList("") : List.of(requestDto.getTransUsage().split(",")), Sort.by(Sort.Direction.ASC, "transUsage"));
		
		transactions.forEach((trans) -> {
			DefaultTransactionIdResponseDto dto = defaultTransactionIdMapper.toDto(trans);
			
			dto.setUsageDescription(trans.getDescription());
			
			defaultTransactionIdResponseDtos.add(dto);
		});

		return defaultTransactionIdResponseDtos;
	}

	public List<DefaultTransactionIdResponseDto> getDefaultTransactionByInstitution(String institutionId) {
		// TODO Auto-generated method stub

		List<DefaultTransactionId> defaultTransactionIds = defaultTransactionIdRepository
				.findByInstitutionUnion(institutionId, Sort.by(Sort.Direction.ASC, "transactionId"));
		List<DefaultTransactionIdResponseDto> defaultTransactionIdResponseDtos = new ArrayList();

//		if (terminals.size() == 0) {
//			//throw new BusinessException(ResponseCode.INVALID_INSTITUTION_ID, HttpStatus.NOT_FOUND);
//		} else {
		defaultTransactionIds.forEach((defaultTransactionId) -> {
			// DefaultTransactionIdResponseDto dto =
			// defaultTransactionIdMapper.toDto(defaultTransactionId);
			
			DefaultTransactionIdResponseDto dto=defaultTransactionIdMapper.toDto(defaultTransactionId);
			
			dto.setUsageDescription(defaultTransactionId.getDescription());
			
			defaultTransactionIdResponseDtos.add(dto);
		});
//		}

		return defaultTransactionIdResponseDtos;
	}
	
	public List<DefaultTransactionIdResponseDto> getInstDefaultTransactionIdByInstitutionId(String institutionId) {
		// TODO Auto-generated method stub

		List<DefaultTransactionId> defaultTransactionIds = defaultTransactionIdRepository
				.findByInstitutionOrInstitution(institutionId, "SYSTEM", Sort.by(Sort.Direction.ASC, "transactionId"));

		List<DefaultTransactionIdResponseDto> defaultTransactionIdResponseDtos = new ArrayList();

//		if (terminals.size() == 0) {
//			//throw new BusinessException(ResponseCode.INVALID_INSTITUTION_ID, HttpStatus.NOT_FOUND);
//		} else {
		defaultTransactionIds.stream().forEach((defaultTransactionId) -> {
			// DefaultTransactionIdResponseDto dto =
			// defaultTransactionIdMapper.toDto(defaultTransactionId);
			
			DefaultTransactionIdResponseDto dto=defaultTransactionIdMapper.toDto(defaultTransactionId);
			
			dto.setUsageDescription(defaultTransactionId.getDescription());
			
			defaultTransactionIdResponseDtos.add(dto);
		});
//		}

		return defaultTransactionIdResponseDtos;
	}

	public List<DefaultTransactionIdResponseDto> getDefaultTransactionIdByTransUsageAndInstitutionId(
			DefaultTransactionIdRequestDto requestDto, String instId) {
		List<DefaultTransactionIdResponseDto> defaultTransactionIdResponseDtos = new ArrayList<DefaultTransactionIdResponseDto>();
//
//		SystemCode transUsage = systemCodeRepository.findById(requestDto.getTransUsage())
//				.orElseThrow(() -> new BusinessException(ResponseCode.INVALID_SYSTEM_CODE_ID, HttpStatus.NOT_FOUND));
		
		
		//String usage = requestDto.getTransUsage();

		List<DefaultTransactionId> transactions = defaultTransactionIdRepository
				.findByTransUsageAndInstitution_InstitutionId(requestDto.getTransUsage(), instId,
						Sort.by(Sort.Direction.ASC, "transactionId"));

		transactions.stream().forEach((trans) -> {
			DefaultTransactionIdResponseDto dto = defaultTransactionIdMapper.toDto(trans);
			dto.setUsageDescription(trans.getDescription());
			defaultTransactionIdResponseDtos.add(dto);
		});

		return defaultTransactionIdResponseDtos;
	}

	public String changeStatus(@Valid ChangeStatusRequestDto changeStatusRequestDto,String instId) {
		DefaultTransactionId transactionId = defaultTransactionIdRepository
				.findByTransactionIdAndInstitution_InstitutionId(changeStatusRequestDto.getIdString(),instId)
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_DEFAULT_TRANSACTION_NOT_FOUND,
						HttpStatus.NOT_FOUND));
		transactionId.setStatus(changeStatusRequestDto.getStatus().charAt(0));
		defaultTransactionIdRepository.save(transactionId);
		return "Status changed successfully";
	}
	
	public boolean checkTransactionIdOnAdd(String transId, String instId) {
		List<DefaultTransactionId> transactions = defaultTransactionIdRepository
				.findByTransactionIdIgnoreCaseAndInstitution_InstitutionId(transId, instId);
		return transactions.size() == 0;
	}

	public boolean checkTransactionIdOnUpdate(String transId, String instId, Integer recordSeqId) {
		List<DefaultTransactionId> transactions = defaultTransactionIdRepository.findByTransactionIdIgnoreCaseAndInstitution_InstitutionId(transId, instId);
		return transactions.size() > 0;
	}

	public ResponseEntity<List<String>> getAllDefaultTransactionId(String institutionId) {
		List<String> defaultTransactionIds = defaultTransactionIdRepository.findAllTransId(institutionId);
		return ResponseEntity.ok(defaultTransactionIds);
	}
}
