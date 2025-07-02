-- Drop tables if exists

DROP TABLE IF EXISTS escaperoom;
DROP TABLE IF EXISTS player;
DROP TABLE IF EXISTS room;
DROP TABLE IF EXISTS clue;
DROP TABLE IF EXISTS decoration;
DROP TABLE IF EXISTS certificate;
DROP TABLE IF EXISTS reward;
DROP TABLE IF EXISTS ticket;
DROP TABLE IF EXISTS sale;
DROP TABLE IF EXISTS certificatewin;
DROP TABLE IF EXISTS rewardwin;
DROP TABLE IF EXISTS notifications;


-- Create tables

CREATE TABLE escaperoom (
  idEscaperoom INTEGER PRIMARY KEY AUTOINCREMENT,
  name VARCHAR(40) NOT NULL
);

CREATE TABLE player (
  idPlayer INTEGER PRIMARY KEY AUTOINCREMENT,
  name VARCHAR(40) NOT NULL,
  email VARCHAR(40) NOT NULL,
  password VARCHAR(20) NOT NULL,
  RegistrationDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  isSubscribed BOOLEAN DEFAULT FALSE,
  isActive BOOLEAN DEFAULT TRUE
);

CREATE TABLE room (
  idRoom INTEGER PRIMARY KEY AUTOINCREMENT,
  idEscapeRoom INTEGER NOT NULL,
  name VARCHAR(40) NOT NULL,
  description VARCHAR(200),
  price DECIMAL(5,2) NOT NULL,
  idDifficulty INTEGER NOT NULL,
  idTheme INTEGER NOT NULL,
  isActive BOOLEAN DEFAULT TRUE  
);

CREATE TABLE clue (
  idClue INTEGER PRIMARY KEY AUTOINCREMENT,
  idRoom INTEGER NOT NULL,
  name VARCHAR(40) NOT NULL,
  description VARCHAR(200),
  price DECIMAL(5,2) NOT NULL,
  isActive BOOLEAN DEFAULT TRUE
);

CREATE TABLE decoration (
  idDecoration INTEGER PRIMARY KEY AUTOINCREMENT,
  idRoom INTEGER NOT NULL,
  name VARCHAR(40) NOT NULL,
  description varchar(200),
  price DECIMAL(5,2) NOT NULL,
  isActive BOOLEAN DEFAULT TRUE
);


CREATE TABLE certificate (
  idCertificate INTEGER PRIMARY KEY AUTOINCREMENT,
  name VARCHAR(40) NOT NULL,
  description VARCHAR(200),
  isActive BOOLEAN DEFAULT TRUE
);

CREATE TABLE reward (
  idReward INTEGER PRIMARY KEY AUTOINCREMENT,
  name VARCHAR(40) NOT NULL,
  description VARCHAR(200),
  isActive BOOLEAN DEFAULT TRUE
);


CREATE TABLE ticket (
  idTicket INTEGER PRIMARY KEY AUTOINCREMENT,
  name VARCHAR(40) NOT NULL,
  description VARCHAR(200),
  price DECIMAL(5,2) NOT NULL,  
  isActive BOOLEAN DEFAULT TRUE
);

CREATE TABLE sale (
	idSale INTEGER PRIMARY KEY AUTOINCREMENT,
	idTicket INTEGER NOT NULL,
	idPlayer INTEGER NOT NULL,
	idRoom INTEGER NOT NULL,
	players INTEGER NOT NULL,
	price DECIMAL(5, 2) NOT NULL,
	completion INTEGER NOT NULL,
	dateSale TIMESTAMP,
    isActive BOOLEAN DEFAULT TRUE
);

CREATE TABLE certificatewin (
	idCertificateWin INTEGER PRIMARY KEY AUTOINCREMENT,
	idCertificate INTEGER NOT NULL,
	idPlayer INTEGER NOT NULL,
	idRoom INTEGER NOT NULL,
	description VARCHAR(200),
	dateDelivery TIMESTAMP,
    isActive BOOLEAN DEFAULT TRUE
);

