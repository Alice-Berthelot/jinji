package com.jinji.backend.service;

import com.jinji.backend.exception.ResourceAlreadyExistsException;
import com.jinji.backend.model.entity.Employee;
import com.jinji.backend.model.entity.Role;
import com.jinji.backend.model.entity.User;
import com.jinji.backend.model.enums.RoleEnum;
import com.jinji.backend.repository.RoleRepository;
import com.jinji.backend.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(String username, String password, Set<RoleEnum> roles, Employee employee) {

        User user = new User();

        if (userRepository.existsByUsername(username)) {
            throw new ResourceAlreadyExistsException(
                    "username",
                    "Username already exists"
            );
        }

        if (roles == null || roles.isEmpty()) {
            roles = Set.of(RoleEnum.EMPLOYEE);
        }

        user.setUsername(username);
        user.setHashedPassword(passwordEncoder.encode(password));

        Set<Role> userRoles = roles.stream()
                .map(roleEnum -> roleRepository.findByCode(roleEnum)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleEnum)))
                .collect(java.util.stream.Collectors.toSet());

        user.setRoles(userRoles);

        if (employee != null) {
            user.setEmployee(employee);
        }

        return userRepository.save(user);
    }
    
    public User createUser(String username, String password, Set<RoleEnum> roles) {
        return createUser(username, password, roles, null);
    }

    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
