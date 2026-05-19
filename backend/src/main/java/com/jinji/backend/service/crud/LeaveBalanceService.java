package com.jinji.backend.service.crud;

import com.jinji.backend.model.dto.EmployeeCreateRequest;
import com.jinji.backend.model.dto.LeaveBalanceDTO;
import com.jinji.backend.model.entity.Department;
import com.jinji.backend.model.entity.Employee;
import com.jinji.backend.model.entity.LeaveBalance;
import com.jinji.backend.model.enums.AnnualLeaveAccrualPeriod;
import com.jinji.backend.model.enums.RoleEnum;
import com.jinji.backend.repository.LeaveBalanceRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
public class LeaveBalanceService {

    private final LeaveBalanceRepository leaveBalanceRepository;
    private final EmployeeService employeeService;
    private final HrPolicyService hrPolicyService;

    public LeaveBalanceService(
            LeaveBalanceRepository leaveBalanceRepository,
            EmployeeService employeeService, HrPolicyService hrPolicyService
    ) {
        this.leaveBalanceRepository = leaveBalanceRepository;
        this.employeeService = employeeService;
        this.hrPolicyService = hrPolicyService;
    }

    public List<LeaveBalanceDTO> getLeaveBalancesByEmployeeId(Long employeeId) {

        return leaveBalanceRepository.findByEmployee_Id(employeeId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    public List<LeaveBalanceDTO> getMyLeaveBalances(String username) {

        Employee employee = employeeService.getCurrentEmployee(username);

        return leaveBalanceRepository.findByEmployee_Id(employee.getId())
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    private LeaveBalanceDTO mapToDto(LeaveBalance leaveBalance) {

        LeaveBalanceDTO dto = new LeaveBalanceDTO();

        dto.setLabel(leaveBalance.getLabel());
        dto.setAcquisitionStartDate(leaveBalance.getAcquisitionStartDate());
        dto.setAcquisitionEndDate(leaveBalance.getAcquisitionEndDate());
        dto.setAcquiredDays(leaveBalance.getAcquiredDays());
        dto.setTakenDays(leaveBalance.getTakenDays());

        dto.setRemainingDays(
                leaveBalance.getAcquiredDays()
                        .subtract(leaveBalance.getTakenDays())
        );

        dto.setLeaveType(
                leaveBalance.getLeaveType().getLabel()
        );

        return dto;
    }

    public LeaveBalance createLeaveBalance(Employee employee) {
        // peut être mettre des années optionnelles pour créer plus tard
// determine accrual start date :
//        - get hr policy accrual start date
//        - compare hr policy with seniorityDate (employee.getSeniorityDate()) : if seniorityDate est après accrual, début de l'acquisition = seniorityDate

        AnnualLeaveAccrualPeriod accrualPeriod = hrPolicyService.getAnnualLeaveAccrualPeriod();



//  save entité LeaveBalance constituée en composant le nom dynamiquement en fonction de l'année
        LeaveBalance leaveBalance = new LeaveBalance();

        leaveBalance.setEmployee(employee);
//        leaveBalance.setLabel();
//        leaveBalance.setAcquisitionStartDate();
//        leaveBalance.setAcquisitionEndDate();
//        leaveBalance.setAcquiredDays();

        LeaveBalance savedLeaveBalance = leaveBalanceRepository.save(leaveBalance);

        return savedLeaveBalance;
    }

}