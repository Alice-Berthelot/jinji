package com.jinji.backend.model.dto;

import com.jinji.backend.model.enums.LeaveRequestReviewerRole;
import com.jinji.backend.model.enums.LeaveReviewDecision;

import java.time.LocalDateTime;

public class LeaveRequestReviewDTO {

    private Long id;
    private LeaveRequestReviewerRole reviewerRole;
    private LeaveReviewDecision decision;
    private String comment;
    private LocalDateTime reviewedAt;

    private Long reviewerId;
    private String reviewerFirstName;
    private String reviewerLastName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LeaveRequestReviewerRole getReviewerRole() {
        return reviewerRole;
    }

    public void setReviewerRole(LeaveRequestReviewerRole reviewerRole) {
        this.reviewerRole = reviewerRole;
    }

    public LeaveReviewDecision getDecision() {
        return decision;
    }

    public void setDecision(LeaveReviewDecision decision) {
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

    public Long getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(Long reviewerId) {
        this.reviewerId = reviewerId;
    }

    public String getReviewerFirstName() {
        return reviewerFirstName;
    }

    public void setReviewerFirstName(String reviewerFirstName) {
        this.reviewerFirstName = reviewerFirstName;
    }

    public String getReviewerLastName() {
        return reviewerLastName;
    }

    public void setReviewerLastName(String reviewerLastName) {
        this.reviewerLastName = reviewerLastName;
    }
}