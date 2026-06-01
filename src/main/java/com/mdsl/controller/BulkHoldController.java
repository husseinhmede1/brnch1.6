package com.mdsl.controller;


import com.mdsl.model.dto.response.FileDirectoryResponseDto;
import com.mdsl.service.BulkHoldService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/bulk-hold")
public class BulkHoldController {

    private final BulkHoldService bulkHoldService;

    @GetMapping("get-file-names/{institutionId}")
    public ResponseEntity<List<FileDirectoryResponseDto>> getDistinctFileNames(@PathVariable("institutionId") String institutionId){
        return ResponseEntity.ok(bulkHoldService.getDistinctFileNames(institutionId));
    }
}
