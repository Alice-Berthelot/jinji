-- Add annual leave carryover boolean in hr_policy
ALTER TABLE configuration.hr_policy
ADD COLUMN allow_annual_leave_carryover Boolean DEFAULT false;