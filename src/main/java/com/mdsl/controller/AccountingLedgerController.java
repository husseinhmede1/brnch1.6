package com.mdsl.controller;


import com.mdsl.model.dto.response.AccountingLedgerResponseDto;
import com.mdsl.model.dto.response.FileDirectoryResponseDto;
import com.mdsl.service.AccountingLedgerService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api("Accounting Ledger Controller")
@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/accounting-ledger")
public class AccountingLedgerController {

    private final AccountingLedgerService accountingLedgerService;

    @GetMapping("payment-date/{acqInstId}")
    public ResponseEntity<List<AccountingLedgerResponseDto>> getMerchPaymentDate(@PathVariable("acqInstId") String acqInstId) {
        return ResponseEntity.ok(accountingLedgerService.getMerchPaymentDate(acqInstId));
    }
    @GetMapping("file-names/{institutionId}")
    public ResponseEntity<List<FileDirectoryResponseDto>> getDistinctFileNames(@PathVariable("institutionId") String institutionId){
        return ResponseEntity.ok(accountingLedgerService.getDistinctFileNames(institutionId));
    }
}
