package com.jinji.backend.service;

import com.jinji.backend.exception.ResourceAlreadyExistsException;
import com.jinji.backend.model.entity.User;
import com.jinji.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(String username, String password) {

        User user = new User();

        if (userRepository.existsByUsername(username)) {
            throw new ResourceAlreadyExistsException(
                    "username",
                    "Username already exists"
            );
        }

        user.setUsername(username);
        user.setHashedPassword(passwordEncoder.encode(password));

        return userRepository.save(user);
    }
}
