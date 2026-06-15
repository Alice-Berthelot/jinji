package com.jinji.backend.config;

import com.jinji.backend.model.entity.Role;
import com.jinji.backend.model.entity.User;
import com.jinji.backend.model.enums.RoleEnum;
import com.jinji.backend.repository.RoleRepository;
import com.jinji.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Value("${INITIAL_HR_USERNAME}")
    private String initialHrUsername;

    @Value("${INITIAL_HR_PASSWORD}")
    private String initialHrPassword;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        if (userRepository.existsByUsername(initialHrUsername)) {
            return;
        }

        Role hrRole = roleRepository.findByCode(RoleEnum.HR)
                .orElseThrow(() ->
                        new RuntimeException("Role HR not found"));

        User admin = new User();
        admin.setUsername(initialHrUsername);
        admin.setHashedPassword(
                passwordEncoder.encode(initialHrPassword)
        );
        admin.setActive(true);
        admin.setRoles(Set.of(hrRole));

        userRepository.save(admin);
    }
}