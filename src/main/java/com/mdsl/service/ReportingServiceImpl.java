package com.mdsl.service;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

@Repository
@Transactional
public class ReportingServiceImpl {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${rpt.repository.path}")
	private String rptRepositoryPath;

	@Autowired
	private DataSource dataSource;

	public JasperPrint fillReport(Map<String, String> parameters) {
		JasperPrint print = null;
		try (Connection conn = dataSource.getConnection()) {
			String reportName = parameters.get("reportName");
			logger.debug("@ReportingServiceImpl#fillReport: Report Name: {}", reportName);

			String path = rptRepositoryPath + reportName;
			logger.debug("@ReportingServiceImpl#fillReport: Report path: {}", path);

			parameters.remove("reportName");

			JasperReport jasperReport = JasperCompileManager.compileReport(path);

			Map<String, Object> jasperParameters = new HashMap<>(parameters);
			jasperParameters.put("net.sf.jasperreports.fill.page.cache.size", 20);

			print = JasperFillManager.fillReport(jasperReport, jasperParameters, conn);

			print.setProperty("net.sf.jasperreports.j2ee.web.startingPage", "1");
			print.setProperty("net.sf.jasperreports.compression", "true");
		} catch (Exception e) {
			logger.error("@ReportingServiceImpl#fillReport: ", e);
		}
		return print;
	}
}