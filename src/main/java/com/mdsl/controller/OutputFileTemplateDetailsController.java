package com.mdsl.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mdsl.model.dto.response.OutputFileTemplateDetailsResponseDto;
import com.mdsl.service.OutputFileTemplateDetailsService;

@CrossOrigin(origins={"*"})
@RestController
@RequestMapping("/output-details")
public class OutputFileTemplateDetailsController {
	private final OutputFileTemplateDetailsService outputFileTemplateDetailsService;

	@Autowired
	public OutputFileTemplateDetailsController(OutputFileTemplateDetailsService outputFileTemplateDetailsService) {
		this.outputFileTemplateDetailsService = outputFileTemplateDetailsService;
	}
	
	@GetMapping("/header/{outputFileTemplateHdrId}")
	public ResponseEntity<List<OutputFileTemplateDetailsResponseDto>> getOutputFileTemplateDetailsByOutputFileTemplateHdrId(@PathVariable("outputFileTemplateHdrId") int outputFileTemplateHdrId) {
		return ResponseEntity.ok(this.outputFileTemplateDetailsService.getOutputFileTemplateDetailsByOutputFileTemplateHdrId(outputFileTemplateHdrId));
	}
	
	@GetMapping("/details/{outputFileTemplateHdrId}/{fieldSection}")
	public ResponseEntity<List<OutputFileTemplateDetailsResponseDto>> getOutputFileTemplateDetailsByOutputFileTemplateHdrIdAndFieldSection(@PathVariable("outputFileTemplateHdrId") int outputFileTemplateHdrId, @PathVariable("fieldSection") String fieldSection) {
		return ResponseEntity.ok(this.outputFileTemplateDetailsService.getOutputFileTemplateDetailsByOutputFileTemplateHdrIdAndFieldSection(outputFileTemplateHdrId, fieldSection));
	}
}