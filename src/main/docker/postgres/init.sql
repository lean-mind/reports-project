DROP DATABASE reports;
DROP USER IF EXISTS reports;

CREATE USER reports
WITH CREATEDB
  LOGIN
  PASSWORD '12345';

CREATE DATABASE reports
OWNER reports
ENCODING 'utf-8';

-- Start from here if you use Docker or Heroku database
-- Heroku database uses another username and database name

DROP TABLE IF EXISTS establishments;
CREATE TABLE establishments
(
  ID        SERIAL PRIMARY KEY NOT NULL,
  NAME      VARCHAR(200)       NOT NULL,
  EXTRADATA TEXT               NULL
);

DROP TABLE IF EXISTS invoices;
CREATE TABLE invoices
(
  ID            SERIAL PRIMARY KEY NOT NULL,
  ESTABLISHMENTID   INTEGER,
  BUSINESSDAY       TEXT               NULL,
  DATE              TEXT               NULL,
  GROSSAMOUNT       TEXT               NULL,
  NETAMOUNT         TEXT               NULL,
  SERIALNUMBER      TEXT               NULL,
  FILENAME          TEXT               NULL,
  XML               TEXT               NULL,
  EXTRADATA         TEXT               NULL
);

DROP TABLE IF EXISTS ftpUsers;
CREATE TABLE ftpUsers
(
  ID            SERIAL PRIMARY KEY NOT NULL,
  ESTABLISHMENT INTEGER,
  USERNAME      VARCHAR(200)       NOT NULL,
  PASSWORDHASH  VARCHAR(200)       NOT NULL,
  HOMEFOLDER    TEXT               NULL,
  EXTRADATA     TEXT               NULL
);

DROP TABLE IF EXISTS webUsers;
CREATE TABLE webUsers
(
  ID           SERIAL PRIMARY KEY NOT NULL,
  USERNAME     VARCHAR(200)       NOT NULL,
  PASSWORDHASH VARCHAR(200)       NOT NULL,
  EXTRADATA    TEXT               NULL
);

DROP TABLE IF EXISTS establishments_webusers;
CREATE TABLE establishments_webusers
(
  ID        SERIAL PRIMARY KEY NOT NULL,
  ESTABLISHMENT INTEGER,
  WEBUSER INTEGER
);

INSERT INTO establishments(id, name)
VALUES (1, 'La cuchara sana'),
  (2, 'Hierba Luisa'),
  (3, 'La Oliva');

INSERT INTO ftpusers(id, establishment, username, passwordhash, homefolder)
VALUES (1, 1, 'ftp@lacucharasana.es', '$2a$12$5DiOhn.Fn2pwBZt0rzDQ8.E9ndkmhOU7RLOIVGxL3mgfxkd6QRAw.', '1 - La cuchara sana'),
  (2, 2, 'ftp@hierbaluisa.es', '$2a$12$7uMrYU6zsGfEg/s9Vf3aS.ZvE7PgLPCfQYSgMLOO4qdQ9AiEIC0jG', '2 - Hierba Luisa'),
  (3, 3, 'ftp@laoliva.es', '$2a$12$Ga4DFIezSABzaHXy/izgaezKn42BlZJKKsWp3bA2KsOgBqdCLg4cC', '3 - Hierba Luisa');

INSERT INTO webusers(id, username, passwordhash)
VALUES (1, 'reports@leanmind.es', '$2a$12$6ECiKFpTcacKjRLwHS12UOJaTp1f2S.b65zzIPwxqDxi/TVHPaZri');

INSERT INTO establishments_webusers(establishment, webuser)
VALUES (1, 1),
  (2, 1),
  (3, 1);

INSERT INTO invoices(establishmentid, businessday, date, grossamount, netamount, serialnumber)
VALUES (1, '2017-04-10', '2017-04-10T09:10:05', '6.50', '6.00', 'T1'),
  (1, '2017-04-11', '2017-04-11T12:30:45', '21.00', '19.50', 'T2'),
  (1, '2017-04-12', '2017-04-12T15:50:12', '14.50', '13.00', 'T3'),
  (2, '2017-04-10', '2017-04-10T15:50:12', '25.50', '24.75', 'T4'),
  (2, '2017-04-12', '2017-04-12T22:10:56', '50.75', '48.30', 'T5'),
  (3, '2017-04-11', '2017-04-11T21:56:23', '28.75', '27.30', 'T6'),
  (3, '2017-04-12', '2017-04-12T22:45:34', '37.10', '35.80', 'T7'),
  (3, '2017-04-12', '2017-04-12T15:32:15', '14.25', '13.40', 'T8');