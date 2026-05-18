package com.jinji.backend.repository;

import com.jinji.backend.model.dto.EmployeeNameDTO;
import com.jinji.backend.model.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("""
    SELECT new com.jinji.backend.model.dto.EmployeeNameDTO(
        e.firstName,
        e.surname
    )
    FROM User u
    JOIN u.employee e
    WHERE u.username = :username
""")
    Optional<EmployeeNameDTO> findEmployeeNameByUsername(String username);

}