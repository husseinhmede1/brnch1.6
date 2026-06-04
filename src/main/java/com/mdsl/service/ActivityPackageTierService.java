package com.mdsl.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.mdsl.utils.MakerCheckerEngine;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.ActivityPackageTierRequestDto;
import com.mdsl.model.dto.response.ActivityPackageDetailResponseDto;
import com.mdsl.model.dto.response.ActivityPackageTierResponseDto;
import com.mdsl.model.entity.ActivityPackageDetail;
import com.mdsl.model.entity.ActivityPackageTier;
import com.mdsl.model.entity.FrequencyMaster;
import com.mdsl.model.entity.Institution;
import com.mdsl.model.entity.SystemCode;
import com.mdsl.model.mapper.ActivityPackageTierMapper;
import com.mdsl.repository.ActivityPackageDetailRepository;
import com.mdsl.repository.ActivityPackageTierRepository;
import com.mdsl.repository.FrequencyMasterRepository;
import com.mdsl.repository.InstitutionRepository;
import com.mdsl.repository.SystemCodeRepository;
import com.mdsl.utils.ResponseCode;

@Service
@RequiredArgsConstructor
public class ActivityPackageTierService {

	private final InstitutionRepository institutionRepository;

	private final ActivityPackageDetailRepository activityPackageDetailRepository;

	private final ActivityPackageTierRepository activityPackageTierRepository;

	private final ActivityPackageTierMapper activityPackageTierMapper;
	
	private final SystemCodeRepository systemCodeRepository;
	
	private final MakerCheckerEngine makerCheckerEngine;

