package com.jinji.backend.model.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "team", schema = "business")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "label")
    private String label;

    @ManyToOne
    @JoinColumn(name = "manager")
    private Employee manager;

    @ManyToMany
    @JoinTable(
        name = "employee_team",
        schema = "business",
        joinColumns = @JoinColumn(name = "team_id"),
        inverseJoinColumns = @JoinColumn(name = "employee_id")
    )
    private Set<Employee> employees;

    public void addEmployee(Employee employee) {
        if (employees == null) {
            employees = new HashSet<>();
        }
        employees.add(employee);

        if (employee.getTeams() == null) {
            employee.setTeams(new HashSet<>());
        }
        employee.getTeams().add(this);
    }

    public Long getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public Employee getManager() {
        return manager;
    }

    public Set<Employee> getEmployees() {
        return employees;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setManager(Employee employee) {
        this.manager = employee;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }
}