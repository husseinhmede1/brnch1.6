package com.mdsl.service;

import com.mdsl.config.JwtUtils;
import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.*;
import com.mdsl.model.mapper.UserMapper;
import com.mdsl.model.mapper.UserRoleMapper;
import com.mdsl.utils.MakerCheckerEngine;
import com.mdsl.utils.PasswordGenerator;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.Validations;
import com.mdsl.utils.enumerations.AdminDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mdsl.model.dto.response.InstitutionResponseInfoDto;
import com.mdsl.model.dto.response.UserInfoResponseDto;
import com.mdsl.model.dto.response.UserNewResponseDto;
import com.mdsl.model.dto.response.UserResponseDto;
import com.mdsl.model.dto.response.UserRoleNewResponseDto;
import com.mdsl.model.dto.response.UserRoleResponseDto;
import com.mdsl.model.entity.AcquiringTransaction;
import com.mdsl.model.entity.ActivityPackage;
import com.mdsl.model.entity.ActivityPackageDetail;
import com.mdsl.model.entity.ActivityPackageTier;
import com.mdsl.model.entity.Address;
import com.mdsl.model.entity.CardScheme;
import com.mdsl.model.entity.Contact;
import com.mdsl.model.entity.Country;
import com.mdsl.model.entity.Currency;
import com.mdsl.model.entity.CurrencyConversion;
import com.mdsl.model.entity.CurrencyRate;
import com.mdsl.model.entity.DefaultTransactionId;
import com.mdsl.model.entity.Employee;
import com.mdsl.model.entity.Entities;
import com.mdsl.model.entity.Institution;
import com.mdsl.model.entity.MCCList;
import com.mdsl.model.entity.ManualMerchantTransaction;
import com.mdsl.model.entity.ManualNonActivityTransaction;
import com.mdsl.model.entity.NonActivityFeeQuery;
import com.mdsl.model.entity.NonActivityPackage;
import com.mdsl.model.entity.NonActivityPackageDetails;
import com.mdsl.model.entity.PaymentAccount;
import com.mdsl.model.entity.Role;
import com.mdsl.model.entity.SystemCode;
import com.mdsl.model.entity.Terminal;
import com.mdsl.model.entity.TerminalTypes;
import com.mdsl.model.entity.TransactionChargeDetails;
import com.mdsl.model.entity.TransactionGroup;
import com.mdsl.model.entity.User;
import com.mdsl.model.entity.UserAccess;
import com.mdsl.model.entity.UserInstitutionMapping;
import com.mdsl.model.entity.UserRole;
import com.mdsl.repository.AcquiringTransactionRepository;
import com.mdsl.repository.ActivityPackageDetailRepository;
import com.mdsl.repository.ActivityPackageRepository;
import com.mdsl.repository.ActivityPackageTierRepository;
import com.mdsl.repository.AddressRepository;
import com.mdsl.repository.CardSchemeRepository;
import com.mdsl.repository.ContactRepository;
import com.mdsl.repository.CountryRepository;
import com.mdsl.repository.CurrencyConversionRepository;
import com.mdsl.repository.CurrencyRateRepository;
import com.mdsl.repository.CurrencyRepository;
import com.mdsl.repository.DefaultTransactionIdRepository;
import com.mdsl.repository.EmployeeRepository;
import com.mdsl.repository.EntitiesRepository;
import com.mdsl.repository.InstitutionRepository;
import com.mdsl.repository.MCCListRepository;
import com.mdsl.repository.ManualMerchantTransactionRepository;
import com.mdsl.repository.ManualNonActivityTransactionRepository;
import com.mdsl.repository.NonActivityFeeQueryRepository;
import com.mdsl.repository.NonActivityPackageDetailsRepository;
import com.mdsl.repository.NonActivityPackageRepository;
import com.mdsl.repository.PaymentAccountRepository;
import com.mdsl.repository.RoleRepository;
import com.mdsl.repository.SystemCodeRepository;
import com.mdsl.repository.TerminalRepository;
import com.mdsl.repository.TerminalTypesRepository;
import com.mdsl.repository.TransactionChargesDetailsRepository;
import com.mdsl.repository.TransactionGroupRepository;
import com.mdsl.repository.UserAccessRepository;
import com.mdsl.repository.UserInstitutionMappingRepository;
import com.mdsl.repository.UserRepository;
import com.mdsl.repository.UserRoleRepository;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final UserRoleRepository userRoleRepository;
	private final PasswordEncoder passwordEncoder;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final UserMapper userMapper;
	private final UserRoleMapper userRoleMapper;
	private final EmailSenderService emailSenderService;
	private final InstitutionRepository institutionRepository;
	private final UserInstitutionMappingRepository userInstitutionMappingRepository;
	private final SystemCodeRepository systemCodeRepository;
	private final UserAccessRepository userAccessRepository;
	private final CountryRepository countryRepository;
	private final CurrencyRepository currencyRepository;
	private final MCCListRepository mccListRepository;
	private final CardSchemeRepository cardSchemeRepository;
	private final TerminalRepository terminalRepository;
	private final EmployeeRepository employeeRepository;
	private final CurrencyConversionRepository currencyConversionRepository;
	private final CurrencyRateRepository currencyRateRepository;
	private final TransactionGroupRepository transactionGroupRepository;
	private final ActivityPackageRepository activityPackageRepository;
	private final NonActivityPackageRepository nonActivityPackageRepository;
	private final EntitiesRepository entitiesRepository;
	private final DefaultTransactionIdRepository defaultTransactionIdRepository;
	private final AcquiringTransactionRepository acquiringTransactionRepository;
	private final ManualMerchantTransactionRepository manualMerchantTransactionRepository;
	private final ManualNonActivityTransactionRepository manualNonActivityTransactionRepository;
	private final NonActivityFeeQueryRepository nonActivityFeeQueryRepository;
	private final ActivityPackageDetailRepository activityPackageDetailRepository;
	private final NonActivityPackageDetailsRepository nonActivityPackageDetailsRepository;
	private final TransactionChargesDetailsRepository transactionChargesDetailsRepository;
	private final ActivityPackageTierRepository activityPackageTierRepository;
	private final TerminalTypesRepository terminalTypesRepository;
	private final PaymentAccountRepository paymentAccountRepository;
	private final ContactRepository contactRepository;
	private final AddressRepository addressRepository;
	private final MakerCheckerEngine makerCheckerEngine;

	private final JwtUtils jwtUtils;
	
	@Value("${spring.mail.username}")
	private String fromEmail;
	/*
	 * Returns a user from table MD_ENT_USER based on the user id
	 */
	public UserResponseDto getUserForLogin(int userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_USER, HttpStatus.NOT_FOUND));
		return userMapper.toDto(user);
	}

	/*
	 * Returns a user from table MD_ENT_USER based on the user id
	 */

