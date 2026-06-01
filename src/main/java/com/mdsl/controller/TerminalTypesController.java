package com.mdsl.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
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
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.ChangeStatusRequestDto;
import com.mdsl.model.dto.response.TerminalTypeResponseDto;
import com.mdsl.model.dto.response.TerminalTypesDto;
import com.mdsl.model.entity.Terminal;
import com.mdsl.model.entity.TerminalTypes;
import com.mdsl.model.mapper.TerminalTypeMapper;
import com.mdsl.repository.TerminalTypesRepository;
import com.mdsl.service.TerminalTypesService;
import com.mdsl.utils.Validations;

@CrossOrigin(origins = "*")
@RestController
//@Transactional(rollbackOn = Exception.class)
@RequestMapping("/terminaltype")
@RequiredArgsConstructor
public class TerminalTypesController {

	private static final Logger logger = LoggerFactory.getLogger(TerminalTypesController.class);

	private final TerminalTypesService terminalTypesService;

	@PostMapping
	public ResponseEntity saveOrUpdateTerminalType(@Valid @RequestBody TerminalTypesDto terminalTypesDto,BindingResult bindingResult) {
		Validations.validate(bindingResult);
		try {
		return ResponseEntity.ok(terminalTypesService.saveOrUpdateTerminmalType(terminalTypesDto));
		}catch(BusinessException ex) {
			logger.error("@TerminalTypeController#saveOrUpdateTerminalType -BusinessException-"+ex.toString());
			throw new BusinessException(ex.getMessage(),ex.getHttpStatus());
		}catch (Exception ex) {
			logger.error("@TerminalTypeController#saveOrUpdateTerminalType -Exception-"+ex.toString());
			throw new BusinessException(ResponseCode.CFG_TERMINAL_TYPE_CANNOT_SAVE_UPDATE,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping
	public ResponseEntity viewTerminalType() {
		return ResponseEntity.ok(terminalTypesService.viewTerminalTypes());
	}

	@GetMapping("/{terminaltypeid}")
	public ResponseEntity getTerminalTypeById(@PathVariable("terminaltypeid") int terminalId) {
		return ResponseEntity.ok(terminalTypesService.getTerminalTypes(terminalId));
	}

	@DeleteMapping("/{terminaltypeid}")
	public ResponseEntity<String> deleteTerminalTye(@PathVariable("terminaltypeid") int terminalId) {
		try {
			terminalTypesService.deleteTerminalType(terminalId);		
			String message = "An item is deleted with id : " + terminalId;
			logger.info("@TerminalTypesController#deleteTerminalTye - "+message);
			return ResponseEntity.ok(message);
		} catch (BusinessException ex) {
			logger.error("@TerminalTypesController#deleteTerminalTye - BusinessException - reference exists");
			throw new BusinessException(ex.getMessage(),ex.getHttpStatus());
		} catch (Exception e) {
			logger.error("@TerminalTypesController#deleteTerminalTye - Exception - "+e.toString());
			throw new BusinessException(ResponseCode.CFG_TERMINAL_TYPE_CANNOT_DELETE,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@PostMapping("/status-change")
	public ResponseEntity<String> changeInstitutionStatus(@Valid @RequestBody ChangeStatusRequestDto changeInstitutionStatusRequestDTO,BindingResult bindingResult, HttpServletRequest request) {
		 Validations.validate(bindingResult);
		return ResponseEntity.ok(terminalTypesService.changeStatus(changeInstitutionStatusRequestDTO));
	}

	@GetMapping("/active-terminaltype")
	public ResponseEntity getActiveTerminalType() {
		return ResponseEntity.ok(terminalTypesService.getActiveTerminalType());
	}
}
