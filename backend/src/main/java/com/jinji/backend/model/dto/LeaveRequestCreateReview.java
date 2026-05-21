package com.jinji.backend.model.dto;

import com.jinji.backend.model.enums.LeaveRequestDecision;

import java.time.LocalDateTime;

public class LeaveRequestCreateReview {

    private LeaveRequestDecision decision;
    private String comment;
    private LocalDateTime reviewedAt;

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

    public LocalDateTime getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(LocalDateTime reviewedAt) {
        this.reviewedAt = reviewedAt;
    }
}
