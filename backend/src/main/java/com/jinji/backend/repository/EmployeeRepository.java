package com.jinji.backend.repository;

import com.jinji.backend.model.dto.EmployeeFullNameDTO;
import com.jinji.backend.model.dto.EmployeePageDTO;
import com.jinji.backend.model.entity.Employee;
import com.jinji.backend.model.projection.EmployeeTeamProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
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

    @Query("""
    SELECT new com.jinji.backend.model.dto.EmployeePageDTO(
        e.id,
        e.employeeNumber,
        e.surname,
        e.firstName,
        e.email,
        e.phoneNumber,
        e.seniorityDate,
        d.name
    )
    FROM Employee e
    LEFT JOIN e.department d
    WHERE (:search IS NULL OR :search = '')
       OR LOWER(e.surname) LIKE LOWER(CONCAT('%', :search, '%'))
       OR LOWER(e.firstName) LIKE LOWER(CONCAT('%', :search, '%'))
       OR LOWER(e.email) LIKE LOWER(CONCAT('%', :search, '%'))
    ORDER BY e.surname ASC, e.firstName ASC
""")
    Page<EmployeePageDTO> findEmployeesForTable(
            @Param("search") String search,
            Pageable pageable
    );

    @Query("""
    SELECT e.id as employeeId, t.label as label
    FROM Employee e
    JOIN e.teams t
    WHERE e.id IN :employeeIds
""")
    List<EmployeeTeamProjection> findTeamNamesByEmployeeIds(
            @Param("employeeIds") List<Long> employeeIds
    );

    @Query("""
    select distinct e
    from Employee e
    left join fetch e.department
    left join fetch e.teams
    where e.id = :employeeId
""")
    Optional<Employee> findByIdWithDetails(@Param("employeeId") Long employeeId);
}