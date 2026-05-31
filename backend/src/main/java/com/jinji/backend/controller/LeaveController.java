package com.jinji.backend.controller;

import com.jinji.backend.model.dto.*;
import com.jinji.backend.model.enums.EmployeePageView;
import com.jinji.backend.service.crud.LeaveService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaves")
public class LeaveController {

    private final LeaveService leaveService;

    public LeaveController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }


    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<LeaveDTO> createLeave(
            @Valid @RequestBody LeaveCreateRequest request) {

        return ResponseEntity.ok(leaveService.createLeave(request));
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<MyLeaveCalendarDTO>> getAllMyLeaves() {
        return ResponseEntity.ok(leaveService.getAllMyLeaves());
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<LeaveCalendarDTO>> getAllLeaves(@RequestParam EmployeePageView pageType) {

        return ResponseEntity.ok(leaveService.getAllLeaves(pageType));
    }
}
