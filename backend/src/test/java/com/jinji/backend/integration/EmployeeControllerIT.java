package com.jinji.backend.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinji.backend.model.dto.request.EmployeeCreateRequest;
import com.jinji.backend.model.entity.Department;
import com.jinji.backend.repository.DepartmentRepository;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
class EmployeeControllerIT extends AbstractIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    DepartmentRepository departmentRepository;

    @Test
    @WithMockUser(roles = "HR")
    void shouldCreateEmployee() throws Exception {

        Department department = new Department();
        department.setCode("IT");

        departmentRepository.save(department);

        EmployeeCreateRequest request = new EmployeeCreateRequest();

        request.setEmployeeNumber("EMP001");
        request.setFirstName("John");
        request.setSurname("Doe");
        request.setEmail("john@company.com");
        request.setPhoneNumber("0600000000");
        request.setSeniorityDate(LocalDate.now());
        request.setDepartmentCode("IT");

        request.setCreateUser(true);
        request.setPassword("Password123");
        request.setRoles(Set.of("EMPLOYEE"));

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        org.hamcrest.Matchers.containsString(
                                "Successful registration"
                        )
                ));
    }
}