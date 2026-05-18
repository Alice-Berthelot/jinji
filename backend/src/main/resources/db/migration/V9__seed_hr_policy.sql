-- seed for hr_policy
INSERT INTO configuration.hr_policy (leave_validation, allow_unpaid_leave, annual_leave_day_type)
VALUES ('MANAGER_THEN_HR', true, 'FR_JOURS_OUVRES');