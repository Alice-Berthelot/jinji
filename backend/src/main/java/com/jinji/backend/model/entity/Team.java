package com.jinji.backend.model.entity;

import jakarta.persistence.*;
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

    public void setManager(Employee manager) {
        this.manager = manager;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }
}