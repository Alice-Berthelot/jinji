package com.jinji.backend.controller;

import com.jinji.backend.model.dto.UserDTO;
import com.jinji.backend.model.entity.User;
import com.jinji.backend.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public User create(@RequestBody UserDTO request) {
        return userService.createUser(
                request.getUsername(),
                request.getPassword()
        );
    }
}