package com.jinji.backend.controller;

import com.jinji.backend.model.dto.*;
import com.jinji.backend.service.crud.LeaveRequestService;
import com.jinji.backend.service.crud.LeaveService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
}
