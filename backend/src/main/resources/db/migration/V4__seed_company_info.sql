-- seed for company
INSERT INTO configuration.company (identification_number, name)
VALUES ('73282932000074', 'HexaTech Solutions');

-- seed for department
INSERT INTO business.department (code, name)
VALUES ('DIR', 'Direction'),
       ('RH', 'Ressources Humaines'),
       ('COM', 'Commercial'),
       ('TECH', 'Pôle Technique'),
       ('ADM', 'Support et Administration');