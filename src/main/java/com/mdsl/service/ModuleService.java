package com.mdsl.service;

import com.mdsl.repository.*;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.Validations;
import com.mdsl.utils.enumerations.StatusEnum;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ModuleService {
    private final JdbcTemplate jdbcTemplate;

	public byte[] getModulesActivitiesByUser(int instId, int userId) {
		return jdbcTemplate.queryForObject(
				"SELECT DATA FROM MV_ROLE_API_ACCESS MV_USER_MODULE_JSON_BLOB WHERE USER_ID = ? AND INST_ID = ?",
				byte[].class,
				userId,
				instId
		);
	}

	/*
	 * Returns a module from table MD_LKU_MODULE based on the module id
	 */
//	@Cacheable(key = "#id", cacheResolver="cacheResolver")
//	public ModuleResponseDto getModuleById(int id) {
//		Module module = moduleRepository.findById(id).orElseThrow(()-> new BusinessException(ResponseCode.CFG_INVALID_MODULE, HttpStatus.NOT_FOUND));
//		return convertModuleToModuleResponseDto(module);
//	}

	/*
	 * Returns the list of modules from table MD_LKU_MODULE
	 */
//	@Cacheable(key="#root.methodName", cacheResolver="cacheResolver")
//	public List<ModuleResponseDto> getAllModules() {
//		List<ModuleResponseDto> allModulesDto = new ArrayList<ModuleResponseDto>();
//		List<com.mdsl.model.entity.Module> allModules = moduleRepository.findAll(Sort.by(Sort.Direction.ASC, "moduleDesc"));
//
//		allModules.forEach((module) -> {
//			allModulesDto.add(convertModuleToModuleResponseDto(module));
//	    });
//
//		Validations.isEmpty(allModules);
//		return allModulesDto;
//	}
	
	/*
	 * Returns the list of active modules from table MD_LKU_MODULE
	 */
//	@Cacheable(key="#root.methodName", cacheResolver="cacheResolver")
//	public List<ModuleResponseDto> getActiveModules() {
//		List<ModuleResponseDto> allModulesDto = new ArrayList<ModuleResponseDto>();
//		List<Module> allModules = moduleRepository.findByModuleStatus(StatusEnum.ENABLED.getValue(), Sort.by(Sort.Direction.ASC, "moduleDesc"));
//
//		allModules.forEach((module) -> {
//			allModulesDto.add(convertModuleToModuleResponseDto(module));
//	    });
//		Validations.isEmpty(allModules);
//		return allModulesDto;
//	}
	
	/*
	 * Returns the list of modules with their corresponding activities based on the institution sent in the request header
	 */
//	@Cacheable(key = "#instId", cacheResolver="cacheResolver")
//	public List<ModuleActivityResponseDto> getModulesActivities(int instId) {
//		List<ModuleActivityResponseDto> listOfModuleActivityResponseDto = new ArrayList<ModuleActivityResponseDto>();
//		List<Module> allModules = moduleRepository.findBySubModuleAndModuleStatus(StatusEnum.DISABLED.getValue(), StatusEnum.ENABLED.getValue(), Sort.by(Sort.Direction.DESC, "moduleId"));
//		for (Module module : allModules) {
//			listOfModuleActivityResponseDto.add(this.getModulesActivities(module, instId));
//		}
//		return listOfModuleActivityResponseDto;
//	}
	
	/*
	 * Returns the list of modules with their corresponding activities based on the institution sent in the request header
	 * and the user role id sent in the path variable
	 */
//	public List<ModuleActivityResponseDto> getModulesActivitiesByUser(int instId) {
//		UserDetailsImpl userDetails = commonService.getLoggedInUser();
//		User user = commonService.getUser(userDetails.getId());
//		Integer delegatedUserId = 0; 
//		
//		//Get delegate user roles
//		if(!Objects.isNull(user.getDelegationFlag()) && user.getDelegationFlag() == StatusEnum.ENABLED.getValue()) {
//			Optional<Delegation> delegation = delegationRepository.findByDelegateUserAndFromDateLessThanEqualAndToDateGreaterThanEqualAndStatus(user.getUserId(), Date.valueOf(LocalDate.now()), Date.valueOf(LocalDate.now()), String.valueOf(StatusEnum.ENABLED.getValue()));
//			if (delegation.isPresent()) {
//				delegatedUserId = delegation.get().getUser();			
//			}
//		}
//		final Integer delegatedUserIdFinal = delegatedUserId;
//
//		List<ModuleActivityResponseDto> listOfModuleActivityResponseDto;
//
//		List<Module> allModules = moduleRepository.findBySubModuleAndModuleStatus(
//				StatusEnum.DISABLED.getValue(),
//				StatusEnum.ENABLED.getValue(),
//				Sort.by(Sort.Direction.ASC, "moduleId"));
//
//		Set<Integer> allModuleIds = new HashSet<>();
//		Map<Integer, List<Module>> moduleTreeByRoot = new HashMap<>();
//
//
//		for (Module module : allModules) {
//			List<Module> tree = moduleRepository.findAllByTree(module.getModuleId());
//			moduleTreeByRoot.put(module.getModuleId(), tree);
//			for (Module m : tree) {
//				allModuleIds.add(m.getModuleId());
//			}
//		}
//		List<Integer> userList = Arrays.asList(userDetails.getId(), delegatedUserIdFinal);
//
//		List<UserRoles> allUserRoles =
//				userActivityViewRepository
//						.findByUserIdDelegateUserIdAndModuleIdInAndInstId(
//								userList,
//								new ArrayList<>(allModuleIds),
//								instId
//						);
//
//		List<Integer> activityIds =
//				allUserRoles.stream()
//						.map(UserRoles::getActivityId)
//						.distinct()
//						.collect(Collectors.toList());
//
//		Map<Integer, List<ActivityUrl>> urlsByActivity =
//				activityUrlRepository.findByActivityIdIn(activityIds)
//						.stream()
//						.collect(Collectors.groupingBy(ActivityUrl::getActivityId));
//
//		Map<Integer, List<UserRoles>> rolesByModule =
//				allUserRoles.stream()
//						.collect(Collectors.groupingBy(UserRoles::getModuleId));
//
//		listOfModuleActivityResponseDto = allModules
//				.stream()
//				.map(module -> this.getModulesActivitiesByUser(
//						module,
//						moduleTreeByRoot.get(module.getModuleId()),
//						rolesByModule,
//						urlsByActivity
//				))
//				.collect(Collectors.toList());
//
//		return listOfModuleActivityResponseDto;
//	}

	/*
	 * Returns the list of all activities per module 
	 */
//	public ModuleActivityResponseDto getModulesActivities(Module module, Integer instId) {
//		ModuleActivityResponseDto response = new ModuleActivityResponseDto();
//		response.setModuleId(module.getModuleId());
//		response.setModuleDesc(module.getModuleDesc());
//		//List<Activity> listOfActivities = activityRepository.findByInstitutionAndModule(instId, module);
//		List<Activities> listOfActivities = activityRepository.findActivitiesForRole(instId, module.getModuleId(), String.valueOf(StatusEnum.ENABLED.getValue()));
//		response.setActivities(convertActivityToActivityResponseDto(listOfActivities));
//
//		List<Module> listOfSubModules = moduleRepository.findByParentModuleIdAndModuleStatus(module.getModuleId(), StatusEnum.ENABLED.getValue(), Sort.by(Sort.Direction.ASC, "moduleDesc"));
//		List<ModuleActivityResponseDto> subModules = new ArrayList<ModuleActivityResponseDto>();
//		for(Module subModule : listOfSubModules) {
//			ModuleActivityResponseDto responseSubModule = new ModuleActivityResponseDto();
//			responseSubModule = getModulesActivities(subModule, instId);
//			subModules.add(responseSubModule);
//		}
//		response.setSubModule(subModules);
//		return response;
//	}
////	public ModuleActivityResponseDto getModulesActivitiesByUser(
////	        int userId,
////	        int delegatedUserId,
////	        Module module,
////	        int instId) {
////
////	    ModuleActivityResponseDto response = new ModuleActivityResponseDto();
////	    response.setModuleId(module.getModuleId());
////	    response.setModuleDesc(module.getModuleDesc());
////	    List<Integer> userList = Arrays.asList(userId, delegatedUserId);
////	    List<UserRoles> listOfUserActivities =
////	            userActivityViewRepository.findByUserIdDelegateUserIdAndModuleIdAndInstId(
////	                    userList, module.getModuleId(), instId);
////
////	    if (listOfUserActivities.isEmpty()) {
////	        response.setActivities(Collections.emptyList());
////	    } else {
////	        List<Integer> activityIds = listOfUserActivities.stream()
////	                .map(UserRoles::getActivityId)
////	                .distinct()
////	                .collect(Collectors.toList());
////	        Map<Integer, List<ActivityUrl>> activityUrlMap = activityUrlRepository
////	                .findByActivityIdIn(activityIds)
////	                .stream()
////	                .collect(Collectors.groupingBy(ActivityUrl::getActivityId));
////	        List<ActivityModuleResponseDto> activityModuleList = listOfUserActivities.parallelStream()
////	                .map(userActivity -> {
////	                    ActivityModuleResponseDto activityResponse =
////	                            activityModuleMapper.userRoleToDto(userActivity);
////
////	                    List<ActivityUrl> urls = activityUrlMap.get(userActivity.getActivityId());
////	                    if (urls != null && !urls.isEmpty()) {
////	                        List<FrontUrl> listOfUrl = urls.stream()
////	                                .map(url -> {
////	                                    FrontUrl frontUrl = new FrontUrl();
////	                                    frontUrl.setIsMenu(url.getIsMenu());
////	                                    frontUrl.setUrl(url.getUrl());
////	                                    return frontUrl;
////	                                })
////	                                .collect(Collectors.toList());
////	                        activityResponse.setUrl(listOfUrl);
////	                    }
////
////	                    return activityResponse;
////	                })
////	                .collect(Collectors.toList());
////
////	        response.setActivities(activityModuleList);
////	    }
////	    List<Module> subModules = moduleRepository
////	            .findByParentModuleIdAndModuleStatus(
////	                    module.getModuleId(),
////	                    StatusEnum.ENABLED.getValue(),
////	                    Sort.by(Sort.Direction.ASC, "moduleDesc")
////	            );
////	    if (subModules.isEmpty()) {
////	        response.setSubModule(Collections.emptyList());
////	    } else {
////	        List<ModuleActivityResponseDto> subModuleResponses = subModules.stream()
////	                .map(subModule -> getModulesActivitiesByUser(userId, delegatedUserId, subModule, instId))
////	                .collect(Collectors.toList());
////
////	        response.setSubModule(subModuleResponses);
////	    }
////
////	    return response;
////	}
//	public ModuleActivityResponseDto getModulesActivitiesByUser(
//			Module rootModule,
//			List<Module> moduleTree,
//			Map<Integer, List<UserRoles>> rolesByModule,
//			Map<Integer, List<ActivityUrl>> urlsByActivity){
//
////	    List<Integer> userList = Arrays.asList(userId, delegatedUserId);
//
//
////	    List<Integer> moduleIds = allModules.stream()
////	            .map(Module::getModuleId)
////	            .collect(Collectors.toList());
//
////	    List<UserRoles> allUserRoles =
////	            userActivityViewRepository.findByUserIdDelegateUserIdAndModuleIdInAndInstId(
////	                    userList, moduleIds, instId
////	            );
//
////	    Map<Integer, List<UserRoles>> rolesByModule =
////	            allUserRoles.stream().collect(Collectors.groupingBy(UserRoles::getModuleId));
////
////	    List<Integer> activityIds = allUserRoles.stream()
////	            .map(UserRoles::getActivityId)
////	            .distinct()
////	            .collect(Collectors.toList());
//
////	    Map<Integer, List<ActivityUrl>> urlsByActivity =
////	            activityUrlRepository.findByActivityIdIn(activityIds)
////	                    .stream()
////	                    .collect(Collectors.groupingBy(ActivityUrl::getActivityId));
////	List<Module> allModules = moduleRepository.findAllByTree(rootModule.getModuleId());
//
//	    Map<Integer, List<Module>> moduleChildren =
//				moduleTree.stream()
//	                    .filter(m -> m.getParentModuleId() != null)
//	                    .collect(Collectors.groupingBy(Module::getParentModuleId));
//
//	    return buildModuleResponse(rootModule, rolesByModule, urlsByActivity, moduleChildren);
//	}
//
//
//	private ModuleActivityResponseDto buildModuleResponse(Module module,Map<Integer, List<UserRoles>> rolesByModule,Map<Integer, List<ActivityUrl>> urlsByActivity,Map<Integer, List<Module>> moduleChildren) {
//
//	    ModuleActivityResponseDto response = new ModuleActivityResponseDto();
//	    response.setModuleId(module.getModuleId());
//	    response.setModuleDesc(module.getModuleDesc());
//
//	    List<UserRoles> userRoles = rolesByModule.getOrDefault(module.getModuleId(), Collections.emptyList());
//
//	    if (userRoles.isEmpty()) {
//	        response.setActivities(Collections.emptyList());
//	    } else {
//	        List<ActivityModuleResponseDto> activityResponses = userRoles.stream()
//	                .map(role -> {
//	                    ActivityModuleResponseDto dto = activityModuleMapper.userRoleToDto(role);
//
//	                    List<ActivityUrl> urls = urlsByActivity.get(role.getActivityId());
//	                    if (urls != null) {
//	                        dto.setUrl(
//	                                urls.stream()
//	                                        .map(url -> {
//	                                            FrontUrl f = new FrontUrl();
//	                                            f.setIsMenu(url.getIsMenu());
//	                                            f.setUrl(url.getUrl());
//	                                            return f;
//	                                        })
//	                                        .collect(Collectors.toList())
//	                        );
//	                    }
//
//	                    return dto;
//	                })
//	                .collect(Collectors.toList());
//
//	        response.setActivities(activityResponses);
//	    }
//
//	    List<Module> subs = moduleChildren.get(module.getModuleId());
//	    if (subs == null || subs.isEmpty()) {
//	        response.setSubModule(Collections.emptyList());
//	    } else {
//	        response.setSubModule(
//	                subs.stream()
//	                        .map(sub -> buildModuleResponse(sub,
//	                                rolesByModule, urlsByActivity, moduleChildren))
//	                        .collect(Collectors.toList())
//	        );
//	    }
//
//	    return response;
//	}
//
//
//	/*
//	 * Convert Module to ModuleResponseDto
//	 */
//	private ModuleResponseDto convertModuleToModuleResponseDto (Module module) {
//		ModuleResponseDto response = moduleMapper.toDto(module);
//
//		Module parentModule = null;
//		if (Objects.nonNull(module.getParentModuleId())) {
//			parentModule = moduleRepository.findById(module.getParentModuleId()).orElseThrow(()-> new BusinessException(ResponseCode.CFG_INVALID_MODULE, HttpStatus.NOT_FOUND));
//			response.setParentModuleDesc(parentModule.getModuleDesc());
//		}
//
//		return response;
//	}
//
//	private List<ActivityModuleResponseDto> convertActivityToActivityResponseDto (List<Activities> activities) {
//		List<ActivityModuleResponseDto> response = new ArrayList<ActivityModuleResponseDto>();
//		for (Activities activity : activities) {
//			ActivityModuleResponseDto activityResponse = activityModuleMapper.toDtoActivities(activity);
////			if(Character.compare(activityResponse.getHasScreen(), StatusEnum.ENABLED.getValue())==0) {
//			List<ActivityUrl> activityUrl = this.activityUrlRepository.findByActivityId(activity.getActivityId());
//			if(Objects.nonNull(activityUrl)&& !activityUrl.isEmpty()) {
//				List<FrontUrl> listOfUrl = new ArrayList<FrontUrl>();
//				for (ActivityUrl url: activityUrl) {
//					FrontUrl frontUrl = new FrontUrl();
//
//					frontUrl.setIsMenu(url.getIsMenu());
//					frontUrl.setUrl(url.getUrl());
//					listOfUrl.add(frontUrl);
//				}
//				activityResponse.setUrl(listOfUrl);
//			}
////			}
//			response.add(activityResponse);
//		}
//		return response;
//	}
}