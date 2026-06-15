package com.jinji.backend.controller;

import com.jinji.backend.model.dto.request.UpdateLeaveValidationRequest;
import com.jinji.backend.model.dto.response.HrPolicyDTO;
import com.jinji.backend.model.dto.response.LeaveValidationResponse;
import com.jinji.backend.model.enums.LeaveValidationProcess;
import com.jinji.backend.service.crud.HrPolicyService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hr-policy")
public class HrPolicyController {

    private final HrPolicyService service;

    public HrPolicyController(HrPolicyService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<HrPolicyDTO> getHrPolicy() {
        return ResponseEntity.ok(service.getHrPolicyDto());
    }

    @GetMapping("/leave-validation")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<LeaveValidationResponse> getLeaveValidation() {
        return ResponseEntity.ok(
                new LeaveValidationResponse(service.getLeaveValidation())
        );
    }

    @PatchMapping("/leave-validation")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<HrPolicyDTO> updateLeaveValidation(@RequestBody UpdateLeaveValidationRequest request) {
        return ResponseEntity.ok(service.updateLeaveValidation(request.getLeaveValidation()));
    }
}