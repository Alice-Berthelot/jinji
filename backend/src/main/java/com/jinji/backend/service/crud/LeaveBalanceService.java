package com.jinji.backend.service.crud;

import com.jinji.backend.mapper.LeaveBalanceMapper;
import com.jinji.backend.model.dto.LeaveBalanceDTO;
import com.jinji.backend.model.entity.Employee;
import com.jinji.backend.model.entity.LeaveBalance;
import com.jinji.backend.model.entity.LeaveType;
import com.jinji.backend.model.entity.User;
import com.jinji.backend.model.enums.AnnualLeaveAccrualPeriod;
import com.jinji.backend.repository.HrPolicyRepository;
import com.jinji.backend.repository.LeaveBalanceRepository;
import com.jinji.backend.repository.LeaveTypeRepository;
import com.jinji.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Service
public class LeaveBalanceService {

    private final LeaveBalanceRepository leaveBalanceRepository;
    private final UserRepository userRepository;
    private final LeaveTypeRepository leaveTypeRepository;
    private final HrPolicyRepository hrPolicyRepository;
    private final HrPolicyService hrPolicyService;
    private final LeaveBalanceMapper leaveBalanceMapper;

    public LeaveBalanceService(
            LeaveBalanceRepository leaveBalanceRepository,
            UserRepository userRepository, LeaveTypeRepository leaveTypeRepository, HrPolicyRepository hrPolicyRepository, HrPolicyService hrPolicyService, LeaveBalanceMapper leaveBalanceMapper
    ) {
        this.leaveBalanceRepository = leaveBalanceRepository;
        this.userRepository = userRepository;
        this.leaveTypeRepository = leaveTypeRepository;
        this.hrPolicyRepository = hrPolicyRepository;
        this.hrPolicyService = hrPolicyService;
        this.leaveBalanceMapper = leaveBalanceMapper;
    }

    public List<LeaveBalanceDTO> getLeaveBalancesByEmployeeId(Long employeeId) {

        return leaveBalanceMapper.toDtos(
                leaveBalanceRepository.findByEmployee_Id(employeeId)
        );
    }

    public List<LeaveBalanceDTO> getMyLeaveBalances(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException(
                        "User not found: " + username
                ));

        Employee employee = user.getEmployee();

