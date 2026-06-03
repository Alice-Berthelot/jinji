-- Add number_of_days in leave
ALTER TABLE business.leave
    ADD COLUMN number_of_days NUMERIC(4,1) DEFAULT 0;

-- Constraint
ALTER TABLE business.leave ADD CONSTRAINT chk_leave_number_of_days_positive CHECK (number_of_days >= 0);