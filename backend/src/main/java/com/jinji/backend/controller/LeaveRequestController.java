package com.jinji.backend.controller;

import com.jinji.backend.model.dto.LeaveRequestCreateRequest;
import com.jinji.backend.service.EmployeeService;
import com.jinji.backend.service.LeaveRequestService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/leave-requests")
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;

    public LeaveRequestController(LeaveRequestService leaveRequestService) {
        this.leaveRequestService = leaveRequestService;
    }


    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> createLeaveRequest(
            @Valid @RequestBody LeaveRequestCreateRequest request) {

        return ResponseEntity.ok(leaveRequestService.createLeaveRequest(request));
    }
}
