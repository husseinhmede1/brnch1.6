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
import java.util.*;

import com.mdsl.framework.DatabaseMessageSource;
import com.mdsl.utils.AckFileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mdsl.controller.JobController;
import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.ImportRequestDto;
import com.mdsl.model.dto.response.RunTaskResponseDto;
import com.mdsl.model.entity.Address;
import com.mdsl.model.entity.BankCode;
import com.mdsl.model.entity.City;
import com.mdsl.model.entity.Contact;
import com.mdsl.model.entity.Country;
import com.mdsl.model.entity.Currency;
import com.mdsl.model.entity.Employee;
import com.mdsl.model.entity.Entities;
import com.mdsl.model.entity.EntityLevels;
import com.mdsl.model.entity.Institution;
import com.mdsl.model.entity.PaymentAccount;
import com.mdsl.model.entity.ProcessingEvents;
import com.mdsl.model.entity.SystemCode;
import com.mdsl.model.entity.Task;
import com.mdsl.model.entity.TaskExecutionLog;
import com.mdsl.model.entity.User;
import com.mdsl.repository.AddressRepository;
import com.mdsl.repository.BankCodeRepository;
import com.mdsl.repository.CityRepository;
import com.mdsl.repository.ContactRepository;
import com.mdsl.repository.CountryRepository;
import com.mdsl.repository.CurrencyRepository;
import com.mdsl.repository.EmployeeRepository;
import com.mdsl.repository.EntitiesRepository;
import com.mdsl.repository.EntityLevelsRepository;
import com.mdsl.repository.InstitutionRepository;
import com.mdsl.repository.MCCListRepository;
import com.mdsl.repository.PaymentAccountRepository;
import com.mdsl.repository.ProcessingEventsRepository;
import com.mdsl.repository.SystemCodeRepository;
import com.mdsl.repository.TaskExecutionLogRepository;
import com.mdsl.repository.TaskRepository;
import com.mdsl.repository.UserRepository;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.enumerations.CodePrefixEnum;
import com.mdsl.utils.enumerations.StatusEnum;
import com.mdsl.utils.enumerations.TaskNameEnum;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImportMerchantsService {
	private static final Logger logger = LoggerFactory.getLogger(ImportMerchantsService.class);

    private final EntitiesRepository entitiesRepository;
    private final InstitutionRepository institutionRepository;
    private final SystemCodeRepository systemCodeRepository;
    private final EmployeeRepository employeeRepository;
    private final CurrencyRepository currencyRepository;
    private final EntityLevelsRepository entityLevelsRepository;
    private final MCCListRepository mccRepository;
    private final AddressRepository addressRepository;
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;
    private final BankCodeRepository bankCodeRepository;
    private final PaymentAccountRepository paymentAccountRepository;
    private final ContactRepository contactRepository;
    private final TaskExecutionLogRepository taskExecutionLogRepository;
    private final TaskRepository taskRepository;
    private final ProcessingEventsRepository processingEventsRepository;
    private final UserRepository userRepository;
    private final DatabaseMessageSource databaseMessageSource;
    private final SystemCodeService systemCodeService;


    public RunTaskResponseDto uploadMerchantFiles(ImportRequestDto importRequestDto) {
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

        File[] files = sourceFolder.listFiles((dir, name) -> name.startsWith("merchant"));
        if (files == null || files.length == 0) {
            throw new BusinessException(ResponseCode.CFG_NO_FILES_FOUND, HttpStatus.NOT_FOUND);
        }

        try (AckFileManager ack = new AckFileManager(sourceFolder, TaskNameEnum.IMPORT_MERCHANTS.getValue(), systemCodeService, institution.getInstitutionId())) {
            try {
                for (File file : files) {
                    ack.startFile(file.getName());

                    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                        int lineNumber = 0;
                        String line;

                        Task task = this.taskRepository.findByInstitutionIdAndTaskName("SYSTEM", TaskNameEnum.IMPORT_MERCHANTS.getValue())
                                .orElseThrow(() -> new BusinessException(ResponseCode.TSK_TASK_NOT_FOUND, HttpStatus.NOT_FOUND));
                        saveTaskExecutionLog.setTaskId(task.getTaskId());
                        saveTaskExecutionLog.setTaskDetails("Import Merchants");
                        saveTaskExecutionLog.setStartDatetime(new Timestamp(System.currentTimeMillis()));
                        saveTaskExecutionLog.setCreatedBy(userDetails.getId());
                        saveTaskExecutionLog.setCreatedDate(new Timestamp(System.currentTimeMillis()));
                        saveTaskExecutionLog.setInstitutionId(institution.getInstitutionId());
                        savedTaskExecutionLog = this.taskExecutionLogRepository.save(saveTaskExecutionLog);

                        while ((line = reader.readLine()) != null) {
                            lineNumber++;
                            final String rawLine = line;

                            try {
                                String[] data = line.split(",");
                                int i = 0;
                                for (String s : data) System.out.println(i++ + ": " + s);
                                System.out.println("Length: " + data.length);

                                if (data.length >= 45) {

                                    // Entity Main Info
                                    Entities entityInfo = new Entities();
                                    entityInfo.setInstitution(institution);
                                    EntityLevels entityLevel = this.entityLevelsRepository
                                            .findByTypeDescriptionAndInstitution_InstitutionId(data[0], institution.getInstitutionId())
                                            .orElseThrow(() -> new BusinessException(ResponseCode.ENT_ENTITYLEVELS_CODE_NOT_FOUND, HttpStatus.NOT_FOUND));
                                    entityInfo.setEntityLevels(entityLevel.getHierarchyLevel());
                                    entityInfo.setEntityId(data[1]);
                                    entityInfo.setEntityName(data[2]);
                                    entityInfo.setEntityNameAlt(data[3]);
                                    entityInfo.setDbaName(data[4]);
                                    entityInfo.setDbaNameAlt(data[5]);
                                    
                                    entityInfo.setParentIdEntity(validateParentEntity(data[0], data[6],institution));
                                    if(entityInfo.getParentIdEntity()!=null) {
                                    	entityInfo.setParentId(validateParentEntity(data[0], data[6],institution).getEntityId());
                                    }else {
                                        entityInfo.setParentId(null);
                                    }
                                    entityInfo.setDefaultMCC(data[7]);
                                    if (data[43].equalsIgnoreCase("D") || data[43].equalsIgnoreCase("P") || data[43].equalsIgnoreCase("T")) {
                                        entityInfo.setEntityStatus(data[43].charAt(0));
                                    } else {
                                        entityInfo.setEntityStatus('D');
                                    }
                                    entityInfo.setEStatementToEntity('N');

                                    SystemCode businessType = systemCodeRepository
                                            .findByCodePrefixAndCodeSuffixAndInstitution_InstitutionId(CodePrefixEnum.BUSINESS_TYPE.getValue(), data[8], "SYSTEM")
                                            .orElseThrow(() -> new BusinessException(ResponseCode.JOB_BUSINESS_TYPE_NOT_FOUND, HttpStatus.NOT_FOUND));
                                    entityInfo.setBusinessType(businessType);
                                    entityInfo.setCompanyRegisterNBR(data[9]);
                                    entityInfo.setCompanyVatNBR(data[10]);
                                    entityInfo.setContractDate(parseDate(dateFormat, data[11], ResponseCode.JOB_INVALID_CONTRACT_DATE));
                                    entityInfo.setExpStartDate(parseDate(dateFormat, data[12], ResponseCode.JOB_INVALID_EXP_START_DATE));
                                    entityInfo.setActualStartDate(parseDate(dateFormat, data[13], ResponseCode.JOB_INVALID_ACTUAL_START_DATE));
                                    entityInfo.setTerminationDate(parseDate(dateFormat, data[14], ResponseCode.JOB_INVALID_TERMINATION_DATE));
                                    entityInfo.setPaymentMethod(data[15].trim());
                                    entityInfo.setPaymentFrequency(data[16].trim());
                                    entityInfo.setAddValueDateDays(this.isValidInteger(data[17], ResponseCode.JOB_INVALID_ADD_VALUE_DATE_DAYS));

                                    if (Objects.nonNull(data[18]) && !data[18].trim().isEmpty()) {
                                        if (data[18].trim().length() > 1) {
                                            throw new BusinessException(ResponseCode.ENT_INVALID_STATEMENT_TYPE, HttpStatus.NOT_FOUND);
                                        }
                                        entityInfo.setStatementType(data[18].trim().charAt(0));
                                    } else {
                                        entityInfo.setStatementType('P');
                                    }

                                    Employee salesman = employeeRepository
                                            .findByInstitutionAndStatusAndEmployeeName(institution, StatusEnum.ENABLED.getValue(), data[19])
                                            .orElseThrow(() -> new BusinessException(ResponseCode.JOB_SALESMAN_NOT_FOUND, HttpStatus.NOT_FOUND));
                                    entityInfo.setSalesman(salesman);
                                    Employee employeeInCharge = employeeRepository
                                            .findByInstitutionAndStatusAndEmployeeName(institution, StatusEnum.ENABLED.getValue(), data[20])
                                            .orElseThrow(() -> new BusinessException(ResponseCode.JOB_EMPLOYEE_INCHARGE_NOT_FOUND, HttpStatus.NOT_FOUND));
                                    entityInfo.setEmployeeIncharge(employeeInCharge);
                                    Currency currency = currencyRepository.findByCurrencyCode(data[21])
                                            .orElseThrow(() -> new BusinessException(ResponseCode.JOB_INVALID_SETTLEMENT_CURRENCY, HttpStatus.NOT_FOUND));
                                    entityInfo.setDefaultSettlementCurrency(currency);

                                    entityInfo.setAssociatedPayment(
                                            (Objects.nonNull(data[22]) && !data[22].trim().isEmpty()) ? data[22].trim().charAt(0) : 'N');
                                    entityInfo.setOnHoldInd(
                                            (Objects.nonNull(data[23]) && !data[23].trim().isEmpty()) ? data[23].trim().charAt(0) : 'N');
                                    entityInfo.setHotMerchantFlag(
                                            (Objects.nonNull(data[24]) && !data[24].trim().isEmpty()) ? data[24].trim().charAt(0) : 'N');

                                    if (Objects.nonNull(data[25]) && !data[25].trim().isEmpty()) {
                                        entityInfo.setStatus(data[25].charAt(0));
                                    } else {
                                        throw new BusinessException(ResponseCode.JOB_INVALID_STATUS, HttpStatus.BAD_REQUEST);
                                    }

                                    entityInfo.setDateCreate(new Date(new Date().getTime()));
                                    entityInfo.setUserCreate(Integer.valueOf(userDetails.getId()).toString());

                                    // Entity Address
                                    Address entityAddress = new Address();
                                    entityAddress.setPhone1(data[26]);
                                    entityAddress.setPhone2(data[27]);
                                    entityAddress.setMobile1(data[28]);
                                    entityAddress.setMobile2(data[29]);
                                    entityAddress.setAddress1(data[30]);
                                    entityAddress.setAddress2(data[31]);
                                    entityAddress.setAddress3(data[32]);
                                    entityAddress.setAddress4(data[33]);
                                    entityAddress.setFax(data[34]);
                                    entityAddress.setUrl(data[35]);

                                    if (Objects.nonNull(data[36]) && !data[36].trim().equalsIgnoreCase("")) {
                                        Country country = this.countryRepository
                                                .findByCntryCodeAndCntryStatus(data[36], StatusEnum.ENABLED.getValue())
                                                .orElseThrow(() -> new BusinessException(ResponseCode.CFG_COUNTRY_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
                                        entityAddress.setCntryCode(country);
                                        if (Objects.nonNull(data[37]) && !data[37].trim().equalsIgnoreCase("")) {
                                            City city = this.cityRepository.findByCntryCodeAndCityName(country, data[37])
                                                    .orElseThrow(() -> new BusinessException(ResponseCode.CFG_CITY_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
                                            entityAddress.setCityCode(city);
                                        }
                                    }

                                    entityAddress.setPostalCodeZip(data[38]);
                                    entityAddress.setGeoCode(data[39]);
                                    entityAddress.setEmailAddress1(data[40]);
                                    entityAddress.setEmailAddress2(data[41]);
                                    entityAddress.setDate(new Date(new Date().getTime()));
                                    entityAddress.setUserCreate(Integer.valueOf(userDetails.getId()).toString());

                                    // Entity Payment Accounts
                                    int paymentAccountsCount = this.isValidInteger(data[42], ResponseCode.JOB_INVALID_PAYMENT_ACCOUNT_COUNT);
                                    int j = 44;
                                    List<PaymentAccount> entityPaymentAccounts = new ArrayList<>();

                                    for (int m = 0; m < paymentAccountsCount; m++) {
                                        PaymentAccount paymentAccount = new PaymentAccount();
                                        paymentAccount.setAccountNumber(data[j++]);
                                        paymentAccount.setIban(data[j++]);
                                        paymentAccount.setCurrencyMarkup(isValidDouble(data[j++]));

                                        BankCode bankCode = bankCodeRepository
                                                .findByBankCodeAndInstitutionId(data[j++], institution.getInstitutionId())
                                                .orElseThrow(() -> new BusinessException(ResponseCode.CFG_BANK_CODE_NOT_FOUND, HttpStatus.NOT_FOUND));

                                        paymentAccount.setBankCodeEntity(bankCode);
                                        paymentAccount.setBankCode(bankCode.getBankCode());

                                        if (Objects.nonNull(data[j])) {
                                            Currency transferCurrency = currencyRepository.findByCurrencyCode(data[j++])
                                                    .orElseThrow(() -> new BusinessException(ResponseCode.JOB_INVALID_TRANSFER_CURRENCY, HttpStatus.NOT_FOUND));
                                            paymentAccount.setTransferCurrency(transferCurrency);
                                        } else { j++; }

                                        if (Objects.nonNull(data[j])) {
                                            Currency settlementCurrency = currencyRepository.findByCurrencyCode(data[j++])
                                                    .orElseThrow(() -> new BusinessException(ResponseCode.JOB_INVALID_SETTLEMENT_CURRENCY, HttpStatus.NOT_FOUND));
                                            paymentAccount.setSettlementCurrency(settlementCurrency);
                                        } else { j++; }

                                        String beneficiaryName = data[j++];
                                        if (beneficiaryName.matches(".*[\\u0600-\\u06FF].*")) {
                                            if (beneficiaryName.length() > 50 || beneficiaryName.trim().equals(""))
                                                throw new BusinessException(ResponseCode.JOB_INVALID_BENEFICIARY_NAME, HttpStatus.BAD_REQUEST);
                                        } else if (beneficiaryName.length() > 100 || beneficiaryName.trim().equals("")) {
                                            throw new BusinessException(ResponseCode.JOB_INVALID_BENEFICIARY_NAME, HttpStatus.BAD_REQUEST);
                                        }
                                        paymentAccount.setBeneficiaryName(beneficiaryName);

                                        String branch = data[j++];
                                        if (branch.length() > 10 || !branch.matches("^[A-Za-z]+$") || branch.trim().equals("")) {
                                            throw new BusinessException(ResponseCode.BRA_INVALID_BRANCH_NAME, HttpStatus.BAD_REQUEST);
                                        }
                                        paymentAccount.setBranch(branch);
                                        paymentAccount.setInstitution(institution);
                                        paymentAccount.setCreatedDate(new Date(new Date().getTime()));
                                        if (userDetails != null)
                                            paymentAccount.setCreatedBy(Integer.valueOf(userDetails.getId()).toString());
                                        entityPaymentAccounts.add(paymentAccount);
                                    }

                                    // Entity Contacts
                                    Integer contactsCount = this.isValidInteger(data[j++], ResponseCode.JOB_INVALID_CONTACTS_COUNT);
                                    List<Contact> entityContacts = new ArrayList<>();

                                    for (int n = 0; n < contactsCount; n++) {
                                        Contact contact = new Contact();
                                        if (j >= data.length)
                                            throw new BusinessException(ResponseCode.JOB_INVALID_CONTACTS_COUNT, HttpStatus.BAD_REQUEST);

                                        contact.setFirstName(data[j++]);
                                        contact.setLastName(data[j++]);
                                        contact.setMiddleName(data[j++]);
                                        contact.setProfessionalTitle(data[j++]);
                                        contact.setPhone(data[j++]);

                                        if (Objects.nonNull(data[j]) && !data[j].trim().equals("")) {
                                            contact.setReceiveEstatement(data[j++].charAt(0));
                                        } else {
                                            contact.setReceiveEstatement('N');
                                            j++;
                                        }

                                        if (j < data.length && Objects.nonNull(data[j]) && !data[j].trim().equals("")) {
                                            contact.setContactStatus(data[j++].charAt(0));
                                        } else {
                                            throw new BusinessException(ResponseCode.JOB_INVALID_STATUS, HttpStatus.BAD_REQUEST);
                                        }

                                        contact.setInstitution(institution);
                                        contact.setDate(new Date(new Date().getTime()));
                                        if (userDetails != null)
                                            contact.setUserCreate(Integer.valueOf(userDetails.getId()).toString());
                                        entityContacts.add(contact);
                                    }

                                    this.doRecordValidation(entityInfo, entityAddress, entityPaymentAccounts, entityContacts);

                                    // Save entity
                                    if(entitiesRepository.findByEntityIdAndInstitution(entityInfo.getEntityId(),institution).isPresent()){
                                        throw new BusinessException(ResponseCode.CFG_ENTITY_ID_ALREADY_EXISTS, HttpStatus.NOT_FOUND);
                                    }
                                    int nextValRecordSeqId = entitiesRepository.findEntitySeqNextValue();
                                    entityInfo.setRecord_seq_id(nextValRecordSeqId);
                                    entityInfo = this.entitiesRepository.save(entityInfo);

                                    // Save address
                                    entityAddress.setEntitiesObject(entityInfo);
                                    entityAddress.setEntities(entityInfo.getEntityId());

                                    entityAddress.setInstitution(institution);
                                    entityAddress = this.addressRepository.save(entityAddress);

                                    // Save payment accounts
                                    for (PaymentAccount paymentAcc : entityPaymentAccounts) {
                                        paymentAcc.setEntity(entityInfo.getEntityId());
                                        paymentAcc.setEntityObject(entityInfo);

                                        this.paymentAccountRepository.save(paymentAcc);
                                    }

                                    // Save contacts
                                    for (Contact contact : entityContacts) {
                                        contact.setEntity(entityInfo.getEntityId());
                                        contact.setEntityObject(entityInfo);

                                        contact.setAddress(entityAddress);
                                        this.contactRepository.save(contact);
                                    }

                                    ack.recordSuccess();

                                    processingEvent = new ProcessingEvents();
                                    processingEvent.setTaskExecutionLogId(savedTaskExecutionLog.getTaskExecutionLogId());
                                    processingEvent.setInstitutionId(institution.getInstitutionId());
                                    processingEvent.setProcessingProgram(TaskNameEnum.IMPORT_MERCHANTS.getValue());
                                    processingEvent.setFileName(file.getName());
                                    processingEvent.setExecutionTime(new Timestamp(System.currentTimeMillis()));
                                    processingEvent.setSuccessResult('S');
                                    processingEvent.setRemarks("Merchant: " + entityInfo.getEntityId() + " - " + entityInfo.getEntityName() + " was created.");
                                    if (userDetails != null) processingEvent.setCreatedBy(user);
                                    processingEvent.setCreatedDate(new Timestamp(System.currentTimeMillis()));
                                    this.processingEventsRepository.save(processingEvent);
                                }

                            } catch (Exception recordEx) {
                                handleRecordFailure(ack, savedTaskExecutionLog, institution, user, userDetails,
                                        TaskNameEnum.IMPORT_MERCHANTS.getValue(), file.getName(), lineNumber, rawLine, recordEx);
                            }
                        } // end while

                        if (ack.getFileSuccess() == 0) {
                            logger.debug("No valid merchant records found in file: " + file.getName());
                            runTaskResponseDto.setResult(-2);
                        }
                    }

                    ack.endFile(file.getName());
                    Files.copy(file.toPath(), new File(destinationFolder, file.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
                    Files.delete(file.toPath());
                }

                ack.writeGlobalSummary(files.length);

                savedTaskExecutionLog.setEndDatetime(new Timestamp(System.currentTimeMillis()));
                savedTaskExecutionLog.setTaskDetails("Import Merchants Successful");
                runTaskResponseDto.setTaskExecutionLogId(savedTaskExecutionLog.getTaskExecutionLogId());
                this.taskExecutionLogRepository.save(savedTaskExecutionLog);
                System.out.println("Merchant files uploaded and processed successfully.");
                runTaskResponseDto.setResult(0);

            } catch (IOException e) {
                savedTaskExecutionLog.setEndDatetime(new Timestamp(System.currentTimeMillis()));
                savedTaskExecutionLog.setTaskDetails("Import Merchants Failed");
                this.taskExecutionLogRepository.save(savedTaskExecutionLog);
                logger.error("Error processing files: " + e.getMessage());
                runTaskResponseDto.setResult(-3);
                runTaskResponseDto.setTaskExecutionLogId(savedTaskExecutionLog.getTaskExecutionLogId());

                processingEvent = new ProcessingEvents();
                processingEvent.setTaskExecutionLogId(savedTaskExecutionLog.getTaskExecutionLogId());
                processingEvent.setInstitutionId(institution.getInstitutionId());
                processingEvent.setProcessingProgram(TaskNameEnum.IMPORT_MERCHANTS.getValue());
                processingEvent.setExecutionTime(new Timestamp(System.currentTimeMillis()));
                processingEvent.setRemarks("Merchant import failure");
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
        String errorMsg = "Duplicate Key";
        try {
            errorMsg = databaseMessageSource.getMessageFromKey(ex.getMessage(), locale, true).get(0);
        } catch (Exception e) {
            errorMsg = "Duplicate Key";
        }
//        String errorMsg = ex.getMessage() != null ? ex.getMessage() : ex.getClass().getSimpleName();
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
    // Validate parent entity logic: Parent should be null if entity level is "Chain"
    private Entities validateParentEntity(String entityLevel, String parentEntity,Institution institution) {
        if ("CHAIN".equalsIgnoreCase(entityLevel)) {
            return null;
        }
        if (Objects.nonNull(parentEntity) && !parentEntity.trim().isEmpty()) {
            return this.entitiesRepository.findByEntityIdAndInstitution(parentEntity,institution)
                    .orElseThrow(() -> new BusinessException(ResponseCode.ENT_INVALID_ENTITY_PARENT_ID, HttpStatus.NOT_FOUND));
        }
        return null;
    }

    private Date parseDate(SimpleDateFormat dateFormat, String dateString,String errorCode) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null; // or throw, depending on contract
        }
        if (!dateString.matches("^[0-9/\\\\-]+$")) {
            throw new BusinessException(
                    errorCode.isBlank() ? ResponseCode.JOB_INVALID_DATE : errorCode,
                    HttpStatus.BAD_REQUEST
            );
        }
        try {
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

    public static boolean isLeap(int year) {
        return (((year % 4 == 0) &&
                (year % 100 != 0)) ||
                (year % 400 == 0));
    }

    public boolean isValidDate(int day, int month, int year) {
        if (month < 1 || month > 12) {
            return false;
        }
        if (year < 1900 || year > 2100) {
            return false;
        }
        if (day < 0 || day > 31) {
            return false;
        }

        if (month == 2) {
            if (isLeap(year) && day != 0 && day > 29) {
                return false;
            } else if (!isLeap(year) && day != 0 && day > 28) {
                return false;
            }
        }

        if (month == 4 || month == 6 || month == 9 || month == 11) {
            if (day != 0 && day > 30) {
                return false;
            }
        }
        return true;
    }

    private Double isValidDouble(String value) {
        try {
            if (Double.valueOf(value) >= 1000 || Double.valueOf(value) < 0) {
                throw new BusinessException(ResponseCode.JOB_INVALID_CHMARKUPNUMBER, HttpStatus.BAD_REQUEST);
            }
            Double val = Double.parseDouble(value);
            return val;
        } catch (NumberFormatException e) {
            throw new BusinessException(ResponseCode.JOB_INVALID_CHMARKUPNUMBER, HttpStatus.BAD_REQUEST);
        }
    }

    private Integer isValidInteger(String value, String errorMsg) {
        try {
            if (Integer.parseInt(value) > 999999999 || Integer.parseInt(value) < 0) {
                throw new BusinessException(errorMsg, HttpStatus.BAD_REQUEST);
            }
            return Integer.valueOf(value);
        } catch (NumberFormatException e) {
            throw new BusinessException(errorMsg, HttpStatus.BAD_REQUEST);
        }
    }

    private boolean doRecordValidation(Entities entityInfo, Address entityAddress, List<PaymentAccount> entityPaymentAccounts,
                                       List<Contact> entityContacts) {

        //Validation on entity id
        if (Objects.isNull(entityInfo.getEntityId()) || entityInfo.getEntityId().trim().equals("") ||
                (entityInfo.getEntityId().trim().length() > 30)) {
            throw new BusinessException(ResponseCode.JOB_INVALID_ENTITY_ID, HttpStatus.BAD_REQUEST);
        }

        //Validation on entity name
        if (Objects.isNull(entityInfo.getEntityName()) || entityInfo.getEntityName().trim().equals("") ||
                (entityInfo.getEntityName().trim().length() > 50 || !entityInfo.getEntityName().trim().matches("^[a-zA-Z0-9 _\\-&\".]*$"))) {
            throw new BusinessException(ResponseCode.ENT_INVALID_ENTITY_NAME, HttpStatus.BAD_REQUEST);
        }

        //Validation on alternate entity name
        if (Objects.isNull(entityInfo.getEntityNameAlt()) || entityInfo.getEntityNameAlt().trim().equals("") ||
                (entityInfo.getEntityNameAlt().trim().length() > 100 || !entityInfo.getEntityNameAlt().trim().matches("^[a-zA-Z0-9 @_]*$"))) {
            throw new BusinessException(ResponseCode.JOB_INVALID_ENTITY_NAME_ALT, HttpStatus.BAD_REQUEST);
        }

        //Validation on dba name
        if (Objects.nonNull(entityInfo.getDbaName()) && !entityInfo.getDbaName().trim().equals("") &&
                (entityInfo.getDbaName().trim().length() > 50 || !entityInfo.getDbaName().trim().matches("^[a-zA-Z0-9-@_]*$"))) {
            throw new BusinessException(ResponseCode.JOB_INVALID_DBA_NAME, HttpStatus.BAD_REQUEST);
        }

        //Validation on alternate dba name
        if (Objects.nonNull(entityInfo.getDbaNameAlt()) && !entityInfo.getDbaNameAlt().trim().equals("") &&
                (entityInfo.getDbaNameAlt().trim().length() > 100 )) {
            throw new BusinessException(ResponseCode.JOB_INVALID_DBA_NAME_ALT, HttpStatus.BAD_REQUEST);
        }

        //Validation on mcc
        if (Objects.isNull(entityInfo.getDefaultMCC()) || entityInfo.getDefaultMCC().trim().equals("") ||
                mccRepository.findByMcc(entityInfo.getDefaultMCC()).size() == 0) {
            throw new BusinessException(ResponseCode.JOB_INVALID_MCC, HttpStatus.BAD_REQUEST);
        }

        //Validation on company registry number
        if (Objects.nonNull(entityInfo.getCompanyRegisterNBR()) && !entityInfo.getCompanyRegisterNBR().trim().equals("") &&
                (entityInfo.getCompanyRegisterNBR().trim().length() > 30 || !entityInfo.getCompanyRegisterNBR().trim().matches("^[0-9]*$"))) {
            throw new BusinessException(ResponseCode.JOB_INVALID_COMPANY_REG_NUM, HttpStatus.BAD_REQUEST);
        }

        //Validation on company vat number
        if (Objects.nonNull(entityInfo.getCompanyVatNBR()) && !entityInfo.getCompanyVatNBR().trim().equals("") &&
                (entityInfo.getCompanyVatNBR().trim().length() > 30 || !entityInfo.getCompanyVatNBR().trim().matches("^[0-9]*$"))) {
            throw new BusinessException(ResponseCode.JOB_INVALID_COMPANY_VAT_NUM, HttpStatus.BAD_REQUEST);
        }

        //Validation on contract date
        if (Objects.isNull(entityInfo.getContractDate())) {
            throw new BusinessException(ResponseCode.JOB_INVALID_CONTRACT_DATE, HttpStatus.BAD_REQUEST);
        }

        //Validation on actual start date
        if (Objects.isNull(entityInfo.getActualStartDate())) {
            throw new BusinessException(ResponseCode.JOB_INVALID_ACTUAL_START_DATE, HttpStatus.BAD_REQUEST);
        }

        //Validation on payment method
        if (Objects.nonNull(entityInfo.getPaymentMethod()) && !entityInfo.getPaymentMethod().trim().equals("") &&
                (entityInfo.getPaymentMethod().trim().length() > 1 || !entityInfo.getPaymentMethod().trim().matches("^[T|C]*$"))) {
            throw new BusinessException(ResponseCode.JOB_INVALID_PAYMENT_METHOD, HttpStatus.BAD_REQUEST);
        }

        //Validation on payment frequency
        if (Objects.nonNull(entityInfo.getPaymentFrequency()) && !entityInfo.getPaymentFrequency().trim().equals("") &&
                (entityInfo.getPaymentFrequency().trim().length() > 1 || !entityInfo.getPaymentFrequency().trim().matches("^[D|W|M]*$"))) {
            throw new BusinessException(ResponseCode.JOB_INVALID_PAYMENT_FREQUENCY, HttpStatus.BAD_REQUEST);
        }

        if (entityInfo.getAddValueDateDays() < 0 || entityInfo.getAddValueDateDays() > 999999999) {
            throw new BusinessException(ResponseCode.JOB_INVALID_ADD_VALUE_DATE_DAYS, HttpStatus.BAD_REQUEST);
        }

        //Validation on statement type
        if (Objects.nonNull(entityInfo.getStatementType()) && !String.valueOf(entityInfo.getStatementType()).trim().equals("") &&
                (String.valueOf(entityInfo.getStatementType()).trim().length() > 1 || !String.valueOf(entityInfo.getStatementType()).trim().matches("^[P|B|E]*$"))) {
            throw new BusinessException(ResponseCode.JOB_INVALID_STATEMENT_TYPE, HttpStatus.BAD_REQUEST);
        }

        //Validation on on hold indicator
        if (Objects.nonNull(entityInfo.getOnHoldInd()) && !String.valueOf(entityInfo.getOnHoldInd()).trim().equals("") &&
                (String.valueOf(entityInfo.getOnHoldInd()).trim().length() > 1 || !String.valueOf(entityInfo.getOnHoldInd()).trim().matches("^[Y|N]*$"))) {
            throw new BusinessException(ResponseCode.JOB_INVALID_ON_HOLD, HttpStatus.BAD_REQUEST);
        }

        //Validation on associated payment
        if (Objects.nonNull(entityInfo.getAssociatedPayment()) && !String.valueOf(entityInfo.getAssociatedPayment()).trim().equals("") &&
                (String.valueOf(entityInfo.getAssociatedPayment()).trim().length() > 1 || !String.valueOf(entityInfo.getAssociatedPayment()).trim().matches("^[Y|N]*$"))) {
            throw new BusinessException(ResponseCode.JOB_INVALID_ASSOCIATED_PAYMENT, HttpStatus.BAD_REQUEST);
        }

        //Validation on hot merchant flag
        if (Objects.nonNull(entityInfo.getHotMerchantFlag()) && !String.valueOf(entityInfo.getHotMerchantFlag()).trim().equals("") &&
                (String.valueOf(entityInfo.getHotMerchantFlag()).trim().length() > 1 || !String.valueOf(entityInfo.getHotMerchantFlag()).trim().matches("^[Y|N]*$"))) {
            throw new BusinessException(ResponseCode.JOB_INVALID_HOT_MERCHANTFLAG, HttpStatus.BAD_REQUEST);
        }

        //Validation on status
        if (Objects.isNull(entityInfo.getStatus()) || String.valueOf(entityInfo.getStatus()).trim().equals("") ||
                (String.valueOf(entityInfo.getStatus()).trim().length() > 1 || !String.valueOf(entityInfo.getStatus()).trim().matches("^[0|1]*$"))) {
            throw new BusinessException(ResponseCode.JOB_INVALID_STATUS, HttpStatus.BAD_REQUEST);
        }

        //Validation on phone 1
		if (Objects.isNull(entityAddress.getPhone1()) || entityAddress.getPhone1().trim().equals("") ||
				(entityAddress.getPhone1().trim().length() > 20 || !entityAddress.getPhone1().trim().matches("^[0-9-]*$"))) {
			throw new BusinessException(ResponseCode.JOB_INVALID_PHONE1, HttpStatus.BAD_REQUEST);
		}

        //Validation on phone 2
		if (Objects.nonNull(entityAddress.getPhone2()) && !entityAddress.getPhone2().trim().equals("") &&
				(entityAddress.getPhone2().trim().length() > 20 || !entityAddress.getPhone2().trim().matches("^[0-9-]*$"))) {
			throw new BusinessException(ResponseCode.JOB_INVALID_PHONE2, HttpStatus.BAD_REQUEST);
		}

        //Validation on mobile 1
		if (Objects.isNull(entityAddress.getMobile1()) || entityAddress.getMobile1().trim().equals("") ||
				(entityAddress.getMobile1().trim().length() > 20 || !entityAddress.getMobile1().trim().matches("^[0-9-]*$"))) {
			throw new BusinessException(ResponseCode.JOB_INVALID_MOBILE1, HttpStatus.BAD_REQUEST);
		}

        //Validation on mobile 2
		if (Objects.nonNull(entityAddress.getMobile2()) && !entityAddress.getMobile2().trim().equals("") &&
				(entityAddress.getMobile2().trim().length() > 20 || !entityAddress.getMobile2().trim().matches("^[0-9-]*$"))) {
			throw new BusinessException(ResponseCode.JOB_INVALID_MOBILE2, HttpStatus.BAD_REQUEST);
		}

        //Validation on address 1
        if (Objects.nonNull(entityAddress.getAddress1()) && !entityAddress.getAddress1().trim().equals("") &&
                (entityAddress.getAddress1().trim().length() > 50 || !entityAddress.getAddress1().trim().matches("^[a-zA-Z0-9-@_ ]*$"))) {
            throw new BusinessException(ResponseCode.JOB_INVALID_ADDRESS1, HttpStatus.BAD_REQUEST);
        }

        //Validation on address 2
        if (Objects.nonNull(entityAddress.getAddress2()) && !entityAddress.getAddress2().trim().equals("") &&
                (entityAddress.getAddress2().trim().length() > 50 || !entityAddress.getAddress2().trim().matches("^[a-zA-Z0-9-@_ ]*$"))) {
            throw new BusinessException(ResponseCode.JOB_INVALID_ADDRESS2, HttpStatus.BAD_REQUEST);
        }

        //Validation on address 3
        if (Objects.nonNull(entityAddress.getAddress3()) && !entityAddress.getAddress3().trim().equals("") &&
                (entityAddress.getAddress3().trim().length() > 50 || !entityAddress.getAddress3().trim().matches("^[a-zA-Z0-9-@_ ]*$"))) {
            throw new BusinessException(ResponseCode.JOB_INVALID_ADDRESS3, HttpStatus.BAD_REQUEST);
        }

        //Validation on address 4
        if (Objects.nonNull(entityAddress.getAddress4()) && !entityAddress.getAddress4().trim().equals("") &&
                (entityAddress.getAddress4().trim().length() > 50 || !entityAddress.getAddress4().trim().matches("^[a-zA-Z0-9-@_ ]*$"))) {
            throw new BusinessException(ResponseCode.JOB_INVALID_ADDRESS4, HttpStatus.BAD_REQUEST);
        }

        //Validation on fax
        if (Objects.nonNull(entityAddress.getFax()) && !entityAddress.getFax().trim().equals("") &&
                entityAddress.getFax().trim().length() > 30) {
            throw new BusinessException(ResponseCode.JOB_INVALID_FAX, HttpStatus.BAD_REQUEST);
        }

        //Validation on URL
        if (Objects.nonNull(entityAddress.getUrl()) && !entityAddress.getUrl().trim().equals("") &&
                entityAddress.getUrl().trim().length() > 255) {
            throw new BusinessException(ResponseCode.JOB_INVALID_URL, HttpStatus.BAD_REQUEST);
        }

        //Validation on postal code zip
        if (Objects.nonNull(entityAddress.getPostalCodeZip()) && !entityAddress.getPostalCodeZip().trim().equals("") &&
                (entityAddress.getPostalCodeZip().trim().length() > 20 || !entityAddress.getPostalCodeZip().trim().matches("^[0-9-_.+]*$"))) {
            throw new BusinessException(ResponseCode.JOB_INVALID_POSTAL_CODE, HttpStatus.BAD_REQUEST);
        }

        //Validation on geo code
        if (Objects.nonNull(entityAddress.getGeoCode()) && !entityAddress.getGeoCode().trim().equals("") &&
                (entityAddress.getGeoCode().trim().length() > 16 || !entityAddress.getGeoCode().trim().matches("^[0-9-_.+]*$"))) {
            throw new BusinessException(ResponseCode.JOB_INVALID_GEO_CODE, HttpStatus.BAD_REQUEST);
        }

        // Validation on email address 1
        if (Objects.nonNull(entityAddress.getEmailAddress1()) && !entityAddress.getEmailAddress1().trim().equals("") &&
                (entityAddress.getEmailAddress1().trim().length() > 100 ||
                        !entityAddress.getEmailAddress1().trim().matches("^(?:([A-Za-z0-9_\\-.])+@([A-Za-z0-9_\\-.])+\\.([A-Za-z_-]{2,6})|)$"))) {
            throw new BusinessException(ResponseCode.JOB_INVALID_EMAIL1, HttpStatus.BAD_REQUEST);
        }

        // Validation on email address 2
        if (Objects.nonNull(entityAddress.getEmailAddress2()) && !entityAddress.getEmailAddress2().trim().equals("") &&
                (entityAddress.getEmailAddress2().trim().length() > 100 ||
                        !entityAddress.getEmailAddress2().trim().matches("^(?:([A-Za-z0-9_\\-.])+@([A-Za-z0-9_\\-.])+\\.([A-Za-z_-]{2,6})|)$"))) {
            throw new BusinessException(ResponseCode.JOB_INVALID_EMAIL2, HttpStatus.BAD_REQUEST);
        }
        
        for (PaymentAccount entityPaymentAccount : entityPaymentAccounts) {
            //Validation on account number
            if (Objects.isNull(entityPaymentAccount.getAccountNumber()) || entityPaymentAccount.getAccountNumber().trim().equals("") ||
                    (entityPaymentAccount.getAccountNumber().trim().length() > 30 || !entityPaymentAccount.getAccountNumber().trim().matches("^[A-Z0-9]*$"))) {
                throw new BusinessException(ResponseCode.JOB_INVALID_ACCOUNT, HttpStatus.BAD_REQUEST);
            }

            //Validation on iban
            if (Objects.isNull(entityPaymentAccount.getIban()) || entityPaymentAccount.getIban().trim().equals("") ||
                    entityPaymentAccount.getIban().trim().length() > 30 || !entityPaymentAccount.getIban().trim().matches("^[A-Z0-9]*$")) {
                throw new BusinessException(ResponseCode.JOB_INVALID_DEF_IBAN, HttpStatus.BAD_REQUEST);
            }
            
            if (entityPaymentAccount.getBeneficiaryName().trim().length() > 100 ) {
                throw new BusinessException(ResponseCode.JOB_INVALID_BENEFICIARY_NAME, HttpStatus.BAD_REQUEST);
            }
        }

        for (Contact entityContact : entityContacts) {
            //Validation on first name
            if (Objects.isNull(entityContact.getFirstName()) || entityContact.getFirstName().trim().equals("") ||
                    (entityContact.getFirstName().trim().length() > 25 || !entityContact.getFirstName().trim().matches("^[a-zA-Z- ]*$"))) {
                throw new BusinessException(ResponseCode.JOB_INVALID_FIRST_NAME, HttpStatus.BAD_REQUEST);
            }

            //Validation on last name
            if (Objects.isNull(entityContact.getLastName()) || entityContact.getLastName().trim().equals("") ||
                    (entityContact.getLastName().trim().length() > 20 || !entityContact.getLastName().trim().matches("^[a-zA-Z- ]*$"))) {
                throw new BusinessException(ResponseCode.JOB_INVALID_LAST_NAME, HttpStatus.BAD_REQUEST);
            }

            //Validation on middle name
            if (Objects.isNull(entityContact.getMiddleName()) || entityContact.getMiddleName().trim().equals("") ||
                    (entityContact.getMiddleName().trim().length() > 20 || !entityContact.getMiddleName().trim().matches("^[a-zA-Z- ]*$"))) {
                throw new BusinessException(ResponseCode.JOB_INVALID_MIDDLE_NAME, HttpStatus.BAD_REQUEST);
            }

            //Validation on professional title
            if (Objects.nonNull(entityContact.getProfessionalTitle()) && !entityContact.getProfessionalTitle().trim().equals("") &&
                    (entityContact.getProfessionalTitle().trim().length() > 50 || !entityContact.getProfessionalTitle().trim().matches("^[a-zA-Z0-9- ]*$"))) {
                throw new BusinessException(ResponseCode.JOB_INVALID_PROF_TITLE, HttpStatus.BAD_REQUEST);
            }

            //Validation on phone
			if (Objects.nonNull(entityContact.getPhone()) && !entityContact.getPhone().trim().equals("") &&
					(entityContact.getPhone().trim().length() > 20 || !entityContact.getPhone().trim().matches("^[0-9-]*$"))) {
				throw new BusinessException(ResponseCode.INVALID_PHONE, HttpStatus.BAD_REQUEST);
			}

            //Validation on receive e statement flag
            if (Objects.nonNull(entityContact.getReceiveEstatement()) && !String.valueOf(entityContact.getReceiveEstatement()).trim().equals("") &&
                    (String.valueOf(entityContact.getReceiveEstatement()).trim().length() > 1 || !String.valueOf(entityContact.getReceiveEstatement()).trim().matches("^[Y|N]*$"))) {
                throw new BusinessException(ResponseCode.JOB_INVALID_ESTATEMENT_TO_ENTITY_ID, HttpStatus.BAD_REQUEST);
            }

            //Validation on status
            if (Objects.isNull(entityContact.getContactStatus()) || String.valueOf(entityContact.getContactStatus()).trim().equals("") ||
                    (String.valueOf(entityContact.getContactStatus()).trim().length() > 1 || !String.valueOf(entityContact.getContactStatus()).trim().matches("^[0|1]*$"))) {
                throw new BusinessException(ResponseCode.JOB_INVALID_STATUS, HttpStatus.BAD_REQUEST);
            }
        }

        return true;

    }

}