//	public UserResponseDto getUserById(int userId) {
//		User user = userRepository.findById(userId)
//				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_USER, HttpStatus.NOT_FOUND));
//		return userMapper.toDto(user);
//	}

	public UserNewResponseDto getUserById(int userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_USER, HttpStatus.NOT_FOUND));

//		UserRoleNewResponseDto userRoleNewResponseDto=new UserRoleNewResponseDto();
//		userRoleNewResponseDto.setRoleId(user.getUserRoles().);
		List<UserRoleNewResponseDto> userRoleNewResponseDtos = new ArrayList();
		for (UserRole userRole : user.getUserRoles()) {
			UserRoleNewResponseDto userRoleNewResponseDto = userRoleMapper.toResponseDto(userRole);

			userRoleNewResponseDtos.add(userRoleNewResponseDto);
		}

		UserNewResponseDto userNewResponseDto = userMapper.toNewResponseDto(user);
		// userResponseDTO.setNbDaysToChangePassword(nbDaysToChangePassword);
		// userResponseDTO.setWarnToChangePassword(nbDaysToChangePassword <=
		// user.getBranch().getInstitution().getWarningChangePassword() ? '1' : '0');

		List<UserInstitutionMapping> institutionMappings = userInstitutionMappingRepository.findByUser(user);
		List<InstitutionResponseInfoDto> institutionResponseInfoDtos = new ArrayList<InstitutionResponseInfoDto>();
		for (UserInstitutionMapping userInstitutionMapping : institutionMappings) {
			Institution institution = institutionRepository
					.findById(userInstitutionMapping.getInstitution().getInstitutionId()).orElseThrow(
							() -> new BusinessException(ResponseCode.CFG_INVALID_INST, HttpStatus.NOT_FOUND));
			InstitutionResponseInfoDto institutionResponseInfoDto = new InstitutionResponseInfoDto();
			institutionResponseInfoDto.setInstitutionId(institution.getInstitutionId());
			institutionResponseInfoDto.setInstitutionName(institution.getInstitutionName());
			institutionResponseInfoDto.setInstitutionTypeAlt(institution.getInstitutionTypeAlt());
			institutionResponseInfoDto.setStatus(institution.getStatus());
			institutionResponseInfoDtos.add(institutionResponseInfoDto);
		}
		userNewResponseDto.setUserRoles(userRoleNewResponseDtos);
		userNewResponseDto.setInstitution(institutionResponseInfoDtos);

		return userNewResponseDto;
	}

	public UserNewResponseDto getUserByIdForUserScreen(GetUserByIdRequestDto getUserByIdRequestDto) {
		int userId = getUserByIdRequestDto.getUserId();
		User user = userRepository.findByIdAndNotAdmin(userId)
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_USER, HttpStatus.NOT_FOUND));

        if(!getUserByIdRequestDto.getIsUserProfile() && user.getUsername().equals(AdminDetails.USERNAME.getValue())){
            logger.error("@UserService#getUserByIdForUserScreen - user is trying to edit system admin - "+userId);
            throw new BusinessException(ResponseCode.ACTIVITY_NOT_ALLOWED,HttpStatus.BAD_REQUEST);
        }

