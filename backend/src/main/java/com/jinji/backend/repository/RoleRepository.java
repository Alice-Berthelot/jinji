package com.jinji.backend.repository;

import com.jinji.backend.model.entity.Role;
import com.jinji.backend.model.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByCode(RoleEnum code);
}
