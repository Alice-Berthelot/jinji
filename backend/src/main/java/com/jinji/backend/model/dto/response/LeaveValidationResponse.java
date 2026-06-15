package com.jinji.backend.model.dto.response;

import com.jinji.backend.model.enums.LeaveValidationProcess;

public record LeaveValidationResponse(
    LeaveValidationProcess value
) {}