	public List<ActivityPackageTierResponseDto> getAllActivationPackageTierByPkgDetailId(int pkgDetailId) {
		try {
			List<ActivityPackageTier> tiers = activityPackageTierRepository
					.findByActivityPackageDetail_PackageDetailId(pkgDetailId, Sort.by(Sort.Direction.ASC, "uptoAmount"));

			if (tiers.isEmpty()) {
				return Collections.emptyList();
			}

			List<ActivityPackageTierResponseDto> responseList = new ArrayList<>(tiers.size());

			for (ActivityPackageTier tier : tiers) {
				SystemCode systemCode = systemCodeRepository
						.findByCodeSuffixCodeValueInstitution("MD_FREQUENCY_MASTER", tier.getTireCumlOn(), "SYSTEM")
						.orElseThrow(() -> new BusinessException(ResponseCode.INVALID_SYSTEM_CODE_ID, HttpStatus.NOT_FOUND));

				ActivityPackageTierResponseDto dto = activityPackageTierMapper.toDto(tier);

				dto.setFrequencySystemCodeId(systemCode.getSystemCodeId());
				dto.setFrequencyCodeSuffix(systemCode.getCodeSuffix());
				dto.setFrequencyCodePrefix(systemCode.getCodePrefix());
				dto.setFrequencyCodeValue(systemCode.getCodeValue());
				dto.setFrequencyCodeDescription(systemCode.getDescription());

				responseList.add(dto);
			}
			return responseList;
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new BusinessException(ResponseCode.VAL_ERROR_OCCURRED, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ActivityPackageTierResponseDto getActivityPackageTierById(int pkgTierId) {
		try {
			ActivityPackageTier tier = activityPackageTierRepository.findById(pkgTierId)
					.orElseThrow(() -> new BusinessException(
							ResponseCode.INVALID_ACT_FEE_PKG_TIER_ID, HttpStatus.NOT_FOUND));

			SystemCode systemCode = systemCodeRepository
					.findByCodeSuffixCodeValueInstitution("MD_FREQUENCY_MASTER", tier.getTireCumlOn(), "SYSTEM")
					.orElseThrow(() -> new BusinessException(
							ResponseCode.INVALID_SYSTEM_CODE_ID, HttpStatus.NOT_FOUND));

			ActivityPackageTierResponseDto dto = activityPackageTierMapper.toDto(tier);
			dto.setFrequencySystemCodeId(systemCode.getSystemCodeId());
			dto.setFrequencyCodeSuffix(systemCode.getCodeSuffix());
			dto.setFrequencyCodePrefix(systemCode.getCodePrefix());
			dto.setFrequencyCodeValue(systemCode.getCodeValue());
			dto.setFrequencyCodeDescription(systemCode.getDescription());

			return dto;

		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new BusinessException(ResponseCode.VAL_ERROR_OCCURRED, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ActivityPackageTierResponseDto saveOrUpdateActivityPackageTier(ActivityPackageTierRequestDto dto) {
		UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();

		ActivityPackageDetail detail = activityPackageDetailRepository.findById(dto.getActivityPackageDetailId())
				.orElseThrow(() -> new BusinessException(ResponseCode.INVALID_ACT_FEE_PKG_DETAIL_ID, HttpStatus.NOT_FOUND));

		SystemCode frequency = systemCodeRepository.findById(dto.getFrequencyId())
				.orElseThrow(() -> new BusinessException(ResponseCode.INVALID_SYSTEM_CODE_ID, HttpStatus.NOT_FOUND));

		Institution institution = institutionRepository.findById(detail.getInstitution().getInstitutionId())
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_INSTITUTION_ID, HttpStatus.NOT_FOUND));

		ActivityPackageTier tier;
		if (makerCheckerEngine.processIfRequired(dto, this.getClass().getName(), new Object() {
		}
				.getClass()
				.getEnclosingMethod()
				.getName(), "")) {
			return null;
		}
		if (dto.getActivityPackageTierId() != 0) {
			tier = activityPackageTierRepository.findById(dto.getActivityPackageTierId())
					.orElseThrow(() -> new BusinessException(ResponseCode.INVALID_ACT_FEE_PKG_TIER_ID, HttpStatus.NOT_FOUND));

			applyTierUpdates(tier, dto, frequency, detail);
			activityPackageTierRepository.save(tier);

			return activityPackageTierMapper.toDto(tier);
		}

		List<ActivityPackageTier> existingTiers = activityPackageTierRepository
				.findByActivityPackageDetail_PackageDetailId(dto.getActivityPackageDetailId(),
						Sort.by(Sort.Direction.ASC, "uptoAmount"));

		tier = activityPackageTierMapper.toEntity(dto);
		applyTierCreation(tier, dto, frequency, institution, detail, user);

		if (!existingTiers.isEmpty()) {
			updateStartAmounts(existingTiers, tier);
		} else {
			activityPackageTierRepository.save(tier);
		}

		return activityPackageTierMapper.toDto(tier);
	}

	private void applyTierUpdates(ActivityPackageTier tier, ActivityPackageTierRequestDto dto,
								  SystemCode frequency, ActivityPackageDetail detail) {
		tier.setTireCumlOn(frequency.getCodeValue());
		tier.setPercentageAmount(dto.getPercentageAmount());
		tier.setFixAmount(dto.getFixAmount());
		tier.setUptoAmount(dto.getUptoAmount());
		tier.setActivityPackageDetail(detail);
	}

	private void applyTierCreation(ActivityPackageTier tier, ActivityPackageTierRequestDto dto,
								   SystemCode frequency, Institution institution,
								   ActivityPackageDetail detail, UserDetailsImpl user) {
		tier.setInstitution(institution);
		tier.setTireCumlOn(frequency.getCodeValue());
		tier.setPackageLine(detail.getPackageLine());
		tier.setTireLine(detail.getPackageLine());
		tier.setTireType(detail.getChargeMethod());
		tier.setDateCreate(new java.sql.Date(System.currentTimeMillis()));
		tier.setActivityPackageDetail(detail);

		if (user != null) {
			tier.setUserCreate(String.valueOf(user.getId()));
		}
	}

	private void updateStartAmounts(List<ActivityPackageTier> existingTiers, ActivityPackageTier newTier) {
		if (existingTiers == null) {
			existingTiers = new ArrayList<>();
		}
		existingTiers.add(newTier);
		for (ActivityPackageTier t : existingTiers) {
			if (t.getUptoAmount() == null || t.getUptoAmount() <= 0) {
				throw new BusinessException(ResponseCode.CFG_INVALID_TIER_AMOUNT, HttpStatus.BAD_REQUEST);
			}
		}

		existingTiers.sort(Comparator.comparing(ActivityPackageTier::getUptoAmount));
		Float previousUpto = 0f;
		for (ActivityPackageTier tier : existingTiers) {
			if (tier.getUptoAmount() <= previousUpto) {
				throw new BusinessException(ResponseCode.CFG_OVERLAPPING_TIER_RANGE, HttpStatus.BAD_REQUEST);
			}
			tier.setStartAmount(previousUpto);
			previousUpto = tier.getUptoAmount();
		}

		activityPackageTierRepository.saveAll(existingTiers);
	}

	public String deleteActivationPackageTier(int pkgTierId) {
		try {
			if (makerCheckerEngine.processIfRequired(pkgTierId, this.getClass().getName(), new Object() {
			}
					.getClass()
					.getEnclosingMethod()
					.getName(), "")) {
				return null;
			}
			activityPackageTierRepository.deleteById(pkgTierId);
			return "Charge Detail Deleted Successfully";
		} catch (Exception e) {
			throw new BusinessException(ResponseCode.VAL_ERROR_OCCURRED, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}