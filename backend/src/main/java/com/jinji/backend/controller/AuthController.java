package com.jinji.backend.controller;

import com.jinji.backend.model.dto.LoginDTO;
import com.jinji.backend.model.dto.LoginResponse;
import com.jinji.backend.model.dto.RegisterUserDTO;
import com.jinji.backend.model.entity.User;
import com.jinji.backend.security.JwtService;
import com.jinji.backend.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(UserService userService, AuthenticationManager authenticationManager,
                          JwtService jwtService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    @PreAuthorize("hasRole('HR')")
    public User create(@RequestBody RegisterUserDTO request) {
        return userService.createUser(
                request.getUsername(),
                request.getPassword(),
                request.getRoles()
        );
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginDTO request) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        UserDetails user = (UserDetails) auth.getPrincipal();

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new LoginResponse(accessToken, refreshToken);
    }
}
