package com.jinji.backend.repository;

import com.jinji.backend.model.entity.LeaveRequest;
import com.jinji.backend.model.entity.LeaveRequestReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaveRequestReviewRepository extends JpaRepository<LeaveRequestReview, Long> {

    List<LeaveRequestReview> findByLeaveRequest_Id(Long leaveRequestId);
}
