package com.mdsl.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.validation.Valid;

import com.mdsl.model.entity.*;
import com.mdsl.utils.MakerCheckerEngine;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.ActivityPackageDetailRequestDto;
import com.mdsl.model.dto.request.ChangeStatusRequestDto;
import com.mdsl.model.dto.response.ActivityPackageDetailResponseDto;
import com.mdsl.model.mapper.ActivityPackageDetailMapper;
import com.mdsl.repository.ActivityPackageDetailRepository;
import com.mdsl.repository.ActivityPackageRepository;
import com.mdsl.repository.CardSchemeRepository;
import com.mdsl.repository.ChargeMethodRepository;
import com.mdsl.repository.CurrencyRepository;
import com.mdsl.repository.DefaultTransactionIdRepository;
import com.mdsl.repository.EntitiesRepository;
import com.mdsl.repository.InstitutionRepository;
import com.mdsl.repository.IssuerProfileRepository;
import com.mdsl.repository.SystemCodeRepository;
import com.mdsl.repository.TransactionGroupRepository;
import com.mdsl.utils.ResponseCode;

@Service
@RequiredArgsConstructor
public class ActivityPackageDetailService {

    private final ActivityPackageRepository activityPackageRepository;

    private final InstitutionRepository institutionRepository;

    private final CardSchemeRepository cardSchemeRepository;

    private final CurrencyRepository currencyRepository;

    private final ActivityPackageDetailRepository activityPackageDetailRepository;

    private final IssuerProfileRepository issuerRepository;

    private final ActivityPackageDetailMapper activityPackageDetailMapper;

    private final DefaultTransactionIdRepository defaultTransactionIdRepository;

    private final SystemCodeRepository systemCodeRepository;

	private final MakerCheckerEngine makerCheckerEngine;

    public List<ActivityPackageDetailResponseDto> getAllActivityPackageDetailByPkgId(String pkgId, String instId) {
        try {


            Optional<Institution> institution = institutionRepository.findById(instId);
            List<ActivityPackageDetail> allActivityPackageDetail = activityPackageDetailRepository.findByPkgIdAndInstitution(
                    pkgId, instId);

            List<ActivityPackageDetailResponseDto> allActivityPackageDetailResponseDto = new ArrayList<>();
            for (ActivityPackageDetail temp : allActivityPackageDetail) {
                ActivityPackageDetailResponseDto dto = activityPackageDetailMapper.toDto(temp);
                dto.setTranGroupName(temp.getTranGroup());
                dto.setTranName(temp.getAssignedTranId());


                if (Objects.nonNull(temp.getAssignedTranId()) && !temp.getAssignedTranId().isEmpty()) {
                    DefaultTransactionId transactionId = defaultTransactionIdRepository
                            .findByTransactionIdAndInstitution(temp.getAssignedTranId(), institution.get())
                            .orElseThrow(() -> new BusinessException(
                                    ResponseCode.CFG_INVALID_DEFAULT_TRANSACTION,
                                    HttpStatus.NOT_FOUND
                            ));


                    dto.setTranId(transactionId.getTransactionId());
                }
                allActivityPackageDetailResponseDto.add(dto);
            }
            return allActivityPackageDetailResponseDto;
        } catch (BusinessException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ResponseCode.VAL_ERROR_OCCURRED, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ActivityPackageDetailResponseDto getActivityPackageDetailById(int pkgDetailId) {
        ActivityPackageDetail activityPackageDetail = activityPackageDetailRepository.findById(pkgDetailId).orElseThrow(
                () -> new BusinessException(ResponseCode.INVALID_ACT_FEE_PKG_DETAIL_ID, HttpStatus.NOT_FOUND));
        ActivityPackageDetailResponseDto activityPackageDetailResponseDto = activityPackageDetailMapper
                .toDto(activityPackageDetail);
        activityPackageDetailResponseDto.setTranGroupName(activityPackageDetail.getTranGroup());
        activityPackageDetailResponseDto.setTranName(activityPackageDetail.getTranGroup());
        SystemCode systemCode = systemCodeRepository.findByCodeSuffixCodeValueInstitution("FEE_METHOD", activityPackageDetail.getChargeMethod(), "SYSTEM")
                .orElseThrow(() -> new BusinessException(ResponseCode.INVALID_ACT_FEE_PKG_ID, HttpStatus.NOT_FOUND));
        activityPackageDetailResponseDto.setChargeMethodSystemCodeId(systemCode.getSystemCodeId());
        activityPackageDetailResponseDto.setChargeMethodCodeSuffix(systemCode.getCodeSuffix());
        if (Objects.nonNull(activityPackageDetail.getAssignedTranId()) && !activityPackageDetail.getAssignedTranId().equals("")) {
            DefaultTransactionId transactionId = defaultTransactionIdRepository.findByTransactionIdAndInstitution(activityPackageDetail.getAssignedTranId(), activityPackageDetail.getInstitution())
                    .orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_DEFAULT_TRANSACTION,
                            HttpStatus.NOT_FOUND));
            activityPackageDetailResponseDto.setTranId(transactionId.getTransactionId());
        }
        return activityPackageDetailResponseDto;
    }

