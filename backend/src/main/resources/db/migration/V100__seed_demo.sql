-- company
INSERT INTO configuration.company (identification_number, name)
VALUES ('73282932000074', 'HexaTech Solutions');

-- hr_policy
INSERT INTO configuration.hr_policy (leave_validation, allow_unpaid_leave, annual_leave_day_type, allow_annual_leave_carryover)
VALUES ('MANAGER_THEN_HR', true, 'FR_JOURS_OUVRES', true);

-- department
INSERT INTO business.department (code, name)
VALUES ('DIR', 'Direction'),
       ('RH', 'Ressources Humaines'),
       ('COM', 'Commercial'),
       ('TECH', 'Pôle Technique'),
       ('ADM', 'Support et Administration');

-- leave types
INSERT INTO business.leave_type (code, label, balance_managed, requestable)
VALUES ('CP', 'Congés payés', true, true),
    ('AM', 'Maladie non professionnelle', false, false),
    ('MP', 'Maladie professionnelle', false, false),
    ('AT', 'Accident du travail', false, false),
    ('EVE', 'Congé pour événement familial', false, false),
    ('TEMP', 'Autres absences', false, false);

-- employee
INSERT INTO business.employee (employee_number, surname, first_name, email, phone_number, seniority_date, department_id, status)
VALUES
    ('EMP483721', 'Bernard', 'Sophie', 'sophie.bernard@hexa-solutions.fr', '0611122233', '2022-09-03',
     (SELECT id FROM business.department WHERE code = 'DIR'), 'INTERNAL'),
    ('EMP158604', 'Roussel', 'Vincent', 'vincent.roussel@hexa-solutions.fr', '0611122234', '2022-01-15',
     (SELECT id FROM business.department WHERE code = 'DIR'), 'INTERNAL'),
    ('EMP726519', 'Lambert', 'Julie', 'julie.lambert@hexa-solutions.fr', '0611122235', '2024-04-19',
     (SELECT id FROM business.department WHERE code = 'RH'), 'INTERNAL'),
    ('EMP394862', 'Petit', 'Marine', 'marine.petit@hexa-solutions.fr', '0611122236', '2023-10-10',
     (SELECT id FROM business.department WHERE code = 'RH'), 'INTERNAL'),
    ('EMP817305', 'Moreau', 'Hugo', 'hugo.moreau@hexa-solutions.fr', '0611122237', '2021-07-22',
     (SELECT id FROM business.department WHERE code = 'RH'), 'INTERNAL'),
    ('EMP261948', 'Dubois', 'Sarah', 'sarah.dubois@hexa-solutions.fr', '0611122238', '2023-02-06',
     (SELECT id FROM business.department WHERE code = 'RH'), 'INTERNAL'),
    ('EMP540173', 'Faure', 'Nicolas', 'nicolas.faure@hexa-solutions.fr', '0611122239', '2024-11-15',
     (SELECT id FROM business.department WHERE code = 'COM'), 'INTERNAL'),
    ('EMP972486', 'Girard', 'Emma', 'emma.girard@hexa-solutions.fr', '0611122240', '2025-08-31',
     (SELECT id FROM business.department WHERE code = 'COM'), 'INTERNAL'),
    ('EMP315807', 'Renard', 'Paul', 'paul.renard@hexa-solutions.fr', '0611122241', '2022-05-17',
     (SELECT id FROM business.department WHERE code = 'COM'), 'INTERNAL'),
    ('EMP684291', 'Bonnet', 'Laura', 'laura.bonnet@hexa-solutions.fr', '0611122242', '2023-01-08',
     (SELECT id FROM business.department WHERE code = 'COM'), 'INTERNAL'),
    ('EMP428563', 'Marchand', 'Lucas', 'lucas.marchand@hexa-solutions.fr', '0611122243', '2022-03-14',
     (SELECT id FROM business.department WHERE code = 'COM'), 'INTERNAL'),
    ('EMP159274', 'Legrand', 'Camille', 'camille.legrand@hexa-solutions.fr', '0611122244', '2021-09-26',
     (SELECT id FROM business.department WHERE code = 'COM'), 'INTERNAL'),
    ('EMP837615', 'Barbier', 'Thomas', 'thomas.barbier@hexa-solutions.fr', '0611122245', '2023-04-03',
     (SELECT id FROM business.department WHERE code = 'COM'), 'EXTERNAL'),
    ('EMP506382', 'Rami', 'Leila', 'leila.rami@hexa-solutions.fr', '0611122263', '2025-03-08',
     (SELECT id FROM business.department WHERE code = 'COM'), 'INTERNAL'),
    ('EMP274951', 'Haddad', 'Rami', 'rami.haddad@hexa-solutions.fr', '0611122264', '2024-06-15',
     (SELECT id FROM business.department WHERE code = 'TECH'), 'INTERNAL'),
    ('EMP961430', 'Robert', 'Julien', 'julien.robert@hexa-solutions.fr', '0611122248', '2023-10-08',
     (SELECT id FROM business.department WHERE code = 'TECH'), 'INTERNAL'),
    ('EMP742186', 'Kovalenko', 'Olena', 'olena.kovalenko@hexa-solutions.fr', '0611122265', '2022-01-10',
     (SELECT id FROM business.department WHERE code = 'TECH'), 'INTERNAL'),
    ('EMP183624', 'Picard', 'Maxime', 'maxime.picard@hexa-solutions.fr', '0611122250', '2021-12-02',
     (SELECT id FROM business.department WHERE code = 'TECH'), 'INTERNAL'),
    ('EMP659807', 'Colin', 'Manon', 'manon.colin@hexa-solutions.fr', '0611122251', '2022-03-16',
     (SELECT id FROM business.department WHERE code = 'TECH'), 'INTERNAL'),
    ('EMP420958', 'Russell', 'Alexander', 'alexander.russell@hexa-solutions.fr', '0611122252', '2023-09-14',
     (SELECT id FROM business.department WHERE code = 'TECH'), 'INTERNAL'),
    ('EMP875314', 'Rames', 'Lina', 'lina.rames@hexa-solutions.fr', '0611122253', '2022-02-08',
     (SELECT id FROM business.department WHERE code = 'TECH'), 'INTERNAL'),
    ('EMP216579', 'Michel', 'Nathan', 'nathan.michel@hexa-solutions.fr', '0611122254', '2021-11-22',
     (SELECT id FROM business.department WHERE code = 'TECH'), 'INTERNAL'),
    ('EMP593148', 'Riviere', 'Isabelle', 'isabelle.riviere@hexa-solutions.fr', '0611122255', '2024-04-11',
     (SELECT id FROM business.department WHERE code = 'TECH'), 'INTERNAL'),
    ('EMP764820', 'Lopez', 'Theo', 'theo.lopez@hexa-solutions.fr', '0611122256', '2022-12-05',
     (SELECT id FROM business.department WHERE code = 'TECH'), 'INTERNAL'),
    ('EMP308451', 'Henry', 'Lea', 'lea.henry@hexa-solutions.fr', '0611122257', '2023-05-15',
     (SELECT id FROM business.department WHERE code = 'TECH'), 'EXTERNAL'),
    ('EMP627093', 'Vasseur', 'Tom', 'tom.vasseur@hexa-solutions.fr', '0611122258', '2024-01-22',
     (SELECT id FROM business.department WHERE code = 'TECH'), 'INTERNAL'),
    ('EMP451762', 'Perrin', 'Laura', 'laura.perrin@hexa-solutions.fr', '0611122259', '2025-07-01',
     (SELECT id FROM business.department WHERE code = 'ADM'), 'INTERNAL'),
    ('EMP982145', 'Arnaud', 'Cedric', 'cedric.arnaud@hexa-solutions.fr', '0611122260', '2025-10-19',
     (SELECT id FROM business.department WHERE code = 'ADM'), 'INTERNAL'),
    ('EMP137580', 'Saidi', 'Nora', 'nora.saidi@hexa-solutions.fr', '0611122267', '2026-04-17',
     (SELECT id FROM business.department WHERE code = 'ADM'), 'INTERNAL'),
    ('EMP846231', 'Lopez', 'Mathieu', 'mathieu.lopez@hexa-solutions.fr', '0611122262', '2023-09-04',
     (SELECT id FROM business.department WHERE code = 'ADM'), 'INTERNAL');

