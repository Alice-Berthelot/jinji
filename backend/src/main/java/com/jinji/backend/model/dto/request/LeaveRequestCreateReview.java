package com.jinji.backend.model.dto.request;

import com.jinji.backend.model.enums.LeaveRequestDecision;

public class LeaveRequestCreateReview {

    private LeaveRequestDecision decision;
    private String comment;

    public LeaveRequestDecision getDecision() {
        return decision;
    }

    public void setDecision(LeaveRequestDecision decision) {
        this.decision = decision;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
