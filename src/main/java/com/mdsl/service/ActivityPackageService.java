package com.mdsl.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.ActivityEntityMappingRequestDto;
import com.mdsl.model.dto.request.ActivityPackageRequestDto;
import com.mdsl.model.dto.request.ChangeStatusRequestDto;
import com.mdsl.model.dto.response.ActivityPackageResponseDto;
import com.mdsl.model.dto.response.EntitiesResponseDto;
import com.mdsl.model.entity.ActivityPackage;
import com.mdsl.model.entity.ActivityPackageDetail;
import com.mdsl.model.entity.Entities;
import com.mdsl.model.entity.Institution;
import com.mdsl.model.mapper.ActivityPackageMapper;
import com.mdsl.model.mapper.EntityMapper;
import com.mdsl.repository.ActivityPackageDetailRepository;
import com.mdsl.repository.ActivityPackageRepository;
import com.mdsl.repository.EntitiesRepository;
import com.mdsl.repository.InstitutionRepository;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.enumerations.StatusEnum;

@Service
public class ActivityPackageService {
	public ActivityPackageService() {
	}

	@Autowired
	private ActivityPackageRepository activityPackageRepository;
	@Autowired
	private ActivityPackageDetailRepository activityPackageDetailRepository;
	@Autowired
	private ActivityPackageMapper activityPackageMapper;
	@Autowired
	private InstitutionRepository institutionRepository;

	@Autowired
	private EntityMapper entityMapper;
	@Autowired
	private EntitiesRepository entitiesRepository;

