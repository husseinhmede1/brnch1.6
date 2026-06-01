package com.mdsl.controller;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.mdsl.model.entity.*;
import com.mdsl.repository.SystemCodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mdsl.config.JwtUtils;
import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.LoginRequestDto;
import com.mdsl.model.dto.response.InstitutionResponseInfoDto;
import com.mdsl.model.dto.response.JwtResponseDto;
import com.mdsl.model.dto.response.UserNewResponseDto;
import com.mdsl.model.dto.response.UserResponseDto;
import com.mdsl.model.entity.Institution;
import com.mdsl.model.entity.User;
import com.mdsl.model.entity.UserAccess;
import com.mdsl.model.entity.UserInstitutionMapping;
import com.mdsl.repository.InstitutionRepository;
import com.mdsl.repository.UserInstitutionMappingRepository;
import com.mdsl.repository.UserRepository;
import com.mdsl.service.UserAccessService;
import com.mdsl.service.UserDetailsImpl;
import com.mdsl.service.UserService;
import com.mdsl.utils.HttpUtils;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.Validations;
import com.mdsl.utils.enumerations.UserStatusEnum;

@RestController
@CrossOrigin
public class JwtAuthenticationController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private AuthenticationManager authenticationManager;
	private UserService userService;
	private UserAccessService userAccessService;
	private UserRepository userRepository;
	private JwtUtils jwtUtils;
	private SystemCodeRepository systemCodeRepository;

	@Autowired
	private UserInstitutionMappingRepository userInstitutionMappingRepository;

	@Autowired
	private InstitutionRepository institutionRepository;

	@Autowired
	public JwtAuthenticationController(AuthenticationManager authenticationManager, UserService userService,
			UserRepository userRepository, UserAccessService userAccessService, JwtUtils jwtUtils, SystemCodeRepository systemCodeRepository) {
		super();
		this.authenticationManager = authenticationManager;
		this.userService = userService;
		this.userRepository = userRepository;
		this.userAccessService = userAccessService;
		this.jwtUtils = jwtUtils;
		this.systemCodeRepository = systemCodeRepository;
	}

	@PostMapping("/authenticate")
	public ResponseEntity authenticateUser(@Valid @RequestBody LoginRequestDto loginRequest,
			BindingResult bindingResult, HttpServletRequest request) {
		Validations.validate(bindingResult);
		Authentication authentication;
		Optional<User> userByName = userRepository.findByUsername(loginRequest.getUsername());
		if (userByName.isPresent() && String.valueOf(userByName.get().getStatus()).equals("3")) {
			//return new ResponseEntity<>(new JwtResponseDto(), HttpStatus.UNAUTHORIZED);
			throw new BusinessException(ResponseCode.USER_BLOCKED, HttpStatus.UNAUTHORIZED);
		}
		try {
			authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
			logger.debug("authenticateUser - Principal: " + authentication.getPrincipal());
		} catch (Exception e) {
			Optional<SystemCode> systemCode = systemCodeRepository.findByCodePrefixAndCodeSuffixAndInstitution_InstitutionId("USER", "PASSWORD_RETRIES", "1");
			if (userByName.isPresent() && systemCode.isPresent()) {
				if (Integer.valueOf(systemCode.get().getCodeValue()).compareTo(userByName.get().getPassRetries() + 1) <= 0) {
					userRepository.updateUserStatus('3', userByName.get().getUserId(), new Timestamp(System.currentTimeMillis()),null);
				}
				userRepository.incrementPasswordRetries(userByName.get().getUserId());
			}
			logger.error("authenticateUser - An error occurred", e);
			return new ResponseEntity<JwtResponseDto>(new JwtResponseDto(), HttpStatus.UNAUTHORIZED);
		}
		SecurityContextHolder.getContext().setAuthentication(authentication);

		logger.debug("authenticateUser - Generate Token");
		String jwt = jwtUtils.generateJwtToken(authentication, false);
		logger.debug("authenticateUser - Token: " + jwt);

		String refreshJwt = jwtUtils.generateJwtToken(authentication, true);
		logger.debug("authenticateUser - Refresh Token: " + refreshJwt);

		// Fetch User object and return it in JwtResponse
		logger.debug("authenticateUser - Principal: " + authentication.getPrincipal());
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		User user = userService.getEntityUserById(userDetails.getId());
		
		boolean checkRole = userService.checkUserDefaultRole(userDetails.getId());
		if (!checkRole) {
			logger.debug("role disabled");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Default role is disabled");
		}
		else {
			logger.debug("role not disabled");
		}
		
		boolean checkInst = userService.checkUserDefaultInstitution(userDetails.getId());
		if (!checkInst) {
			logger.debug("inst disabled");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Default institution is disabled");
		}
		else {
			logger.debug("inst not disabled");
		}

		// Do login activities (last login date, status, ...)
		String sourceIp = HttpUtils.getRequestIP(request);
		/*
		 * logger.trace("INSTITUTION CONFIG: DaysToChangePassword[" +
		 * user.getBranch().getInstitution().getDaysToChangePassword() +
		 * "] - WarningChangePassword[" +
		 * user.getBranch().getInstitution().getWarningChangePassword() +
		 * "] - DaysToLockUser[" + user.getBranch().getInstitution().getDaysToLockUser()
		 * + "]");
		 */
		long nbDaysLastLogin = Duration.between(user.getLastLoginDate().toLocalDateTime().toLocalDate().atStartOfDay(),
				LocalDate.now().atStartOfDay()).toDays();
		logger.trace("nbDaysLastLogin[" + nbDaysLastLogin + "]");

		/*
		 * long nbDaysLastPassChange =
		 * Duration.between(user.getLastPasswordChange().toLocalDateTime().toLocalDate()
		 * .atStartOfDay(), LocalDate.now().atStartOfDay()).toDays();
		 * logger.trace("nbDaysLastPassChange[" + nbDaysLastPassChange + "]");
		 */
		long nbDaysToChangePassword = 0;

		/*
		 * if(user.getStatus() == UserStatusEnum.ENABLED.getValue()) {
		 * if(nbDaysLastLogin >= user.getBranch().getInstitution().getDaysToLockUser())
		 * { logger.debug("No login period exceeded - User status changed to " +
		 * UserStatusEnum.DISABLED.getValue() + " (blocked)");
		 * userRepository.updateUserStatus(UserStatusEnum.DISABLED.getValue(),
		 * user.getUserId()); throw new BusinessException(ResponseCode.VAL_USER_BLOCKED,
		 * HttpStatus.UNAUTHORIZED); }
		 * 
		 * if(nbDaysLastPassChange >=
		 * user.getBranch().getInstitution().getDaysToChangePassword()) {
		 * logger.debug("Expired password - User status changed to " +
		 * UserStatusEnum.FORCE_CHANGE_PASSWORD.getValue() +
		 * " (Force change password)");
		 * userRepository.updateUserStatus(UserStatusEnum.FORCE_CHANGE_PASSWORD.getValue
		 * (), user.getUserId()); throw new
		 * BusinessException(ResponseCode.VAL_USER_PASSWORD_EXPIRY,
		 * HttpStatus.UNAUTHORIZED); }else { nbDaysToChangePassword =
		 * user.getBranch().getInstitution().getDaysToChangePassword() -
		 * nbDaysLastPassChange; logger.debug(nbDaysToChangePassword +
		 * " days to change password"); } }else
		 */
		if (user.getStatus() == UserStatusEnum.FORCE_CHANGE_PASSWORD_ENABLED.getValue()) {
			throw new BusinessException(ResponseCode.USR_USER_CHANGE_PASS, HttpStatus.UNAUTHORIZED);
		} else if (user.getStatus() == UserStatusEnum.DISABLED.getValue()) {
	//		throw new BusinessException(ResponseCode.VAL_USER_BLOCKED, HttpStatus.UNAUTHORIZED);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User is disabled");
		}

		logger.trace("updateLastLoginDate");
		// userService.updateLastLoginDate(user.getUserId());

		UserAccess userAccess = UserAccess.builder().user(user).loginDate(new Timestamp(System.currentTimeMillis()))
				.jwt(jwt).refreshJwt(refreshJwt).sourceIp(sourceIp).build();

		userAccessService.saveUserAccess(userAccess);

//		UserResponseDto userResponseDTO = userService.getUserById(user.getUserId());
		UserNewResponseDto userResponseDTO = userService.getUserById(user.getUserId());
		
		// userResponseDTO.setNbDaysToChangePassword(nbDaysToChangePassword);
		// userResponseDTO.setWarnToChangePassword(nbDaysToChangePassword <=
		// user.getBranch().getInstitution().getWarningChangePassword() ? '1' : '0');

		List<UserInstitutionMapping> institutionMappings = userInstitutionMappingRepository.findByUser(user);
		List<InstitutionResponseInfoDto> institutionResponseInfoDtos = new ArrayList<InstitutionResponseInfoDto>();
		for (UserInstitutionMapping userInstitutionMapping : institutionMappings) {
			Institution institution = institutionRepository
					.findById(userInstitutionMapping.getInstitution().getInstitutionId()).orElseThrow(
							() -> new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
			InstitutionResponseInfoDto institutionResponseInfoDto = new InstitutionResponseInfoDto();
			institutionResponseInfoDto.setInstitutionId(institution.getInstitutionId());
			institutionResponseInfoDto.setInstitutionName(institution.getInstitutionName());
			institutionResponseInfoDto.setInstitutionTypeAlt(institution.getInstitutionTypeAlt());
			institutionResponseInfoDto.setStatus(institution.getStatus());
			institutionResponseInfoDtos.add(institutionResponseInfoDto);
		}

		userResponseDTO.setInstitution(institutionResponseInfoDtos);

		JwtResponseDto response = JwtResponseDto.builder().token(jwt).refreshToken(refreshJwt).type("Bearer")
				.user(userResponseDTO).build();

		return new ResponseEntity<JwtResponseDto>(response, HttpStatus.OK);
	}

	@PostMapping("/refresh-token")
	public ResponseEntity<JwtResponseDto> refreshAuthenticateUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		logger.debug("refreshAuthenticateUser - New Token");
		String jwt = jwtUtils.generateJwtToken(authentication, false);
		UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
		Optional<User> user=userRepository.findById(userPrincipal.getId());
		UserNewResponseDto userResponseDTO=new UserNewResponseDto();
		if(user.isPresent()) {
			userResponseDTO = userService.getUserById(user.get().getUserId());
		}
		
		userAccessService.updateToken(userPrincipal.getId(), jwt);
		logger.debug("refreshAuthenticateUser - New Token from refreshToken: " + jwt);
		return new ResponseEntity<JwtResponseDto>(JwtResponseDto.builder().token(jwt).user(userResponseDTO).build(), HttpStatus.OK);
	}

	@PostMapping("/logout")
	public void logout() {
		UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		logger.debug("logout - User ID: " + userDetails.getId());
	}
}