//		UserRoleNewResponseDto userRoleNewResponseDto=new UserRoleNewResponseDto();
//		userRoleNewResponseDto.setRoleId(user.getUserRoles().);
		List<UserRoleNewResponseDto> userRoleNewResponseDtos = new ArrayList();
		for (UserRole userRole : user.getUserRoles()) {
			UserRoleNewResponseDto userRoleNewResponseDto = userRoleMapper.toResponseDto(userRole);

			userRoleNewResponseDtos.add(userRoleNewResponseDto);
		}

		UserNewResponseDto userNewResponseDto = userMapper.toNewResponseDto(user);
		// userResponseDTO.setNbDaysToChangePassword(nbDaysToChangePassword);
		// userResponseDTO.setWarnToChangePassword(nbDaysToChangePassword <=
		// user.getBranch().getInstitution().getWarningChangePassword() ? '1' : '0');

		List<UserInstitutionMapping> institutionMappings = userInstitutionMappingRepository.findByUser(user);
		List<InstitutionResponseInfoDto> institutionResponseInfoDtos = new ArrayList<InstitutionResponseInfoDto>();
		for (UserInstitutionMapping userInstitutionMapping : institutionMappings) {
			Institution institution = institutionRepository
					.findById(userInstitutionMapping.getInstitution().getInstitutionId()).orElseThrow(
							() -> new BusinessException(ResponseCode.CFG_INVALID_INST, HttpStatus.NOT_FOUND));
			InstitutionResponseInfoDto institutionResponseInfoDto = new InstitutionResponseInfoDto();
			institutionResponseInfoDto.setInstitutionId(institution.getInstitutionId());
			institutionResponseInfoDto.setInstitutionName(institution.getInstitutionName());
			institutionResponseInfoDto.setInstitutionTypeAlt(institution.getInstitutionTypeAlt());
			institutionResponseInfoDto.setStatus(institution.getStatus());
			institutionResponseInfoDtos.add(institutionResponseInfoDto);
		}
		userNewResponseDto.setUserRoles(userRoleNewResponseDtos);
		userNewResponseDto.setInstitution(institutionResponseInfoDtos);
		userNewResponseDto.setIsSystemAdmin(userNewResponseDto.getUsername().equals(AdminDetails.USERNAME.getValue()));
		return userNewResponseDto;
	}


	public List<UserInfoResponseDto> getUsersByInstitutionId(String instId) {
//		List<UserInfoResponseDto> userList = new ArrayList<UserInfoResponseDto>();
//		List<User> users = userRepository.findUserByInstitutionId(instId, Sort.by(Sort.Direction.ASC, "userId"));
//		for (User tempUser : users) {
//			UserInfoResponseDto tempDto = userMapper.toDtoInfo(tempUser);
//			userList.add(tempDto);
//		}
//
//		return userList;

		List<UserInfoResponseDto> userList = new ArrayList<>();

		Institution institution = institutionRepository.findByInstitutionId(instId)
				.orElseThrow(()-> new BusinessException(ResponseCode.CFG_INVALID_INST,HttpStatus.NOT_FOUND));

		List<UserInstitutionMapping> usersInstitutionMappings = userRepository.findUserByInstitutionId(instId, AdminDetails.USERNAME.getValue());

		if(usersInstitutionMappings.isEmpty()){
			throw new BusinessException(ResponseCode.CFG_NO_DATA_FOUND,HttpStatus.NOT_FOUND);
		}

		for (UserInstitutionMapping tempUserInstitutionMapping : usersInstitutionMappings) {

			UserInfoResponseDto tempDto = userMapper.toDtoInfo(tempUserInstitutionMapping.getUser());
			userList.add(tempDto);
		}
		return userList;
	}

	public List<UserInfoResponseDto> getAllUsers() {
		List<UserInfoResponseDto> listOfUsers = new ArrayList<UserInfoResponseDto>();
		List<User> users = userRepository.findAll(Sort.by(Sort.Direction.ASC, "userId"));

		users.forEach((user) -> {
			listOfUsers.add(userMapper.toDtoInfo(user));
		});

		Validations.isEmpty(listOfUsers);
		return listOfUsers;
	}

	/*
	 * Saves or creates a user based on the user id If user id is not available or
	 * is equal to 0, we create If user id is available we update The user name is
	 * unique The email is unique
	 */

	
	public UserResponseDto saveUser(UserRequestDto userRequestDto) {
		String action = "";
		String description = "";
		User saveUser;
		Institution institution1 = null;
		User savedUser = new User();
		String clearPassword = null;
		UserResponseDto userResponseDto=new UserResponseDto();
		List<UserInstitutionMapping> listOfUserMappingIds =new ArrayList<UserInstitutionMapping>();
		
		List<UserRole> listUserRoles =new ArrayList<UserRole>();
		
		UserRole userRole =new UserRole();
		
		SystemCode systemCode = systemCodeRepository.findById(userRequestDto.getPreferedLanguage())
				.orElseThrow(() -> new BusinessException(ResponseCode.INVALID_SYSTEM_CODE_ID, HttpStatus.NOT_FOUND));
		
		List<Institution> listofInstitution = new ArrayList<>();
		for (int i = 0; i < userRequestDto.getInstitutionId().size(); i++) {

			Institution institution = institutionRepository.findById(userRequestDto.getInstitutionId().get(i))
					.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_INST, HttpStatus.NOT_FOUND));

			listofInstitution.add(institution);
		}
		UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();

		List<UserRoleResponseDto> assignedRoles = new ArrayList<UserRoleResponseDto>();

		if (userRequestDto.getDefaultInstitutionId() == null || userRequestDto.getDefaultInstitutionId().isEmpty()) {
			institution1 = listofInstitution.get(0);
		} else {
			institution1 = institutionRepository.findById(userRequestDto.getDefaultInstitutionId())
					.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_INST, HttpStatus.NOT_FOUND));
		}

		if (makerCheckerEngine.processIfRequired(userRequestDto, this.getClass().getName(), new Object() {
		}
				.getClass()
				.getEnclosingMethod()
				.getName(), "")) {
			return null;
		}

		if (Objects.isNull(userRequestDto.getUserId()) || userRequestDto.getUserId() == 0) { // create user

			Optional<User> userByEmail =findUserByEmail(userRequestDto.getEmail());
			if(userByEmail.isPresent()) {
				throw new BusinessException(ResponseCode.CFG_EMAIL_ALREADY_EXISTS, HttpStatus.CONFLICT);
			}
			
			if (userRepository.existsByUsernameIgnoreCase(userRequestDto.getUsername())) {
				throw new BusinessException(ResponseCode.USR_USERNAME_ALREADY_EXISTS, HttpStatus.CONFLICT);
			}

			clearPassword = PasswordGenerator.generatePassword(10);
			String encryptedPassword = bCryptPasswordEncoder.encode(clearPassword);
			saveUser = userMapper.toEntity(userRequestDto);
			saveUser.setLastLoginDate(new Timestamp(System.currentTimeMillis()));
			saveUser.setPassword(encryptedPassword);
			saveUser.setCreatedBy(getEntityUserById(userDetails.getId()));
			saveUser.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			savedUser.setInstitution(listofInstitution);
//			saveUser.setInstitution1(listofInstitution.get(0));
			saveUser.setInstitution1(institution1);
			saveUser.setPreferedLanguage(systemCode);
			savedUser = userRepository.save(saveUser);

			for (Institution institution : listofInstitution) {

				UserInstitutionMapping userInstitutionMapping = new UserInstitutionMapping();
				userInstitutionMapping.setUser(savedUser);
				userInstitutionMapping.setInstitution(institution);
				userInstitutionMapping.setCreatedDate(new Timestamp(System.currentTimeMillis()));
				userInstitutionMapping.setCreatedBy(getEntityUserById(userDetails.getId()));
				userInstitutionMapping = userInstitutionMappingRepository.save(userInstitutionMapping);
				listOfUserMappingIds.add(userInstitutionMapping);
			}

			if (!Objects.isNull(userRequestDto.getRoleIds())) {
				logger.info("@UserService#saveUser "+userRequestDto.getRoleIds().size());
				for (int i = 0; i < userRequestDto.getRoleIds().size(); i++) {

					Institution institution = institutionRepository
							.findById(userRequestDto.getRoleIds().get(i).getInstitutionId()).orElseThrow(
									() -> new BusinessException(ResponseCode.CFG_INVALID_INST, HttpStatus.NOT_FOUND));

//					Role role = roleRepository
//							.findByRoleIdAndInstitution(userRequestDto.getRoleIds().get(i).getRoleId(), institution)
//							.orElseThrow(
//									() -> new BusinessException(ResponseCode.CFG_INVALID_ROLE, HttpStatus.NOT_FOUND));

					Role role = roleRepository.findByRoleId(userRequestDto.getRoleIds().get(i).getRoleId()).orElseThrow(
							() -> new BusinessException(ResponseCode.CFG_INVALID_ROLE, HttpStatus.NOT_FOUND));
					// role.setInstitution(institution);

//					UserRole userRole = UserRole.builder().role(role).user(savedUser)
//							.createdBy(getEntityUserById(userDetails.getId()))
//							.createdDate(new Timestamp(System.currentTimeMillis())).build();

					 userRole = UserRole.builder().role(role).user(savedUser).institution(institution)
							.createdBy(getEntityUserById(userDetails.getId()))
							.createdDate(new Timestamp(System.currentTimeMillis())).build();
					userRole = userRoleRepository.save(userRole);
					listUserRoles.add(userRole);
					assignedRoles.add(userRoleMapper.toDto(userRole));
				}
			}
			action = "create";
			description = "Created user [" + userRequestDto.getUsername() + "]";
			
			if(savedUser.getUserId()!=0) {
				// send email
				try {
//				emailSenderService.sendSimpleEmail(fromEmail,savedUser.getEmail(), "MAS Prime Product - User Creation",
//					"Dears, \n\nYour MAS Prime Product account password is " + clearPassword  + "\n\nRegards,\nACS");
					emailSenderService.sendMail(fromEmail,savedUser.getEmail(), "MAS Prime Product - User Creation",
							"Dears, \n\nYour MAS Prime Product account password is " + clearPassword  + "\n\nRegards,\nACS");
				}catch(Exception e) {
					
					listUserRoles.stream().forEach((userRole1) ->{
						userRoleRepository.delete(userRole1);
					});
					
					listOfUserMappingIds.stream().forEach((userInstituteMapping) ->{
						userInstitutionMappingRepository.delete(userInstituteMapping);
					});
					logger.info("@UserService#saveUser "+savedUser.getUserRoles());
					userRepository.delete(savedUser);
					
					throw new BusinessException(ResponseCode.CFG_INVALID_MAIL, HttpStatus.CONFLICT);
				}
			}

		} else { // update user

			User user = userRepository.findById(userRequestDto.getUserId())
					.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_USER, HttpStatus.NOT_FOUND));

			List<User> userByUserName =userRepository.findByUserNameAndUserId(userRequestDto.getUsername().toLowerCase(),userRequestDto.getUserId()); 
			if(userByUserName.size()>0){
				throw new BusinessException(ResponseCode.CFG_INVALID_USER_NAME, HttpStatus.CONFLICT);
			}
			
			List<User> userByEmail= userRepository.findByEmailEqualsIgnoreCaseAndUserIdNot(userRequestDto.getEmail().toUpperCase(),user.getUserId());
			if(userByEmail.size()>0) {
				throw new BusinessException(ResponseCode.CFG_EMAIL_ALREADY_EXISTS, HttpStatus.NOT_FOUND);
			}
			
			saveUser = userMapper.toEntity(userRequestDto);
			saveUser.setLastLoginDate(user.getLastLoginDate());
			saveUser.setPassword(user.getPassword());
			saveUser.setCreatedBy(user.getCreatedBy());
			saveUser.setCreatedDate(user.getCreatedDate());
			saveUser.setUpdatedBy(getEntityUserById(userDetails.getId()));
			saveUser.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
			savedUser.setInstitution(listofInstitution);
