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
import com.mdsl.model.dto.request.OutputFileTemplateHdrRequestDto;
import com.mdsl.model.dto.response.OutputFileTemplateHdrResponseDto;
import com.mdsl.service.OutputFileTemplateHdrService;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.Validations;

@CrossOrigin(origins={"*"})
@RestController
@RequestMapping("/output-template")
public class OutputTemplateHdrController {
	private static final Logger logger = LoggerFactory.getLogger(OutputTemplateHdrController.class);
	private final OutputFileTemplateHdrService outputFileTemplateHdrService;

	@Autowired
	public OutputTemplateHdrController(OutputFileTemplateHdrService outputFileTemplateHdrService) {
		this.outputFileTemplateHdrService = outputFileTemplateHdrService;
	}

	@GetMapping({"/inst/{instId}"})
	public ResponseEntity<List<OutputFileTemplateHdrResponseDto>> getAllOutputFileTemplateHdrsByInstitution(@PathVariable("instId") String instId) {
		return ResponseEntity.ok(this.outputFileTemplateHdrService.getAllOutputFileTemplateHdrsByInstitution(instId));
	}

	@GetMapping({"/{id}"})
	public ResponseEntity<OutputFileTemplateHdrResponseDto> getOutputFileTemplateHdrById(@PathVariable("id") int id) {
		return ResponseEntity.ok(this.outputFileTemplateHdrService.getOutputFileTemplateHdrById(id));
	}

	@PostMapping
	public ResponseEntity<OutputFileTemplateHdrResponseDto> saveOutputFileTemplateHdr(@Valid @RequestBody OutputFileTemplateHdrRequestDto outputFileTemplateHdrRequestDto, BindingResult bindingResult) {
		Validations.validate(bindingResult);
		try {
			return ResponseEntity.ok(this.outputFileTemplateHdrService.saveOutputFileTemplateHdr(outputFileTemplateHdrRequestDto));
		} catch (BusinessException e) {
		    throw new BusinessException (e.getMessage(), e.getHttpStatus());
		} catch (Exception e) {
		    logger.error("@OutputTemplateHdrController#saveOutputFileTemplateHdr: " + e.getMessage());
		    throw new BusinessException (ResponseCode.CFG_OUTPUT_FILE_TEMPLATE_NO_SAVE, HttpStatus.BAD_REQUEST);
	    }
	}

	@DeleteMapping("/{id}")
	public void deleteOutputFileTemplateHdr(@PathVariable("id") int id) {
		try {
			this.outputFileTemplateHdrService.deleteOutputFileTemplateHdr(id);
		} catch (BusinessException e) {
		    throw new BusinessException (e.getMessage(), e.getHttpStatus());
		} catch (Exception e) {
		    logger.error("@OutputTemplateHdrController#deleteOutputFileTemplateHdr: " + e.getMessage());
		    throw new BusinessException (ResponseCode.CFG_OUTPUT_FILE_TEMPLATE_NO_DELETE, HttpStatus.BAD_REQUEST);
	    }
	}
}