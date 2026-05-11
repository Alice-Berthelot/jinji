package com.jinji.backend.controller;

import com.jinji.backend.model.entity.HrPolicy;
import com.jinji.backend.model.enums.LeaveValidationProcess;
import com.jinji.backend.service.HrPolicyService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hr-policy")
public class HrPolicyController {

    private final HrPolicyService service;

    public HrPolicyController(HrPolicyService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasRole('HR')")
    public HrPolicy getPolicy() {
        return service.getHrPolicy();
    }

    @GetMapping("/leave-validation")
    @PreAuthorize("isAuthenticated()")
    public LeaveValidationProcess getLeaveValidation() {
        return service.getLeaveValidation();
    }
}