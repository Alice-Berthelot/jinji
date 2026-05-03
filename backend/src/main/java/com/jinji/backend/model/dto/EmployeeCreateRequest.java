package com.jinji.backend.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.util.Set;

public class EmployeeCreateRequest {

    private String employeeNumber;
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s-]+$")
    private String surname;
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s-]+$")
    private String firstName;
    @Email
    private String email;
    private String phoneNumber;
    private LocalDate seniorityDate;
    private String departmentCode;
    private Boolean createUser; // flag
    private String password;
    private Set<String> roles;

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getSeniorityDate() {
        return seniorityDate;
    }

    public void setSeniorityDate(LocalDate seniorityDate) {
        this.seniorityDate = seniorityDate;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public Boolean getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Boolean createUser) {
        this.createUser = createUser;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}