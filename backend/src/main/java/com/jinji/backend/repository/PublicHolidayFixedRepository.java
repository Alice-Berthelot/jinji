package com.jinji.backend.repository;

import com.jinji.backend.model.entity.PublicHolidayFixed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublicHolidayFixedRepository
        extends JpaRepository<PublicHolidayFixed, Long> {
}