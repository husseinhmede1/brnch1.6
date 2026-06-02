package com.mdsl.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.ChangeStatusRequestDto;
import com.mdsl.model.dto.request.DeleteNonActivityPackageRequestDto;
import com.mdsl.model.dto.request.NonActivityPackageEntityMappingRequestDto;
import com.mdsl.model.dto.request.NonActivityPackageRequestDto;
import com.mdsl.model.dto.response.EntitiesResponseDto;
import com.mdsl.model.dto.response.NonActivityPackageResponseDto;
import com.mdsl.model.entity.Entities;
import com.mdsl.model.entity.Institution;
import com.mdsl.model.entity.NonActivityPackage;
import com.mdsl.model.mapper.EntityMapper;
import com.mdsl.model.mapper.NonActivityPackageMapper;
import com.mdsl.repository.EntitiesRepository;
import com.mdsl.repository.InstitutionRepository;
import com.mdsl.repository.NonActivityPackageDetailsRepository;
import com.mdsl.repository.NonActivityPackageRepository;
import com.mdsl.utils.MakerCheckerEngine;
import com.mdsl.utils.ResponseCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NonActivityPackageService {
	@Autowired
	private NonActivityPackageRepository nonActivityPackageRepository;

	@Autowired
	private InstitutionRepository institutionRepository;

	@Autowired
	private NonActivityPackageMapper nonActivityPackageMapper;

	@Autowired
	private EntitiesRepository entitiesRepository;

	@Autowired
	private NonActivityPackageDetailsRepository nonActivityPackageDetailsRepository;
	
	@Autowired
	private EntityMapper entityMapper;
    private final MakerCheckerEngine makerCheckerEngine;

	public List<NonActivityPackageResponseDto> getAllNonActivityPackages() {
		List<NonActivityPackageResponseDto> allNonActivityPackageCodesDto = new ArrayList<NonActivityPackageResponseDto>();
		List<NonActivityPackage> allNonActivityPackageCodes = nonActivityPackageRepository
				.findAll(Sort.by(Sort.Direction.ASC, "packageId"));

		allNonActivityPackageCodes.stream().forEach((nonActivityPackageCode) -> {
			NonActivityPackageResponseDto nonActivityPackageCodeResponseDto = nonActivityPackageMapper
					.toDto(nonActivityPackageCode);
			allNonActivityPackageCodesDto.add(nonActivityPackageCodeResponseDto);
		});

		return allNonActivityPackageCodesDto;
	}

	public List<NonActivityPackageResponseDto> getActiveNonActivityPackagesByInstitutionId(String id) {
		List<NonActivityPackageResponseDto> allNonActivityPackageCodesDto = new ArrayList<NonActivityPackageResponseDto>();
		List<NonActivityPackage> allNonActivityPackageCodes = nonActivityPackageRepository
				.findByInstitution_InstitutionIdAndStatus(id, "1".charAt(0), Sort.by(Sort.Direction.ASC, "packageId"));

		allNonActivityPackageCodes.stream().forEach((nonActivityPackageCode) -> {
			NonActivityPackageResponseDto nonActivityPackageCodeResponseDto = nonActivityPackageMapper
					.toDto(nonActivityPackageCode);
			allNonActivityPackageCodesDto.add(nonActivityPackageCodeResponseDto);
		});

		return allNonActivityPackageCodesDto;
	}

	public List<NonActivityPackageResponseDto> getNonActivityPackageByInstitutionId(String instId) {
		List<NonActivityPackage> nonActivityPackages = nonActivityPackageRepository
				.findNonActivityPackageByInstitutionId(instId, Sort.by(Sort.Direction.ASC, "packageId"));
		List<NonActivityPackageResponseDto> allNonActivityPackageResponseDto = new ArrayList<NonActivityPackageResponseDto>();

		nonActivityPackages.stream().forEach((nonActivityPackageCode) -> {
			NonActivityPackageResponseDto nonActivityPackageCodeResponseDto = nonActivityPackageMapper
					.toDto(nonActivityPackageCode);
			allNonActivityPackageResponseDto.add(nonActivityPackageCodeResponseDto);
		});

		return allNonActivityPackageResponseDto;
	}

	public NonActivityPackageResponseDto getNonActivityPackageById(String code, String instId) {
		Optional<NonActivityPackage> nonActivityPackage = nonActivityPackageRepository.findByPackageIdAndInstitution_institutionId(code, instId);
		NonActivityPackage nonActivityPkg = nonActivityPackage
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_ACTIVITY, HttpStatus.NOT_FOUND));
		return nonActivityPackageMapper.toDto(nonActivityPkg);
	}

	public NonActivityPackageResponseDto saveOrUpdateNonActivityPackage(
			NonActivityPackageRequestDto nonActivityPackageRequestDto) {

		NonActivityPackage pkg = null;
		NonActivityPackage nonActivityPackage;
		
		UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();

		Institution institution = institutionRepository.findById(nonActivityPackageRequestDto.getInstitutionId())
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_INST_CODE, HttpStatus.NOT_FOUND));
		
		Optional<NonActivityPackage> nonActivityPackage1 = nonActivityPackageRepository.findByPackageIdAndInstitution_institutionId(nonActivityPackageRequestDto.getPackageId(), institution.getInstitutionId());

