package com.mdsl.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.ChangeStatusRequestDto;
import com.mdsl.model.dto.request.TransactionChargesDetailDto;
import com.mdsl.model.dto.request.TransactionGroupRequestDto;
import com.mdsl.model.dto.response.TransactionGroupDto;
import com.mdsl.model.entity.DefaultTransactionId;
import com.mdsl.model.entity.Institution;
import com.mdsl.model.entity.TransactionChargeDetails;
import com.mdsl.model.entity.TransactionGroup;
import com.mdsl.model.mapper.TransactionChargesDetailsMapper;
import com.mdsl.model.mapper.TransactionGroupMapper;
import com.mdsl.repository.DefaultTransactionIdRepository;
import com.mdsl.repository.InstitutionRepository;
import com.mdsl.repository.TransactionChargesDetailsRepository;
import com.mdsl.repository.TransactionGroupRepository;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.enumerations.StatusEnum;

@Service
public class TransactionChargesDetailsService {

	@Autowired
	private TransactionChargesDetailsRepository chargesDetailsRepository;

	@Autowired
	private TransactionChargesDetailsMapper transactionChargesDetailsMapper;

	@Autowired
	private InstitutionRepository institutionRepository;

	@Autowired
	private DefaultTransactionIdRepository defaultTransactionIdRepository;

	@Autowired
	private TransactionGroupRepository transactionGroupRepository;

	@Autowired
	private TransactionGroupMapper transactionGroupMapper;

	@Autowired
	private TransactionChargesDetailsRepository transactionChargesDetailsRepository;

