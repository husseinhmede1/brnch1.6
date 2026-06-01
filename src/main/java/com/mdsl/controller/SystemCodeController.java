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
import com.mdsl.model.dto.request.ChangeStatusRequestDto;
import com.mdsl.model.dto.request.SystemCodeRequestDto;
import com.mdsl.model.dto.request.SystemCodeUniqueRequestDto;
import com.mdsl.model.dto.request.SystemOutputFileTemplateRequestDto;
import com.mdsl.model.dto.response.SystemCodeResponseDto;
import com.mdsl.model.mapper.SystemCodeMapper;
import com.mdsl.repository.SystemCodeRepository;
import com.mdsl.service.SystemCodeService;
import com.mdsl.utils.Validations;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/system-code")
@RequiredArgsConstructor
public class SystemCodeController {

	private static final Logger logger = LoggerFactory.getLogger(SystemCodeController.class);

	private final SystemCodeService systemCodeService;

	@GetMapping
	public ResponseEntity<List<SystemCodeResponseDto>> getAllSystemCodes() {
		return ResponseEntity.ok(systemCodeService.fetchAllSystemCodes());
	}

	@GetMapping("/{id}")
	public ResponseEntity<SystemCodeResponseDto> getSystemCodeById(@PathVariable("id") int id) {
		return ResponseEntity.ok(systemCodeService.fetchSystemCodeById(id));
	}

	@GetMapping("/inst/{instId}")
	public ResponseEntity<List<SystemCodeResponseDto>> getSystemCodesByInstitution(@PathVariable("instId") String instId) {
		return ResponseEntity.ok(systemCodeService.getSystemCodesByInstitution(instId));
	}

	@GetMapping({ "/{prefix}/{instId}" })
    public ResponseEntity<List<SystemCodeResponseDto>> fetchSystemCodeByPrefixAndInstitutionId(@PathVariable("prefix") final String prefix, @PathVariable("instId") final String instId) {
        return (ResponseEntity<List<SystemCodeResponseDto>>)ResponseEntity.ok(this.systemCodeService.fetchSystemCodeByPrefixAndInstitutionId(prefix, instId));
    }
	
	@PostMapping({ "/details" })
    public ResponseEntity<List<SystemCodeResponseDto>> getSystemCodesByPrefixAndValue(@Valid @RequestBody SystemOutputFileTemplateRequestDto systemOutputFileTemplateRequestDto, BindingResult bindingResult) {
		Validations.validate(bindingResult);
		return (ResponseEntity<List<SystemCodeResponseDto>>)ResponseEntity.ok(this.systemCodeService.getSystemCodesByPrefixAndValueUpdated(systemOutputFileTemplateRequestDto));
    }
	
	@PostMapping({ "/unique" })
    public ResponseEntity<SystemCodeResponseDto> getSystemCodesByUniqueFields(@Valid @RequestBody SystemCodeUniqueRequestDto systemCodeUniqueRequestDto, BindingResult bindingResult) {
		Validations.validate(bindingResult);
		return (ResponseEntity<SystemCodeResponseDto>)ResponseEntity.ok(this.systemCodeService.getSystemCodesByUniqueFields(systemCodeUniqueRequestDto));
    }
	
	
	@PostMapping
	public ResponseEntity<String> saveOrUpdateSystemCode(@Valid @RequestBody SystemCodeRequestDto systemCodeRequestDto, BindingResult bindingResult) {
		Validations.validate(bindingResult);
		try {
			 ResponseEntity.ok(systemCodeService.saveOrUpdateSystemCode(systemCodeRequestDto));
			 return null;
		} catch(BusinessException e) {
			logger.error("@SystemCodeController#saveOrUpdateSystemCode - BusinessException -"+e.toString());
			throw new BusinessException(e.getMessage(),e.getHttpStatus());
		} catch(Exception e){
			logger.error("@SystemCodeController#saveOrUpdateSystemCode - Exception -"+e.toString());
			throw new BusinessException(ResponseCode.CANNOT_SAVE_UDPATE_SYSTEM_CODE,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteSystemCode(@PathVariable("id") int id) {
		try {
			systemCodeService.deleteSystemCodeById(id);
			String message = "An item is deleted with id : " + id;
			return ResponseEntity.ok(message);
		} catch(BusinessException ex){
			logger.error("@SystemCodeController#deleteSystemCode - BusinessException -"+ex.toString());
			throw new BusinessException(ex.getMessage(),ex.getHttpStatus());
		} catch(Exception ex){
			logger.error("@SystemCodeController#deleteSystemCode - Exception -"+ex.toString());
			throw new BusinessException(ResponseCode.CANNOT_SAVE_UDPATE_SYSTEM_CODE,HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("/prefix-suffix")
	public ResponseEntity<List<SystemCodeResponseDto>> getSystemCodeByPrefixAndSuffix(@Valid @RequestBody 
			SystemCodeRequestDto systemCodeRequestDto, BindingResult bindingResult)
	{
		Validations.validate(bindingResult);
		return ResponseEntity.ok(systemCodeService.fetchSystemCodeByPrefixAndSuffix(systemCodeRequestDto));
	}

	@PostMapping("/status-change")
	public void changeSystemCodeStatus(@Valid @RequestBody ChangeStatusRequestDto changeInstitutionStatusRequestDTO,
			BindingResult bindingResult, HttpServletRequest request) {
		 Validations.validate(bindingResult);
		systemCodeService.changeStatus(changeInstitutionStatusRequestDTO);
	}
	
	@GetMapping("/active-systemcode")
	public ResponseEntity<List<SystemCodeResponseDto>> getActiveSystemCodes() {
		return ResponseEntity.ok(systemCodeService.getActiveSystemCode());
	}

	@GetMapping("/hold-type")
	public ResponseEntity<List<SystemCodeResponseDto>> getHoldTypes() {
        return ResponseEntity.ok(systemCodeService.getHoldTypes());
    }

}
