package com.jinji.backend.repository;

import com.jinji.backend.model.dto.EmployeeFullNameDTO;
import com.jinji.backend.model.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("""
    SELECT new com.jinji.backend.model.dto.EmployeeFullNameDTO(
        e.firstName,
        e.surname
    )
    FROM User u
    JOIN u.employee e
    WHERE u.username = :username
""")
    Optional<EmployeeFullNameDTO> findEmployeeNameByUsername(String username);

    @Query("""
    SELECT new com.jinji.backend.model.dto.EmployeeFullNameDTO(
        e.firstName,
        e.surname
    )
    FROM Employee e
    WHERE e.id = :employeeId
""")
    Optional<EmployeeFullNameDTO> findEmployeeNameById(Long employeeId);

}