	@Transactional
	public TransactionGroupRequestDto saveOrUpdateTransactionGroup(TransactionGroupRequestDto transactionGroupRequestDto) {

		// TODO Auto-generated method stub

		TransactionChargeDetails chargeDetails = new TransactionChargeDetails();
		
		UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();

		Institution institution = institutionRepository.findById(transactionGroupRequestDto.getInstitutionId())
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND));

		TransactionGroup transactionGroup = new TransactionGroup();
		if (institution != null) {
			if (Integer.parseInt(transactionGroupRequestDto.getTransactionGroupId()) != 0) {
				transactionGroup = transactionGroupRepository
						.findById(Integer.parseInt(transactionGroupRequestDto.getTransactionGroupId())).get();
			}
			TransactionGroupDto transactionGroupDto = new TransactionGroupDto();
			transactionGroupDto.setInstitutionId(transactionGroupRequestDto.getInstitutionId());
			transactionGroupDto.setStatus(transactionGroupRequestDto.getStatus());
			transactionGroupDto
					.setTransactionGroupDescription(transactionGroupRequestDto.getTransactionGroupDescription());
			transactionGroupDto.setTransactionGroupName(transactionGroupRequestDto.getTransactionGroupName());
			transactionGroupDto.setTransactionGroupId(Integer.parseInt(transactionGroupRequestDto.getTransactionGroupId()));
			transactionGroup = transactionGroupMapper.toEntity(transactionGroupDto);
			transactionGroup.setStatus(StatusEnum.ENABLED.getValue());
			transactionGroup.setInstitution(institution);
			transactionGroup.setUserCreate("user1");
			transactionGroup.setDateCreate(new Date());
			transactionGroup.setUserCreate(Integer.valueOf(userDetails.getId()).toString());
			transactionGroupRepository.save(transactionGroup);

			transactionGroupDto = transactionGroupMapper.toDto(transactionGroup);

			List<TransactionChargesDetailDto> transactionChargesDetailDtos = new ArrayList<TransactionChargesDetailDto>();

			for (TransactionChargesDetailDto chargesDetailDto : transactionGroupRequestDto.getChargesDetailDtos()) {
				TransactionChargeDetails transactionChargeDetails = new TransactionChargeDetails();
				DefaultTransactionId defaultTransactionId = defaultTransactionIdRepository
						.findByTransactionIdAndInstitution(chargesDetailDto.getDefaultTransactionId(),institution)
						.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
				if (Integer.parseInt(chargesDetailDto.getTransactionChargeDetailsId()) != 0) {
					chargeDetails = chargesDetailsRepository.findById(Integer.parseInt(chargesDetailDto.getTransactionChargeDetailsId()))
							.get();
				}
				TransactionChargesDetailDto transactionChargesDetailDto = new TransactionChargesDetailDto();
				transactionChargesDetailDto.setDefaultTransactionId(chargesDetailDto.getDefaultTransactionId());
				transactionChargesDetailDto.setDescription(chargesDetailDto.getDescription());
				transactionChargesDetailDto.setInstitutionId(chargesDetailDto.getInstitutionId());
				transactionChargesDetailDto
						.setTransactionChargeDetailsId(chargesDetailDto.getTransactionChargeDetailsId());
				transactionChargeDetails = transactionChargesDetailsMapper.toEntity(transactionChargesDetailDto);

				transactionChargeDetails.setInstitution(institution);
				transactionChargeDetails.setUserCreate("user1");
				transactionChargeDetails.setDateCreate(new Date());
				transactionChargeDetails.setTransactionGroup(transactionGroup.getTransactionGroupName());
				transactionChargeDetails.setUserCreate(Integer.valueOf(userDetails.getId()).toString());
				transactionChargesDetailsRepository.save(transactionChargeDetails);

				chargesDetailDto = transactionChargesDetailsMapper.toDto(transactionChargeDetails);

				transactionChargesDetailDtos.add(chargesDetailDto);

			}
			transactionGroupRequestDto.setChargesDetailDtos(transactionChargesDetailDtos);
			transactionGroupRequestDto.setTransactionGroupId(transactionGroup.getTransactionGroupId()+"");
		} else {
			throw new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND);
		}
		return transactionGroupRequestDto;
	}

	public List<TransactionGroupRequestDto> viewTransactionChargesDetails() {
		// TODO Auto-generated method stub

		List<TransactionGroup> transactionGroups = transactionGroupRepository
				.findAll(Sort.by(Sort.Direction.ASC, "transactionGroupId"));

		List<TransactionGroupRequestDto> transactionChargesDetailsRequestDtoList = new ArrayList<TransactionGroupRequestDto>();

		transactionGroups.stream().forEach((transactionGroup) -> {
			TransactionGroupRequestDto transactionChargesDetailsRequestDto = new TransactionGroupRequestDto();
			List<TransactionChargesDetailDto> chargesDetailDtos = new ArrayList<TransactionChargesDetailDto>();
			List<TransactionChargeDetails> chargeDetails = chargesDetailsRepository
					.findByTransactionGroupIdAndInstitution(transactionGroup.getTransactionGroupName(),transactionGroup.getInstitution().getInstitutionId());

			chargeDetails.stream().forEach((chargeDetail) -> {
				chargesDetailDtos.add(transactionChargesDetailsMapper.toDto(chargeDetail));
			});

			transactionChargesDetailsRequestDto.setChargesDetailDtos(chargesDetailDtos);
			transactionChargesDetailsRequestDto.setInstitutionId(transactionGroup.getInstitution().getInstitutionId());
			transactionChargesDetailsRequestDto
					.setTransactionGroupDescription(transactionGroup.getTransactionGroupDescription());
			transactionChargesDetailsRequestDto.setTransactionGroupName(transactionGroup.getTransactionGroupName());
			;
			transactionChargesDetailsRequestDto.setStatus(transactionGroup.getStatus());
			transactionChargesDetailsRequestDto.setTransactionGroupId(transactionGroup.getTransactionGroupId()+"");

			transactionChargesDetailsRequestDtoList.add(transactionChargesDetailsRequestDto);

		});

		return transactionChargesDetailsRequestDtoList;
	}

	public void deleteTransactionGroup(int transactionGroupId) throws Exception {
		// TODO Auto-generated method stub

		TransactionGroup transactionGroup = transactionGroupRepository.findById(transactionGroupId).orElseThrow(
				() -> new BusinessException(ResponseCode.CFG_INVALID_TRANSACTION_GROUP_ID, HttpStatus.NOT_FOUND));
		if (transactionGroup != null) {
			List<TransactionChargeDetails> chargeDetails = chargesDetailsRepository
					.findByTransactionGroupIdAndInstitution(transactionGroup.getTransactionGroupName(),transactionGroup.getInstitution().getInstitutionId());

			chargeDetails.stream().forEach((chargeDetail) -> {
				chargesDetailsRepository.deleteById(chargeDetail.getTransactionChargeDetailsId());
			});

		}
		transactionGroupRepository.deleteById(transactionGroup.getTransactionGroupId());
	}

	public String deleteTransactionChargesDetails(int transactionchargedetailid) {
		// TODO Auto-generated method stub

		TransactionChargeDetails transactionChargeDetail = transactionChargesDetailsRepository.findById(transactionchargedetailid).orElseThrow(
				() -> new BusinessException(ResponseCode.CFG_INVALID_TRANSACTION_CHARGES_DETAILS_ID, HttpStatus.NOT_FOUND));
		if (transactionChargeDetail != null) {
			transactionChargesDetailsRepository.deleteById(transactionchargedetailid);
		}
		
		return "Delete TransactionChargeDetail";
	}

	
	public TransactionGroupRequestDto getTransactionChargesDetails(int transactionGroupId,String instId) {
		// TODO Auto-generated method stub
		Institution institution = institutionRepository.findById(instId)
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
		List<TransactionChargesDetailDto> chargesDetailDtos = new ArrayList<TransactionChargesDetailDto>();
		TransactionGroupRequestDto transactionChargesDetailsRequestDto = new TransactionGroupRequestDto();
		TransactionGroup transactionGroup = transactionGroupRepository.findById(transactionGroupId).orElseThrow(
				() -> new BusinessException(ResponseCode.CFG_INVALID_TRANSACTION_GROUP_ID, HttpStatus.NOT_FOUND));
		if (transactionGroup != null) {
			List<TransactionChargeDetails> chargeDetails = chargesDetailsRepository
					.findByTransactionGroupIdAndInstitution(transactionGroup.getTransactionGroupName(),institution.getInstitutionId());

			chargeDetails.stream().forEach((chargeDetail) -> {
				chargesDetailDtos.add(transactionChargesDetailsMapper.toDto(chargeDetail));
			});

			transactionChargesDetailsRequestDto.setChargesDetailDtos(chargesDetailDtos);
			transactionChargesDetailsRequestDto.setInstitutionId(transactionGroup.getInstitution().getInstitutionId());
			transactionChargesDetailsRequestDto
					.setTransactionGroupDescription(transactionGroup.getTransactionGroupDescription());
			transactionChargesDetailsRequestDto.setTransactionGroupName(transactionGroup.getTransactionGroupName());
			;
			transactionChargesDetailsRequestDto.setStatus(transactionGroup.getStatus());
			transactionChargesDetailsRequestDto.setTransactionGroupId(transactionGroup.getTransactionGroupId()+"");
		} else {
			throw new BusinessException(ResponseCode.CFG_INVALID_TRANSACTION_GROUP_ID, HttpStatus.NOT_FOUND);
		}

		return transactionChargesDetailsRequestDto;
	}

	public String changeStatus(@Valid ChangeStatusRequestDto changeStatusRequestDto) {
		TransactionGroup transactionGroup = transactionGroupRepository.findById(changeStatusRequestDto.getId())
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_TRANSACTION_GROUP_ID,
						HttpStatus.NOT_FOUND));
		transactionGroup.setStatus(changeStatusRequestDto.getStatus().charAt(0));
		transactionGroupRepository.save(transactionGroup);

		return "Status changed successfully";
	}

	public List<TransactionGroupRequestDto> getActiveTransactionGroup() {
		List<TransactionGroupRequestDto> transactionChargesDetailsRequestDtos = new ArrayList<TransactionGroupRequestDto>();

		List<TransactionGroup> listTransactionGroups = transactionGroupRepository
				.findByStatus(StatusEnum.ENABLED.getValue(), Sort.by(Sort.Direction.ASC, "transactionGroupId"));

		listTransactionGroups.stream().forEach((transactionGroup) -> {
			List<TransactionChargesDetailDto> chargesDetailDtos = new ArrayList<TransactionChargesDetailDto>();
			TransactionGroupRequestDto transactionChargesDetailsRequestDto = new TransactionGroupRequestDto();
			List<TransactionChargeDetails> chargeDetails = chargesDetailsRepository.findByTransactionGroupIdAndInstitution(transactionGroup.getTransactionGroupName(),transactionGroup.getInstitution().getInstitutionId());

			chargeDetails.stream().forEach((chargeDetail) -> {
				chargesDetailDtos.add(transactionChargesDetailsMapper.toDto(chargeDetail));
			});

			transactionChargesDetailsRequestDto.setChargesDetailDtos(chargesDetailDtos);
			transactionChargesDetailsRequestDto.setInstitutionId(transactionGroup.getInstitution().getInstitutionId());
			transactionChargesDetailsRequestDto.setStatus(transactionGroup.getStatus());
			transactionChargesDetailsRequestDto.setTransactionGroupDescription(transactionGroup.getTransactionGroupDescription());
			transactionChargesDetailsRequestDto.setTransactionGroupId(transactionGroup.getTransactionGroupId()+"");
			transactionChargesDetailsRequestDto.setTransactionGroupName(transactionGroup.getTransactionGroupName());

			transactionChargesDetailsRequestDtos.add(transactionChargesDetailsRequestDto);
		});

		return transactionChargesDetailsRequestDtos;
	}

}
