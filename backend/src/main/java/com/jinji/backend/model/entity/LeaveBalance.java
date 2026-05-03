package com.jinji.backend.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

import java.time.LocalDate;

@Entity
@Table(name = "leave_balance", schema = "business")
public class LeaveBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "label", nullable = false)
    private String label;

    @Column(name = "acquisition_start_date", nullable = false)
    private LocalDate acquisitionStartDate;

    @Column(name = "acquisition_end_date", nullable = false)
    private LocalDate acquisitionEndDate;

    @Min(0)
    @Column(name = "acquired_days", nullable = false)
    private Integer acquiredDays;

    @Min(0)
    @Column(name = "taken_days", nullable = false)
    private Integer takenDays;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "leave_type_id", nullable = false)
    private LeaveType leaveType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public LocalDate getAcquisitionStartDate() {
        return acquisitionStartDate;
    }

    public void setAcquisitionStartDate(LocalDate acquisitionStartDate) {
        this.acquisitionStartDate = acquisitionStartDate;
    }

    public LocalDate getAcquisitionEndDate() {
        return acquisitionEndDate;
    }

    public void setAcquisitionEndDate(LocalDate acquisitionEndDate) {
        this.acquisitionEndDate = acquisitionEndDate;
    }

    public Integer getAcquiredDays() {
        return acquiredDays;
    }

    public void setAcquiredDays(Integer acquiredDays) {
        this.acquiredDays = acquiredDays;
    }

    public Integer getTakenDays() {
        return takenDays;
    }

    public void setTakenDays(Integer takenDays) {
        this.takenDays = takenDays;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public LeaveType getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(LeaveType leaveType) {
        this.leaveType = leaveType;
    }
}
