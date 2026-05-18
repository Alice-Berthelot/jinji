-- seed for FIXED French public holidays
INSERT INTO configuration.public_holiday_fixed (month, day, label)
VALUES (1, 1, 'Jour de l''An'),
(5, 1, 'Fête du Travail'),
(5, 8, 'Victoire 1945'),
(7, 14, 'Fête Nationale'),
(8, 15, 'Assomption'),
(11, 1, 'Toussaint'),
(11, 11, 'Armistice 1918'),
(12, 25, 'Noël');
