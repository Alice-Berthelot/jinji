package com.jinji.backend.controller;

import com.jinji.backend.model.dto.request.LeaveRequestCreateRequest;
import com.jinji.backend.model.dto.request.LeaveRequestCreateReview;
import com.jinji.backend.model.dto.response.LeaveRequestActionResponseDTO;
import com.jinji.backend.model.dto.response.LeaveRequestDTO;
import com.jinji.backend.model.dto.response.LeaveRequestSummaryDTO;
import com.jinji.backend.model.dto.response.MyLeaveRequestSummaryDTO;
import com.jinji.backend.service.crud.LeaveRequestService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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

    // GET

    @GetMapping("/{leaveRequestId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<LeaveRequestDTO> getLeaveRequestDetail(@PathVariable Long leaveRequestId) {
        LeaveRequestDTO response = leaveRequestService.getLeaveRequestById(leaveRequestId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/summary")
    @PreAuthorize("hasRole('HR') or hasRole('MANAGER')")
    public ResponseEntity<Page<LeaveRequestSummaryDTO>> getLeaveRequestsSummary(Pageable pageable) {

        return ResponseEntity.ok(leaveRequestService.getLeaveRequestsSummary(pageable));
    }

    @GetMapping("/me/summary")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<MyLeaveRequestSummaryDTO>> getMyLeaveRequestsSummary(Pageable pageable) {
        return ResponseEntity.ok(
                leaveRequestService.getMyLeaveRequestsSummary(
                        pageable
                )
        );
    }

    // POST

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<LeaveRequestDTO> createLeaveRequest(
            @Valid @RequestBody LeaveRequestCreateRequest request) {
        LeaveRequestDTO response = leaveRequestService.createLeaveRequest(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/{leaveRequestId}/review")
    @PreAuthorize("hasRole('HR') or hasRole('MANAGER')")
    public ResponseEntity<LeaveRequestDTO> processLeaveRequest(@PathVariable Long leaveRequestId,
            @Valid @RequestBody LeaveRequestCreateReview leaveRequestCreateReview
    ) {
        LeaveRequestDTO response = leaveRequestService.processLeaveRequest(leaveRequestId, leaveRequestCreateReview);
        return ResponseEntity.ok(response);
    }

    // PATCH

    @PatchMapping("/{leaveRequestId}/cancel")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<LeaveRequestActionResponseDTO> cancelLeaveRequest(
            @PathVariable Long leaveRequestId
    ) {
        LeaveRequestActionResponseDTO updated = leaveRequestService.cancelLeaveRequest(leaveRequestId);
        return ResponseEntity.ok(updated);
    }
}
