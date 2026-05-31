package com.jinji.backend.controller;

import com.jinji.backend.model.dto.request.TeamCreateRequest;
import com.jinji.backend.model.projection.TeamSummary;
import com.jinji.backend.service.crud.TeamService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/team")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<String> createTeam(
            @Valid @RequestBody TeamCreateRequest request) {

        return ResponseEntity.ok(teamService.createTeam(request));
    }

    @GetMapping("/summary")
    public ResponseEntity<List<TeamSummary>> getAllTeams() {
        return ResponseEntity.ok(teamService.getAllTeams());
    }
}