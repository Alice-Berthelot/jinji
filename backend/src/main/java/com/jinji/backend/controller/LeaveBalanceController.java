package com.jinji.backend.controller;

import com.jinji.backend.model.dto.LeaveBalanceDTO;
import com.jinji.backend.service.crud.LeaveBalanceService;
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
    public List<LeaveBalanceDTO> getMyBalances(Authentication authentication) {

        return leaveBalanceService.getMyLeaveBalances(
                authentication.getName()
        );
    }

    @GetMapping("/{employeeId}")
    public List<LeaveBalanceDTO> getByEmployeeId(
            @PathVariable Long employeeId
    ) {

        return leaveBalanceService.getLeaveBalancesByEmployeeId(employeeId);
    }
}