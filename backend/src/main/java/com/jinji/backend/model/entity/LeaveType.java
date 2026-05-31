package com.jinji.backend.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "leave_type", schema = "business")
public class LeaveType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false)
    private String code;
    @Column(name = "label", nullable = false)
    private String label;
    @Column(name = "balance_managed")
    private boolean balanceManaged;
    @Column(name = "requestable")
    private boolean requestable;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isBalanceManaged() {
        return balanceManaged;
    }

    public void setBalanceManaged(boolean balanceManaged) {
        this.balanceManaged = balanceManaged;
    }

    public boolean isRequestable() {
        return requestable;
    }

    public void setRequestable(boolean requestable) {
        this.requestable = requestable;
    }
}