        return leaveBalanceMapper.toDtos(
                leaveBalanceRepository.findByEmployee_Id(employee.getId())
        );
    }

    public LeaveBalance createLeaveBalance(Employee employee) {
        // peut-être mettre des années optionnelles pour créer plus tard

        // 1. determine the accrual start and end dates, depending on the accrual period configured by HR:
        //        - get hr policy accrual period
        //        - compare hr policy start date with employee seniority date:
        //          if seniority date is after the hr policy start date, effective accrual date = employee seniority date
        AnnualLeaveAccrualPeriod accrualPeriod = hrPolicyService.getAnnualLeaveAccrualPeriod();
        LocalDate seniorityDate = employee.getSeniorityDate();
        LocalDate today = LocalDate.now();

        LocalDate legalAccrualStartDate = computeLegalAccrualStartDate(today);
        LocalDate effectiveAccrualStartDate = determineEffectiveAccrualStartDate(accrualPeriod, seniorityDate, today);
        LocalDate legalAccrualEndDate = computeLegalAccrualEndDate(accrualPeriod, legalAccrualStartDate);

        //  2. check if a leave balance already exists for the same employee and same period
        boolean alreadyExists =
                leaveBalanceRepository.existsByEmployeeAndAcquisitionStartDate(
                        employee,
                        effectiveAccrualStartDate
                );

        if (alreadyExists) {
            throw new IllegalStateException(
                    "Leave balance already exists for employee "
                            + employee.getId()
                            + " and acquisition start date "
                            + effectiveAccrualStartDate
            );
        }

        //  3. create and save a new Leave Balance for the employee, with the appropriate dates
        LeaveBalance leaveBalance = new LeaveBalance();
        leaveBalance.setEmployee(employee);
        leaveBalance.setLabel(
                buildLeaveBalanceLabel(
                        legalAccrualStartDate,
                        legalAccrualEndDate
                )
        );
        leaveBalance.setAcquisitionStartDate(effectiveAccrualStartDate);
        leaveBalance.setAcquisitionEndDate(legalAccrualEndDate);
        leaveBalance.setAcquiredDays(BigDecimal.ZERO);
        leaveBalance.setTakenDays(BigDecimal.ZERO);
        LeaveType leaveType = leaveTypeRepository
                .findByCode("CP")
                .orElseThrow(() -> new RuntimeException(
                        "Leave type not found"
                ));

        leaveBalance.setLeaveType(leaveType);

        LeaveBalance savedLeaveBalance = leaveBalanceRepository.save(leaveBalance);

        return savedLeaveBalance;
    }

    public LocalDate determineEffectiveAccrualStartDate(
            AnnualLeaveAccrualPeriod accrualPeriod,
            LocalDate seniorityDate,
            LocalDate today
    ) {

        return switch (accrualPeriod) {

            case LEGAL -> computeEffectiveLegalAccrualStartDate(
                    seniorityDate,
                    today
            );

            case CALENDAR_YEAR, CUSTOM ->
                    throw new UnsupportedOperationException(
                            "Accrual period not implemented yet: " + accrualPeriod
                    );
        };
    }

    private LocalDate computeLegalAccrualStartDate(
            LocalDate today
    ) {

        // May 31st, current year
        LocalDate may31 = LocalDate.of(
                today.getYear(),
                Month.MAY,
                31
        );

        // Before or equal to May 31st, current year
        if (!today.isAfter(may31)) {
            // use June 1st, previous year
            return LocalDate.of(
                    today.getYear() - 1,
                    Month.JUNE,
                    1
            );
        // After May 31st, current year
        } else {
            // use June 1st, current year
            return LocalDate.of(
                    today.getYear(),
                    Month.JUNE,
                    1
            );
        }
    }

    private LocalDate computeEffectiveLegalAccrualStartDate(
            LocalDate seniorityDate,
            LocalDate today
    ) {

        LocalDate legalAccrualStartDate =
                computeLegalAccrualStartDate(today);

        // Keep the most recent date between:
        // - legal start date
        // - employee seniority date
        return seniorityDate.isAfter(legalAccrualStartDate)
                ? seniorityDate
                : legalAccrualStartDate;
    }

    public LocalDate computeLegalAccrualEndDate(
            AnnualLeaveAccrualPeriod accrualPeriod,
            LocalDate legalAccrualStartDate
    ) {

        return switch (accrualPeriod) {

            case LEGAL -> legalAccrualStartDate
                    .plusYears(1)
                    .minusDays(1);

            case CALENDAR_YEAR, CUSTOM ->
                    throw new UnsupportedOperationException(
                            "Accrual period not implemented yet: "
                                    + accrualPeriod
                    );
        };
    }

    private String buildLeaveBalanceLabel(
            LocalDate acquisitionStartDate,
            LocalDate acquisitionEndDate
    ) {

        return "CP "
                + acquisitionStartDate.getYear()
                + "/"
                + acquisitionEndDate.getYear();
    }

    public void deductLeaveBalance(Employee employee, LeaveType leaveType, BigDecimal days) {

        boolean allowCarryover =
                hrPolicyRepository.findAllowAnnualLeaveCarryover();

        List<LeaveBalance> balances;

        if (allowCarryover) {
            System.out.println("allowCarryover");
            // FIFO: oldest balances consumed first
            balances =
                    leaveBalanceRepository
                            .findByEmployeeAndLeaveTypeOrderByAcquisitionStartDateAsc(
                                    employee,
                                    leaveType
                            );

        } else {
            System.out.println("does not allow carryover");
            // only current/latest balance
            balances =
                    leaveBalanceRepository
                            .findTopByEmployeeAndLeaveTypeOrderByAcquisitionStartDateDesc(
                                    employee,
                                    leaveType
                            )
                            .stream()
                            .toList();
        }

        if (balances.isEmpty()) {
            throw new IllegalArgumentException(
                    "No LeaveBalance found for employee " + employee.getFirstName() + employee.getSurname() +
                            " and leave type " + leaveType.getLabel()
            );
        }

        BigDecimal remainingToDeduct = days;
        for (LeaveBalance balance : balances) {
            BigDecimal available = balance.getRemainingDays();

            // available <= 0
            if (available.compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }

            // available >= remainingToDeduct
            if (available.compareTo(remainingToDeduct) >= 0) {

                balance.setTakenDays(
                        balance.getTakenDays().add(remainingToDeduct)
                );

                leaveBalanceRepository.save(balance);
                return;
            }

            // consume all available balance
            balance.setTakenDays(
                    balance.getTakenDays().add(available)
            );

            leaveBalanceRepository.save(balance);

            remainingToDeduct =
                    remainingToDeduct.subtract(available);
        }

        if (remainingToDeduct.compareTo(BigDecimal.ZERO) > 0) {
            throw new IllegalArgumentException("Insufficient leave balance");
        }
    }
}