package com.mdsl.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;

import com.mdsl.model.dto.request.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.coyote.Response;
import org.hibernate.exception.JDBCConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.ChangePasswordRequestDto;
import com.mdsl.model.dto.request.ChangeStatusRequestDto;
import com.mdsl.model.dto.request.ResetPasswordRequestDto;
import com.mdsl.model.dto.request.UserRequestDto;
import com.mdsl.model.dto.response.UserInfoResponseDto;
import com.mdsl.model.dto.response.UserNewResponseDto;
import com.mdsl.model.dto.response.UserResponseDto;
import com.mdsl.service.UserService;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.Validations;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@Transactional(rollbackOn = Exception.class)
@RequestMapping("/users")
public class UserController {

	private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@PostMapping("/user")
	public ResponseEntity<UserNewResponseDto> getUserById(@RequestBody GetUserByIdRequestDto getUserByIdRequestDto) {
		return ResponseEntity.ok(userService.getUserByIdForUserScreen(getUserByIdRequestDto));
	}

	@GetMapping("/institution-id")
	public ResponseEntity<List<UserInfoResponseDto>> getUsersByInstitutionId(HttpServletRequest request) {
		return ResponseEntity.ok(userService.getUsersByInstitutionId(request.getHeader("instId")));
	}

	@GetMapping("/{instId}/users")
	public ResponseEntity<List<UserInfoResponseDto>> getUsersByInstitution(@PathVariable("instId") String instId) {
		return ResponseEntity.ok(userService.getUsersByInstitutionId(instId));
	}

	@GetMapping
	public ResponseEntity<List<UserInfoResponseDto>> getAllUsers() {
		return ResponseEntity.ok(userService.getAllUsers());
	}

	@PostMapping
	public ResponseEntity saveUser(@Valid @RequestBody UserRequestDto user, BindingResult bindingResult,HttpServletRequest request) {
		Validations.validate(bindingResult);
        try{
            return ResponseEntity.ok(userService.saveUser(user));
        } catch(BusinessException ex){
            logger.error("@UserController#saveUser-business exception "+ex.toString());
            throw new BusinessException(ex.getMessage(),ex.getHttpStatus());
		} catch(Exception ex){
            logger.error("@UserController#saveUser-generic exception "+ex.toString());
            throw new BusinessException(ResponseCode.VAL_ERROR_OCCURRED,HttpStatus.INTERNAL_SERVER_ERROR);
        }
	}

	@DeleteMapping("/{id}")
	public ResponseEntity deleteUser(@PathVariable(value = "id") int userId, HttpServletRequest request) {
		  try {
	         userService.deleteUser(userId);
			 return ResponseEntity.ok("");
		  }catch(BusinessException ex) {
			  logger.error("@UserController#deleteUser-business exception "+ex.toString());
			  return ResponseEntity.status(ex.getHttpStatus()).body(ex.getMessage());
		  } catch(Exception ex){
			  logger.error("@UserController#deleteUser-generic exception "+ex.toString());
			  return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.toString());
		  }
	}

	@PostMapping("/password-change")
	public void changePassword(@Valid @RequestBody ChangePasswordRequestDto changePasswordRequestDTO,BindingResult bindingResult, HttpServletRequest request) {
		Validations.validate(bindingResult);
		userService.changePassword(changePasswordRequestDTO);
	}

	@PostMapping("/password-reset")
	public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequestDto resetPasswordRequestDto,
			BindingResult bindingResult, HttpServletRequest request) {
		try {
			Validations.validate(bindingResult);
			return ResponseEntity.ok(userService.resetPassword(request.getRemoteAddr(), resetPasswordRequestDto.getUserName(),request));
		}catch(BusinessException ex) {
            logger.error("@UserController#resetPassword-business exception "+ ex);
            return ResponseEntity.ok("if the username exists, you will receive an email with a link to reset your password");
		} catch(Exception ex){
            logger.error("@UserController#resetPassword-generic exception "+ex.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.toString());
        }
	}

	@PostMapping("/status-change")
	public ResponseEntity<String> changeUserStatus(@Valid @RequestBody ChangeStatusRequestDto changeUserStatusRequestDTO,BindingResult bindingResult, HttpServletRequest request) {
		Validations.validate(bindingResult);
		changeUserStatusRequestDTO.setRemoteAddress(request.getRemoteAddr());
		return ResponseEntity.ok(userService.changeUserStatus(changeUserStatusRequestDTO));
	}

	@PostMapping("/unblock")
	public ResponseEntity<String> unBlockUser(@RequestBody UnBlockUserRequestDto unBlockUserRequestDto, BindingResult bindingResult) {
		Validations.validate(bindingResult);
		return ResponseEntity.ok(userService.unBlockUser(unBlockUserRequestDto));
	}
}
