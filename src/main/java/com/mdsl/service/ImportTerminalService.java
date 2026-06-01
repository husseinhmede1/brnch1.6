package com.mdsl.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import com.mdsl.framework.DatabaseMessageSource;
import com.mdsl.utils.AckFileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.ImportRequestDto;
import com.mdsl.model.dto.response.RunTaskResponseDto;
import com.mdsl.model.entity.Currency;
import com.mdsl.model.entity.Entities;
import com.mdsl.model.entity.Institution;
import com.mdsl.model.entity.ProcessingEvents;
import com.mdsl.model.entity.Task;
import com.mdsl.model.entity.TaskExecutionLog;
import com.mdsl.model.entity.Terminal;
import com.mdsl.model.entity.TerminalTypes;
import com.mdsl.model.entity.User;
import com.mdsl.repository.CurrencyRepository;
import com.mdsl.repository.EntitiesRepository;
import com.mdsl.repository.InstitutionRepository;
import com.mdsl.repository.MCCListRepository;
import com.mdsl.repository.ProcessingEventsRepository;
import com.mdsl.repository.TaskExecutionLogRepository;
import com.mdsl.repository.TaskRepository;
import com.mdsl.repository.TerminalRepository;
import com.mdsl.repository.TerminalTypesRepository;
import com.mdsl.repository.UserRepository;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.enumerations.TaskNameEnum;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImportTerminalService {
	private static final Logger logger = LoggerFactory.getLogger(ImportMerchantsService.class);

	private final InstitutionRepository institutionRepository;
	private final TerminalRepository terminalRepository;
	private final EntitiesRepository entitiesRepository;
	private final MCCListRepository mccRepository;
	private final TerminalTypesRepository terminalTypesRepository;
	private final CurrencyRepository currencyRepository;
	private final TaskExecutionLogRepository taskExecutionLogRepository;
	private final TaskRepository taskRepository;
	private final ProcessingEventsRepository processingEventsRepository;
	private final UserRepository userRepository;
	private final DatabaseMessageSource databaseMessageSource;
	private final SystemCodeService systemCodeService;

	public RunTaskResponseDto uploadTerminalFiles(ImportRequestDto importRequestDto) {
		File sourceFolder = new File(importRequestDto.getFilePath());
		File destinationFolder = new File(importRequestDto.getBackupFilePath());
		RunTaskResponseDto runTaskResponseDto = new RunTaskResponseDto();
		runTaskResponseDto.setResult(0);
		ProcessingEvents processingEvent = null;

		if (!sourceFolder.isDirectory() || !destinationFolder.isDirectory()) {
			logger.error("Error: Invalid source or destination folder.");
			runTaskResponseDto.setResult(-1);
		}

		Institution institution = this.institutionRepository.findById(importRequestDto.getInstitutionId())
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
		UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		User user = this.userRepository.findById(userDetails.getId())
				.orElseThrow(() -> new BusinessException(ResponseCode.USR_USER_NOT_FOUND, HttpStatus.NOT_FOUND));

		TaskExecutionLog saveTaskExecutionLog = new TaskExecutionLog();
		TaskExecutionLog savedTaskExecutionLog = new TaskExecutionLog();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

		File[] files = sourceFolder.listFiles((dir, name) -> name.startsWith("terminal"));
		if (files == null || files.length == 0) {
			throw new BusinessException(ResponseCode.CFG_NO_FILES_FOUND, HttpStatus.NOT_FOUND);
		}

		try (AckFileManager ack = new AckFileManager(sourceFolder, TaskNameEnum.IMPORT_TERMINALS.getValue(), systemCodeService, institution.getInstitutionId())) {
			try {
				for (File file : files) {
					ack.startFile(file.getName());

					try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
						int lineNumber = 0;
						String line;

						Task task = this.taskRepository.findByInstitutionIdAndTaskName("SYSTEM", TaskNameEnum.IMPORT_TERMINALS.getValue())
								.orElseThrow(() -> new BusinessException(ResponseCode.TSK_TASK_NOT_FOUND, HttpStatus.NOT_FOUND));
						saveTaskExecutionLog.setTaskId(task.getTaskId());
						saveTaskExecutionLog.setTaskDetails("Import Terminals");
						saveTaskExecutionLog.setStartDatetime(new Timestamp(System.currentTimeMillis()));
						saveTaskExecutionLog.setCreatedBy(userDetails.getId());
						saveTaskExecutionLog.setCreatedDate(new Timestamp(System.currentTimeMillis()));
						saveTaskExecutionLog.setInstitutionId(institution.getInstitutionId());
						savedTaskExecutionLog = this.taskExecutionLogRepository.save(saveTaskExecutionLog);

						while ((line = reader.readLine()) != null) {
							lineNumber++;
							final String rawLine = line;

							try {
								String[] data = line.split(",", -1);
								int i = 0;
								for (String s : data) System.out.println(i++ + ": " + s);
								System.out.println("Length: " + data.length);

								if (data.length >= 10) {
									Terminal terminal = new Terminal();
									terminal.setTerminalId(data[0]);

									Entities entity = this.entitiesRepository.findByEntityIdAndInstitution(data[1],institution)
											.orElseThrow(() -> new BusinessException(ResponseCode.CFG_ENTITY_NOT_FOUND, HttpStatus.NOT_FOUND));
									terminal.setEntities(entity.getEntityId());
									terminal.setEntitiesObject(entity);

									terminal.setSerialNumber(data[2]);

									TerminalTypes terminalType = this.terminalTypesRepository.findByTerminalTypeEqualsIgnoreCase(data[3])
											.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_TERMINAL_TYPE, HttpStatus.NOT_FOUND));
									terminal.setTerminalTypes(terminalType);

									terminal.setMccList(data[4]);

									Currency currency = currencyRepository.findByCurrencyCode(data[5])
											.orElseThrow(() -> new BusinessException(ResponseCode.CUR_CURRENCY_NOT_FOUND, HttpStatus.NOT_FOUND));
									terminal.setCurrency(currency);

									String eCommerceFlag = Objects.nonNull(data[6]) ? data[6].toString().trim() : null;
									if (eCommerceFlag == null || !eCommerceFlag.matches("^[YN]$")) {
										throw new BusinessException(ResponseCode.CFG_INVALID_ECOMMERCEFLAG, HttpStatus.BAD_REQUEST);
									}
									terminal.setECommerceFlag(data[6].charAt(0)); // ← capital C as per your latest code

									terminal.setActualStartDate(parseDate(dateFormat, data[7], ResponseCode.JOB_INVALID_ACTUAL_START_DATE));
									terminal.setTerminationDate(parseDate(dateFormat, data[8], ResponseCode.JOB_INVALID_TERMINATION_DATE));

									if (Objects.isNull(data[9]) || data[9].isBlank() || String.valueOf(data[9].charAt(0)).trim().equals("") ||
											(!String.valueOf(data[9].charAt(0)).trim().matches("^[0|1]*$"))) {
										throw new BusinessException(ResponseCode.JOB_INVALID_STATUS, HttpStatus.BAD_REQUEST);
									}
									terminal.setStatus(data[9].charAt(0));

									terminal.setInstitution(institution.getInstitutionId());
									terminal.setInstitutionEntity(institution);

									if (userDetails != null)
										terminal.setUserCreate(Integer.valueOf(userDetails.getId()).toString());
									terminal.setDateCreate(new Date(new Date().getTime()));

									this.doRecordValidation(terminal);

									// Save terminal
									this.terminalRepository.save(terminal);

									ack.recordSuccess();

									processingEvent = new ProcessingEvents();
									processingEvent.setTaskExecutionLogId(savedTaskExecutionLog.getTaskExecutionLogId());
									processingEvent.setInstitutionId(institution.getInstitutionId());
									processingEvent.setProcessingProgram(TaskNameEnum.IMPORT_TERMINALS.getValue());
									processingEvent.setFileName(file.getName());
									processingEvent.setExecutionTime(new Timestamp(System.currentTimeMillis()));
									processingEvent.setSuccessResult('S');
									processingEvent.setRemarks("Terminal with ID: " + terminal.getTerminalId()
											+ " was created and assigned to entity: "
											+ terminal.getEntitiesObject().getEntityId() + " - "
											+ terminal.getEntitiesObject().getEntityName());
									if (userDetails != null) processingEvent.setCreatedBy(user);
									processingEvent.setCreatedDate(new Timestamp(System.currentTimeMillis()));
									this.processingEventsRepository.save(processingEvent);
								}

							} catch (Exception recordEx) {
								handleRecordFailure(ack, savedTaskExecutionLog, institution, user, userDetails,
										TaskNameEnum.IMPORT_TERMINALS.getValue(), file.getName(), lineNumber, rawLine, recordEx);
							}
						} // end while

						if (ack.getFileSuccess() == 0) {
							logger.debug("No valid terminal records found in file: " + file.getName());
							runTaskResponseDto.setResult(-2);
						}
					}

					ack.endFile(file.getName());
					Files.copy(file.toPath(), new File(destinationFolder, file.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
					Files.delete(file.toPath());
				}

				ack.writeGlobalSummary(files.length);

				savedTaskExecutionLog.setEndDatetime(new Timestamp(System.currentTimeMillis()));
				savedTaskExecutionLog.setTaskDetails("Import Terminals Successful");
				runTaskResponseDto.setTaskExecutionLogId(savedTaskExecutionLog.getTaskExecutionLogId());
				this.taskExecutionLogRepository.save(savedTaskExecutionLog);
				System.out.println("Terminal files uploaded and processed successfully.");
				runTaskResponseDto.setResult(0);

			} catch (IOException e) {
				savedTaskExecutionLog.setEndDatetime(new Timestamp(System.currentTimeMillis()));
				savedTaskExecutionLog.setTaskDetails("Import Terminals Failed");
				this.taskExecutionLogRepository.save(savedTaskExecutionLog);
				logger.error("Error processing files: " + e.getMessage());
				runTaskResponseDto.setResult(-3);
				runTaskResponseDto.setTaskExecutionLogId(savedTaskExecutionLog.getTaskExecutionLogId());

				processingEvent = new ProcessingEvents();
				processingEvent.setTaskExecutionLogId(savedTaskExecutionLog.getTaskExecutionLogId());
				processingEvent.setInstitutionId(institution.getInstitutionId());
				processingEvent.setProcessingProgram(TaskNameEnum.IMPORT_TERMINALS.getValue());
				processingEvent.setExecutionTime(new Timestamp(System.currentTimeMillis()));
				processingEvent.setRemarks("Terminal import failure");
				processingEvent.setSuccessResult('F');
				if (userDetails != null) processingEvent.setCreatedBy(user);
				processingEvent.setCreatedDate(new Timestamp(System.currentTimeMillis()));
				this.processingEventsRepository.save(processingEvent);
			}

		} catch (IOException ackEx) {
			logger.error("Warning: Could not write ACK file: " + ackEx.getMessage());
		}

		return runTaskResponseDto;
	}

	private void handleRecordFailure(AckFileManager ack,
									 TaskExecutionLog savedLog,
									 Institution institution,
									 User user,
									 UserDetailsImpl userDetails,
									 String programName,
									 String fileName,
									 int lineNumber,
									 String rawLine,
									 Exception ex) {
		Locale locale = LocaleContextHolder.getLocale();
		String errorMsg = databaseMessageSource.getMessageFromKey(ex.getMessage(), locale, true).get(0);
//		String errorMsg = ex.getMessage() != null ? ex.getMessage() : ex.getClass().getSimpleName();
		System.out.println("Record skipped - Line " + lineNumber + " in [" + fileName + "]: " + errorMsg);

		try {
			ack.writeFailure(lineNumber, fileName, errorMsg, rawLine);
		} catch (IOException ackEx) {
			System.out.println("Warning: Could not write to ACK file: " + ackEx.getMessage());
		}

		ProcessingEvents processingEvent = new ProcessingEvents();
		processingEvent.setTaskExecutionLogId(savedLog.getTaskExecutionLogId());
		processingEvent.setInstitutionId(institution.getInstitutionId());
		processingEvent.setProcessingProgram(programName);
		processingEvent.setFileName(fileName);
		processingEvent.setExecutionTime(new Timestamp(System.currentTimeMillis()));
		processingEvent.setSuccessResult('F');
		processingEvent.setRemarks("Line " + lineNumber + " failed: " + errorMsg);
		if (userDetails != null) {
			processingEvent.setCreatedBy(user);
		}
		processingEvent.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		this.processingEventsRepository.save(processingEvent);
	}
	private boolean doRecordValidation(Terminal terminal) {
    	
    	//Validation on terminal id
		if (Objects.isNull(terminal.getTerminalId()) || terminal.getTerminalId().trim().equals("") ||
				(terminal.getTerminalId().trim().length() > 10)) {
			throw new BusinessException(ResponseCode.CFG_INVALID_TERMINAL_ID, HttpStatus.BAD_REQUEST);
		}
    	
    	//Validation on serial number
		if (Objects.isNull(terminal.getSerialNumber()) || terminal.getSerialNumber().trim().equals("") ||
				(terminal.getSerialNumber().trim().length() > 30 || !terminal.getSerialNumber().trim().matches("^[a-zA-Z0-9]*$"))) {
			throw new BusinessException(ResponseCode.CFG_INVALID_SERIAL_NUMBER, HttpStatus.BAD_REQUEST);
		}
		
		//Validation on mcc
		if (Objects.isNull(terminal.getMccList()) || terminal.getMccList().trim().equals("") ||
				mccRepository.findByMcc(terminal.getMccList()).size() == 0) {
			throw new BusinessException(ResponseCode.JOB_INVALID_MCC, HttpStatus.BAD_REQUEST);
		}
		
		//Validation on ecommerce flag
		if (Objects.nonNull(terminal.getECommerceFlag()) && !String.valueOf(terminal.getECommerceFlag()).trim().equals("") &&
				(String.valueOf(terminal.getECommerceFlag()).trim().length() > 1 || !String.valueOf(terminal.getECommerceFlag()).trim().matches("^[Y|N]*$"))) {
			throw new BusinessException(ResponseCode.CFG_INVALID_ECOMMERCEFLAG, HttpStatus.BAD_REQUEST);
		}
		
		// Validation that actual start date is before termination date
	    if (Objects.nonNull(terminal.getActualStartDate()) && Objects.nonNull(terminal.getTerminationDate()) &&!terminal.getActualStartDate().before(terminal.getTerminationDate())) {
	        throw new BusinessException(ResponseCode.CFG_INVALID_START_DATE_TERMINATION_DATE, HttpStatus.BAD_REQUEST);
	    }
		
		//Validation on status
		if (Objects.isNull(terminal.getStatus()) || String.valueOf(terminal.getStatus()).trim().equals("") ||(String.valueOf(terminal.getStatus()).trim().length() > 1 || !String.valueOf(terminal.getStatus()).trim().matches("^[0|1]*$"))) {
			throw new BusinessException(ResponseCode.JOB_INVALID_STATUS, HttpStatus.BAD_REQUEST);
		}
				
		return true;
    	
    }
	
	public static boolean isLeap(int year) {
    	return (((year % 4 == 0) &&
    	         (year % 100 != 0)) ||
    	         (year % 400 == 0));
    }
    
    public boolean isValidDate (int day, int month, int year) {
		if(month<1 || month>12) {
			return false;
		}
		if(year<1900 || year>2100) {
			return false;
		}
		if(day<0 || day>31) {
			return false;
		}
			
		if (month == 2){
	        if (isLeap(year) && day!=0 && day>29) {
	        	return false;
	        } else if(!isLeap(year) && day!=0 && day>28) {
				return false;
	        }
	    }
		
		if (month == 4 || month == 6 || month == 9 || month == 11) {
			if(day!=0 && day>30) {
				return false;
			}
		}
		return true;
	}


	private Date parseDate(SimpleDateFormat dateFormat, String dateString,String errorCode) {
		try {
			if (dateString == null || dateString.trim().isEmpty()) {
				return null; // or throw, depending on contract
			}
			if (!dateString.matches("^[0-9/\\\\-]+$")) {
				throw new BusinessException(
						errorCode.isBlank() ? ResponseCode.JOB_INVALID_DATE : errorCode,
						HttpStatus.BAD_REQUEST
				);
			}
			if (Objects.nonNull(dateString) && !dateString.trim().isEmpty()) {
				int day = Integer.parseInt(dateString.substring(0, 2));
				int month = Integer.parseInt(dateString.substring(3, 5));
				int year = Integer.parseInt(dateString.substring(6, 10));

				if (!this.isValidDate(day, month, year)) {
					throw new BusinessException(errorCode, HttpStatus.BAD_REQUEST);
				}
			}

			return dateFormat.parse(dateString);
		} catch (ParseException e) {
			System.out.println("ParseException: " + e.getMessage());
			throw new BusinessException(errorCode.isBlank()?ResponseCode.JOB_INVALID_DATE:errorCode, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
			throw new BusinessException(errorCode.isBlank()?ResponseCode.JOB_INVALID_DATE:errorCode, HttpStatus.BAD_REQUEST);
		}
	}

}