-- team
INSERT INTO business.team (label)
VALUES
    ('Ressources Humaines'),
    ('Développement commercial'),
    ('Back-end'),
    ('Front-end'),
    ('DevOps'),
    ('QA / Support'),
    ('Data / BI'),
    ('Support Administration');

-- employee_team
INSERT INTO business.employee_team (team_id, employee_id)
SELECT t.id, e.id
FROM business.team t, business.employee e
WHERE t.label = 'Ressources Humaines'
  AND e.email IN (
                  'julie.lambert@hexa-solutions.fr',
                  'marine.petit@hexa-solutions.fr',
                  'sarah.dubois@hexa-solutions.fr',
                  'laura.perrin@hexa-solutions.fr'
    );

INSERT INTO business.employee_team (team_id, employee_id)
SELECT t.id, e.id
FROM business.team t, business.employee e
WHERE t.label = 'Support Administration'
  AND e.email IN (
                  'cedric.arnaud@hexa-solutions.fr',
                  'nora.saidi@hexa-solutions.fr',
                  'manon.colin@hexa-solutions.fr'
    );

INSERT INTO business.employee_team (team_id, employee_id)
SELECT t.id, e.id
FROM business.team t, business.employee e
WHERE t.label = 'Développement commercial'
  AND e.email IN (
                  'emma.girard@hexa-solutions.fr',
                  'paul.renard@hexa-solutions.fr',
                  'laura.bonnet@hexa-solutions.fr',
                  'lucas.marchand@hexa-solutions.fr',
                  'camille.legrand@hexa-solutions.fr',
                  'thomas.barbier@hexa-solutions.fr'
    );