	public List<ActivityPackageResponseDto> getAllActivityPackage() {
		try {
			List<ActivityPackage> allActivityPackage = activityPackageRepository
					.findAll(Sort.by(Sort.Direction.ASC, "packageId"));

			return allActivityPackage.stream()
					.map(activityPackageMapper::toDto)
					.collect(Collectors.toList());
		} catch (Exception e) {
			throw new BusinessException(ResponseCode.VAL_ERROR_OCCURRED, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public List<ActivityPackageResponseDto> getActiveActivityPackageByInstitutionId(String id) {
		try {
			List<ActivityPackage> allActiveActivityPackage = activityPackageRepository
					.findByInstitution_InstitutionIdAndStatus(
							id, StatusEnum.ENABLED.getValue(),
							Sort.by(Sort.Direction.ASC, "packageId")
					);

			return allActiveActivityPackage.stream()
					.map(activityPackageMapper::toDto)
					.collect(Collectors.toList());

		} catch (Exception e) {
			throw new BusinessException(ResponseCode.VAL_ERROR_OCCURRED, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public List<ActivityPackageResponseDto> findActivityPackageByInstitutionId(String instId) {
		try {
			List<ActivityPackage> allActivityPackage = activityPackageRepository
					.findActivityPackageByInstitutionId(instId, Sort.by(Sort.Direction.ASC, "packageId"));

			return allActivityPackage.stream()
					.map(activityPackageMapper::toDto)
					.collect(Collectors.toList());

		} catch (Exception e) {
			throw new BusinessException(ResponseCode.VAL_ERROR_OCCURRED, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ActivityPackageResponseDto getActivityPackageById(Integer id) {
		try {
			ActivityPackage activityPackage = activityPackageRepository.findById(id)
					.orElseThrow(() -> new BusinessException(ResponseCode.INVALID_ACT_FEE_PKG, HttpStatus.NOT_FOUND));
			return activityPackageMapper.toDto(activityPackage);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new BusinessException(ResponseCode.VAL_ERROR_OCCURRED, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ActivityPackageResponseDto saveOrUpdateActivityPackage(ActivityPackageRequestDto dto) {
		Institution institution = institutionRepository.findById(dto.getInstitutionId().trim())
				.orElseThrow(() -> new BusinessException(ResponseCode.INVALID_INSTITUTION_ID, HttpStatus.BAD_REQUEST));

		String packageId = Optional.ofNullable(dto.getPackageId())
				.map(String::trim)
				.map(String::toUpperCase)
				.orElse("");
		String packageName = Optional.ofNullable(dto.getPackageName())
				.map(String::trim)
				.orElse("");

		if (packageId.isEmpty() || packageId.equals("0")) {
			throw new BusinessException(ResponseCode.INVALID_ACT_FEE_PKG, HttpStatus.BAD_REQUEST);
		}
		if (packageName.isEmpty()) {
			throw new BusinessException(ResponseCode.INVALID_ACT_FEEPKG_NAME, HttpStatus.BAD_REQUEST);
		}

		UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		Optional<ActivityPackage> existing = activityPackageRepository
				.findByPackageIdAndInstitution_InstitutionId(packageId, institution.getInstitutionId());

		boolean isUpdate = "1".equals(String.valueOf(dto.getUpdateFlag()));

		if (isUpdate) {
			ActivityPackage entity = existing.orElseThrow(() ->
					new BusinessException(ResponseCode.INVALID_ACT_FEE_PKG, HttpStatus.NOT_FOUND));

			entity.setPackageName(packageName);
			entity.setInstitution(institution);
			entity.setInstitutionId(institution.getInstitutionId());

			entity.setStatus(StatusEnum.ENABLED.getValue());
			activityPackageRepository.save(entity);
			return activityPackageMapper.toDto(entity);

		} else {
			if (existing.isPresent()) {
				throw new BusinessException(ResponseCode.ACT_ID_EXISTS_OR_FLAG_INVALID, HttpStatus.BAD_REQUEST);
			}

			ActivityPackage entity = activityPackageMapper.toEntity(dto);
			entity.setPackageId(packageId);
			entity.setPackageName(packageName);
			entity.setInstitution(institution);
			entity.setInstitutionId(institution.getInstitutionId());

			entity.setStatus(StatusEnum.ENABLED.getValue());
			entity.setDateCreate(new java.sql.Date(System.currentTimeMillis()));
			entity.setUserCreate(String.valueOf(userDetails.getId()));
			entity.setRecordSeqId(null);

			activityPackageRepository.save(entity);
			return activityPackageMapper.toDto(entity);
		}
	}
	@Transactional
	public void deleteActivityPackage(Integer id){

		if (Objects.nonNull(id)) {
			ActivityPackage activityPackage = activityPackageRepository.findById(id).orElseThrow(() -> new BusinessException(ResponseCode.INVALID_ACT_FEE_PKG_ID, HttpStatus.NOT_FOUND));
			List<ActivityPackageDetail> activityPackageDetails = activityPackageDetailRepository.findByPkgIdAndInstitution(activityPackage.getPackageId(),activityPackage.getInstitution().getInstitutionId());
			activityPackageDetailRepository.deleteAll(activityPackageDetails);
			activityPackageRepository.deleteActivityPackage(id);
		}
		else{
			throw new BusinessException(ResponseCode.CFG_INVALID_ACT_PKG_ID, HttpStatus.BAD_REQUEST);
		}
	}

	public ActivityEntityMappingRequestDto mapPackageWithEntity(ActivityEntityMappingRequestDto requestDto, String instId) {
		ActivityPackage activityPackage = activityPackageRepository.findById(requestDto.getId())
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_ACT_PKG_ID, HttpStatus.NOT_FOUND));
		List<String> listofEntityRequstDto = requestDto.getEntities();

		List<Entities> oldENtityAssign = entitiesRepository.findByActivityFeePKGEntity(activityPackage);
		if (!oldENtityAssign.isEmpty()) {

			List<String> oldEntityList = oldENtityAssign.stream().map(Entities::getEntityId)
					.collect(Collectors.toList());
			listofEntityRequstDto = listofEntityRequstDto.stream().filter(e -> !oldEntityList.contains(e))
					.collect(Collectors.toList());

			List<String> entityIds = oldENtityAssign.stream().map(Entities::getEntityId).collect(Collectors.toList());
			entityIds.removeAll(requestDto.getEntities());
			
			entityIds.forEach((id) -> {
				Entities entities = entitiesRepository.findByEntityIdAndInstitution_InstitutionId(id,instId).orElseThrow(
						() -> new BusinessException(ResponseCode.CFG_ENTITY_NOT_FOUND, HttpStatus.NOT_FOUND));
				entities.setActivityFeePKG(null);
				entitiesRepository.save(entities);
			});
		}
		listofEntityRequstDto.forEach((id) -> {
			Entities entities = entitiesRepository.findByEntityIdAndInstitution_InstitutionId(id, instId)
					.orElseThrow(() -> new BusinessException(ResponseCode.CFG_ENTITY_NOT_FOUND, HttpStatus.NOT_FOUND));
			entities.setActivityFeePKGEntity(activityPackage);
			entities.setActivityFeePKG(activityPackage.getPackageId());

			entitiesRepository.save(entities);
		});

		return requestDto;
	}

	public String changeStatus(@Valid ChangeStatusRequestDto changeStatusRequestDto) {
		try {
			ActivityPackage activityPackage = activityPackageRepository
					.findById(changeStatusRequestDto.getId())
					.orElseThrow(() -> new BusinessException(
							ResponseCode.INVALID_ACT_FEE_PKG,
							HttpStatus.NOT_FOUND
					));

			if (changeStatusRequestDto.getStatus() == null || changeStatusRequestDto.getStatus().isEmpty()) {
				throw new BusinessException(ResponseCode.INVALID_ACT_FEE_PKG_STATUS, HttpStatus.BAD_REQUEST);
			}

			activityPackage.setStatus(changeStatusRequestDto.getStatus().charAt(0));
			activityPackageRepository.save(activityPackage);
			return "Status changed successfully";
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new BusinessException(ResponseCode.VAL_ERROR_OCCURRED, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public List<EntitiesResponseDto> getMappedEntitiesByActivityPackageId(String id, String instId) {

		List<EntitiesResponseDto> responseDtos = new ArrayList<EntitiesResponseDto>();
		List<Entities> entities = entitiesRepository.findByActivityFeePKGEntity_PackageIdAndInstitution_InstitutionId(id,instId,
				Sort.by(Sort.Direction.ASC, "entityId"));

		entities.stream().forEach((ent) -> {
			EntitiesResponseDto dto = entityMapper.toDto(ent);
			responseDtos.add(dto);
		});

		return responseDtos;
	}

}