CREATE TABLE rewardwin (
	idRewardWin INTEGER PRIMARY KEY AUTOINCREMENT,
	idReward INTEGER NOT NULL,
	idPlayer INTEGER NOT NULL,
	description VARCHAR(200),
	dateDelivery TIMESTAMP,
    isActive BOOLEAN DEFAULT TRUE
);

CREATE TABLE notifications (
     idNotification INTEGER PRIMARY KEY AUTOINCREMENT,
     idPlayer INTEGER NOT NULL,
     message TEXT NOT NULL,
     dateTimeSent TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    isActive BOOLEAN DEFAULT TRUE
);


-- create view's

DROP VIEW IF EXISTS inventoryTotal;
DROP VIEW IF EXISTS inventory;

CREATE VIEW inventory AS
SELECT final.inventory, final.id, final.name, final.price
FROM (
    SELECT 'Room' inventory, r.idRoom id, r.name, r.price
    FROM ROOM r
    WHERE r.ISACTIVE = TRUE

    UNION ALL

    SELECT 'Clue' inventory, c.idClue id, c.name, c.price
    FROM clue c
    WHERE c.ISACTIVE = TRUE

    UNION ALL

    SELECT 'Decoration' inventory, d.idDecoration id, d.name, d.price
    FROM decoration d
    WHERE d.ISACTIVE = TRUE
) final;

CREATE VIEW inventoryTotal AS
    SELECT u.inventory, u.id, u.name, u.PRICE
    FROM inventory u

    UNION ALL

    SELECT 'TOTAL' AS inventory, 0 AS id, '' AS name, ti.PRICE
    FROM (
        SELECT sum(i.price) AS price
        FROM inventory i
     ) ti;


-- Insert data test

INSERT INTO escaperoom (idEscaperoom, name) VALUES
(1, 'Escape Room');

INSERT INTO player (name, email, password, isSubscribed, isActive) VALUES
('Alan Turin', 'AlanTurin@gmail.com', 'passDeATurin', FALSE, TRUE),
('Joan Clarke', 'JoanClarke@gmail.com', 'passDeJClarke', FALSE, TRUE),
('Katherine Johnson', 'KatherineJohnson@gmail.com', 'passDeKJohnson', FALSE, TRUE),
('Dorothy Vaughan', 'DorothyVaughan@gmail.com', 'passDeDVaughan', FALSE, TRUE),
('Mary Jackson', 'MaryJackson@gmail.com', 'passDeMJackson', FALSE, TRUE),
('Albert Einstein', 'AlbertEinstein@gmail.com', 'passDeAEinstein', FALSE, TRUE),
('Marie Curie', 'MarieCurie@gmail.com', 'passDeMCurie', FALSE, TRUE),
('Isaac Newton', 'IsaacNewton@gmail.com', 'passDeINewton', FALSE, TRUE),
('Alexander Fleming', 'AlexanderFleming@gmail.com', 'passDeAFleming', FALSE, TRUE),
('Dmitri Mendeléyev', 'DmitriMendeleyev@gmail.com', 'passDeDMendeleyev', FALSE, TRUE),
('Wilhelm Conrad Röntgen', 'WilhelmConradRontgen@gmail.com', 'passDeWiCRontgen', FALSE, TRUE),
('Tim Berners-Lee', 'TimBernersLee@gmail.com', 'passDeTBernersLee', FALSE, TRUE);

INSERT INTO room (idEscapeRoom, name, description, price, idDifficulty, idTheme, isActive) VALUES
(1, 'Muerte en las piramedes', 'Un vertiginoso recorrido por las piramedes de Egipto buscando al culpable', 25, 1, 1, TRUE),
(1, 'Donde esta mi licor', 'Podras ayudar a nuestro gangster a encontrar su licor perdido en la liy seca?', 40, 1, 1, TRUE),
(1, 'Escapada a Marte', 'Realizaremos un viaje hasta Marte, podras sobrevivir a todos los incidentes?', 40, 1, 1, TRUE);

