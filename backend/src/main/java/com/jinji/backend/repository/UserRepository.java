package com.jinji.backend.repository;

import com.jinji.backend.model.entity.Employee;
import com.jinji.backend.model.entity.User;
import com.jinji.backend.model.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    Optional<User> findByEmployee_Id(Long employeeId);

    @Query("SELECT u.employee FROM User u WHERE u.username = :username")
    Optional<Employee> findEmployeeByUsername(String username);

    List<User> findByRoles_Code(RoleEnum role);
}
