-- seed for leave_type
INSERT INTO business.leave_type (code, label, balance_managed)
VALUES ('AM', 'Maladie non professionnelle', false),
       ('MP', 'Maladie professionnelle', false),
       ('AT', 'Accident du travail', false),
       ('EVE', 'Congé pour événement familial', false);