-- Add annual leave accrual period in hr_policy
ALTER TABLE configuration.hr_policy
ADD COLUMN annual_leave_accrual_period varchar(15) DEFAULT 'LEGAL';

-- Constraint
ALTER TABLE configuration.hr_policy
    ADD CONSTRAINT chk_annual_leave_accrual_period CHECK (annual_leave_accrual_period IN ('LEGAL', 'CALENDAR_YEAR', 'CUSTOM'));
