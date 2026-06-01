package com.mdsl.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import com.mdsl.utils.ResponseCode;
import lombok.RequiredArgsConstructor;
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
import com.mdsl.model.dto.request.MCCListRequestDto;
import com.mdsl.model.dto.response.MCCListResponseDto;
import com.mdsl.model.dto.response.PaginationResponseDto;
import com.mdsl.service.MCCListService;
import com.mdsl.utils.Validations;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/mcc")
@RequiredArgsConstructor
public class MCCListController {

	private static final Logger logger = LoggerFactory.getLogger(MCCListController.class);
	private final MCCListService mccListService;

	@GetMapping
	public ResponseEntity<List<MCCListResponseDto>> getAllMccList() {
		return ResponseEntity.ok(mccListService.fetchAllMccList());
	}

	@PostMapping("/getMCCList")
	public ResponseEntity<PaginationResponseDto> getAllMccListPagination(
			@Valid @RequestBody MCCListRequestDto mccListRequestDto,BindingResult bindingResult) {
		Validations.validate(bindingResult);
		ResponseEntity<PaginationResponseDto> responseEntity = null;
		try {
			responseEntity = mccListService.fetchAllMccList(mccListRequestDto);

		} catch (Exception e) {
			e.printStackTrace();
			responseEntity = new ResponseEntity<>(new PaginationResponseDto(false, null, null, null, null),
					HttpStatus.OK);
		}
		return responseEntity;

	}

	@GetMapping("/{id}")
	public ResponseEntity<MCCListResponseDto> getMccListById(@PathVariable("id") int id) {
		return ResponseEntity.ok(mccListService.fetchMccListById(id));
	}

	@PostMapping
	public ResponseEntity saveOrUpdateMccList(@Valid @RequestBody MCCListRequestDto mccListRequestDto, BindingResult bindingResult) {
		Validations.validate(bindingResult);
		try {
			return ResponseEntity.ok(mccListService.saveOrUpdateMccList(mccListRequestDto));
		} catch (BusinessException e) {
			logger.error("@MCCListController#saveOrUpdateMCC - business exception "+e.toString());
			throw new BusinessException(e.getMessage(), e.getHttpStatus());
		}
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteMccList(@PathVariable("id") int id) {
		try {
			mccListService.deleteMccListById(id);
			String message = "An item is deleted with id : " + id;
			return ResponseEntity.ok(message);
		} catch (BusinessException ex) {
			logger.error("@MCCListController#deleteMccList-business exception " + ex.toString());
			throw new BusinessException(ex.getMessage(), ex.getHttpStatus());
		} catch (Exception ex) {
			logger.error("@MCCListController#deleteMccList-generic exception " + ex.toString());
			throw new BusinessException(ResponseCode.VAL_ERROR_OCCURRED, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@PostMapping("/search")
	public ResponseEntity<PaginationResponseDto> getMCCBySearch(HttpServletRequest httpServletRequest,@RequestBody MCCListRequestDto mccListRequestDto,BindingResult bindingResult) {
		Validations.validate(bindingResult);
		ResponseEntity<PaginationResponseDto> responseEntity = null;
		try {
			responseEntity = mccListService.getMCCBySearch(mccListRequestDto);
		} catch (Exception e) {
			e.printStackTrace();
			responseEntity = new ResponseEntity<>(new PaginationResponseDto(false, null, null, null, null),
					HttpStatus.OK);
		}
		return responseEntity;
	}
}