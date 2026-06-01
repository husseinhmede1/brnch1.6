package com.mdsl.controller;

import com.mdsl.service.WorkflowApprovalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pending-actions")
public class PendingActionController {

    private final WorkflowApprovalService workflowApprovalService;

    public PendingActionController(WorkflowApprovalService workflowApprovalService) {
        this.workflowApprovalService = workflowApprovalService;
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<String> approve(@PathVariable Integer id) {
        try {
            workflowApprovalService.approveActivity(id);
            return ResponseEntity.ok("Activity successfully validated, processed, and applied.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Processing exception encountered: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<String> reject(@PathVariable Integer id, @RequestParam String reason) {
        workflowApprovalService.rejectActivity(id, reason);
        return ResponseEntity.ok("Activity effectively rejected.");
    }
}