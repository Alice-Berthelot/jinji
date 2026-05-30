package com.jinji.backend.service.crud;

import com.jinji.backend.exception.BadRequestException;
import com.jinji.backend.exception.ResourceNotFoundException;
import com.jinji.backend.model.dto.TeamCreateRequest;
import com.jinji.backend.model.entity.Employee;
import com.jinji.backend.model.entity.Team;
import com.jinji.backend.repository.EmployeeRepository;
import com.jinji.backend.repository.TeamRepository;
import com.jinji.backend.model.projection.TeamSummary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class TeamService {

    private final TeamRepository teamRepository;
    private final EmployeeRepository employeeRepository;

    public TeamService(
            TeamRepository teamRepository,
            EmployeeRepository employeeRepository
    ) {
        this.teamRepository = teamRepository;
        this.employeeRepository = employeeRepository;
    }

    public String createTeam(TeamCreateRequest request) {

        if (teamRepository.existsByLabel(request.getLabel())) {
            throw new BadRequestException(
                    "Team already exists with label: " + request.getLabel()
            );
        }

        Employee manager = employeeRepository.findById(request.getManagerId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Manager not found with id: "
                                        + request.getManagerId()
                        )
                );

        Set<Employee> employees = new HashSet<>();

        if (request.getEmployeeIds() != null &&
                !request.getEmployeeIds().isEmpty()) {

            employees.addAll(
                    employeeRepository.findAllById(request.getEmployeeIds())
            );

            if (employees.size() != request.getEmployeeIds().size()) {
                throw new ResourceNotFoundException(
                        "One or more employees do not exist"
                );
            }
        }

        Team team = new Team();
        team.setLabel(request.getLabel().trim());
        team.setManager(manager);
        team.setEmployees(employees);

        teamRepository.save(team);

        return "Team " + team.getLabel() + " created successfully";
    }

    @Transactional(readOnly = true)
    public List<TeamSummary> getAllTeams() {
        return teamRepository.findAllByOrderByLabelAsc();
    }
}