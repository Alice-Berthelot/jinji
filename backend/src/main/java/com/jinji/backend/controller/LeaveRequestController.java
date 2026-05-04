package com.jinji.backend.controller;

import com.jinji.backend.model.dto.EmployeeDTO;
import com.jinji.backend.model.dto.LeaveRequestCreateRequest;
import com.jinji.backend.model.dto.LeaveRequestDTO;
import com.jinji.backend.model.dto.LeaveRequestSummaryDTO;
import com.jinji.backend.service.EmployeeService;
import com.jinji.backend.service.LeaveRequestService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public List<LeaveRequestDTO> getMyLeaveRequests(@AuthenticationPrincipal UserDetails userDetails) {
        return leaveRequestService.getMyLeaveRequests(userDetails.getUsername());
    }

    @GetMapping("/me/summary")
    @PreAuthorize("isAuthenticated()")
    public List<LeaveRequestSummaryDTO> getMyLeaveRequestsSummary(@AuthenticationPrincipal UserDetails userDetails) {
        return leaveRequestService.getMyLeaveRequestsSummary(userDetails.getUsername());
    }

}
