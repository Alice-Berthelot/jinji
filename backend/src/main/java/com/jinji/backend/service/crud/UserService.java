package com.jinji.backend.service.crud;

import com.jinji.backend.exception.ResourceAlreadyExistsException;
import com.jinji.backend.exception.ResourceNotFoundException;
import com.jinji.backend.mapper.UserMapper;
import com.jinji.backend.model.dto.response.UserResponseDTO;
import com.jinji.backend.model.entity.Employee;
import com.jinji.backend.model.entity.Role;
import com.jinji.backend.model.entity.User;
import com.jinji.backend.model.enums.RoleEnum;
import com.jinji.backend.repository.RoleRepository;
import com.jinji.backend.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public UserResponseDTO createUser(String username, String password, Set<RoleEnum> roles, Employee employee) {

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
        Set<Role> userRoles = roles.stream()
                .map(roleEnum -> roleRepository.findByCode(roleEnum)
                        .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleEnum)))
                .collect(java.util.stream.Collectors.toSet());

        user.setUsername(username);
        user.setRoles(userRoles);
        user.setHashedPassword(passwordEncoder.encode(password));

        if (employee != null) {
            user.setEmployee(employee);
        }

        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }
    
    public UserResponseDTO createUser(String username, String password, Set<RoleEnum> roles) {
        return createUser(username, password, roles, null);
    }

    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public Optional<User> findByEmployeeId(Long employeeId) {
        return userRepository.findByEmployee_Id(employeeId);
    }

    public List<User> findHrUsers() {
        return userRepository.findByRoles_Code(RoleEnum.HR);
    }
}
