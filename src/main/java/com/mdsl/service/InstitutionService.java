package com.mdsl.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.ChangeStatusRequestDto;
import com.mdsl.model.dto.request.InstitutionRequestDto;
import com.mdsl.model.dto.response.InstitutionResponseDto;
import com.mdsl.model.entity.EntityLevels;
import com.mdsl.model.entity.Institution;
import com.mdsl.model.entity.Role;
import com.mdsl.model.entity.SystemCode;
import com.mdsl.model.entity.User;
import com.mdsl.model.entity.UserInstitutionMapping;
import com.mdsl.model.entity.UserRole;
import com.mdsl.model.mapper.EntityLevelsMapper;
import com.mdsl.model.mapper.InstitutionMapper;
import com.mdsl.repository.EntityLevelsRepository;
import com.mdsl.repository.InstitutionRepository;
import com.mdsl.repository.InstitutionTypeRepository;
import com.mdsl.repository.RoleRepository;
import com.mdsl.repository.SystemCodeRepository;
import com.mdsl.repository.UserInstitutionMappingRepository;
import com.mdsl.repository.UserRepository;
import com.mdsl.repository.UserRoleRepository;
import com.mdsl.utils.MakerCheckerEngine;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.SequenceGeneratedClass;
import com.mdsl.utils.enumerations.StatusEnum;

@Service
public class InstitutionService {
	public InstitutionService() {
	}

	@Autowired
	private InstitutionTypeRepository institutionTypeRepository;

	@Autowired
	private InstitutionRepository institutionRepository;

	@Autowired
	private InstitutionMapper institutionMapper;

	@Autowired
	private SystemCodeRepository systemCodeRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRoleRepository userRoleRepository;

	@Autowired
	private UserInstitutionMappingRepository userInstitutionMappingRepository;
	
	@Autowired
	private EntityLevelsRepository entityLevelsRepository;
	
	@Autowired 
	private EntityLevelsMapper entityLevelsMapper; 
	
	@Autowired
	private InstitutionControlService institutionControlService;

	@Autowired
	private MakerCheckerEngine makerCheckerEngine;

	@Value("${default.UserRole}")
	private String defaultRole;

	@Value("${default.AdminRole}")
	private String defaultAdminRole;

	static int countOfLastRecordSequence;
	
	@PersistenceContext
	EntityManager em;
	
	SequenceGeneratedClass sequenceGeneratedClass =new SequenceGeneratedClass();
	
	
//	@Modifying
//	public List updateRecordSequence() {
//		return em.createNativeQuery("ALTER SEQUENCE MD_INSTITUTION_SEQ INCREMENT BY 5")
//	    .getResultList(); 
//	}
	
	
	public List<InstitutionResponseDto> getAllInstitutuion() {
//		List<Institution> allInstitution = institutionRepository.findAll(Sort.by(Sort.Direction.ASC, "institutionId"));
		List<Institution> allInstitution = institutionRepository.findAll(Sort.by(Sort.Direction.DESC, "dateCreate"));
		List<InstitutionResponseDto> allInstitutionResponseDto = new ArrayList<InstitutionResponseDto>();
		allInstitution.stream().forEach((institution) -> {
	        if (!"SYSTEM".equals(institution.getInstitutionId())) {
	            InstitutionResponseDto institutionResponseDto = institutionMapper.toDto(institution);
	            allInstitutionResponseDto.add(institutionResponseDto);
	        }
	    });
		return allInstitutionResponseDto;
	}

	public List<InstitutionResponseDto> getActiveInstitutuions() {
		List<Institution> allActiveInstitution = institutionRepository.findByStatus(StatusEnum.ENABLED.getValue(),
				Sort.by(Sort.Direction.DESC, "dateCreate"));
		List<InstitutionResponseDto> allInstitutionResponseDto = new ArrayList<InstitutionResponseDto>();
		allActiveInstitution.stream().forEach((institution) -> {
	        if (!"SYSTEM".equals(institution.getInstitutionId())) {
	            InstitutionResponseDto institutionResponseDto = institutionMapper.toDto(institution);
	            allInstitutionResponseDto.add(institutionResponseDto);
	        }
	    });
		return allInstitutionResponseDto;
	}
	
	public List<InstitutionResponseDto> getAllActiveInstitutuions() {
		List<Institution> allActiveInstitution = institutionRepository.findByStatus(StatusEnum.ENABLED.getValue(),
				Sort.by(Sort.Direction.DESC, "dateCreate"));
		List<InstitutionResponseDto> allInstitutionResponseDto = new ArrayList<InstitutionResponseDto>();
		allActiveInstitution.stream().forEach((institution) -> {
            InstitutionResponseDto institutionResponseDto = institutionMapper.toDto(institution);
            allInstitutionResponseDto.add(institutionResponseDto);
	    });
		return allInstitutionResponseDto;
	}

