package com.jinji.backend.model.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "public_holiday_fixed", schema = "configuration")
public class PublicHolidayFixed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "month", nullable = false)
    private int month;

    @Column(name = "day", nullable = false)
    private int day;

    @Column(name = "label")
    private String label;

    public boolean matches(LocalDate date) {
        return date.getMonthValue() == month
                && date.getDayOfMonth() == day;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}