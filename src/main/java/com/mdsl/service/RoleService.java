package com.mdsl.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.ChangeStatusRequestDto;
import com.mdsl.model.dto.request.RoleActivityRequestDto;
import com.mdsl.model.dto.request.RoleRequestDto;
import com.mdsl.model.dto.response.ActivityResponseDto;
import com.mdsl.model.dto.response.RoleActivityResponseDto;
import com.mdsl.model.dto.response.RoleResponseDto;
import com.mdsl.model.entity.Activity;
import com.mdsl.model.entity.Role;
import com.mdsl.model.entity.RoleActivity;
import com.mdsl.model.entity.User;
import com.mdsl.model.mapper.ActivityMapper;
import com.mdsl.model.mapper.RoleActivityMapper;
import com.mdsl.model.mapper.RoleMapper;
import com.mdsl.repository.ActivityRepository;
//import com.mdsl.repository.BranchRepository;
import com.mdsl.repository.InstitutionRepository;
import com.mdsl.repository.RoleActivityRepository;
import com.mdsl.repository.RoleRepository;
import com.mdsl.repository.UserRoleRepository;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.enumerations.StatusEnum;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleService {
	private final RoleRepository roleRepository;
	private final InstitutionRepository institutionRepository;
	private final UserRoleRepository userRoleRepository;
	private final RoleActivityRepository roleActivityRepository;
	private final ActivityRepository activityRepository;
	// private final BranchRepository branchRepository;
	private final UserService userService;
	private final RoleMapper roleMapper;
	private final RoleActivityMapper roleActivityMapper;
	private final ActivityMapper activityMapper;
	// private final BackEndLogService backEndLogService;

	/*
	 * Returns a list of all roles from table MD_ENT_ROLE for a specific institution
	 * id
	 */
//	public List<RoleResponseDto> getAllRolesByInstitution (String instId)
//	{
//		//instId=1;
//		Institution institution = institutionRepository.findById(instId)
//				.orElseThrow(()-> new BusinessException (ResponseCode.CFG_INVALID_INST, HttpStatus.NOT_FOUND)); 
//		
//		List<RoleResponseDto> allRoles= new ArrayList<RoleResponseDto>(); 
//		//List<Role> roles = roleRepository.findByInstitutionOrderByRoleName(institution);
//		List<Role> roles=roleRepository.findByInstitution(institution);
//		
//		List<RoleActivityResponseDto> listOfRoleActivityResponseDTO = new ArrayList<RoleActivityResponseDto>();
//		List<RoleActivity> roleActivity = new ArrayList<RoleActivity>();
//		
//		for(Role tempRole:roles)
//		{
//			roleActivity = roleActivityRepository.findAllByRoleId(tempRole.getRoleId()); 
//			for(RoleActivity tempRoleActivity:roleActivity)
//			{
//				Activity activity=tempRoleActivity.getActivity();
//				ActivityResponseDto activityResponseDto=activityMapper.toDto(activity);
//				RoleActivityResponseDto roleActivityResponseDto=roleActivityMapper.toDto(tempRoleActivity);
//				roleActivityResponseDto.setActivity(activityResponseDto);
//				listOfRoleActivityResponseDTO.add(roleActivityResponseDto);
//				
//			}
//			
//			RoleResponseDto roleResponse = roleMapper.toDto(tempRole); 
//			roleResponse.setRoleActivities(listOfRoleActivityResponseDTO); 
//			allRoles.add(roleResponse); 
//			
//		}
//		
////		Validations.isEmpty(roles);
//		return allRoles;
//	}

	public List<RoleResponseDto> getAllRoles() {
		// instId=1;
//		Institution institution = institutionRepository.findById(instId)
//				.orElseThrow(()-> new BusinessException (ResponseCode.CFG_INVALID_INST, HttpStatus.NOT_FOUND)); 

		List<RoleResponseDto> allRoles = new ArrayList<RoleResponseDto>();
		// List<Role> roles =
		// roleRepository.findByInstitutionOrderByRoleName(institution);
		List<Role> roles = roleRepository.findAll();

		List<RoleActivityResponseDto> listOfRoleActivityResponseDTO = new ArrayList<RoleActivityResponseDto>();
		List<RoleActivity> roleActivity = new ArrayList<RoleActivity>();

		for (Role tempRole : roles) {
			roleActivity = roleActivityRepository.findAllByRoleId(tempRole.getRoleId());
			for (RoleActivity tempRoleActivity : roleActivity) {
				Activity activity = tempRoleActivity.getActivity();
				ActivityResponseDto activityResponseDto = activityMapper.toDto(activity);
				RoleActivityResponseDto roleActivityResponseDto = roleActivityMapper.toDto(tempRoleActivity);
				roleActivityResponseDto.setActivity(activityResponseDto);
				listOfRoleActivityResponseDTO.add(roleActivityResponseDto);

			}

			RoleResponseDto roleResponse = roleMapper.toDto(tempRole);
			roleResponse.setRoleActivities(listOfRoleActivityResponseDTO);
			allRoles.add(roleResponse);

		}

//		Validations.isEmpty(roles);
		return allRoles;
	}

	/*
	 * Returns a specific role from table MD_ENT_ROLE based on a role Id
	 */
//	public RoleResponseDto getRoleById (int roleId, String instId)
//	{
//		Institution institution = institutionRepository.findById(instId).orElseThrow(()-> new BusinessException (ResponseCode.CFG_INVALID_INST, HttpStatus.NOT_FOUND));
//		Role role = roleRepository.findByRoleIdAndInstitution(roleId, institution).orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_ROLE, HttpStatus.NOT_FOUND)); 
//		
//		List<RoleActivityResponseDto> listOfRoleActivityResponseDTO = new ArrayList<RoleActivityResponseDto>();
//		List<RoleActivity> roleActivity = new ArrayList<RoleActivity>();
//		roleActivity = roleActivityRepository.findAllByRole(role); 
//		
//		roleActivity.forEach((roleAct) -> {
//			RoleActivityResponseDto roleActivityResponseDTO = roleActivityMapper.toDto(roleAct); 
//			ActivityResponseDto activityResponseDto = activityMapper.toDto(roleAct.getActivity()); 
//			roleActivityResponseDTO.setActivity(activityResponseDto); 					
//			listOfRoleActivityResponseDTO.add(roleActivityResponseDTO); 
//		}); 
//		
//		RoleResponseDto roleResponse = roleMapper.toDto(role); 
//		roleResponse.setRoleActivities(listOfRoleActivityResponseDTO); 
//		
//		return roleResponse; 
//	}

	public RoleResponseDto getRoleById(int roleId) {
//		Institution institution = institutionRepository.findById(instId).orElseThrow(()-> new BusinessException (ResponseCode.CFG_INVALID_INST, HttpStatus.NOT_FOUND));
		Role role = roleRepository.findByRoleId(roleId)
				.orElseThrow(() -> new BusinessException(ResponseCode.ROL_ROLE_NOT_FOUND, HttpStatus.NOT_FOUND));

		List<RoleActivityResponseDto> listOfRoleActivityResponseDTO = new ArrayList<RoleActivityResponseDto>();
		List<RoleActivity> roleActivity = new ArrayList<RoleActivity>();
		roleActivity = roleActivityRepository.findAllByRole(role);

		roleActivity.forEach((roleAct) -> {
			RoleActivityResponseDto roleActivityResponseDTO = roleActivityMapper.toDto(roleAct);
			ActivityResponseDto activityResponseDto = activityMapper.toDto(roleAct.getActivity());
			roleActivityResponseDTO.setActivity(activityResponseDto);
			listOfRoleActivityResponseDTO.add(roleActivityResponseDTO);
		});

		RoleResponseDto roleResponse = roleMapper.toDto(role);
		roleResponse.setRoleActivities(listOfRoleActivityResponseDTO);

		return roleResponse;
	}

	/*
	 * Returns a list of all active roles from table MD_ENT_ROLE for a specific
	 * institution id
	 */
//	public List<RoleResponseDto> getActiveRolesByInstitution (String instId)
//	{
//		Institution institution = institutionRepository.findById(instId).orElseThrow(()-> new BusinessException (ResponseCode.CFG_INVALID_INST, HttpStatus.NOT_FOUND)); 
//		
//		List<RoleResponseDto> allRoles= new ArrayList<RoleResponseDto>(); 
//		List<Role> roles = roleRepository.findByStatusAndInstitutionOrderByRoleName(StatusEnum.ENABLED.getValue(), institution);
//		
//		List<RoleActivityResponseDto> listOfRoleActivityResponseDTO = new ArrayList<RoleActivityResponseDto>();
//		List<RoleActivity> roleActivity = new ArrayList<RoleActivity>();
//		
//		for(Role tempRole:roles)
//		{
//			roleActivity = roleActivityRepository.findAllByRole(tempRole); 
//			roleActivity.forEach((roleAct) -> {
//				RoleActivityResponseDto roleActivityResponseDTO = roleActivityMapper.toDto(roleAct); 
//				ActivityResponseDto activityResponseDto = activityMapper.toDto(roleAct.getActivity()); 
//				roleActivityResponseDTO.setActivity(activityResponseDto); 					
//				listOfRoleActivityResponseDTO.add(roleActivityResponseDTO); 
//			}); 
//			
//			RoleResponseDto roleResponse = roleMapper.toDto(tempRole); 
//			roleResponse.setRoleActivities(listOfRoleActivityResponseDTO); 
//			allRoles.add(roleResponse); 
//			
//		}
//		Validations.isEmpty(roles);
//		return allRoles;
//	}

	public List<RoleResponseDto> getActiveRoles() {
//		Institution institution = institutionRepository.findById(instId).orElseThrow(()-> new BusinessException (ResponseCode.CFG_INVALID_INST, HttpStatus.NOT_FOUND)); 

		List<RoleResponseDto> allRoles = new ArrayList<RoleResponseDto>();
		List<Role> roles = roleRepository.findByStatusOrderByRoleName(StatusEnum.ENABLED.getValue());

		List<RoleActivityResponseDto> listOfRoleActivityResponseDTO = new ArrayList<RoleActivityResponseDto>();
		List<RoleActivity> roleActivity = new ArrayList<RoleActivity>();

		for (Role tempRole : roles) {
			roleActivity = roleActivityRepository.findAllByRole(tempRole);
			roleActivity.forEach((roleAct) -> {
				RoleActivityResponseDto roleActivityResponseDTO = roleActivityMapper.toDto(roleAct);
				ActivityResponseDto activityResponseDto = activityMapper.toDto(roleAct.getActivity());
				roleActivityResponseDTO.setActivity(activityResponseDto);
				listOfRoleActivityResponseDTO.add(roleActivityResponseDTO);
			});

			RoleResponseDto roleResponse = roleMapper.toDto(tempRole);
			roleResponse.setRoleActivities(listOfRoleActivityResponseDTO);
			allRoles.add(roleResponse);

		}
//		Validations.isEmpty(roles);
		return allRoles;
	}

	/*
	 * Saves or created a role based on the role id If role id is not available or
	 * is equal to 0, we create If role id is available we update The role name is
	 * unique The transactions are logged in table MD_ADT_BKD_LOG
	 */
	public RoleResponseDto saveRole(RoleRequestDto roleRequestDto, String remoteAddress, String instId) {
		String action = "";
		String description = "";
		Role saveRole;
		Role savedRole;
		List<RoleActivity> assignedActivities = new ArrayList<RoleActivity>();
		UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();

//		Institution institution = institutionRepository.findById(instId).orElseThrow(() -> new BusinessException (ResponseCode.CFG_INVALID_INST, HttpStatus.NOT_FOUND));
		// Optional<Branch> branch = branchRepository.findById(branchId);

		if (Objects.isNull(roleRequestDto.getRoleId()) || roleRequestDto.getRoleId() == 0) {// create role
//			if (roleRepository.existsByRoleNameAndInstitution(roleRequestDto.getRoleName(), institution)){
//				throw new BusinessException(ResponseCode.CFG_ROLE_ALREADY_EXIST, HttpStatus.CONFLICT);
//			}

			if (roleRepository.existsByRoleName(roleRequestDto.getRoleName())) {
				throw new BusinessException(ResponseCode.ROL_ROLE_ALREADY_EXISTS, HttpStatus.CONFLICT);
			}

			saveRole = roleMapper.toEntity(roleRequestDto);
//			saveRole.setInstitution(institution);
			saveRole.setCreatedBy(userService.getEntityUserById(userDetails.getId()));
			saveRole.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			savedRole = roleRepository.save(saveRole);

			if (!Objects.isNull(roleRequestDto.getRoleActivities())) {
				List<RoleActivityRequestDto> roleActivities = roleRequestDto.getRoleActivities();
				roleActivities.forEach((roleActivity) -> {
//					Activity activity = activityRepository.findByActivityIdAndInstitution(roleActivity.getActivityId(), institution).orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_ACTIVITY, HttpStatus.NOT_FOUND)); 
					Activity activity = activityRepository.findByActivityId(roleActivity.getActivityId()).orElseThrow(
							() -> new BusinessException(ResponseCode.CFG_INVALID_ACTIVITY, HttpStatus.NOT_FOUND));
					RoleActivity roleAct = roleActivityMapper.toEntity(roleActivity);
					roleAct.setRole(savedRole);
					roleAct.setActivity(activity);
					roleAct.setCreatedBy(userService.getEntityUserById(userDetails.getId()));
					roleAct.setCreatedDate(new Timestamp(System.currentTimeMillis()));
					roleAct = roleActivityRepository.save(roleAct);
					assignedActivities.add(roleAct);
				});
			}
			savedRole.setRoleActivities(assignedActivities);
			action = "create";
			description = "Created Role [" + roleRequestDto.getRoleName() + " - " + roleRequestDto.getRoleDesc() + "]";
		} else {// update role
//			if (!roleRepository.existsByRoleIdAndInstitution(roleRequestDto.getRoleId(), institution)) {
//				throw new BusinessException(ResponseCode.CFG_INVALID_ROLE, HttpStatus.NOT_FOUND); 
//			}
			
			List<Role> roles = roleRepository.findByRoleNameIgnoreCaseAndRoleIdNot(roleRequestDto.getRoleName(),roleRequestDto.getRoleId());
			if(!roles.isEmpty()) {
				throw new BusinessException(ResponseCode.ROL_ROLE_ALREADY_EXISTS, HttpStatus.NOT_FOUND);
			}

			if (!roleRepository.existsByRoleId(roleRequestDto.getRoleId())) {
				throw new BusinessException(ResponseCode.ROL_ROLE_NOT_FOUND, HttpStatus.NOT_FOUND);
			}

			saveRole = roleMapper.toEntity(roleRequestDto);
//			saveRole.setInstitution(institution);
			saveRole.setCreatedBy(userService.getEntityUserById(userDetails.getId()));
			saveRole.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			savedRole = roleRepository.save(saveRole);

			roleActivityRepository.deleteByRole(savedRole);
			roleActivityRepository.flush();

			if (!Objects.isNull(roleRequestDto.getRoleActivities())) {
				List<RoleActivityRequestDto> roleActivities = roleRequestDto.getRoleActivities();
				roleActivities.forEach((roleActivity) -> {
//					Activity activity = activityRepository.findByActivityIdAndInstitution(roleActivity.getActivityId(), institution).orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_ACTIVITY, HttpStatus.NOT_FOUND)); 
					Activity activity = activityRepository.findByActivityId(roleActivity.getActivityId()).orElseThrow(
							() -> new BusinessException(ResponseCode.CFG_INVALID_ACTIVITY, HttpStatus.NOT_FOUND));
					RoleActivity roleAct = roleActivityMapper.toEntity(roleActivity);
					roleAct.setRole(savedRole);
					roleAct.setActivity(activity);
					roleAct.setCreatedBy(userService.getEntityUserById(userDetails.getId()));
					roleAct.setCreatedDate(new Timestamp(System.currentTimeMillis()));
					roleAct = roleActivityRepository.save(roleAct);
					assignedActivities.add(roleAct);
				});
			}

			savedRole.setRoleActivities(assignedActivities);
			action = "update";
			description = "Updated Role [" + roleRequestDto.getRoleName() + " - " + roleRequestDto.getRoleDesc() + "]";
		}

		RoleResponseDto roleResponseDto = roleMapper.toDto(savedRole);
		// backEndLogService.logBackEndActivity(userService.getEntityUserById(userDetails.getId()),
		// LoggingCategoriesEnum.CONFIGURATION.getValue(), ("Role-" + action),
		// description, remoteAddress, roleRequestDto.toString(), institution,
		// branch.get());
		return roleResponseDto;
	}

	/*
	 * Deletes a role from table MD_ENT_ROLE based on the role id Logs the
	 * transaction in table MD_ADT_BKD_LOG
	 */
//	public void deleteRole (int roleId, String instId,String remoteAddress) { 
//		UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();		
//		Institution institution = institutionRepository.findById(instId).orElseThrow(() -> new BusinessException (ResponseCode.CFG_INVALID_INST, HttpStatus.NOT_FOUND)); 
//		//Optional<Branch> branch = branchRepository.findById(branchId);
//		Role role = roleRepository.findByRoleIdAndInstitution(roleId, institution).orElseThrow(()-> new BusinessException(ResponseCode.CFG_INVALID_ROLE, HttpStatus.NOT_FOUND));
//		
//		if(userRoleRepository.existsByRole(role)) {
//			throw new BusinessException(ResponseCode.CFG_ROLE_NO_DELETE, HttpStatus.CONFLICT); 
//		}
//		
//		roleActivityRepository.deleteByRole(role);
//		roleRepository.deleteById(roleId);
//		//backEndLogService.logBackEndActivity(userService.getEntityUserById(userDetails.getId()), LoggingCategoriesEnum.CONFIGURATION.getValue(), ("Role-delete"), "Deleted role [" + role.getRoleName() + " - " + role.getRoleDesc() + "]", remoteAddress, "", institution, branch.get());
//	}

	public void deleteRole(int roleId, String remoteAddress) {
		UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
//		Institution institution = institutionRepository.findById(instId).orElseThrow(() -> new BusinessException (ResponseCode.CFG_INVALID_INST, HttpStatus.NOT_FOUND)); 
		// Optional<Branch> branch = branchRepository.findById(branchId);
		Role role = roleRepository.findByRoleId(roleId)
				.orElseThrow(() -> new BusinessException(ResponseCode.ROL_ROLE_NOT_FOUND, HttpStatus.NOT_FOUND));

		if (userRoleRepository.existsByRole(role)) {
			throw new BusinessException(ResponseCode.ROL_ROLE_NO_DELETE, HttpStatus.CONFLICT);
		}

		roleActivityRepository.deleteByRole(role);
		roleRepository.deleteById(roleId);
		// backEndLogService.logBackEndActivity(userService.getEntityUserById(userDetails.getId()),
		// LoggingCategoriesEnum.CONFIGURATION.getValue(), ("Role-delete"), "Deleted
		// role [" + role.getRoleName() + " - " + role.getRoleDesc() + "]",
		// remoteAddress, "", institution, branch.get());
	}

	/*
	 * Enables/disables a role by changing the status Logs the transaction in table
	 * MD_ADT_BKD_LOG
	 */
//	public void changeRoleStatus (ChangeStatusRequestDto changeRoleStatusRequestDto, String instId, String remoteAddress) {
//		UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		Institution institution = institutionRepository.findById(instId).orElseThrow(() -> new BusinessException (ResponseCode.CFG_INVALID_INST, HttpStatus.NOT_FOUND));
//	//	Optional<Branch> branch = branchRepository.findById(branchId);
//		Role role = roleRepository.findByRoleIdAndInstitution(changeRoleStatusRequestDto.getId(), institution).orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_ROLE, HttpStatus.NOT_FOUND));
//		
//		if (Character.compare(changeRoleStatusRequestDto.getStatus().charAt(0), role.getStatus()) == 0) {
//			throw new BusinessException(ResponseCode.CFG_STATUS_NOT_CHANGED, HttpStatus.CONFLICT);
//		}
//		
//		Date currentDate=new Date();
//		 
//		roleRepository.updateRoleStatus(changeRoleStatusRequestDto.getId(), changeRoleStatusRequestDto.getStatus().charAt(0),userDetails.getId(),currentDate);
//	//	backEndLogService.logBackEndActivity(userService.getEntityUserById(userDetails.getId()), LoggingCategoriesEnum.CONFIGURATION.getValue(), ("Role-status-change"), "Status changed for role [" + role.getRoleName() + " - " + role.getRoleDesc() + "]", remoteAddress, changeRoleStatusRequestDto.toString(), institution, branch.get());
//	}

	public String changeRoleStatus(ChangeStatusRequestDto changeRoleStatusRequestDto, String remoteAddress) {
		UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();

		Role role = roleRepository.findByRoleId(changeRoleStatusRequestDto.getId())
				.orElseThrow(() -> new BusinessException(ResponseCode.ROL_ROLE_NOT_FOUND, HttpStatus.NOT_FOUND));

		if (Character.compare(changeRoleStatusRequestDto.getStatus().charAt(0), role.getStatus()) == 0) {
			throw new BusinessException(ResponseCode.ROL_ROLE_STATUS_NOT_CHANED, HttpStatus.CONFLICT);
		}

		Date currentDate = new Date();

		roleRepository.updateRoleStatus(changeRoleStatusRequestDto.getId(),
				changeRoleStatusRequestDto.getStatus().charAt(0), userDetails.getId(), currentDate);

		return "Status changed successfully" ;
	}
}