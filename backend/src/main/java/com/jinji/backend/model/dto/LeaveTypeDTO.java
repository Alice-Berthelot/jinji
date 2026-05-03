package com.jinji.backend.model.dto;

public class LeaveTypeDTO {

    private String code;
    private String label;

    public LeaveTypeDTO(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public String getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }
}