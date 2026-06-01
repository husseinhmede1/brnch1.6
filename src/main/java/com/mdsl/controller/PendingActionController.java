package com.mdsl.controller;

import com.mdsl.model.dto.request.PendingActivityRejectRequestDto;
import com.mdsl.model.dto.request.PendingActivitySearchRequestDto;
import com.mdsl.model.dto.response.PageablePendingActivityResponseDto;
import com.mdsl.service.PendingActivitySearchService;
import com.mdsl.service.WorkflowApprovalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Api("Pending Activity Controller")
@RestController
@RequestMapping("/v1/checker/pending-activities")
@RequiredArgsConstructor
public class PendingActionController {

    private final PendingActivitySearchService searchService;
    private final WorkflowApprovalService      workflowApprovalService;

    @PostMapping("/search")
    @ApiOperation(value = "Search pending activities with optional filters")
    public ResponseEntity<PageablePendingActivityResponseDto> search(
            @Valid @RequestBody PendingActivitySearchRequestDto request,
            HttpServletRequest httpRequest) {
        int instId = Integer.parseInt(httpRequest.getHeader("instId"));
        return ResponseEntity.ok(searchService.search(request, instId));
    }

    @PostMapping("/{id}/approve")
    @ApiOperation(value = "Approve a pending activity")
    public ResponseEntity<String> approve(@PathVariable Integer id) {
        workflowApprovalService.approveActivity(id);
        return ResponseEntity.ok("Activity approved and applied successfully.");
    }

    @PostMapping("/{id}/reject")
    @ApiOperation(value = "Reject a pending activity")
    public ResponseEntity<String> reject(
            @PathVariable Integer id,
            @Valid @RequestBody PendingActivityRejectRequestDto request) {
        workflowApprovalService.rejectActivity(id, request.getNote());
        return ResponseEntity.ok("Activity rejected.");
    }
}
