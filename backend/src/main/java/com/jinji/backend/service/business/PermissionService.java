package com.jinji.backend.service.business;

import com.jinji.backend.model.entity.Employee;
import com.jinji.backend.model.entity.Team;
import com.jinji.backend.model.entity.User;
import org.springframework.stereotype.Service;

@Service
public class PermissionService {

    public boolean canViewLeaveBalances(User user, Employee targetEmployee) {
        if (user.isHr()) {
            return true;
        }
        Employee currentEmployee = user.getEmployee();
        if (currentEmployee == null || targetEmployee == null) {
            return false;
        }
        if (currentEmployee.getId().equals(targetEmployee.getId())) {
            return true;
        }
        if (user.isManager() && isManagerOf(currentEmployee, targetEmployee)) {
            return true;
        }
        return false;
    }

    private boolean isManagerOf(Employee manager, Employee employee) {
        if (employee.getTeams() == null || employee.getTeams().isEmpty()) {
            return false;
        }
        return employee.getTeams().stream()
                .anyMatch(team -> isManagerOfTeam(manager, team));
    }

    private boolean isManagerOfTeam(Employee manager, Team team) {
        return team.getManager() != null
                && team.getManager().getId().equals(manager.getId());
    }
}