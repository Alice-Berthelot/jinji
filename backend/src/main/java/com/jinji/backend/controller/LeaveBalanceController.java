package com.jinji.backend.controller;

import com.jinji.backend.model.dto.request.AdjustAcquiredDaysRequest;
import com.jinji.backend.model.dto.response.LeaveBalanceDTO;
import com.jinji.backend.service.crud.LeaveBalanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leave-balances")
public class LeaveBalanceController {

    private final LeaveBalanceService leaveBalanceService;

    public LeaveBalanceController(LeaveBalanceService leaveBalanceService) {
        this.leaveBalanceService = leaveBalanceService;
    }

    @GetMapping("/me")
    public ResponseEntity<List<LeaveBalanceDTO>> getMyLeaveBalances(Authentication authentication) {
        return ResponseEntity.ok(leaveBalanceService.getMyLeaveBalances(
                authentication.getName())
        );
    }

    @GetMapping("/{employeeId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<LeaveBalanceDTO>> getByEmployeeId(
            @PathVariable Long employeeId
    ) {
        return ResponseEntity.ok(leaveBalanceService.getLeaveBalancesByEmployeeId(employeeId));
    }

    @PatchMapping("/{id}/acquired-days")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<LeaveBalanceDTO> updateAcquiredDays(
            @PathVariable Long id,
            @RequestBody AdjustAcquiredDaysRequest request
    ) {
        return ResponseEntity.ok(leaveBalanceService.adjustAcquiredDays(
                id,
                request.newAcquiredDays())
        );
    }
}