	public InstitutionResponseDto getInstitutuionById(String instId) {
		Institution institution = institutionRepository.findById(instId)
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
		InstitutionResponseDto institutionResponseDto = institutionMapper.toDto(institution);
		return institutionResponseDto;
	}	

	
	public InstitutionResponseDto saveOrUpdateInstitution(InstitutionRequestDto institutionRequestDto) {
		Institution institution = new Institution();
		
		UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		int count = 0;

		SystemCode institutionType = systemCodeRepository.findById(institutionRequestDto.getInstitutionTypeId())
				.orElseThrow(() -> new BusinessException(ResponseCode.SYS_CODE_ID_NOT_FOUND, HttpStatus.NOT_FOUND));

		Optional<Institution> institution1 = institutionRepository
				.findByInstitutionId(institutionRequestDto.getInstitutionId());

		if (makerCheckerEngine.processIfRequired(institutionRequestDto, this.getClass().getName(), new Object() {}.getClass().getEnclosingMethod().getName(), "")) {
			return null;
		}
		if (!(institution1.isEmpty()) && (String.valueOf(institutionRequestDto.getUpdateFlag()).equals("1"))) {
			List<Institution> insList = institutionRepository.findByInstitutionNameEqualsIgnoreCaseAndInstitutionId(
					institutionRequestDto.getInstitutionName(), institutionRequestDto.getInstitutionId());
			if (insList.size() == 0) {
				boolean inst = checkInstitutionOnUpdate(institutionRequestDto.getInstitutionId(), institutionRequestDto.getInstitutionName(), institution1.get().getRecordSeqId());
				if (inst == true) {
					count = 1;
					throw new BusinessException(ResponseCode.INT_INSTITUTION_ALREADY_ISSUED, HttpStatus.NOT_FOUND);
				} else {
					count = 0;
				}
			}

			if (count == 0) {
				institution = institutionRepository.findByInstitutionId(institutionRequestDto.getInstitutionId())
						.orElseThrow(
								() -> new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
				institution.setInstitutionName(institutionRequestDto.getInstitutionName());
				institution.setInstitutionTypeAlt(institutionRequestDto.getInstitutionTypeAlt());
				institution.setInstitutionType(institutionType);
				institution.setStatus(institutionRequestDto.getStatus());
				institutionRepository.save(institution);
				
				return institutionMapper.toDto(institution);
			} else {
				throw new BusinessException(ResponseCode.INT_INSTITUTION_ALREADY_ISSUED, HttpStatus.NOT_FOUND);
			}

		} else {
			if (institutionRequestDto.getInstitutionId().equals("0")) {
				throw new BusinessException(ResponseCode.CFG_INVALID_INST, HttpStatus.NOT_FOUND);
			} else {

				if (!(institution1.isEmpty()) || (String.valueOf(institutionRequestDto.getUpdateFlag()).equals("1"))) {
					throw new BusinessException(ResponseCode.CFG_INST_ID_EXISTS_OR_FLAG_INVALID, HttpStatus.NOT_FOUND);
				}
				boolean inst = checkInstitutionOnAdd(institutionRequestDto.getInstitutionId(), institutionRequestDto.getInstitutionName());
				if (inst == true) {
					institution = institutionMapper.toEntity(institutionRequestDto);
					institution.setInstitutionId(institutionRequestDto.getInstitutionId());
					institution.setInstitutionType(institutionType);
					
					institution.setRecordSeqId(null);
					institution.setDateCreate(new Date());
					institution.setStatus(institutionRequestDto.getStatus());
					if(userDetails!=null) {
						institution.setUserCreate(Integer.valueOf(userDetails.getId()).toString());
					}
					institution = institutionRepository.saveAndFlush(institution);
					List<EntityLevels> entityLevels = this.entityLevelsRepository.findByInstitution_InstitutionId("SYSTEM", Sort.by("entityLevelId"));
					int i = 1;
					for(EntityLevels entityLevel : entityLevels) {
						EntityLevels newEntitutLevel = entityLevelsMapper.clone(entityLevel); 
						newEntitutLevel.setEntityLevelId(null);
						newEntitutLevel.setInstitution(institution);
						newEntitutLevel.setHierarchyLevel(i);
						newEntitutLevel.setUserCreate(userDetails.getUsername());
						java.util.Date utilDate = new java.util.Date(System.currentTimeMillis());
						newEntitutLevel.setDateCreate(new java.sql.Date(utilDate.getTime()));
						this.entityLevelsRepository.save(newEntitutLevel);
						i++;
					}

					if (userDetails.getUsername().equals("systemadmin@mas")) {
						addAdminUserInstitutionMapping(userDetails, institution, defaultAdminRole);
					} else {
						addNormalUserInstitutionMapping(userDetails, institution, defaultRole);
						addAdminUserInstitutionMapping(userDetails, institution, defaultAdminRole);
					}

				} else {
					throw new BusinessException(ResponseCode.INT_INSTITUTION_ALREADY_ISSUED, HttpStatus.NOT_FOUND);
				}
				return institutionMapper.toDto(institution);
			}
		}

	}
	
	public void addAdminUserInstitutionMapping(UserDetailsImpl userDetails, Institution institution, String role) {

		User adminUser = new User();
		
		Role role1 = new Role();

		adminUser = userRepository.findByUsername("systemadmin@mas")
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_USER_NAME, HttpStatus.NOT_FOUND));
		if(!role.equals("")) {
			role1 = roleRepository.findByRoleName(role.trim())
					.orElseThrow(() -> new BusinessException(ResponseCode.ROL_ROLE_NOT_FOUND, HttpStatus.NOT_FOUND));
			
			UserRole userRole1 = UserRole.builder().role(role1).user(adminUser).institution(institution)
					.createdBy(adminUser).createdDate(new Timestamp(System.currentTimeMillis())).build();
			userRole1 = userRoleRepository.save(userRole1);
		}else {
			throw new BusinessException(ResponseCode.ROL_INVALID_ROLE_ID, HttpStatus.NOT_FOUND);
		}

