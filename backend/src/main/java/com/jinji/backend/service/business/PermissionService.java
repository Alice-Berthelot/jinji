package com.jinji.backend.service.business;

import com.jinji.backend.exception.ForbiddenException;
import com.jinji.backend.model.entity.Employee;
import com.jinji.backend.model.entity.Leave;
import com.jinji.backend.model.entity.Team;
import com.jinji.backend.model.entity.User;
import com.jinji.backend.model.enums.EmployeePageView;
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

    public boolean canViewEmployeeDetails(EmployeePageView pageType,
                                          User currentUser,
                                          Employee authEmployee,
                                          Employee target) {

        return switch (pageType) {
            case HR -> currentUser.isHr();
            case MANAGER -> currentUser.isManager()
                    && isManagerOf(authEmployee, target);
        };
    }

    public void canCancelLeave(User user, Leave leave) {

        Employee currentEmployee = user.getEmployee();

        if (currentEmployee == null && !user.isHr()) {
            throw new ForbiddenException("Not authorized");
        }

        boolean isOwner = currentEmployee != null
                && leave.getCreatedBy().getId().equals(currentEmployee.getId());

        boolean isHr = user.isHr();

        if (!isOwner && !isHr) {
            throw new ForbiddenException("User is not authorized to cancel leave n°" + leave.getId());
        }
    }

    public boolean isManagerOf(Employee manager, Employee employee) {
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