//			savedUser.setInstitution1(listofInstitution.get(0));
			savedUser.setInstitution1(institution1);
			savedUser.setPreferedLanguage(systemCode);
			savedUser = userRepository.save(saveUser);

//			for (Institution institution : listofInstitution) {

			List<UserInstitutionMapping> userInstitutionMappingList = userInstitutionMappingRepository.findByUser(user);

			List<Institution> oldInstitutionList = userInstitutionMappingList.stream()
					.map(UserInstitutionMapping::getInstitution).collect(Collectors.toList());

			List<Institution> tempInstitution = oldInstitutionList.stream()
					.filter(e -> !(listofInstitution.contains(e))).collect(Collectors.toList());
			for (Institution institution : tempInstitution) {
				UserInstitutionMapping userInstitutionMapping = userInstitutionMappingRepository
						.findByUserAndInstitution(user, institution);
				userInstitutionMappingRepository.delete(userInstitutionMapping);
			}

			for (Institution institution : listofInstitution) {
				user.setInstitution1(institution1);
				UserInstitutionMapping userInstitutionMapping = userInstitutionMappingRepository
						.findByUserAndInstitution(user, institution);
				if (userInstitutionMapping != null) {
					userInstitutionMapping.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
					userInstitutionMapping.setUpdatedBy(getEntityUserById(userDetails.getId()));
					userInstitutionMappingRepository.save(userInstitutionMapping);
				} else {
					UserInstitutionMapping userInstitutionMapping1 = new UserInstitutionMapping();
					userInstitutionMapping1.setUser(savedUser);
					userInstitutionMapping1.setInstitution(institution);
					userInstitutionMapping1.setCreatedDate(new Timestamp(System.currentTimeMillis()));
					userInstitutionMapping1.setCreatedBy(getEntityUserById(userDetails.getId()));
					userInstitutionMappingRepository.save(userInstitutionMapping1);
				}
			}

			userRoleRepository.deleteByUser(savedUser);
			userRoleRepository.flush();

			if (!Objects.isNull(userRequestDto.getRoleIds())) {
				logger.info("@UserService#saveUser "+userRequestDto.getRoleIds().size());
				for (int i = 0; i < userRequestDto.getRoleIds().size(); i++) {

					Institution institution = institutionRepository
							.findById(userRequestDto.getRoleIds().get(i).getInstitutionId()).orElseThrow(
									() -> new BusinessException(ResponseCode.CFG_INVALID_INST, HttpStatus.NOT_FOUND));

//					Role role = roleRepository
//							.findByRoleIdAndInstitution(userRequestDto.getRoleIds().get(i).getRoleId(), institution)
//							.orElseThrow(
//									() -> new BusinessException(ResponseCode.CFG_INVALID_ROLE, HttpStatus.NOT_FOUND));

					Role role = roleRepository.findByRoleId(userRequestDto.getRoleIds().get(i).getRoleId()).orElseThrow(
							() -> new BusinessException(ResponseCode.CFG_INVALID_ROLE, HttpStatus.NOT_FOUND));

//					UserRole userRole = UserRole.builder().role(role).user(savedUser)
//							.createdBy(getEntityUserById(userDetails.getId()))
//							.createdDate(new Timestamp(System.currentTimeMillis())).build();
					
				UserRole userRole1 = UserRole.builder().role(role).user(savedUser).institution(institution)
							.createdBy(getEntityUserById(userDetails.getId()))
							.createdDate(new Timestamp(System.currentTimeMillis())).build();
					
					userRole1 = userRoleRepository.save(userRole1);

					assignedRoles.add(userRoleMapper.toDto(userRole1));
				}
			}
			action = "update";
			description = "Update user [" + userRequestDto.getUserId() + " - " + userRequestDto.getUsername() + "]";
		}

		 userResponseDto = userMapper.toDto(savedUser);
		userResponseDto.setUserRoles(assignedRoles);
		
		return userResponseDto;
	}

	/*
	 * Deletes a user from table MD_ENT_USER based on the user id If a user has
	 * activities it cannot be deleted
	 */
	
	
	public void deleteUser(int userId) {
		UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_USER, HttpStatus.NOT_FOUND));

