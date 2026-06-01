package com.mdsl.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.mdsl.model.dto.request.MerchantTypeRequestDto;
import com.mdsl.model.dto.response.MerchantTypeResponseDto;
import com.mdsl.model.entity.MerchantType;
import com.mdsl.repository.MerchantTypeRepository;
import com.mdsl.service.MerchantTypeService;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.Validations;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/merchanttype")
public class MerchantTypeController {
	private static final Logger logger = LoggerFactory.getLogger(MerchantTypeController.class);

	@Autowired
	private MerchantTypeService merchantTypeService;
	
	@GetMapping
	public ResponseEntity<List<MerchantTypeResponseDto>> getAllMerchantType()
	{
		return ResponseEntity.ok(merchantTypeService.fetchAllMerchantType());
	}

	@GetMapping("/{id}")
	public ResponseEntity<MerchantTypeResponseDto> getMerchantTypeById(@PathVariable("id") int id)
	{
		return ResponseEntity.ok(merchantTypeService.fetchMerchantTypeById(id));
	}
	
	@PostMapping
	public ResponseEntity<MerchantTypeResponseDto> saveOrUpdateMerchantType(@Valid @RequestBody 
									MerchantTypeRequestDto merchantTypeRequestDto, BindingResult bindingResult)
	{
		Validations.validate(bindingResult);
		try {
			return ResponseEntity.ok(merchantTypeService.saveOrUpdateMerchantType(merchantTypeRequestDto));
		} catch (BusinessException e) {
		    throw new BusinessException (e.getMessage(), e.getHttpStatus());
		} catch (Exception e) {
		    logger.error("@MerchantTypeController#saveOrUpdateMerchantType: " + e.getMessage());
		    throw new BusinessException (ResponseCode.MRC_MERCHANT_TYPE_NO_SAVE, HttpStatus.BAD_REQUEST);
	    }
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteMerchantType(@PathVariable("id") int id)
	{
		try {
			merchantTypeService.deleteMerchantTypeById(id);
			String message="An item is deleted with id : "+id;
			return ResponseEntity.ok(message);
		} catch (BusinessException e) {
		    throw new BusinessException (e.getMessage(), e.getHttpStatus());
		} catch (Exception e) {
		    logger.error("@MerchantTypeController#deleteMerchantType: " + e.getMessage());
		    throw new BusinessException (ResponseCode.MRC_MERCHANT_TYPE_NO_DELETE, HttpStatus.BAD_REQUEST);
	    }
	}
	
}
