package com.jinji.backend.service;

import com.jinji.backend.model.entity.HrPolicy;
import com.jinji.backend.model.enums.LeaveValidationProcess;
import com.jinji.backend.repository.HrPolicyRepository;
import org.springframework.stereotype.Service;

@Service
public class HrPolicyService {
    private final HrPolicyRepository repository;

    public HrPolicyService(HrPolicyRepository repository) {
        this.repository = repository;
    }

    public HrPolicy getHrPolicy() {
        return repository.findHrPolicy();
    }

    public LeaveValidationProcess getLeaveValidation() {
        return repository.findLeaveValidation();
    }
}
