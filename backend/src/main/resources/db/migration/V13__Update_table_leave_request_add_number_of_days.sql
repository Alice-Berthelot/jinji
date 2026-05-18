-- Add number_of_days in leave_request
ALTER TABLE business.leave_request
ADD COLUMN number_of_days NUMERIC(4,1) DEFAULT 0;

-- Constraint
ALTER TABLE business.leave_request ADD CONSTRAINT chk_number_of_days_positive CHECK (number_of_days >= 0);