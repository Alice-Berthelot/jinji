package com.jinji.backend.service.crud;

import com.jinji.backend.exception.ResourceNotFoundException;
import com.jinji.backend.model.dto.request.LeaveRequestCreateReview;
import com.jinji.backend.model.entity.Employee;
import com.jinji.backend.model.entity.LeaveRequest;
import com.jinji.backend.model.entity.LeaveRequestReview;
import com.jinji.backend.model.enums.LeaveRequestReviewerRole;
import com.jinji.backend.repository.LeaveRequestReviewRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LeaveRequestReviewService {

    private final LeaveRequestReviewRepository leaveRequestReviewRepository;

    public LeaveRequestReviewService(LeaveRequestReviewRepository leaveRequestReviewRepository) {
        this.leaveRequestReviewRepository = leaveRequestReviewRepository;
    }

    public void createLeaveRequestReview(LeaveRequest leaveRequest,
                                           Employee reviewer,
                                           LeaveRequestReviewerRole reviewerRole,
                                           LeaveRequestCreateReview dto) {

        if (leaveRequest == null) {
            throw new ResourceNotFoundException("Leave request not found");
        }

        LeaveRequestReview review = new LeaveRequestReview();

        review.setLeaveRequest(leaveRequest);
        review.setReviewedBy(reviewer);
        review.setReviewerRole(reviewerRole);
        review.setReviewedAt(LocalDateTime.now());
        review.setDecision(dto.getDecision());
        review.setComment(dto.getComment());

        leaveRequestReviewRepository.save(review);
    }
}