INSERT INTO business.employee_team (team_id, employee_id)
SELECT t.id, e.id
FROM business.team t, business.employee e
WHERE t.label = 'Back-end'
  AND e.email IN (
                  'julien.robert@hexa-solutions.fr',
                  'nathan.michel@hexa-solutions.fr',
                  'alexander.russell@hexa-solutions.fr',
                  'maxime.picard@hexa-solutions.fr',
                  'olena.kovalenko@hexa-solutions.fr'
    );

INSERT INTO business.employee_team (team_id, employee_id)
SELECT t.id, e.id
FROM business.team t, business.employee e
WHERE t.label = 'Front-end'
  AND e.email IN (
                  'theo.lopez@hexa-solutions.fr',
                  'isabelle.riviere@hexa-solutions.fr',
                  'lina.rames@hexa-solutions.fr',
                  'camille.legrand@hexa-solutions.fr'
    );

INSERT INTO business.employee_team (team_id, employee_id)
SELECT t.id, e.id
FROM business.team t, business.employee e
WHERE t.label = 'DevOps'
  AND e.email IN (
                  'lea.henry@hexa-solutions.fr',
                  'tom.vasseur@hexa-solutions.fr',
                  'rami.haddad@hexa-solutions.fr'
    );

INSERT INTO business.employee_team (team_id, employee_id)
SELECT t.id, e.id
FROM business.team t, business.employee e
WHERE t.label = 'QA / Support'
  AND e.email IN (
                  'manon.colin@hexa-solutions.fr',
                  'isabelle.riviere@hexa-solutions.fr',
                  'lina.rames@hexa-solutions.fr'
    );

INSERT INTO business.employee_team (team_id, employee_id)
SELECT t.id, e.id
FROM business.team t, business.employee e
WHERE t.label = 'Data / BI'
  AND e.email IN (
                  'olena.kovalenko@hexa-solutions.fr',
                  'maxime.picard@hexa-solutions.fr'
    );