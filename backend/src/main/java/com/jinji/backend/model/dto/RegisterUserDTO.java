package com.jinji.backend.model.dto;

import com.jinji.backend.model.enums.RoleEnum;

import java.util.Set;

public class RegisterUserDTO {

    private String username;
    private String password;
    private Set<RoleEnum> roles;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<RoleEnum> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleEnum> roles) {
        this.roles = roles;
    }
}