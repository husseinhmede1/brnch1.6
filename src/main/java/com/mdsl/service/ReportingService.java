package com.mdsl.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.entity.Api;
import com.mdsl.repository.ApiRepository;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.Validations;
//import com.mdsl.exceptionHandling.BusinessException;
//import com.mdsl.utils.Validations;
import com.mdsl.utils.enumerations.ReportFormatEnum;

import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
@Service
@RequiredArgsConstructor
public class ReportingService {

	private Map<String, String> resultMapOfJsonString = new HashMap<String, String>();
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final ReportingServiceImpl reportingServiceImplementation;
	
	private final ApiRepository apiRepository;

	//private final UserRoleAccessRepository

	private void processAnyJsonNode(JsonNode jsonNode) {
	    if (jsonNode.isArray()) {
	        //go through all the items and process them one by one
	        for (final JsonNode objInArray: jsonNode) {
	            // process the item in the array
	            processAnyJsonNode(objInArray);
	        }
	    } else if (jsonNode.isContainerNode()) {
	        //go through all object fields
	        Iterator< Map.Entry < String, JsonNode >> it = jsonNode.fields();
	        while (it.hasNext()) {
	            Map.Entry < String, JsonNode > field = it.next();
	            resultMapOfJsonString.put(field.getKey(), field.getValue().asText());
	            //process every field in the array
	            processAnyJsonNode(field.getValue());
	        }
	    }
	}

	/*
	 * Validates if the API has view - update - delete rights
	 */
	public void checkAccessToReport(String reportName) {
		UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		Api api = this.apiRepository.findByApiUrl(reportName);

		if (Objects.isNull(api)) {
			throw new BusinessException(ResponseCode.REP_INVALID_REPORT_REQUEST, HttpStatus.BAD_REQUEST);
		}

//		if (!userRoleAccessRepository.existsByUserIdAndApiIdAndAccessView(userDetails.getId(), api.getApiId(),
//				"true")) {
//			throw new BusinessException(ResponseCode.VAL_FUNCTION_NOT_ALLOWED, HttpStatus.BAD_REQUEST);
//		}
	}

	public JasperPrint fillReport(Map<String, String> mapOfJsonString) throws SQLException, JRException, IOException {
		return reportingServiceImplementation.fillReport(mapOfJsonString);  
	}
	
	
	public String generateReport(String reportRequest, HttpServletResponse response, int instId) {
		resultMapOfJsonString.clear();
		String decodedRequest = "";
		try {
			decodedRequest = java.net.URLDecoder.decode(reportRequest, StandardCharsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			logger.error("@ReportingService#generateReport: Could not decode URI to String: " + e.getMessage());
			throw new BusinessException(ResponseCode.REP_INVALID_REPORT_REQUEST, HttpStatus.BAD_REQUEST);
		}

		JasperPrint jasperPrint = null;
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode;
		try {
			jsonNode = mapper.readTree(decodedRequest);
			processAnyJsonNode(jsonNode);

			String reportName = resultMapOfJsonString.get("reportName");
			String reportFormat = resultMapOfJsonString.get("reportFormat");

			logger.debug("@ReportingService#generateReport: Report Name: " + reportName);
			logger.debug("@ReportingService#generateReport: Report Format: " + reportFormat);

			///checkAccessToReport(reportName);

			jasperPrint = this.fillReport(resultMapOfJsonString);
			if (Objects.isNull(jasperPrint)) {
				throw new BusinessException(ResponseCode.REP_COULD_NOT_GENERATE_REPORT, HttpStatus.BAD_REQUEST);
			}

			if(reportFormat.equalsIgnoreCase(ReportFormatEnum.PDF.getValue())) {
				response.setContentType("application/pdf");
				response.addHeader("Content-Disposition", "attachment; filename="+reportName.replace(".jrxml", "")+".pdf");
				JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
				return "PDF Report Successfully Generated.";

			} else if (reportFormat.equalsIgnoreCase(ReportFormatEnum.HTML.getValue())) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				HtmlExporter exporter = new HtmlExporter();
				exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
				exporter.setExporterOutput(new SimpleHtmlExporterOutput(out));
				exporter.exportReport();
				return out.toString("UTF-8");

			}  else if (reportFormat.equalsIgnoreCase(ReportFormatEnum.EXCEL.getValue())){
				response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
				response.addHeader("Content-Disposition", "attachment; filename="+reportName.replace(".jrxml", "")+".xlsx");
				response.setCharacterEncoding("UTF-8");
				response.setHeader("Content-Transfer-Encoding", "binary");
				response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
				response.setHeader("Pragma", "no-cache");
				response.setHeader("Expires", "0");

				JRXlsxExporter exporter = new JRXlsxExporter();

				SimpleXlsxReportConfiguration reportConfigXLS = new SimpleXlsxReportConfiguration();

				reportConfigXLS.setRemoveEmptySpaceBetweenColumns(true);
				reportConfigXLS.setRemoveEmptySpaceBetweenRows(true);
				reportConfigXLS.setWrapText(true);
				reportConfigXLS.setIgnoreCellBorder(false);
				reportConfigXLS.setIgnoreCellBackground(false);
				reportConfigXLS.setDetectCellType(true);
				reportConfigXLS.setOnePagePerSheet(false);
				reportConfigXLS.setIgnoreGraphics(false);
				reportConfigXLS.setIgnorePageMargins(false);
				reportConfigXLS.setCollapseRowSpan(true);

				exporter.setConfiguration(reportConfigXLS);
				exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
				exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));

				try {
					exporter.exportReport();
					response.getOutputStream().flush();
				} catch (Exception e) {
					logger.error("Error generating Excel report: ", e);
				} finally {
					response.getOutputStream().close();
				}

				return "EXCEL Report Successfully Generated.";
			}else {
				throw new BusinessException(ResponseCode.REP_INVALID_REPORT_FORMAT, HttpStatus.BAD_REQUEST);
			}
		} catch (BusinessException e) {
			logger.error(
					"@ReportingService#generateReport: An error occured, could not generate report. " + e.getMessage());
			throw new BusinessException(e.getMessage(), e.getHttpStatus());
		} catch (Exception e) {
			logger.error(
					"@ReportingService#generateReport: An error occured, could not generate report. " + e.getMessage());
			throw new BusinessException(ResponseCode.VAL_ERROR_OCCURRED, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