//		if (user.getLastLoginDate()!=null) {
//			throw new BusinessException(ResponseCode.CFG_NO_DELETE, HttpStatus.METHOD_NOT_ALLOWED);
//		}
		List<UserInstitutionMapping> userInstitutionMappings = userInstitutionMappingRepository.findByUser(user);
//		if (userInstitutionMapping.size()!= 0) {
//			
//		} 
		
		List<Country> countries=countryRepository.findByUserCreate(user.getUserId().toString());
		logger.debug("@UserService#deleteUser Countries List Size when delete the user : " + countries.size());
		logger.info("@UserService#deleteUser Countries List Size when delete the user : " + countries.size());
		List<Currency> currencies=currencyRepository.findByUserCreate(user.getUserId().toString());
		logger.debug("@UserService#deleteUser Currencies List Size when delete the user : " + currencies.size());
		logger.debug("@UserService#deleteUser Currencies List Size when delete the user : " + currencies.size());
		List<MCCList> mccLists=mccListRepository.findByCreatedBy(user.getUserId().toString());
		logger.debug("MccLists List Size when delete the user : " + mccLists.size());
		logger.debug("@UserService#deleteUser MccLists List Size when delete the user : " + mccLists.size());
		List<CardScheme> cardSchemes=cardSchemeRepository.findByCreatedBy(user.getUserId().toString());
		logger.debug("CardScheme List Size when delete the user : " + cardSchemes.size());
		logger.debug("@UserService#deleteUser CardScheme List Size when delete the user : " + cardSchemes.size());
		List<TerminalTypes> terminalTypes=terminalTypesRepository.findByUserCreate(user.getUserId().toString());
		logger.debug("@UserService#deleteUser TerminalTypes List Size when delete the user : " + terminalTypes.size());
		logger.debug("@UserService#deleteUser TerminalTypes List Size when delete the user : " + terminalTypes.size());
		List<Institution> institutions=institutionRepository.findByUserCreate(user.getUserId().toString());
		logger.debug("@UserService#deleteUser Institution List Size when delete the user : " + institutions.size());
		logger.debug("@UserService#deleteUser Institution List Size when delete the user : " + institutions.size());
		List<Employee> employees=employeeRepository.findByCreatedBy(user.getUserId());
		logger.debug("Employees List Size when delete the user : " + employees.size());
		logger.debug("@UserService#deleteUser Employees List Size when delete the user : " + employees.size());
		List<CurrencyConversion> currencyConversions=currencyConversionRepository.findByUserCreate(user.getUserId().toString());
		logger.debug("CurrencyConversion List Size when delete the user : " + currencyConversions.size());
		logger.debug("@UserService#deleteUser CurrencyConversion List Size when delete the user : " + currencyConversions.size());
		List<CurrencyRate> currencyRates=currencyRateRepository.findByUserCreate(user.getUserId().toString());
		logger.debug("@UserService#deleteUser CurrencyRates List Size when delete the user : " + currencyRates.size());
		logger.debug("@UserService#deleteUser CurrencyRates List Size when delete the user : " + currencyRates.size());
		List<TransactionGroup> transactionGroups=transactionGroupRepository.findByUserCreate(user.getUserId().toString());
		logger.debug("@UserService#deleteUser TransactionGroup List Size when delete the user : " + transactionGroups.size());
		logger.debug("@UserService#deleteUser TransactionGroup List Size when delete the user : " + transactionGroups.size());
		List<ActivityPackage> activityPackages=activityPackageRepository.findByUserCreate(user.getUserId().toString());
		logger.debug("@UserService#deleteUser ActivityPackage List Size when delete the user : " + activityPackages.size());
		logger.debug("@UserService#deleteUser ActivityPackage List Size when delete the user : " + activityPackages.size());
		List<NonActivityPackage> nonActivityPackages=nonActivityPackageRepository.findByUserCreate(user.getUserId().toString());
		logger.debug("@UserService#deleteUser NonActivityPackage List Size when delete the user : " + nonActivityPackages.size());
		logger.debug("@UserService#deleteUser NonActivityPackage List Size when delete the user : " + nonActivityPackages.size());
		List<Entities> entities=entitiesRepository.findByUserCreate(user.getUserId().toString());
		logger.debug("@UserService#deleteUser Entities List Size when delete the user : " + entities.size());
		logger.debug("@UserService#deleteUser Entities List Size when delete the user : " + entities.size());
		List<DefaultTransactionId> defaultTransactionIds=defaultTransactionIdRepository.findByUserCreate(user.getUserId().toString());
		logger.debug("@UserService#deleteUser DefaultTransactionId List Size when delete the user : " + defaultTransactionIds.size());
		logger.debug("@UserService#deleteUser DefaultTransactionId List Size when delete the user : " + defaultTransactionIds.size());
		List<SystemCode> systemCodes=systemCodeRepository.findByCreatedBy(user.getUserId().toString());
		logger.debug("@UserService#deleteUser SystemCode List Size when delete the user : " + systemCodes.size());
		logger.debug("@UserService#deleteUser SystemCode List Size when delete the user : " + systemCodes.size());
		List<AcquiringTransaction> acquiringTransactions=acquiringTransactionRepository.findByUserCreate(user.getUserId().toString());
		logger.debug("@UserService#deleteUser AcquiringTransaction List Size when delete the user : " + acquiringTransactions.size());
		logger.debug("@UserService#deleteUser AcquiringTransaction List Size when delete the user : " + acquiringTransactions.size());
		List<ManualMerchantTransaction> manualMerchantTransactions=manualMerchantTransactionRepository.findByUserCreate(user.getUserId().toString());
		logger.debug("@UserService#deleteUser ManualMerchantTransaction List Size when delete the user : " + manualMerchantTransactions.size());
		logger.debug("@UserService#deleteUser ManualMerchantTransaction List Size when delete the user : " + manualMerchantTransactions.size());
		List<ManualNonActivityTransaction> manualNonActivityTransactions=manualNonActivityTransactionRepository.findByUserCreate(user.getUserId().toString());
		logger.debug("@UserService#deleteUser ManualNonActivityTransactions List Size when delete the user : " + manualNonActivityTransactions.size());
		logger.debug("@UserService#deleteUser ManualNonActivityTransactions List Size when delete the user : " + manualNonActivityTransactions.size());
		List<NonActivityFeeQuery> activityFeeQueries=nonActivityFeeQueryRepository.findByUserCreate(user.getUserId().toString());
		logger.debug("@UserService#deleteUser NonActivityFeeQuery List Size when delete the user : " + activityFeeQueries.size());
		logger.debug("@UserService#deleteUser NonActivityFeeQuery List Size when delete the user : " + activityFeeQueries.size());
		List<ActivityPackageDetail> activityPackageDetails=activityPackageDetailRepository.findByUserCreate(user.getUserId().toString());
		logger.debug("@UserService#deleteUser ActivityPackageDetail List Size when delete the user : " + activityPackageDetails.size());
		logger.debug("@UserService#deleteUser ActivityPackageDetail List Size when delete the user : " + activityPackageDetails.size());
		List<NonActivityPackageDetails> nonActivityPackageDetails=nonActivityPackageDetailsRepository.findByUserCreate(user.getUserId().toString());
		logger.debug("@UserService#deleteUser NonActivityPackageDetail List Size when delete the user : " + nonActivityPackageDetails.size());
		logger.debug("@UserService#deleteUser NonActivityPackageDetail List Size when delete the user : " + nonActivityPackageDetails.size());
		List<TransactionChargeDetails> transactionChargeDetails=transactionChargesDetailsRepository.findByUserCreate(user.getUserId().toString());
		logger.debug("@UserService#deleteUser TransactionChargeDetail List Size when delete the user : " + transactionChargeDetails.size());
		logger.debug("@UserService#deleteUser TransactionChargeDetail List Size when delete the user : " + transactionChargeDetails.size());
		List<ActivityPackageTier> activityPackageTiers=activityPackageTierRepository.findByUserCreate(user.getUserId().toString());
		logger.debug("@UserService#deleteUser ActivityPackageTier List Size when delete the user : " + activityPackageTiers.size());
		logger.debug("@UserService#deleteUser ActivityPackageTier List Size when delete the user : " + activityPackageTiers.size());
		List<Terminal> terminals=terminalRepository.findByUserCreate(user.getUserId().toString());
		logger.debug("@UserService#deleteUser Terminal List Size when delete the user : " + terminals.size());
		logger.debug("@UserService#deleteUser Terminal List Size when delete the user : " + terminals.size());
		List<PaymentAccount> paymentAccounts=paymentAccountRepository.findByCreatedBy(user.getUserId().toString());
		logger.debug("@UserService#deleteUser PaymentAccount List Size when delete the user : " + paymentAccounts.size());
		logger.debug("@UserService#deleteUser PaymentAccount List Size when delete the user : " + paymentAccounts.size());
		List<Contact> contacts=contactRepository.findByUserCreate(user.getUserId().toString());
		logger.debug("@UserService#deleteUser Contacts List Size when delete the user : " + contacts.size());
		logger.debug("@UserService#deleteUser Contacts List Size when delete the user : " + contacts.size());
		List<Address> addresses=addressRepository.findByUserCreate(user.getUserId().toString());
		logger.debug("@UserService#deleteUser Addresses List Size when delete the user : " + addresses.size());
		logger.debug("@UserService#deleteUser Addresses List Size when delete the user : " + addresses.size());
		
		if((!countries.isEmpty()) || (!currencies.isEmpty()) || (!mccLists.isEmpty()) || (!cardSchemes.isEmpty()) || (!terminalTypes.isEmpty()) || (institutions.size()>0) || (employees.size()>0) || (currencyConversions.size()>0) || (currencyRates.size()>0) || (transactionGroups.size()>0) || (activityPackages.size()>0) || (nonActivityPackages.size()>0) || (entities.size()>0) || (defaultTransactionIds.size()>0) || (systemCodes.size()>0) || (acquiringTransactions.size()>0) || (manualMerchantTransactions.size()>0) || (manualNonActivityTransactions.size()>0) || (activityFeeQueries.size()>0) || (activityPackageDetails.size()>0) || (nonActivityPackageDetails.size()>0) || (transactionChargeDetails.size()>0) || (activityPackageTiers.size()>0) || (terminals.size()>0) || (paymentAccounts.size()>0) || (contacts.size()>0) || (addresses.size()>0)) {
			throw new BusinessException(ResponseCode.REFERENCE_EXISTS,
					HttpStatus.NOT_FOUND);
		}
		
		List<Role> role=roleRepository.findByCreatedBy(user);
		if(role.size()>0) {
			throw new BusinessException(ResponseCode.CFG_INVALID_ROLE,
					HttpStatus.NOT_FOUND);
		}
		
		List<UserRole> userRole= userRoleRepository.findByCreatedBy(user);
		if(userRole.size()>0) {
			throw new BusinessException(ResponseCode.CFG_INVALID_USER,
					HttpStatus.NOT_FOUND);
		}

		if (makerCheckerEngine.processIfRequired(userId, this.getClass().getName(), new Object() {
		}
				.getClass()
				.getEnclosingMethod()
				.getName(), "")) {
			return;
		}

		for (UserInstitutionMapping userInstitutionMapping : userInstitutionMappings) {
			UserInstitutionMapping userInstitutionMapping2 = userInstitutionMappingRepository
					.findById(userInstitutionMapping.getMappingId())
					.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_USER_INSTITUTION_MAPPING,
							HttpStatus.NOT_FOUND));

			userInstitutionMappingRepository.delete(userInstitutionMapping2);
		}

		for(UserRole userRole2:user.getUserRoles()) {
			UserRole userRole1 = userRoleRepository
					.findById(userRole2.getUserRoleId())
					.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_ROLE,
							HttpStatus.NOT_FOUND));
			
			userRoleRepository.deleteById(userRole1.getUserRoleId());
		}
		
		
		Optional<UserAccess> userAccess=userAccessRepository.findByUser(user);
		if(!userAccess.isEmpty()) {
			userAccessRepository.delete(userAccess.get());
		}
	
		
	//	UserRole userRole= userRoleRepository.findByUser(user);

		userRepository.delete(user);
	}

	/*
	 * Changes a user password in field PASSWORD in table MD_ENT_USER based on the
	 * user id Last password change field will be updated
	 */
	public void changePassword(ChangePasswordRequestDto changePasswordRequestDTO) {
		// Branch branch = branchRepository.findById(branchId).orElseThrow(()-> new
		// BusinessException(ResponseCode.CFG_INVALID_BRANCH, HttpStatus.NOT_FOUND));
		UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		User user = userRepository.findById(changePasswordRequestDTO.getUserId())
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_USER, HttpStatus.NOT_FOUND));
		// PasswordPolicy passwordPolicy =
		// passwordPolicyRepository.findByInstitution(user.getBranch().getInstitution()).orElseThrow(()->
		// new BusinessException(ResponseCode.CFG_INVALID_PASSWORD_POLICY,
		// HttpStatus.NOT_FOUND));

		if (!passwordEncoder.matches(changePasswordRequestDTO.getOldPassword().trim(), user.getPassword())) {
			throw new BusinessException(ResponseCode.CFG_INVALID_OLD_PASSWORD, HttpStatus.BAD_REQUEST);
		}

		// Validations.isValidPassword(passwordPolicy,
		// changePasswordRequestDTO.getNewPassword(), user.getPasswordHistory(),
		// passwordEncoder);

		user.setPassword(passwordEncoder.encode(changePasswordRequestDTO.getNewPassword()));
		// user.setLastPasswordChange(new Timestamp(System.currentTimeMillis()));

		if (makerCheckerEngine.processIfRequired(changePasswordRequestDTO, this.getClass().getName(), new Object() {
		}
				.getClass()
				.getEnclosingMethod()
				.getName(), "")) {
			return;
		}

		userRepository.save(user);

		// passwordHistoryService.savePasswordHistory(passwordPolicy.getPasswordHistory(),
		// user, passwordEncoder.encode(changePasswordRequestDTO.getNewPassword()),
		// this.getEntityUserById(userDetails.getId()));
		// backEndLogService.logBackEndActivity(this.getEntityUserById(userDetails.getId()),
		// LoggingCategoriesEnum.CONFIGURATION.getValue(), ("User-change-password"),
		// "Change password for User [" + user.getUserId() + " - " + user.getUsername()
		// + "]", remoteAddress, "");
	}

	/*
	 * Resets a user password in field PASSWORD in table MD_ENT_USER based on the
	 * user id Last password change field will be updated
	 */
	public String resetPassword(String remoteAddress, String username, HttpServletRequest request) {

		Optional<User> loggedInUser = Optional.empty();

		try{
			String authorizationHeader = request.getHeader("Authorization");

			if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
				loggedInUser = userRepository.findByUsername(jwtUtils.getUserNameFromJwtToken(authorizationHeader.substring(7)));
			}
		} catch(Exception ex){
			logger.error("@UserService#resetPassword  {}",ex);
			loggedInUser = Optional.empty();
		}

		// Branch branch = branchRepository.findById(branchId).orElseThrow(()-> new
		// BusinessException(ResponseCode.CFG_INVALID_BRANCH, HttpStatus.NOT_FOUND));
		// UserDetailsImpl userDetails = (UserDetailsImpl)
		// SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_USER_NAME, HttpStatus.NOT_FOUND));

		// PasswordPolicy passwordPolicy =
		// passwordPolicyRepository.findByInstitution(user.getBranch().getInstitution()).orElseThrow(()->
		// new BusinessException(ResponseCode.CFG_INVALID_PASSWORD_POLICY,
		// HttpStatus.NOT_FOUND));

		String clearPassword = PasswordGenerator.generatePassword(10);
		String encryptedPassword = bCryptPasswordEncoder.encode(clearPassword);

		user.setPassword(encryptedPassword);
		// user.setLastPasswordChange(new Timestamp(System.currentTimeMillis()));
		// user.setUpdatedBy(getEntityUserById(userDetails.getId()));
		user.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
		if(loggedInUser.isPresent()){
			user.setUpdatedBy(loggedInUser.get());
		}
 		userRepository.save(user);

		// send email
		
		emailSenderService.sendMail(fromEmail,user.getEmail(), "MAS Prime Product - User Creation",
				"Dears, \n\nYour MAS Prime Product account password is " + clearPassword  + "\n\nRegards,\nACS");
