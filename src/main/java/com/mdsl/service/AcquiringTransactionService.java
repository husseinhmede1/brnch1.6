package com.mdsl.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.mdsl.model.dto.request.*;
import com.mdsl.utils.MakerCheckerEngine;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.response.AcquiringTransactionHaltResponseDto;
import com.mdsl.model.dto.response.AcquiringTransactionResponseDto;
import com.mdsl.model.dto.response.PaginationResponseDto;
import com.mdsl.model.dto.response.TransactionCurrentSearchResponseDto;
import com.mdsl.model.entity.AcquiringTransaction;
import com.mdsl.model.entity.CardScheme;
import com.mdsl.model.entity.Currency;
import com.mdsl.model.entity.DefaultTransactionId;
import com.mdsl.model.entity.Entities;
import com.mdsl.model.entity.Institution;
import com.mdsl.model.entity.SystemCode;
import com.mdsl.model.entity.Terminal;
import com.mdsl.model.entity.TransactionCurrent;
import com.mdsl.model.mapper.AcquiringTransactionMapper;
import com.mdsl.model.mapper.TransactionCurrentMapper;
import com.mdsl.repository.AcquiringTransactionHoldRepository;
import com.mdsl.repository.AcquiringTransactionRepository;
import com.mdsl.repository.CardSchemeRepository;
import com.mdsl.repository.CurrencyRepository;
import com.mdsl.repository.DefaultTransactionIdRepository;
import com.mdsl.repository.EntitiesRepository;
import com.mdsl.repository.InstitutionRepository;
import com.mdsl.repository.SystemCodeRepository;
import com.mdsl.repository.TerminalRepository;
import com.mdsl.repository.TransactionCurrentRepository;
import com.mdsl.utils.PaginationCommonCode;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.enumerations.HaltTypes;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AcquiringTransactionService {

    private final InstitutionRepository institutionRepository;

    private final EntitiesRepository entitiesRepository;

    private final TerminalRepository terminalRepository;

    private final AcquiringTransactionRepository acquiringTransactionRepository;

    private final CardSchemeRepository cardSchemeRepository;

    private final AcquiringTransactionMapper acquiringTransactionMapper;

    private final TransactionCurrentMapper transactionCurrentMapper;

    private final CurrencyRepository currencyRepository;

    private final SystemCodeRepository systemCodeRepository;

    private final DefaultTransactionIdRepository defaultTransactionIdRepository;

    private final TransactionCurrentRepository transactionCurrentRepository;

    private final AcquiringTransactionHoldRepository acquiringTransactionHoldRepository;

    private final MakerCheckerEngine makerCheckerEngine;


    public AcquiringTransactionResponseDto saveOrUpdateAcquiringTransaction(
            AcquiringTransactionRequestDto acquiringTransactionRequestDto) throws ParseException {

        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        AcquiringTransactionResponseDto acquiringTransactionResponseDto;

        Currency transactionCurrency = currencyRepository
                .findById(acquiringTransactionRequestDto.getTransactionCurrencyId())
                .orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_CURRENCY, HttpStatus.NOT_FOUND));

        Currency billingCurrency = currencyRepository
                .findById(acquiringTransactionRequestDto.getBillingCurrencyId())
                .orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_CURRENCY, HttpStatus.NOT_FOUND));

        Currency localCurrency = currencyRepository
                .findById(acquiringTransactionRequestDto.getLocalCurrencyId())
                .orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_CURRENCY, HttpStatus.NOT_FOUND));

        Currency tipsCurrency = currencyRepository
                .findById(acquiringTransactionRequestDto.getTipsCurrencyId())
                .orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_CURRENCY, HttpStatus.NOT_FOUND));

        Currency dccCurrency = currencyRepository
                .findById(acquiringTransactionRequestDto.getDccCurrencyId())
                .orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_CURRENCY, HttpStatus.NOT_FOUND));

        Currency dccMerchantSettleCurrency = currencyRepository
                .findById(acquiringTransactionRequestDto.getDccMerchantSettlAmountCurrencyId())
                .orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_CURRENCY, HttpStatus.NOT_FOUND));

        Currency feeAmnt1Currency = currencyRepository
                .findById(acquiringTransactionRequestDto.getFeeAmount1CurrencyId())
                .orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_CURRENCY, HttpStatus.NOT_FOUND));

        Currency feeAmnt2Currency = currencyRepository
                .findById(acquiringTransactionRequestDto.getFeeAmount2CurrencyId())
                .orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_CURRENCY, HttpStatus.NOT_FOUND));

        Currency settlementCurrency = currencyRepository
                .findById(acquiringTransactionRequestDto.getSettlementCurrencyId())
                .orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_CURRENCY, HttpStatus.NOT_FOUND));

        Currency merchantAccountCurrency = currencyRepository
                .findById(acquiringTransactionRequestDto.getMerchantAccountCurrId())
                .orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_CURRENCY, HttpStatus.NOT_FOUND));

        Institution institution = institutionRepository
                .findById(acquiringTransactionRequestDto.getInstitutionId())
                .orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_INST, HttpStatus.NOT_FOUND));

        Entities entities = entitiesRepository
                .findByEntityIdAndInstitution(acquiringTransactionRequestDto.getEntitiesId(), institution)
                .orElseThrow(() -> new BusinessException(ResponseCode.INVALID_ENTITY_ID, HttpStatus.NOT_FOUND));

        Terminal terminal = terminalRepository.findByTerminalIdAndInstitutionEntity_InstitutionId(acquiringTransactionRequestDto.getTerminalId(), institution.getInstitutionId())
                .orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_TERMINAL_ID, HttpStatus.NOT_FOUND));

        SystemCode systemCode = systemCodeRepository
                .findById(acquiringTransactionRequestDto.getSystemCodeId())
                .orElseThrow(() -> new BusinessException(ResponseCode.INVALID_SYSTEM_CODE_ID, HttpStatus.NOT_FOUND));

        CardScheme cardScheme = cardSchemeRepository
                .findById(acquiringTransactionRequestDto.getCardSchemeId())
                .orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_CARDSCHEME_ID, HttpStatus.NOT_FOUND));

        DefaultTransactionId defaultTransactionId = defaultTransactionIdRepository
                .findByTransactionIdAndInstitution(acquiringTransactionRequestDto.getTransactionId(), institution)
                .orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_DEFAULT_TRANSACTION, HttpStatus.NOT_FOUND));
        if (makerCheckerEngine.processIfRequired(acquiringTransactionRequestDto, this.getClass().getName(), new Object() {}.getClass().getEnclosingMethod().getName(), "")) {
            return null;
        }

        // ----------------- Prepare entity for save/update -----------------
        AcquiringTransaction acquiringTransaction = acquiringTransactionMapper.toEntity(acquiringTransactionRequestDto);
        acquiringTransaction.setInstitution(institution);
        acquiringTransaction.setTerminal(terminal.getTerminalId());

        acquiringTransaction.setEntities(entities.getEntityId());
        acquiringTransaction.setEntitiesObject(entities);

        acquiringTransaction.setTransactionCurrency(transactionCurrency);
        acquiringTransaction.setBillingCurrency(billingCurrency);
        acquiringTransaction.setLocalCurrency(localCurrency);
        acquiringTransaction.setTipsCurrency(tipsCurrency);
        acquiringTransaction.setDccCurrency(dccCurrency);
        acquiringTransaction.setDccMerchantSettlAmountCurrency(dccMerchantSettleCurrency);
        acquiringTransaction.setFeeAmount1Currency(feeAmnt1Currency);
        acquiringTransaction.setFeeAmount2Currency(feeAmnt2Currency);
        acquiringTransaction.setSettlementCurrency(settlementCurrency);
        acquiringTransaction.setMerchantAccountCurr(merchantAccountCurrency);
        acquiringTransaction.setReasonCode(systemCode);
        acquiringTransaction.setCardScheme(cardScheme);
        acquiringTransaction.setTransactionId(defaultTransactionId);

        if (userDetails != null) {
            acquiringTransaction.setUserCreate(Integer.valueOf(userDetails.getId()).toString());
        }

        acquiringTransaction.setDateCreate(new Date());

        try {
            acquiringTransaction = acquiringTransactionRepository.save(acquiringTransaction);
        } catch (Exception ex) {
            throw new BusinessException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        acquiringTransactionResponseDto = acquiringTransactionMapper.toDto(acquiringTransaction);
        return acquiringTransactionResponseDto;
    }

    public ResponseEntity<PaginationResponseDto> viewAcquiringTransaction(
            AcquiringTransactionRequestDto acquiringTransactionRequestDto) {

        try {
            PaginationCommonCode paginationCommonCode = new PaginationCommonCode();

            PageRequest pageRequest = paginationCommonCode.getPageRequestForPagination(
                    acquiringTransactionRequestDto.getSort(),
                    "acquiringTransactionId",
                    acquiringTransactionRequestDto.getPageNo(),
                    acquiringTransactionRequestDto.getPageSize()
            );

            Page<AcquiringTransaction> page = acquiringTransactionRepository.findAll(pageRequest);

            List<AcquiringTransactionResponseDto> acquiringTransactionResponseDtos = page.getContent().stream()
                    .map(acquiringTransaction -> {
                        AcquiringTransactionResponseDto dto = acquiringTransactionMapper.toDto(acquiringTransaction);
                        dto.setInstitutionId(acquiringTransaction.getAcqInstitutionId().getInstitutionId());
                        return dto;
                    })
                    .collect(Collectors.toList());

            PaginationResponseDto responseDto = new PaginationResponseDto(
                    true,
                    null,
                    acquiringTransactionResponseDtos,
                    page.getTotalPages(),
                    page.getTotalElements()
            );

            return new ResponseEntity<>(responseDto, HttpStatus.OK);

        } catch (Exception ex) {
            throw new BusinessException(
                    ResponseCode.CFG_INVALID_ACQUIRING_TRANSACTION + ": " + ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    public TransactionCurrentSearchResponseDto getAcquiringTransaction(int acquiringTransactionId) {
        TransactionCurrent transactionCurrent = transactionCurrentRepository.findById(acquiringTransactionId)
                .orElseThrow(() -> new BusinessException(
                        ResponseCode.CFG_INVALID_ACQUIRING_TRANSACTION_ID,
                        HttpStatus.NOT_FOUND
                ));

        return transactionCurrentMapper.toSearchDto(transactionCurrent);
    }

    public void deleteAcquiringTransaction(int acquiringTransactionId) {
        // Ensure the entity exists, otherwise throw exception
        acquiringTransactionRepository.findById(acquiringTransactionId)
                .orElseThrow(() -> new BusinessException(
                        ResponseCode.CFG_INVALID_ACQUIRING_TRANSACTION_ID,
                        HttpStatus.NOT_FOUND
                ));
        if (makerCheckerEngine.processIfRequired(acquiringTransactionId, this.getClass().getName(), new Object() {}.getClass().getEnclosingMethod().getName(), "")) {
            return;
        }
        acquiringTransactionRepository.deleteById(acquiringTransactionId);
    }

    public TransactionCurrentSearchResponseDto saveOrUpdateRepresentment(
            AcquiringTransactionRequestDto acquiringTransactionRequestDto) throws ParseException, CloneNotSupportedException {

        SystemCode systemCode = systemCodeRepository.findById(acquiringTransactionRequestDto.getSystemCodeId())
                .orElseThrow(() -> new BusinessException(ResponseCode.INVALID_SYSTEM_CODE_ID, HttpStatus.NOT_FOUND));

        TransactionCurrent transactionCurrent = transactionCurrentRepository
                .findById(acquiringTransactionRequestDto.getAcquiringTransactionId())
                .orElseThrow(() -> new BusinessException(
                        ResponseCode.CFG_INVALID_ACQUIRING_TRANSACTION_ID, HttpStatus.NOT_FOUND));

        Institution institution = institutionRepository.findById(acquiringTransactionRequestDto.getInstitutionId())
                .orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_INST, HttpStatus.NOT_FOUND));
        if (makerCheckerEngine.processIfRequired(acquiringTransactionRequestDto, this.getClass().getName(), new Object() {}.getClass().getEnclosingMethod().getName(), "")) {
            return null;
        }

        if (transactionCurrent.getUsageCode() == 0) {
            transactionCurrent.setChargebackReason(systemCode.getCodeSuffix());
            transactionCurrent.setChargebackComment(acquiringTransactionRequestDto.getChargebackComment());
            transactionCurrent.setSettlMerchHalt(acquiringTransactionRequestDto.getSettlMerchHalt());
            transactionCurrent.setAcquirerRecordToAppear(acquiringTransactionRequestDto.getAccquierRecordToAppear());
            transactionCurrent.setUsageCode(2);
            transactionCurrent.setProcessingDate(null);
            transactionCurrent.setSettlProcessingFlag('N');
            transactionCurrent.setReasonCode(systemCode.getSystemCodeId());
            transactionCurrent.setRefNumberSeq(transactionCurrent.getRefNumberSeq() + 1);
            transactionCurrent.setAcqInstId(institution.getInstitutionId());

            transactionCurrent = transactionCurrentRepository.save(transactionCurrent);
        }

        TransactionCurrentSearchResponseDto dto = transactionCurrentMapper.toSearchDto(transactionCurrent);
        dto.setInstitutionId(transactionCurrent.getAcqInstId());

        return dto;
    }

    public List<AcquiringTransactionResponseDto> unhaltTransaction(UnhaltRequestDto unhaltRequestDto) {

        Institution institution = institutionRepository.findById(unhaltRequestDto.getInstitutionId())
                .orElseThrow(() -> new BusinessException(
                        ResponseCode.CFG_INVALID_INST, HttpStatus.NOT_FOUND));
        if (makerCheckerEngine.processIfRequired(unhaltRequestDto, this.getClass().getName(), new Object() {}.getClass().getEnclosingMethod().getName(), "")) {
            return null;
        }
        acquiringTransactionRepository.UnhaltTransactions(unhaltRequestDto.getAcquiringTransactionIds());

        List<AcquiringTransaction> updatedTransactions = acquiringTransactionRepository
                .findAllById(unhaltRequestDto.getAcquiringTransactionIds());


        List<AcquiringTransactionResponseDto> responseDtoList = updatedTransactions.stream()
                .map(acqTrans -> {
                    AcquiringTransactionResponseDto dto = acquiringTransactionMapper.toDto(acqTrans);
                    dto.setInstitutionId(institution.getInstitutionId());
                    return dto;
                })
                .collect(Collectors.toList());

        return responseDtoList;
    }

    public String validateHaltTransaction(AcquiringTransaction acquiringTransaction) {
        Integer recSeqId = acquiringTransaction.getAcquiringTransactionId();

        if (acquiringTransaction.getSettlProcessingFlag().equals('N')) {
            //ALLOW
            return null;
        } else if (acquiringTransaction.getSettlProcessingFlag().equals('Y')) {
            if (acquiringTransactionRepository.validateAcountingLedger(acquiringTransaction.getAcquiringTransactionId().toString()).equals(0)) {
                //ALLOW
                return null;
            } else {
                //DECLINE
                return "Transaction with record ID : " + recSeqId + ", accounting entries already generated for this transaction. You must revert related accounting entries before halting this transaction - Settlement processing flag is 'Y'";
            }
        } else if (acquiringTransaction.getSettlProcessingFlag().equals('P')) {
            //DECLINE
            return "Transaction with record ID: " + recSeqId + ", is already halted - Settlement processing flag is 'P' ";
        } else {
            //DECLINE - ERROR
            return "Unexpected case check system admin";
        }
    }

    @Transactional
    public AcquiringTransactionHaltResponseDto saveOrUpdateHaltPayTransaction(AcquiringTransactionRequestDto acquiringTransactionRequestDto) {

        AcquiringTransactionHaltResponseDto responseDtoList = new AcquiringTransactionHaltResponseDto();
        List<AcquiringTransactionResponseDto> acquiringTransactionResponseDto = new ArrayList<>();

        List<Integer> transactionIds = acquiringTransactionRequestDto.getAcquiringTransactionIds();
        Character haltMerchPay = acquiringTransactionRequestDto.getPayHaltStatus().equalsIgnoreCase(HaltTypes.PERMANENT.getValue()) ? 'Y' : 'N';
        Character settleProcessionFlag = 'P';

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String formattedNowDate = LocalDateTime.now().format(formatter);

        Institution institution = institutionRepository.findById(acquiringTransactionRequestDto.getInstitutionId())
                .orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_INST, HttpStatus.NOT_FOUND));

        List<String> errorResponses = new ArrayList<String>();
        String validationResponse = null;
        boolean locked = true;
        Map<AcquiringTransaction, Pair<UpdateAqcuiringTransactionDto, InsetHoldTransactionBackUpAndDeleteOriginalDto>> tobeSaved = new HashMap<>();
        for (Integer transactionId : transactionIds) {
            locked = true;
            AcquiringTransaction acquiringTransaction = acquiringTransactionRepository.findById(transactionId).orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_ACQUIRING_TRANSACTION_ID, HttpStatus.NOT_FOUND));

            validationResponse = validateHaltTransaction(acquiringTransaction);
            if (validationResponse != null) {
                errorResponses.add(validationResponse);
                locked = true;
            } else {
                locked = false;
            }
            if (!locked) {
                Integer processed = acquiringTransactionRepository.findProcessed(acquiringTransaction.getAcquiringTransactionId());
                if (processed > 0) {
                    throw new BusinessException(ResponseCode.CFG_INVALID_ACQUIRING_TRANSACTION_ID, HttpStatus.CONFLICT);
                }
                acquiringTransaction.setSettlMerchHalt(haltMerchPay);
                acquiringTransaction.setPayHaltComment(acquiringTransactionRequestDto.getPayHaltComment());
                acquiringTransaction.setConfirmStoppingPayment(acquiringTransactionRequestDto.getConfirmStoppingPayment());
                acquiringTransaction.setInstitution(institution);


                String processingRefNbr5 = "MANUAL_HOLD_" + formattedNowDate + "_" + haltMerchPay;
                UpdateAqcuiringTransactionDto updateAqcuiringTransactionDto = new UpdateAqcuiringTransactionDto();
                updateAqcuiringTransactionDto.setHaltMerchPay(haltMerchPay);
                updateAqcuiringTransactionDto.setLinkupCode(acquiringTransaction.getLinkupCode());
                updateAqcuiringTransactionDto.setSettlProcessingFlag(settleProcessionFlag);
                updateAqcuiringTransactionDto.setProcessingRefNbr5(processingRefNbr5);
                updateAqcuiringTransactionDto.setMicrofilmRefNbr(acquiringTransaction.getMicrofilmRefNbr());
                updateAqcuiringTransactionDto.setTerminalId(acquiringTransaction.getTerminal());
                updateAqcuiringTransactionDto.setBatchId(acquiringTransaction.getBatchId());
                updateAqcuiringTransactionDto.setOutletCode(acquiringTransaction.getEntities());


                InsetHoldTransactionBackUpAndDeleteOriginalDto insetHoldTransactionBackUpAndDeleteOriginalDto = new InsetHoldTransactionBackUpAndDeleteOriginalDto();
                insetHoldTransactionBackUpAndDeleteOriginalDto.setMicrofilmRefNbr(acquiringTransaction.getMicrofilmRefNbr());
                insetHoldTransactionBackUpAndDeleteOriginalDto.setEntityId(acquiringTransaction.getEntities());
                insetHoldTransactionBackUpAndDeleteOriginalDto.setTerminalId(acquiringTransaction.getTerminal());
                insetHoldTransactionBackUpAndDeleteOriginalDto.setBatchId(acquiringTransaction.getBatchId());
                insetHoldTransactionBackUpAndDeleteOriginalDto.setLinkupCode(acquiringTransaction.getLinkupCode());
                insetHoldTransactionBackUpAndDeleteOriginalDto.setProcessingRefNbr5(processingRefNbr5);
                insetHoldTransactionBackUpAndDeleteOriginalDto.setSettleProcessionFlag(settleProcessionFlag);
                insetHoldTransactionBackUpAndDeleteOriginalDto.setHaltMerchPay(haltMerchPay);
                Pair<UpdateAqcuiringTransactionDto, InsetHoldTransactionBackUpAndDeleteOriginalDto> pair = Pair.of(updateAqcuiringTransactionDto, insetHoldTransactionBackUpAndDeleteOriginalDto);
                tobeSaved.put(acquiringTransaction, pair);
                AcquiringTransactionResponseDto dto = acquiringTransactionMapper.toDto(acquiringTransaction);
                dto.setConfirmStoppingPayment(acquiringTransaction.getConfirmStoppingPayment());
                dto.setInstitutionId(acquiringTransaction.getAcqInstitutionId().getInstitutionId());

                acquiringTransactionResponseDto.add(dto);

            }
        }
        if (makerCheckerEngine.processIfRequired(acquiringTransactionRequestDto, this.getClass().getName(), new Object() {}.getClass().getEnclosingMethod().getName(), "")) {
            return null;
        }
        tobeSaved.forEach((key, value) -> {
            acquiringTransactionRepository.saveAndFlush(key);
            updateAcquiringTransaction(value.getFirst());
            insetHoldTransactionBackUpAndDeleteOriginal(value.getSecond());
        });
        responseDtoList.setAcquiringTransactionResponseDto(acquiringTransactionResponseDto);
        responseDtoList.setErrorMessages(errorResponses);
        return responseDtoList;
    }


    private void insetHoldTransactionBackUpAndDeleteOriginal(InsetHoldTransactionBackUpAndDeleteOriginalDto insetHoldTransactionBackUpAndDeleteOriginalDto) {
        acquiringTransactionHoldRepository.insertTempData(insetHoldTransactionBackUpAndDeleteOriginalDto.getMicrofilmRefNbr(), insetHoldTransactionBackUpAndDeleteOriginalDto.getEntityId(), insetHoldTransactionBackUpAndDeleteOriginalDto.getTerminalId(), insetHoldTransactionBackUpAndDeleteOriginalDto.getBatchId(), insetHoldTransactionBackUpAndDeleteOriginalDto.getLinkupCode(),
                insetHoldTransactionBackUpAndDeleteOriginalDto.getProcessingRefNbr5(), insetHoldTransactionBackUpAndDeleteOriginalDto.getSettleProcessionFlag(), insetHoldTransactionBackUpAndDeleteOriginalDto.getHaltMerchPay());
        acquiringTransactionRepository.deleteHoldTransaction(insetHoldTransactionBackUpAndDeleteOriginalDto.getMicrofilmRefNbr(), insetHoldTransactionBackUpAndDeleteOriginalDto.getEntityId(), insetHoldTransactionBackUpAndDeleteOriginalDto.getTerminalId(), insetHoldTransactionBackUpAndDeleteOriginalDto.getBatchId(), insetHoldTransactionBackUpAndDeleteOriginalDto.getLinkupCode(),
                insetHoldTransactionBackUpAndDeleteOriginalDto.getProcessingRefNbr5(), insetHoldTransactionBackUpAndDeleteOriginalDto.getSettleProcessionFlag(), insetHoldTransactionBackUpAndDeleteOriginalDto.getHaltMerchPay());
    }

    private void updateAcquiringTransaction(UpdateAqcuiringTransactionDto updateAqcuiringTransactionDto) {

        acquiringTransactionRepository.haltTransaction(updateAqcuiringTransactionDto.getSettlProcessingFlag(), updateAqcuiringTransactionDto.getHaltMerchPay(), updateAqcuiringTransactionDto.getProcessingRefNbr5(), updateAqcuiringTransactionDto.getMicrofilmRefNbr(),
                updateAqcuiringTransactionDto.getOutletCode(), updateAqcuiringTransactionDto.getTerminalId(), updateAqcuiringTransactionDto.getBatchId(), updateAqcuiringTransactionDto.getLinkupCode());
    }

    public TransactionCurrentSearchResponseDto saveOrUpdateReversalTransaction(AcquiringTransactionRequestDto acquiringTransactionRequestDto)
            throws ParseException, CloneNotSupportedException {
        SystemCode systemCode = systemCodeRepository.findById(acquiringTransactionRequestDto.getSystemCodeId())
                .orElseThrow(() -> new BusinessException(ResponseCode.INVALID_SYSTEM_CODE_ID, HttpStatus.NOT_FOUND));

        TransactionCurrentSearchResponseDto transactionCurrentSearchResponseDto = new TransactionCurrentSearchResponseDto();

        Institution institution = institutionRepository.findById(acquiringTransactionRequestDto.getInstitutionId())
                .orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_INST, HttpStatus.NOT_FOUND));

        TransactionCurrent transactionCurrent = transactionCurrentRepository
                .findById(acquiringTransactionRequestDto.getAcquiringTransactionId())
                .orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_ACQUIRING_TRANSACTION_ID,
                        HttpStatus.NOT_FOUND));

        if (makerCheckerEngine.processIfRequired(acquiringTransactionRequestDto, this.getClass().getName(), new Object() {}.getClass().getEnclosingMethod().getName(), "")) {
            return null;
        }

        if (transactionCurrent != null && transactionCurrent.getUsageCode() == 0) {
            transactionCurrent.setRefNumberSeq(0);
            transactionCurrent.setReversalFlag("Y");
            transactionCurrent.setReversalComment(acquiringTransactionRequestDto.getReversalComment());
            transactionCurrent.setReversalReason(acquiringTransactionRequestDto.getReversalReason());
            transactionCurrent.setBillingProcessingFlag('N');
            transactionCurrent.setSettlProcessingFlag('N');
            transactionCurrent.setAcquirerRecordToAppear(acquiringTransactionRequestDto.getAccquierRecordToAppear());
            transactionCurrent.setProcessingDate(null);
            transactionCurrent.setReasonCode(systemCode.getSystemCodeId());
            transactionCurrent.setAcqInstId(institution.getInstitutionId());
            transactionCurrent.setRefNumberSeq(transactionCurrent.getRefNumberSeq() + 1);
            transactionCurrent = transactionCurrentRepository.save(transactionCurrent);

            transactionCurrentSearchResponseDto = transactionCurrentMapper.toSearchDto(transactionCurrent);
            transactionCurrentSearchResponseDto.setInstitutionId(transactionCurrent.getAcqInstId());
        }
        return transactionCurrentSearchResponseDto;

    }

    public ResponseEntity<PaginationResponseDto> getAcquiringTransactionByInstitutionId(String institutionId,
                                                                                        AcquiringTransactionRequestDto acquiringTransactionRequestDto) {
        // TODO Auto-generated method stub

        Page<AcquiringTransaction> page = null;

        PaginationCommonCode paginationCommonCode = new PaginationCommonCode();

        PageRequest pageRequest = paginationCommonCode.getPageRequestForPagination(
                acquiringTransactionRequestDto.getSort(), "acquiringTransactionId",
                acquiringTransactionRequestDto.getPageNo(), acquiringTransactionRequestDto.getPageSize());

        page = acquiringTransactionRepository.findByInstitution(institutionId, pageRequest);

        List<AcquiringTransactionResponseDto> acquiringTransactionResponseDtos = new ArrayList();

        if (page.getContent().size() == 0) {
            throw new BusinessException(ResponseCode.CFG_INVALID_INSTITUTION_ID, HttpStatus.NOT_FOUND);
        } else {
            page.getContent().stream().forEach((acquiringTransaction) -> {
                AcquiringTransactionResponseDto acquiringTransactionResponseDto = acquiringTransactionMapper.toDto(acquiringTransaction);
                acquiringTransactionResponseDto.setInstitutionId(acquiringTransaction.getAcqInstitutionId().getInstitutionId());
                acquiringTransactionResponseDtos.add(acquiringTransactionResponseDto);
            });
        }

        return new ResponseEntity<PaginationResponseDto>(new PaginationResponseDto(true, null,
                acquiringTransactionResponseDtos, page.getTotalPages(), page.getTotalElements()), HttpStatus.OK);
    }

    public List<AcquiringTransactionResponseDto> getAcquiringTransactionByEntitiesId(String entitiesId) {
        // TODO Auto-generated method stub

        List<AcquiringTransaction> acquiringTransactions = acquiringTransactionRepository.findByEntitiesObject(entitiesId,
                Sort.by(Sort.Direction.ASC, "acquiringTransactionId"));

        List<AcquiringTransactionResponseDto> acquiringTransactionResponseDtos = new ArrayList();

        if (acquiringTransactions.size() == 0) {
            throw new BusinessException(ResponseCode.CFG_INVALID_INSTITUTION_ID, HttpStatus.NOT_FOUND);
        } else {
            acquiringTransactions.stream().forEach((acquiringTransaction) -> {
                AcquiringTransactionResponseDto acquiringTransactionResponseDto = acquiringTransactionMapper.toDto(acquiringTransaction);
                acquiringTransactionResponseDto.setInstitutionId(acquiringTransaction.getAcqInstitutionId().getInstitutionId());
                acquiringTransactionResponseDtos.add(acquiringTransactionResponseDto);
            });
        }

        return acquiringTransactionResponseDtos;
    }

    public ResponseEntity<PaginationResponseDto> getAcquiringTransactionByEntitiesIdAndInstitutionId(String entitiesId,
                                                                                                     String institutionId, AcquiringTransactionRequestDto acquiringTransactionRequestDto) {
        // TODO Auto-generated method stub

        Page<AcquiringTransaction> page = null;

        PaginationCommonCode paginationCommonCode = new PaginationCommonCode();

        PageRequest pageRequest = paginationCommonCode.getPageRequestForPagination(
                acquiringTransactionRequestDto.getSort(), "acquiringTransactionId",
                acquiringTransactionRequestDto.getPageNo(), acquiringTransactionRequestDto.getPageSize());

        page = acquiringTransactionRepository.findByEntitiesObjectAndInstitution(entitiesId, institutionId, pageRequest);

        List<AcquiringTransactionResponseDto> acquiringTransactionResponseDtos = new ArrayList();

        if (page.getContent().size() == 0) {
            throw new BusinessException(ResponseCode.CFG_INVALID_INSTITUTION_ID, HttpStatus.NOT_FOUND);
        } else {
            page.getContent().stream().forEach((terminal) -> {
                AcquiringTransactionResponseDto acquiringTransactionResponseDto = acquiringTransactionMapper.toDto(terminal);
                acquiringTransactionResponseDto.setInstitutionId(terminal.getAcqInstitutionId().getInstitutionId());
                acquiringTransactionResponseDtos.add(acquiringTransactionResponseDto);

            });
        }

        return new ResponseEntity<PaginationResponseDto>(new PaginationResponseDto(true, null,
                acquiringTransactionResponseDtos, page.getTotalPages(), page.getTotalElements()), HttpStatus.OK);

    }


    public ResponseEntity<PaginationResponseDto> getAcquiringTransactionBySearchImproved(AcquiringTransactionRequestDto request) {
        Page<TransactionCurrent> page;
        PaginationCommonCode paginationCommonCode = new PaginationCommonCode();


        PageRequest pageRequestNative = paginationCommonCode.getPageRequestForPaginationNative(
                request.getSort(), "recordSeqId", request.getPageNo(), request.getPageSize());

        // Validate mandatory fields
        if (!validateMandatoryFields(request)) {
            return new ResponseEntity<>((new PaginationResponseDto(true, "Bad request", null, 0, 0L)),
                    HttpStatus.BAD_REQUEST);
        }

        // Handle optional fields
        page = handleOptionalFields(request, pageRequestNative);

        if (page == null) {
            return ResponseEntity.noContent().build();
        }
        System.out.println("page>>>>>" + page);
        List<TransactionCurrentSearchResponseDto> responseDtos = new ArrayList<>();

        page.getContent().forEach((trans) -> {
            System.out.println("trans object: " + trans.toString());

            TransactionCurrentSearchResponseDto dto = transactionCurrentMapper.toSearchDto(trans);
            responseDtos.add(dto);
        });
        System.out.println("responseDtos>>>>>" + responseDtos);

        return new ResponseEntity<>(
                new PaginationResponseDto(true, null, responseDtos, page.getTotalPages(), page.getTotalElements()),
                HttpStatus.OK);
    }

    // Helper function to validate mandatory fields
    private boolean validateMandatoryFields(AcquiringTransactionRequestDto request) {
        return !StringUtils.isEmpty(request.getInstitutionId()) &&
                //!StringUtils.isEmpty(request.getEntitiesId()) &&
                request.getFromProcessingDate() != null &&
                request.getToProcessingDate() != null;
    }

    // Helper function to handle optional fields
    private Page<TransactionCurrent> handleOptionalFields(AcquiringTransactionRequestDto request,
                                                          PageRequest pageRequestNative) {
        boolean isManualEntry = "2".equals(request.getManualEntry());
        String formattedFromDate = formatDate(request.getFromProcessingDate());
        String formattedToDate = formatDate(request.getToProcessingDate());
//		TODO TO BE REMOVED HERE FOR TESTING
//		Case 0
//		if(Objects.equals(request.getEntitiesId(), "009")){
//			return transactionCurrentRepository.findAllNative(pageRequestNative);
//		}
        if (request.getSettlMerchHalt() == 'N') {
            return transactionCurrentRepository.findTransactionSettlMerchHaltYes(pageRequestNative,
                    request.getInstitutionId(),
                    Objects.nonNull(request.getEntitiesId()) ? (request.getEntitiesId().isBlank() ? null : request.getEntitiesId()) : null,
                    formattedFromDate, formattedToDate,
                    request.getCardNumber().isEmpty() ? null : request.getCardNumber(),
                    request.getTransactionId().isEmpty() ? null : request.getTransactionId(),
                    request.getTerminalId(),
                    request.getBatchId().isEmpty() ? null : request.getBatchId(),
                    request.getAuthorizationNumber().isEmpty() ? null : request.getAuthorizationNumber(),
                    isManualEntry ? null : request.getManualEntry().charAt(0),
                    Objects.nonNull(request.getMerchantName()) ? request.getMerchantName().isBlank() ? null : request.getMerchantName() : null,
                    request.getMerchantAccountNumber().isEmpty() ? null : request.getMerchantAccountNumber()
            );
        } else {
            return transactionCurrentRepository.findTransactionSettlMerchHaltNo(pageRequestNative,
                    request.getInstitutionId(),
                    Objects.nonNull(request.getEntitiesId()) ? (request.getEntitiesId().isBlank() ? null : request.getEntitiesId()) : null,
                    formattedFromDate, formattedToDate,
                    request.getCardNumber().isEmpty() ? null : request.getCardNumber(),
                    request.getTransactionId().isEmpty() ? null : request.getTransactionId(),
                    request.getTerminalId(),
                    request.getBatchId().isEmpty() ? null : request.getBatchId(),
                    request.getAuthorizationNumber().isEmpty() ? null : request.getAuthorizationNumber(),
                    isManualEntry ? null : request.getManualEntry().charAt(0),
                    Objects.nonNull(request.getMerchantName()) ? request.getMerchantName().isBlank() ? null : request.getMerchantName() : null,
                    request.getMerchantAccountNumber().isEmpty() ? null : request.getMerchantAccountNumber()
            );
        }
    }

    // Helper function to format date
    private String formatDate(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }


    public boolean getAcquiringTransactionByReversal(int acquiringTransactionId) {
        TransactionCurrent transactionCurrent = transactionCurrentRepository.findById(acquiringTransactionId)
                .orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_ACQUIRING_TRANSACTION_ID,
                        HttpStatus.NOT_FOUND));
        List<TransactionCurrent> transactionCurrents = transactionCurrentRepository
                .findByMicrofilmRefNumberAndTransIdAndSourceAmountAndReversalFlag(
                        transactionCurrent.getMicrofilmRefNumber(), transactionCurrent.getTransId(),
                        transactionCurrent.getSourceAmount(), "Y");

        if (transactionCurrents.size() == 1) {
            return true;
        } else {
            return false;
        }

    }

    public boolean getAcquiringTransactionByRepresenment(int acquiringTransactionId) {

        TransactionCurrent transactionCurrent = transactionCurrentRepository.findById(acquiringTransactionId)
                .orElseThrow(() -> new BusinessException(
                        ResponseCode.CFG_INVALID_ACQUIRING_TRANSACTION_ID,
                        HttpStatus.NOT_FOUND
                ));

        List<TransactionCurrent> transactionCurrents = transactionCurrentRepository
                .findByMicrofilmRefNumberAndTransIdAndSourceAmountAndUsageCode(
                        transactionCurrent.getMicrofilmRefNumber(),
                        transactionCurrent.getTransId(),
                        transactionCurrent.getSourceAmount(),
                        2
                );

        return transactionCurrents.size() == 1;
    }

    public List<TransactionCurrentSearchResponseDto> applyAccountingAdjustment(
            AcquiringTransactionAdjustmentRequestDto requestDto) {

        char holdFlag = requestDto.isHold() ? 'Y' : 'N';
        List<TransactionCurrent> transactionToBeSaved = new ArrayList<>();
        requestDto.getTransactions()
                .forEach(id -> {
                    TransactionCurrent transactionCurrent = transactionCurrentRepository.findById(id)
                            .orElseThrow(() -> new BusinessException(
                                    ResponseCode.CFG_INVALID_ACQUIRING_TRANSACTION_ID,
                                    HttpStatus.NOT_FOUND));

                    transactionCurrent.setSettlMerchHalt(holdFlag);
                    if (requestDto.isHold()) {
                        transactionCurrent.setSettlProcessingFlag('P');
                        transactionCurrent.setMerchSettlementDate(null);
                        transactionCurrent.setMerchPaymentDate(null);
                        transactionCurrent.setMerchantAccountNumber(null);
                        transactionCurrent.setSettlementCurrency(null);
                    }
                    transactionToBeSaved.add(transactionCurrent);
                });
        if (makerCheckerEngine.processIfRequired(requestDto, this.getClass().getName(), new Object() {}.getClass().getEnclosingMethod().getName(), "")) {
            return null;
        }
        return transactionToBeSaved.stream()
                .map(e -> {
                    TransactionCurrent savedTransaction = transactionCurrentRepository.save(e);
                    return transactionCurrentMapper.toSearchDto(savedTransaction);
                })
                .collect(Collectors.toList());
    }
}