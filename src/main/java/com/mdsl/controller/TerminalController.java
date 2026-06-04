package com.mdsl.controller;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.AllTerminalsRequestDto;
import com.mdsl.model.dto.request.ChangeStatusRequestDto;
import com.mdsl.model.dto.request.DeleteTerminalRequestDto;
import com.mdsl.model.dto.request.TerminalRequestDto;
import com.mdsl.model.dto.response.PaginationResponseDto;
import com.mdsl.model.dto.response.TerminalResponseDto;
import com.mdsl.service.TerminalService;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.Validations;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.text.ParseException;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/terminal")
@RequiredArgsConstructor
public class TerminalController {
	private static final Logger logger = LoggerFactory.getLogger(TerminalController.class);

	private final TerminalService terminalService;

	@PostMapping
	@Transactional
	public ResponseEntity saveOrUpdateTerminal(@Valid @RequestBody TerminalRequestDto terminalDto,BindingResult bindingResult) {

		Validations.validate(bindingResult);
		try {
			return ResponseEntity.ok(terminalService.saveOrUpdateTerminal(terminalDto));
		} catch(BusinessException ex){
			logger.error("@TerminalController#saveOrUpdateTerminal - BusinessException - "+ex.toString());
			throw new BusinessException(ex.getMessage(),ex.getHttpStatus());
		} catch (Exception ex) {
			logger.error("@TerminalController#saveOrUpdateTerminal - Exception - "+ex.toString());
			throw new BusinessException(ResponseCode.VAL_ERROR_OCCURRED,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/get-terminal/{terminalid}/{instId}")
	public ResponseEntity getTerminalById(@PathVariable("terminalid") String terminalid,
			@PathVariable("instId") String instId) {
		return ResponseEntity.ok(terminalService.getTerminal(terminalid, instId));
	}

	@GetMapping("/institution/{institutionid}")
	public ResponseEntity getTerminalByInstitutionId(@PathVariable("institutionid") String institutionid) {
		return ResponseEntity.ok(terminalService.getTerminalByInstitutionId(institutionid));
	}

	@GetMapping("/entities/{entitiesid}")
	public ResponseEntity getTerminalByEntityId(@PathVariable("entitiesid") String entitiesid) {
		return ResponseEntity.ok(terminalService.getTerminalByEntitiesId(entitiesid));
	}

	@GetMapping("/{entitiesid}/{institutionid}")
	public ResponseEntity getTerminalByEntityIdAndInstitutionId(@PathVariable("entitiesid") String entitiesid,
			@PathVariable("institutionid") String institutionid) {
		return ResponseEntity.ok(terminalService.getTerminalByEntitiesIdAndInstitutionId(entitiesid, institutionid));
	}

	@DeleteMapping("/{terminalid}/{instId}")
	public ResponseEntity<String> deleteTerminal(@PathVariable("terminalid") String terminalid,
			@PathVariable("instId") String instId) {
		try {
			DeleteTerminalRequestDto deleteTerminalRequestDto = DeleteTerminalRequestDto.builder()
					.terminalId(terminalid)
					.instId(instId)
					.build();
			terminalService.deleteTerminal(deleteTerminalRequestDto);
			String message = "An item is deleted with id : " + terminalid;
			return ResponseEntity.ok(message);
		} catch(BusinessException ex){
			logger.error("@TerminalController#deleteTerminal - BusinessException - "+ex.toString());
			throw new BusinessException(ex.getMessage(),ex.getHttpStatus());
		} catch (Exception ex) {
			logger.error("@TerminalController#deleteTerminal - Exception - "+ex.toString());
			throw new BusinessException(ResponseCode.VAL_ERROR_OCCURRED,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/status-change/{instId}")
	public ResponseEntity<String> changeTerminalStatus(@Valid @RequestBody ChangeStatusRequestDto changeInstitutionStatusRequestDTO,@PathVariable("instId") String instId, BindingResult bindingResult, HttpServletRequest request) {
		Validations.validate(bindingResult);
		changeInstitutionStatusRequestDTO.setInstId(instId);
		return ResponseEntity.ok(terminalService.changeStatus(changeInstitutionStatusRequestDTO));
	}

	@GetMapping("/active-terminal")
	public ResponseEntity<List<TerminalResponseDto>> getActiveTerminal() {
		return ResponseEntity.ok(terminalService.getActiveTerminal());
	}


	@PostMapping("/getAllTerminal")
	public ResponseEntity<PaginationResponseDto> terminalPagination(@Valid @RequestBody AllTerminalsRequestDto allTerminalsRequestDto, BindingResult bindingResult) {
		Validations.validate(bindingResult);
		ResponseEntity<PaginationResponseDto> responseEntity = null;
		try {
			responseEntity = terminalService.viewTerminal(allTerminalsRequestDto);
		} catch (Exception e) {
			logger.error("@TerminalController#terminalPagination - Exception - "+e.toString());
			e.printStackTrace();
			responseEntity = new ResponseEntity<>(new PaginationResponseDto(false, null, null, null, null),HttpStatus.OK);
		}
		return responseEntity;
	}


}
