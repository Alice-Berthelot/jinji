package com.jinji.backend.model.dto;

import java.time.LocalDate;

public class EmployeeDTO {

    private String employeeNumber;
    private String surname;
    private String firstName;
    private String email;
    private String phoneNumber;
    private LocalDate seniorityDate;
    private String departmentCode;

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
}