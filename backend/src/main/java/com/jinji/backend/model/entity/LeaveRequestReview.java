package com.jinji.backend.model.entity;

import com.jinji.backend.model.enums.LeaveRequestReviewerRole;
import com.jinji.backend.model.enums.LeaveReviewDecision;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "leave_request_review", schema = "business")
public class LeaveRequestReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "reviewer_role", nullable = false)
    private LeaveRequestReviewerRole reviewerRole;

    @Enumerated(EnumType.STRING)
    @Column(name = "decision", nullable = false)
    private LeaveReviewDecision decision;

    @Column(name = "comment")
    private String comment;

    @Column(name = "reviewed_at", nullable = false)
    private LocalDateTime reviewedAt;

    @ManyToOne
    @JoinColumn(name = "leave_request_id", nullable = false)
    private LeaveRequest leaveRequest;

    @ManyToOne
    @JoinColumn(name = "reviewed_by", nullable = false)
    private Employee reviewedBy;

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

    public LeaveRequest getLeaveRequest() {
        return leaveRequest;
    }

    public void setLeaveRequest(LeaveRequest leaveRequest) {
        this.leaveRequest = leaveRequest;
    }

    public Employee getReviewedBy() {
        return reviewedBy;
    }

    public void setReviewedBy(Employee reviewedBy) {
        this.reviewedBy = reviewedBy;
    }
}
