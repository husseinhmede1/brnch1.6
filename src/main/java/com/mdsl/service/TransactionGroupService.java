package com.mdsl.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
import com.mdsl.model.entity.ActivityPackageDetail;
import com.mdsl.model.entity.DefaultTransactionId;
import com.mdsl.model.entity.Institution;
import com.mdsl.model.entity.TransactionChargeDetails;
import com.mdsl.model.entity.TransactionGroup;
import com.mdsl.model.mapper.TransactionChargesDetailsMapper;
import com.mdsl.model.mapper.TransactionGroupMapper;
import com.mdsl.repository.ActivityPackageDetailRepository;
import com.mdsl.repository.DefaultTransactionIdRepository;
import com.mdsl.repository.InstitutionRepository;
import com.mdsl.repository.TransactionChargesDetailsRepository;
import com.mdsl.repository.TransactionGroupRepository;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.enumerations.StatusEnum;


@Service
public class TransactionGroupService  {

	@Autowired
	private TransactionGroupRepository transactionGroupRepository;

	@Autowired
	private TransactionGroupMapper transactionGroupMapper;

	@Autowired
	private InstitutionRepository institutionRepository;
	
	@Autowired
	private TransactionChargesDetailsMapper transactionChargesDetailsMapper;
	
	@Autowired
	private TransactionChargesDetailsRepository chargesDetailsRepository;
	
	@Autowired
	private DefaultTransactionIdRepository defaultTransactionIdRepository;
	
	@Autowired
	private TransactionChargesDetailsRepository transactionChargesDetailsRepository;
	