//		if (!(nonActivityPackageRequestDto.getPackageId().trim().equals(""))
//				&& !(nonActivityPackageRequestDto.getPackageId().equals(null))) {
		
		if(!(nonActivityPackage1.isEmpty()) && (String.valueOf(nonActivityPackageRequestDto.getUpdateFlag()).equals("1"))) {

			pkg = nonActivityPackageRepository.findByPackageIdAndInstitution_institutionId(nonActivityPackageRequestDto.getPackageId(), institution.getInstitutionId())
					.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_ACTIVITY, HttpStatus.NOT_FOUND));

			pkg.setPackageName(nonActivityPackageRequestDto.getPackageName());
			pkg.setInstitution(institution);
		//	pkg.setUserCreate(Integer.valueOf(userDetails.getId()).toString());
			if (makerCheckerEngine.processIfRequired(nonActivityPackageRequestDto, NonActivityPackageService.class.getName(), "saveOrUpdateNonActivityPackage", "")) {
				return null;
			}
			nonActivityPackage = nonActivityPackageRepository.save(pkg);

		}

		else {
			if (nonActivityPackageRequestDto.getPackageId().equals("0")) {
				throw new BusinessException(ResponseCode.INVALID_ACT_FEE_PKG_ID, HttpStatus.NOT_FOUND);
			}

			if(!(nonActivityPackage1.isEmpty())){
				throw new BusinessException(ResponseCode.ACTIVITY_FEE_PACKAGE_NOT_FOUND, HttpStatus.NOT_FOUND);
			}
			if((String.valueOf(nonActivityPackageRequestDto.getUpdateFlag()).equals("1"))){
				throw new BusinessException(ResponseCode.INVALID_UPDATE_FLAG, HttpStatus.NOT_FOUND);
			}

			nonActivityPackage = nonActivityPackageMapper.toEntity(nonActivityPackageRequestDto);
			nonActivityPackage.setUserCreate("test");
			nonActivityPackage.setStatus('1');
			nonActivityPackage.setDateCreate(new Date());
			nonActivityPackage.setInstitution(institution);
			if(userDetails!=null) {
				nonActivityPackage.setUserCreate(Integer.valueOf(userDetails.getId()).toString());
			}
			if (makerCheckerEngine.processIfRequired(nonActivityPackageRequestDto, NonActivityPackageService.class.getName(), "saveOrUpdateNonActivityPackage", "")) {
				return null;
			}
			nonActivityPackage = nonActivityPackageRepository.save(nonActivityPackage);
		}
		return nonActivityPackageMapper.toDto(nonActivityPackage);
	}

	@Modifying
	@Transactional
	public void deleteNonActivityPackage(DeleteNonActivityPackageRequestDto deleteNonActivityPackageRequestDto) {
	    // Verify it exists first
	    nonActivityPackageRepository.findByPackageIdAndInstitution_institutionId(deleteNonActivityPackageRequestDto.getId(),deleteNonActivityPackageRequestDto.getInstId())
	        .orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_ACTIVITY, HttpStatus.NOT_FOUND));
		if (makerCheckerEngine.processIfRequired(deleteNonActivityPackageRequestDto, NonActivityPackageService.class.getName(), "deleteNonActivityPackage", "")) {
			return;
		}
	    // Delete details first (bulk delete - already working)
	    nonActivityPackageDetailsRepository.deleteByNonActivityPackageAndInstitution(deleteNonActivityPackageRequestDto.getId(),deleteNonActivityPackageRequestDto.getInstId());
	    
	    // Delete parent (bulk delete - avoids the orphanRemoval check)
	    nonActivityPackageRepository.deleteByPackageIdAndInstitutionId(deleteNonActivityPackageRequestDto.getId(),deleteNonActivityPackageRequestDto.getInstId());
	}

	public String changeStatus(ChangeStatusRequestDto changeStatusRequestDto) {
		NonActivityPackage nonActivityPackage = nonActivityPackageRepository
				.findByPackageIdAndInstitution_institutionId(String.valueOf(changeStatusRequestDto.getIdString()), changeStatusRequestDto.getInstId())
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_ACTIVITY, HttpStatus.NOT_FOUND));
		nonActivityPackage.setStatus(changeStatusRequestDto.getStatus().charAt(0));
		if (makerCheckerEngine.processIfRequired(changeStatusRequestDto, NonActivityPackageService.class.getName(), "changeStatus", "")) {
			return null;
		}
		nonActivityPackageRepository.save(nonActivityPackage);

		return "Status changed successfully";
	}


	public NonActivityPackageEntityMappingRequestDto mapPackageWithEntity(
			NonActivityPackageEntityMappingRequestDto requestDto) {
		NonActivityPackage nonActivityPackage = nonActivityPackageRepository.findByPackageIdAndInstitution_institutionId(requestDto.getId(), requestDto.getInstId())
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_ACT_PKG_ID, HttpStatus.NOT_FOUND));
		List<String> listofEntityRequstDto = requestDto.getEntities();

		List<Entities> oldEntityAssign = entitiesRepository.findByNonactivityFeePKGEntity_PackageIdAndInstitution_institutionId(nonActivityPackage.getPackageId(),requestDto.getInstId(),
				Sort.by(Sort.Direction.ASC, "entityId"));
		if (!oldEntityAssign.isEmpty()) {

			List<String> oldEntityList = oldEntityAssign.stream().map(Entities::getEntityId)
					.collect(Collectors.toList());

			listofEntityRequstDto = listofEntityRequstDto.stream().filter(e -> !oldEntityList.contains(e))
					.collect(Collectors.toList());

			List<String> entityIds = oldEntityAssign.stream().map(Entities::getEntityId).collect(Collectors.toList());
			if (makerCheckerEngine.processIfRequired(requestDto, NonActivityPackageService.class.getName(), "mapPackageWithEntity", "")) {
				return null;
			}
			entityIds.removeAll(requestDto.getEntities());
			entityIds.forEach((id) -> {
				Entities entities = entitiesRepository.findByEntityIdAndInstitution(id,nonActivityPackage.getInstitution()).orElseThrow(
						() -> new BusinessException(ResponseCode.CFG_ENTITY_NOT_FOUND, HttpStatus.NOT_FOUND));
				entities.setNonactivityFeePKG(null);
				entitiesRepository.save(entities);
			});
		}
		listofEntityRequstDto.forEach((id) -> {
			Entities entities = entitiesRepository.findByEntityIdAndInstitution(String.valueOf(id),nonActivityPackage.getInstitution())
					.orElseThrow(() -> new BusinessException(ResponseCode.CFG_ENTITY_NOT_FOUND, HttpStatus.NOT_FOUND));
			entities.setNonactivityFeePKGEntity(nonActivityPackage);
			entities.setNonactivityFeePKG(nonActivityPackage.getPackageId());

			entitiesRepository.save(entities);
		});

		return requestDto;
	}

	public List<EntitiesResponseDto> getMappedEntitiesByNonActivityPackageId(String id, String instId) {
		List<EntitiesResponseDto> responseDtos = new ArrayList<EntitiesResponseDto>();
		Optional<NonActivityPackage> nonActivityPackage = nonActivityPackageRepository.findByPackageId(id);

		List<Entities> entities = entitiesRepository.findByNonactivityFeePKGEntity_PackageIdAndInstitution_institutionId(id,nonActivityPackage.get().getInstitutionId(),
				Sort.by(Sort.Direction.ASC, "entityId"));

		entities.forEach((ent) -> {
			EntitiesResponseDto dto = entityMapper.toDto(ent);
			responseDtos.add(dto);
		});

		return responseDtos;
	}
}
