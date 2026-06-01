package com.mdsl.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mdsl.service.ReportingService;
import com.mdsl.utils.Validations;

@RestController
@Transactional(rollbackOn = Exception.class)
@RequestMapping("/report/report")
public class ReportController {

	private final ReportingService reportingService;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	public ReportController(ReportingService reportingService) {
		super();
		this.reportingService = reportingService;
	}

	@PostMapping
	public String generateReport(@Valid @RequestBody String reportRequest, BindingResult bindingResult,
			HttpServletResponse response, HttpServletRequest request) {
		Validations.validate(bindingResult);
		return this.reportingService.generateReport(reportRequest, response,
				Integer.parseInt(request.getHeader("instId")));
	}
}
