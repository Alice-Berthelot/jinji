package com.jinji.backend.model.dto;

import java.time.LocalDate;
import java.util.List;

public class EmployeeHrDTO {

    private Long id;
    private String employeeNumber;
    private String surname;
    private String firstName;
    private String email;
    private String phoneNumber;
    private LocalDate seniorityDate;

    private String departmentCode;

    private List<String> teams;

    // plus tard :
    // salary
    // balances
    // manager
    // etc
}