		UserInstitutionMapping userInstitutionMapping1 = new UserInstitutionMapping();
		userInstitutionMapping1.setUser(adminUser);
		userInstitutionMapping1.setInstitution(institution);
		userInstitutionMapping1.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		userInstitutionMapping1.setCreatedBy(adminUser);
		userInstitutionMappingRepository.save(userInstitutionMapping1);
	}

	
	public void addNormalUserInstitutionMapping(UserDetailsImpl userDetails, Institution institution, String role) {

		User user1 = new User();

		user1 = userRepository.findByUsername(userDetails.getUsername())
				.orElseThrow(() -> new BusinessException(ResponseCode.USR_INVALID_USER_NAME, HttpStatus.NOT_FOUND));

		Role role1 = new Role();
		if(!role.equals("")) {
			role1 = roleRepository.findByRoleName(role.trim())
					.orElseThrow(() -> new BusinessException(ResponseCode.ROL_ROLE_NOT_FOUND, HttpStatus.NOT_FOUND));
			
			UserRole userRole = UserRole.builder().role(role1).user(user1).institution(institution).createdBy(user1)
					.createdDate(new Timestamp(System.currentTimeMillis())).build();
			userRole = userRoleRepository.save(userRole);
		}else {
			throw new BusinessException(ResponseCode.ROL_INVALID_ROLE_ID, HttpStatus.NOT_FOUND);
		}
		
		UserInstitutionMapping userInstitutionMapping = new UserInstitutionMapping();
		userInstitutionMapping.setUser(user1);
		userInstitutionMapping.setInstitution(institution);
		userInstitutionMapping.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		userInstitutionMapping.setCreatedBy(user1);
		userInstitutionMappingRepository.save(userInstitutionMapping);
	}

	@Transactional(rollbackOn=Exception.class)
	public void deleteInstitution(String instId) throws Exception {
		
		 Institution institution=institutionRepository.findById(instId)
			.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
		 
		 List<EntityLevels> entityLevels = this.entityLevelsRepository.findByInstitution_InstitutionId(institution.getInstitutionId(), Sort.by("entityLevelId"));

		 if (makerCheckerEngine.processIfRequired(instId, this.getClass().getName(), new Object() {}.getClass().getEnclosingMethod().getName(), "")) {
			 return;
		 }
		 entityLevels.stream().forEach((entityLevel) -> {
			 this.entityLevelsRepository.delete(entityLevel);
		 });
		 
		 UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();
		 List<UserInstitutionMapping> userInstitutionMappings=userInstitutionMappingRepository.findByInstitution_InstitutionId(institution.getInstitutionId());
		 List<UserRole> userRoles=userRoleRepository.findByInstitution_InstitutionId(institution.getInstitutionId());  
		 
		 if(userRoles.size()>0) {
			 userRoles.stream().forEach((userRole) -> {
				 userRoleRepository.delete(userRole);
			 });
		 }
		 
		 this.institutionControlService.deleteInstitutionControlByInstitutionId(institution.getInstitutionId());
		 
		 if(userInstitutionMappings.size()>0) {
			 userInstitutionMappings.stream().forEach((userInstitutionMapping) -> {
				 userInstitutionMappingRepository.delete(userInstitutionMapping);
			 });
		 }		if(institution!=null) {
			 
			institutionRepository.deleteById(institution.getInstitutionId());
		}
	}

	public String changeStatus(@Valid ChangeStatusRequestDto changeStatusRequestDto) {

		Institution institution = institutionRepository.findById(changeStatusRequestDto.getIdString())
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND));

		institution.setStatus(changeStatusRequestDto.getStatus().charAt(0));
		if (makerCheckerEngine.processIfRequired(changeStatusRequestDto, this.getClass().getName(), new Object() {}.getClass().getEnclosingMethod().getName(), "")) {
			return null;
		}
		institutionRepository.save(institution);

		return "Status changed successfully";
	}
	
	public boolean checkInstitutionOnAdd(String instId, String instName) {
		List<Institution> institutions = institutionRepository
				.findInstitutionExistsFromInstitutionIdOrInstitutionName(instId.toLowerCase(), instName.toLowerCase());
		return institutions.size() == 0;
	}

	public boolean checkInstitutionOnUpdate(String instId, String instName, Integer recordSeqId) {
		List<Institution> institutions = institutionRepository
				.findInstitutionExistsFromRecordSeqIdAndInstitutionIdOrInstitutionName(recordSeqId, instName.toLowerCase());
		return institutions.size() > 0;
	}

}