	@Autowired
	private ActivityPackageDetailRepository activityPackageDetailRepository;
	
//	public List viewTransactionGroup() {
//		// TODO Auto-generated method stub
//		List<TransactionGroup> transactionGroups = transactionGroupRepository.findAll();
//
//		List<TransactionGroupDto> transactionGroupDtos = new ArrayList();
//
//		transactionGroups.stream().forEach((transactionGroup) -> {
//			transactionGroupDtos.add(transactionGroupMapper.toDto(transactionGroup));
//		});
//
//		return transactionGroupDtos;
//	}
//
//	
//	public TransactionGroupDto getTransactionGroup(int transactionGroupId) {
//		// TODO Auto-generated method stub
//		
//		TransactionGroup transactionGroup=transactionGroupRepository.findById(transactionGroupId).orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_TRANSACTION_GROUP_ID, HttpStatus.NOT_FOUND));
//		
//		if(transactionGroup!=null) {
//			return transactionGroupMapper.toDto(transactionGroup);
//		}else {
//			throw new BusinessException(ResponseCode.CFG_INVALID_INST, HttpStatus.NOT_FOUND);
//		}
//		
//		
//	}
//
//	
//	public String deleteTransactionGroup(int transactionGroupId) {
//		// TODO Auto-generated method stub
//		transactionGroupRepository.findById(transactionGroupId)
//		.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_TRANSACTION_GROUP_ID, HttpStatus.NOT_FOUND));
//		transactionGroupRepository.deleteById(transactionGroupId);
//		return "TransactionGroup Deleted Successfully";
//	}

	
//	public void updateStatus(int transactionGroupId) {
//
//		TransactionGroup transactionGroup = transactionGroupRepository.findById(transactionGroupId)
//				.orElseThrow(() -> new BusinessException("TransactionGroup is not available", HttpStatus.NOT_FOUND));
//
//		if (transactionGroup != null) {
//
//			if (transactionGroup.getStatus() == '1')
//				transactionGroupRepository.updateStatus(transactionGroupId, '0');
//			else
//				transactionGroupRepository.updateStatus(transactionGroupId, '1');
//		}
//
//	}
	
//	public void changeStatus(@Valid ChangeStatusRequestDto changeStatusRequestDto) {
//		TransactionGroup transactionGroup = transactionGroupRepository.findById(changeStatusRequestDto.getId())
//				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_TRANSACTION_GROUP_ID, HttpStatus.NOT_FOUND));
//		transactionGroup.setStatus(changeStatusRequestDto.getStatus().charAt(0));
//		//institution.s(new java.sql.Date(new java.util.Date().getTime()));
//		transactionGroupRepository.save(transactionGroup);
//		//return institutionMapper.toDto(institution);
//	}
	
	
	public TransactionGroupRequestDto saveOrUpdateTransactionGroup(
			TransactionGroupRequestDto transactionGroupRequestDto,String institutionId) {

		// TODO Auto-generated method stub

		TransactionChargeDetails chargeDetails = new TransactionChargeDetails();
		UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		if(transactionGroupRequestDto.getTransactionGroupName()!=null) {
			transactionGroupRequestDto.setTransactionGroupName(transactionGroupRequestDto.getTransactionGroupName().trim());
		}
		
		if(transactionGroupRequestDto.getInstitutionId()!=null) {
			transactionGroupRequestDto.setInstitutionId(transactionGroupRequestDto.getInstitutionId().trim());
		}
		Institution institution = institutionRepository.findById(transactionGroupRequestDto.getInstitutionId())
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_INST, HttpStatus.NOT_FOUND));

		TransactionGroup transactionGroup = new TransactionGroup();
		if (institution != null) {
			if (Objects.nonNull(transactionGroupRequestDto.getTransactionGroupId()) &&  Integer.parseInt(transactionGroupRequestDto.getTransactionGroupId()) != 0) {
				
				List<TransactionGroup> transactionGroups=transactionGroupRepository.findByTransactionGroupNameEqualsIgnoreCaseAndInstitution_InstitutionIdAndTransactionGroupIdNot(transactionGroupRequestDto.getTransactionGroupName().toUpperCase(),transactionGroupRequestDto.getInstitutionId(),Integer.parseInt(transactionGroupRequestDto.getTransactionGroupId()));
				if(transactionGroups.size()>0) {
					throw new BusinessException(ResponseCode.CFG_TRANS_GROUP_ALREADY_EXIST, HttpStatus.NOT_FOUND);
			 	}
				transactionGroup = transactionGroupRepository
						.findById(Integer.parseInt(transactionGroupRequestDto.getTransactionGroupId())).get();
				
				List<ActivityPackageDetail> activityPackageDetails= activityPackageDetailRepository.findByTranGroupAndInstitution(transactionGroup.getTransactionGroupName(),institution);
				
				for(ActivityPackageDetail activityPackageDetail:activityPackageDetails) {
					activityPackageDetail.setTranGroup(transactionGroupRequestDto.getTransactionGroupName());
					//activityPackageDetail.setUserCreate(Integer.valueOf(userDetails.getId()).toString());
					activityPackageDetail=activityPackageDetailRepository.save(activityPackageDetail);
				}
			}else {
	
			List<TransactionGroup> transactionGroups=transactionGroupRepository.findByTransactionGroupNameEqualsIgnoreCaseAndInstitution_InstitutionId(transactionGroupRequestDto.getTransactionGroupName().toUpperCase(),transactionGroupRequestDto.getInstitutionId());
				if(transactionGroups.size()>0) {
					throw new BusinessException(ResponseCode.CFG_TRANS_GROUP_ALREADY_EXIST, HttpStatus.NOT_FOUND);
			 	}
			}
		

			TransactionGroupDto transactionGroupDto = new TransactionGroupDto();
			if(institutionId!=null && !(institutionId.isEmpty())) {
				transactionGroupDto.setInstitutionId(institutionId.trim());
			}else {
				transactionGroupDto.setInstitutionId(transactionGroupRequestDto.getInstitutionId());
			}
			
			transactionGroupDto.setStatus(transactionGroupRequestDto.getStatus());
			transactionGroupDto.setTransactionGroupDescription(
					transactionGroupRequestDto.getTransactionGroupDescription());
			transactionGroupDto.setTransactionGroupName(transactionGroupRequestDto.getTransactionGroupName());
			if(Objects.nonNull(transactionGroupRequestDto.getTransactionGroupId())){
				transactionGroupDto.setTransactionGroupId(Integer.parseInt(transactionGroupRequestDto.getTransactionGroupId()));
			}
			transactionGroup = transactionGroupMapper.toEntity(transactionGroupDto);
		//	transactionGroup.setStatus(StatusEnum.ENABLED.getValue());
			transactionGroup.setInstitution(institution);
		//	transactionGroup.setUserCreate("user1");
			transactionGroup.setDateCreate(new Date());
			if(userDetails!=null) {
				transactionGroup.setUserCreate(Integer.valueOf(userDetails.getId()).toString());
			}
			transactionGroupRepository.save(transactionGroup);

			transactionGroupDto = transactionGroupMapper.toDto(transactionGroup);

			List<TransactionChargesDetailDto> transactionChargesDetailDtos = new ArrayList<TransactionChargesDetailDto>();

			for (TransactionChargesDetailDto chargesDetailDto : transactionGroupRequestDto
					.getChargesDetailDtos()) {
				TransactionChargeDetails transactionChargeDetails = new TransactionChargeDetails();
				DefaultTransactionId defaultTransactionId = defaultTransactionIdRepository
						.findByTransactionIdAndInstitution(chargesDetailDto.getDefaultTransactionId(),institution)
						.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_INST, HttpStatus.NOT_FOUND));
				if (Integer.parseInt(chargesDetailDto.getTransactionChargeDetailsId()) != 0) {
					chargeDetails = chargesDetailsRepository.findById(Integer.parseInt(chargesDetailDto.getTransactionChargeDetailsId()))
							.get();
				}
				TransactionChargesDetailDto transactionChargesDetailDto = new TransactionChargesDetailDto();
				transactionChargesDetailDto.setDefaultTransactionId(chargesDetailDto.getDefaultTransactionId());
				transactionChargesDetailDto.setDescription(chargesDetailDto.getDescription());
				
				if(institutionId!=null && !(institutionId.isEmpty())) {
					transactionChargesDetailDto.setInstitutionId(institutionId);
				}else {
					transactionChargesDetailDto.setInstitutionId(chargesDetailDto.getInstitutionId());
				}
				
				transactionChargesDetailDto
						.setTransactionChargeDetailsId(chargesDetailDto.getTransactionChargeDetailsId());
				transactionChargeDetails = transactionChargesDetailsMapper.toEntity(transactionChargesDetailDto);

				transactionChargeDetails.setInstitution(institution);
				//transactionChargeDetails.setUserCreate("user1");
				transactionChargeDetails.setDateCreate(new Date());
				transactionChargeDetails.setTransactionGroup(transactionGroup.getTransactionGroupName());
				if(userDetails!=null) {
					transactionChargeDetails.setUserCreate(Integer.valueOf(userDetails.getId()).toString());
				}
				transactionChargesDetailsRepository.save(transactionChargeDetails);

				chargesDetailDto = transactionChargesDetailsMapper.toDto(transactionChargeDetails);

				transactionChargesDetailDtos.add(chargesDetailDto);

			}
			transactionGroupRequestDto.setChargesDetailDtos(transactionChargesDetailDtos);
			transactionGroupRequestDto.setTransactionGroupId(transactionGroup.getTransactionGroupId()+"");
		} else {
			throw new BusinessException(ResponseCode.CFG_INVALID_INST, HttpStatus.NOT_FOUND);
		}
		return transactionGroupRequestDto;
	}

	
	public List<TransactionGroupRequestDto> viewTransactionGroup(String instId) {
		Institution institution = institutionRepository.findById(instId)
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_INST_CODE, HttpStatus.NOT_FOUND));
		
		List<TransactionGroup> transactionGroups = transactionGroupRepository.findByInstitution_InstitutionId(institution.getInstitutionId(),
				Sort.by(Sort.Direction.ASC, "transactionGroupId"));
		
		List<TransactionGroupRequestDto> transactionChargesDetailsRequestDtoList = new ArrayList<TransactionGroupRequestDto>();

		transactionGroups.stream().forEach((transactionGroup) -> {
			TransactionGroupRequestDto transactionChargesDetailsRequestDto = new TransactionGroupRequestDto();
			List<TransactionChargesDetailDto> chargesDetailDtos = new ArrayList<TransactionChargesDetailDto>();
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
			transactionChargesDetailsRequestDto.setStatus(transactionGroup.getStatus());
			transactionChargesDetailsRequestDto.setTransactionGroupId(transactionGroup.getTransactionGroupId()+"");
			transactionChargesDetailsRequestDtoList.add(transactionChargesDetailsRequestDto);
		});
		return transactionChargesDetailsRequestDtoList;
	}

	public String deleteTransactionGroup(int transactionGroupId) {

		TransactionGroup transactionGroup = transactionGroupRepository.findById(transactionGroupId).orElseThrow(
				() -> new BusinessException(ResponseCode.CFG_INVALID_TRANSACTION_GROUP_ID, HttpStatus.NOT_FOUND));
		if (transactionGroup != null) {
			List<TransactionChargeDetails> chargeDetails = chargesDetailsRepository.findByTransactionGroupIdAndInstitution(transactionGroup.getTransactionGroupName(),transactionGroup.getInstitution().getInstitutionId());			
			List<ActivityPackageDetail> activityPackageDetails= activityPackageDetailRepository.findByTranGroupAndInstitution_InstitutionId(transactionGroup.getTransactionGroupName(),transactionGroup.getInstitution().getInstitutionId());		
			if((chargeDetails.size() > 0) || (activityPackageDetails.size()>0)) {
//				throw new BusinessException(ResponseCode.REFERENCE_EXISTS, HttpStatus.NOT_FOUND);
				chargesDetailsRepository.deleteAll(chargeDetails);
				activityPackageDetailRepository.deleteAll(activityPackageDetails);
			}
		}
		transactionGroupRepository.deleteById(transactionGroup.getTransactionGroupId());
		return "Delete TransactionChargesDetails";
	}

	public TransactionGroupRequestDto getTransactionGroup(int transactionGroupId) {
		// TODO Auto-generated method stub
		List<TransactionChargesDetailDto> chargesDetailDtos = new ArrayList<TransactionChargesDetailDto>();
		TransactionGroupRequestDto transactionChargesDetailsRequestDto = new TransactionGroupRequestDto();
		TransactionGroup transactionGroup = transactionGroupRepository.findById(transactionGroupId).orElseThrow(
				() -> new BusinessException(ResponseCode.CFG_INVALID_TRANSACTION_GROUP_ID, HttpStatus.NOT_FOUND));
		if (transactionGroup != null) {
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
		} else {
			throw new BusinessException(ResponseCode.CFG_INVALID_TRANSACTION_GROUP_ID, HttpStatus.NOT_FOUND);
		}

		return transactionChargesDetailsRequestDto;
	}
	
	public void changeStatus(@Valid ChangeStatusRequestDto changeStatusRequestDto) {
		TransactionGroup transactionGroup = transactionGroupRepository.findById(changeStatusRequestDto.getId()).orElseThrow(
				() -> new BusinessException(ResponseCode.CFG_INVALID_TRANSACTION_GROUP_ID, HttpStatus.NOT_FOUND));
		transactionGroup.setStatus(changeStatusRequestDto.getStatus().charAt(0));
		transactionGroupRepository.save(transactionGroup);
		
	}

	public List<TransactionGroupRequestDto> getActiveTransactionGroup() {
		List<TransactionGroupRequestDto> transactionChargesDetailsRequestDtos=new ArrayList<TransactionGroupRequestDto>();
		List<TransactionChargesDetailDto> chargesDetailDtos = new ArrayList<TransactionChargesDetailDto>();
		List<TransactionGroup> listTransactionGroups = transactionGroupRepository.findByStatus(StatusEnum.ENABLED.getValue(),
				Sort.by(Sort.Direction.ASC, "transactionGroupId"));

		listTransactionGroups.stream().forEach((transactionGroup) -> {
//			chargesDetailDtos.add(transactionChargesDetailsMapper.toDto(chargeDetail));
			
			TransactionGroupRequestDto transactionChargesDetailsRequestDto=new TransactionGroupRequestDto();
			List<TransactionChargeDetails> chargeDetails = chargesDetailsRepository
					.findByTransactionGroupIdAndInstitution(transactionGroup.getTransactionGroupName(),transactionGroup.getInstitution().getInstitutionId());

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


	public List<TransactionGroupDto> getTransactionGroupsByInstitutionId(TransactionGroupDto requestDto) {
		List<TransactionGroupDto> groupsDto = new ArrayList<TransactionGroupDto>();

		Institution institution = institutionRepository.findById(requestDto.getInstitutionId())
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_INST_CODE, HttpStatus.NOT_FOUND));

		List<TransactionGroup> grps = transactionGroupRepository.findByInstitution_InstitutionId(institution.getInstitutionId(),
				Sort.by(Sort.Direction.ASC, "transactionGroupId"));

		grps.stream().forEach((trans) -> {
			TransactionGroupDto transactionGroupRequestDto = transactionGroupMapper.toDto(trans);
			groupsDto.add(transactionGroupRequestDto);
		});

		return groupsDto;
	}

	

	
}
