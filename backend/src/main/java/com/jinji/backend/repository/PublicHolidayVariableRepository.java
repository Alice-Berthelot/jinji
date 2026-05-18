package com.jinji.backend.repository;

import com.jinji.backend.model.entity.PublicHolidayVariable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface PublicHolidayVariableRepository
        extends JpaRepository<PublicHolidayVariable, Long> {

    boolean existsByDate(LocalDate date);

    Optional<PublicHolidayVariable>
    findByLabelAndDateBetween(
            String label,
            LocalDate start,
            LocalDate end
    );
}