    public ActivityPackageDetailResponseDto saveOrUpdateActivityPackageDetail(
            ActivityPackageDetailRequestDto dto) {
        try {
            ActivityPackageDetail activityPackageDetail;
            DefaultTransactionId transactionId = null;
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();

            SystemCode chargeMethod = systemCodeRepository.findById(Integer.parseInt(dto.getChargeMethodId()))
                    .orElseThrow(() -> new BusinessException(ResponseCode.INVALID_SYSTEM_CODE, HttpStatus.NOT_FOUND));
            Currency currency = currencyRepository.findById(Integer.parseInt(dto.getCurrencyCodeId()))
                    .orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_CURRENCY, HttpStatus.NOT_FOUND));

            CardScheme cardScheme = cardSchemeRepository.findById(dto.getCardSchemeId())
                    .orElseThrow(() -> new BusinessException(ResponseCode.INVALID_CARDSCHEME, HttpStatus.NOT_FOUND));
            ActivityPackage activityPackage = activityPackageRepository.findById(Integer.parseInt(dto.getPackageId()))
                    .orElseThrow(() -> new BusinessException(ResponseCode.INVALID_ACT_FEE_PKG, HttpStatus.NOT_FOUND));
            Institution institution = institutionRepository.findById(activityPackage.getInstitution().getInstitutionId())
                    .orElseThrow(() -> new BusinessException(ResponseCode.INVALID_INSTITUTION_ID, HttpStatus.NOT_FOUND));
            if (dto.getTranId() != null && !dto.getTranId().trim().isEmpty()) {
                transactionId = defaultTransactionIdRepository.findByTransactionIdAndInstitution(dto.getTranId(), institution)
                        .orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_DEFAULT_TRANSACTION,
                                HttpStatus.NOT_FOUND));
            }
            IssuerProfile issuer = issuerRepository.findById(Integer.parseInt(dto.getIssuerId()))
                    .orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_ISSUER_ID, HttpStatus.NOT_FOUND));

            if ("USER".equalsIgnoreCase(chargeMethod.getCodeSuffix()) &&
                    transactionId != null && !"FEE".equalsIgnoreCase(transactionId.getTransUsage())) {
                throw new BusinessException(ResponseCode.INVALID_USAGE_CODE_OR_CHARGE_METHOD, HttpStatus.NOT_FOUND);
            }

            if (Integer.parseInt(dto.getPackageDetailId()) != 0) {
                activityPackageDetail = activityPackageDetailRepository.findById(Integer.parseInt(dto.getPackageDetailId()))
                        .orElseThrow(() -> new BusinessException(ResponseCode.INVALID_ACT_FEE_PKG_DETAIL_ID,
                                HttpStatus.NOT_FOUND));
            } else {
                activityPackageDetail = activityPackageDetailMapper.toEntity(dto);
                activityPackageDetail.setInstitution(institution);
                activityPackageDetail.setActivityPackage(activityPackage.getPackageId());
                activityPackageDetail.setActivityPackageEntity(activityPackage);

                activityPackageDetail.setDateCreate(new java.sql.Date(new java.util.Date().getTime()));
                if (userDetails != null) {
                    activityPackageDetail.setUserCreate(String.valueOf(userDetails.getId()));
                }
            }

			if (makerCheckerEngine.processIfRequired(dto, this.getClass().getName(), new Object() {
			}
					.getClass()
					.getEnclosingMethod()
					.getName(), "")) {
				return null;
			}
            activityPackageDetail.setAssignedTranId(transactionId != null ? transactionId.getTransactionId() : null);
            activityPackageDetail.setCardScheme(cardScheme);
            activityPackageDetail.setCurrency(currency);
            activityPackageDetail.setTranGroup(dto.getTranGroupName());
            activityPackageDetail.setChargeMethod(chargeMethod.getCodeValue());

            activityPackageDetail.setIssuerAcqProfile(issuer.getIssuerAcqProfile());
            activityPackageDetail.setIssuerAcqProfileEntity(issuer);

            activityPackageDetail.setTips(dto.getTips());
            activityPackageDetail.setStatus(dto.getStatus() == null || dto.getStatus().equals(' ') ? '0' : dto.getStatus());
            activityPackageDetail.setFixAmount(dto.getFixAmount());
            activityPackageDetail.setPercentageAmount(dto.getPercentageAmount());
            activityPackageDetail.setMinAmount(dto.getMinAmount());
            activityPackageDetail.setMaxAmount(dto.getMaxAmount());
            activityPackageDetail.setStartDate(dto.getStartDate());
            activityPackageDetail.setEndDate(dto.getEndDate());
            activityPackageDetail = activityPackageDetailRepository.save(activityPackageDetail);
            ActivityPackageDetailResponseDto responseDto = activityPackageDetailMapper.toDto(activityPackageDetail);
            responseDto.setTranGroupName(activityPackageDetail.getTranGroup());
            return responseDto;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ResponseCode.VAL_ERROR_OCCURRED, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public String deleteActivationPackageDetail(int pkgDetailId) {

        ActivityPackageDetail activityPackageDetail = activityPackageDetailRepository.findById(pkgDetailId).orElseThrow(
                () -> new BusinessException(ResponseCode.INVALID_ACT_FEE_PKG_DETAIL_ID, HttpStatus.NOT_FOUND));

        try {
			if (makerCheckerEngine.processIfRequired(pkgDetailId, this.getClass().getName(), new Object() {
			}
					.getClass()
					.getEnclosingMethod()
					.getName(), "")) {
				return null;
			}
            activityPackageDetailRepository.deleteById(activityPackageDetail.getPackageDetailId());
        } catch (BusinessException e) {
            throw new BusinessException(ResponseCode.INVALID_ACT_FEE_PKG_DETAIL_ID, HttpStatus.NOT_FOUND);
        }
        return activityPackageDetail.getActivityPackageEntity().getPackageId();

    }

    public void changeStatus(@Valid ChangeStatusRequestDto changeStatusRequestDto) {
        ActivityPackageDetail activityPackageDetail = activityPackageDetailRepository
                .findById(changeStatusRequestDto.getId()).orElseThrow(
                        () -> new BusinessException(ResponseCode.INVALID_ACT_FEE_PKG_DETAIL_ID, HttpStatus.NOT_FOUND));
		if (makerCheckerEngine.processIfRequired(changeStatusRequestDto, this.getClass().getName(), new Object() {
		}
				.getClass()
				.getEnclosingMethod()
				.getName(), "")) {
			return;
		}
        activityPackageDetail.setStatus(changeStatusRequestDto.getStatus().charAt(0));
        activityPackageDetailMapper.toDto(activityPackageDetail);
    }

    public static boolean isValidIndex(String[] arr, int index) {
        try {
            String i = arr[index];
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
        return true;
    }

}