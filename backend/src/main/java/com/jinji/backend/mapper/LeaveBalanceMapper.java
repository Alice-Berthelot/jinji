package com.jinji.backend.mapper;

import com.jinji.backend.model.dto.response.LeaveBalanceDTO;
import com.jinji.backend.model.entity.LeaveBalance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring")
public interface LeaveBalanceMapper {

    @Mapping(source = "leaveType.label", target = "leaveType")
    @Mapping(
            target = "remainingDays",
            expression = "java(calculateRemainingDays(leaveBalance))"
    )
    LeaveBalanceDTO toDto(LeaveBalance leaveBalance);

    List<LeaveBalanceDTO> toDtos(List<LeaveBalance> leaveBalances);

    default BigDecimal calculateRemainingDays(LeaveBalance leaveBalance) {

        if (leaveBalance.getAcquiredDays() == null
                || leaveBalance.getTakenDays() == null) {
            return BigDecimal.ZERO;
        }

        return leaveBalance.getAcquiredDays()
                .subtract(leaveBalance.getTakenDays());
    }
}