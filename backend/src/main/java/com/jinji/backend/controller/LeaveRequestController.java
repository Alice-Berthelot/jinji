package com.jinji.backend.controller;

import com.jinji.backend.model.dto.*;
import com.jinji.backend.model.dto.request.LeaveRequestCreateRequest;
import com.jinji.backend.model.dto.request.LeaveRequestCreateReview;
import com.jinji.backend.model.dto.response.LeaveRequestActionResponseDTO;
import com.jinji.backend.service.crud.LeaveRequestService;
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
    public List<MyLeaveRequestSummaryDTO> getMyLeaveRequestsSummary(@AuthenticationPrincipal UserDetails userDetails) {
        return leaveRequestService.getMyLeaveRequestsSummary(userDetails.getUsername());
    }

    @GetMapping("/{leaveRequestId}")
    @PreAuthorize("isAuthenticated()")
    public LeaveRequestDTO getLeaveRequestDetail(@PathVariable Long leaveRequestId) {
        return leaveRequestService.getLeaveRequestById(leaveRequestId);
    }

    @GetMapping("/summary")
    @PreAuthorize("hasRole('HR') or hasRole('MANAGER')")
    public List<LeaveRequestSummaryDTO> getLeaveRequestsSummary(@AuthenticationPrincipal UserDetails userDetails) {
        return leaveRequestService.getLeaveRequestsSummary(userDetails.getUsername());
    }

    @PostMapping("/{leaveRequestId}/review")
    @PreAuthorize("hasRole('HR') or hasRole('MANAGER')")
    public ResponseEntity<LeaveRequestDTO> processLeaveRequest(@PathVariable Long leaveRequestId,
            @Valid @RequestBody LeaveRequestCreateReview leaveRequestCreateReview
    ) {

        LeaveRequestDTO response = leaveRequestService.processLeaveRequest(leaveRequestId, leaveRequestCreateReview);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{leaveRequestId}/cancel")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<LeaveRequestActionResponseDTO> cancelLeaveRequest(
            @PathVariable Long leaveRequestId
    ) {
        LeaveRequestActionResponseDTO updated = leaveRequestService.cancel(leaveRequestId);
        return ResponseEntity.ok(updated);
    }
}