//		emailSenderService.sendSimpleEmail(fromEmail,user.getEmail(), "MAS Prime Product - User Reset Password",
//				"Dears, \n\nYour MAS Prime Product account password is " + clearPassword + "\n\nRegards,\nACS");

		return "if the username exists, you will receive an email with a link to reset your password";

		// passwordHistoryService.savePasswordHistory(passwordPolicy.getPasswordHistory(),
		// user, clearPassword, this.getEntityUserById(userDetails.getId()));
		// backEndLogService.logBackEndActivity(this.getEntityUserById(userDetails.getId()),
		// LoggingCategoriesEnum.CONFIGURATION.getValue(), ("User-reset-password"),
		// "Reset password for User [" + user.getUserId() + " - " + user.getUsername() +
		// "]", remoteAddress, "", branch.getInstitution(), branch);
	}

	/*
	 * Enables/disables a user by changing the status
	 */
	public String changeUserStatus(ChangeStatusRequestDto changeUserRequestDTO) {

		UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		User user = userRepository.findById(changeUserRequestDTO.getId())
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_USER, HttpStatus.NOT_FOUND));

		if (Character.compare(changeUserRequestDTO.getStatus().charAt(0), user.getStatus()) == 0) {
			throw new BusinessException(ResponseCode.CFG_USER_STATUS_NOT_CHANGED, HttpStatus.CONFLICT);
		}

		Timestamp currentDate = new Timestamp(System.currentTimeMillis());

		if (makerCheckerEngine.processIfRequired(changeUserRequestDTO, this.getClass().getName(), new Object() {
		}
				.getClass()
				.getEnclosingMethod()
				.getName(), "")) {
			return null;
		}

		userRepository.updateUserStatus(changeUserRequestDTO.getStatus().charAt(0), changeUserRequestDTO.getId(),
				currentDate,Integer.valueOf(userDetails.getId()));

		return "Status changed successfully";
	}

	public String unBlockUser(UnBlockUserRequestDto unBlockUserRequestDto) {
		User user = userRepository.findById(unBlockUserRequestDto.getUserId())
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_USER, HttpStatus.NOT_FOUND));

		if (user.getStatus() != '3') {
			throw new BusinessException(ResponseCode.CFG_INVALID_USER, HttpStatus.CONFLICT);
		}

		Timestamp currentDate = new Timestamp(System.currentTimeMillis());
		UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();

		if (makerCheckerEngine.processIfRequired(unBlockUserRequestDto, this.getClass().getName(), new Object() {
		}
				.getClass()
				.getEnclosingMethod()
				.getName(), "")) {
			return null;
		}

		userRepository.updateUserStatusAndPasswordRetries('1', unBlockUserRequestDto.getUserId(),
				currentDate, 0,Integer.valueOf(userDetails.getId()).toString());

		return "User unblocked successfully";
	}

	public User getEntityUserById(int userId) {
		return userRepository.findById(userId).get();
	}

	/*
	 * public int updateLastLoginDate(int userId) { return
	 * userRepository.updateLastLoginDate(userId); }
	 */
	
	public Optional<User> findUserByEmail(String email) {
		return userRepository.findByEmailEqualsIgnoreCase(email.toUpperCase());
	}

	public Optional<User> findUserById(int userId) {
		return userRepository.findById(userId);
	}

	public User findByUsername(String username) {
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_USER, HttpStatus.NOT_FOUND));
	}

	public boolean checkUserDefaultRole(int userId) {

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_USER, HttpStatus.NOT_FOUND));
		Institution institution = institutionRepository.findById(user.getInstitution1().getInstitutionId())
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_INST, HttpStatus.NOT_FOUND));
		
		System.err.println(institution.getInstitutionId());
		UserRole userRole = userRoleRepository.findByUserAndInstitution(user, institution);
		if(Objects.nonNull(userRole)) {
			Role role = roleRepository.findById(userRole.getRole().getRoleId())
					.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_ROLE, HttpStatus.NOT_FOUND));
			if (role.getStatus() == "1".charAt(0)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean checkUserDefaultInstitution(int userId) {

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_USER, HttpStatus.NOT_FOUND));
		Institution institution = institutionRepository.findById(user.getInstitution1().getInstitutionId())
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_INST, HttpStatus.NOT_FOUND));
        return institution.getStatus().equals('1');
	}
//	public Optional<User> getLoggedInUser(){
//		Optional<Integer> userId = commonService.getLoggedInUser().getId();
//		return userRepository.findByUserId(userId.isPresent()?Integer.parseInt(userId.get()+""):-1);
//	}

}