INSERT INTO clue (idRoom, name, description, price, isActive) VALUES
(1, 'Pergamino', 'Una historia para leer', 12, TRUE),
(1, 'Figura', 'Una vella afrodita', 18, TRUE),
(2, 'Botella', 'El preciado tesoro', 10, TRUE),
(3, 'Casco', 'Importante para salir al espacio exterior', 60, TRUE),
(3, 'Bota', 'Que puede decirnos?', 40, TRUE);

INSERT INTO decoration (idRoom, name, description, price, isActive) VALUES
(1, 'Piramide', 'Aquí se desarrolla nuestra historia', 12, TRUE),
(1, 'Silla', 'Que hace aquí una silla victoriana', 18, TRUE),
(2, 'Mesa', 'En esta mesa se escribio el final', 60, TRUE),
(3, 'Perchero', 'Tendremos que guardar las chaquetas', 60, TRUE),
(3, 'Cocina', 'Un lugar donde reunirse', 40, TRUE);


INSERT INTO certificate (name, description, isActive) VALUES
('Sala completada', 'Has demostrado que podias terminar nuestra sala', TRUE),
('El mas rapido', 'Eres un relampago', TRUE),
('Mas desafios completados', 'Eres nuestro heroe', TRUE);


INSERT INTO reward (name, description, isActive) VALUES
('Reward 01', 'Lo has logrado', TRUE),
('Reward 02', 'Lo has logrado otra vez', TRUE),
('Reward 03', 'Eres un genio', TRUE);


INSERT INTO ticket (name, description, price, isActive) VALUES
('Normal', 'Ticket con tiempo estandard para la resolución de la habitación', 100, TRUE),
('Rapido', 'Ticket para jugar una habitación con la mitad de pistas', 50, TRUE),
('Extendido', 'Ticket con tiempo extra para la resolución de la habitación', 200, TRUE),
('Super', 'Ticket con tiempo extra y ayudas para la resolución de la habitación', 250, TRUE);

INSERT INTO sale (idTicket, idPlayer, idRoom, players, price, completion, dateSale, isActive) VALUES
(1, 1, 1, 2, 200, 100, '2025-06-07 14:13:31', TRUE),
(2, 2, 1, 6, 300, 98, '2025-06-07 14:13:31', TRUE),
(3, 6, 2, 4, 800, 100, '2025-06-07 14:13:31', TRUE),
(4, 8, 3, 2, 500, 90, '2025-06-07 14:13:31', TRUE);


INSERT INTO certificatewin (idCertificate, idPlayer, idRoom, description, dateDelivery, isActive) VALUES
(1, 1, 1, 'No esperabamos menos de ti', '2025-06-07 14:13:31', TRUE),
(2, 1, 2, 'No esperabamos menos de ti', '2025-06-07 14:13:31', TRUE),
(3, 1, 3, 'No esperabamos menos de ti', '2025-06-07 14:13:31', TRUE),
(1, 2, 1, 'No esperabamos menos de ti', '2025-06-07 14:13:31', TRUE),
(2, 3, 2, 'No esperabamos menos de ti', '2025-06-07 14:13:31', TRUE);

INSERT INTO rewardwin (idReward, idPlayer, description, dateDelivery, isActive) VALUES
(1, 1, 'No esperabamos menos de ti', '2025-06-07 14:13:31', TRUE),
(2, 1, 'No esperabamos menos de ti', '2025-06-07 14:13:31', TRUE),
(3, 1, 'No esperabamos menos de ti', '2025-06-07 14:13:31', TRUE),
(1, 2, 'No esperabamos menos de ti', '2025-06-07 14:13:31', TRUE),
(2, 3, 'No esperabamos menos de ti', '2025-06-07 14:13:31', TRUE);
