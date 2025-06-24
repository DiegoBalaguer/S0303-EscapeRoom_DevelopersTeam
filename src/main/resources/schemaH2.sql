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


-- Create tables

CREATE TABLE escaperoom (
  idEscaperoom INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(40) NOT NULL
);

CREATE TABLE player (
  idPlayer INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(40) NOT NULL,
  email VARCHAR(40) NOT NULL,
  password VARCHAR(20) NOT NULL,
  isSubscribed BOOLEAN DEFAULT FALSE,
  isActive BOOLEAN DEFAULT TRUE
);

CREATE TABLE room (
  idRoom INT PRIMARY KEY AUTO_INCREMENT,
  idEscapeRoom INT NOT NULL,
  name VARCHAR(40) NOT NULL,
  description VARCHAR(200),
  price DECIMAL(5,2) NOT NULL,
  idDifficulty INT NOT NULL,
  idTheme INT NOT NULL,
  isActive BOOLEAN DEFAULT TRUE  
);

CREATE TABLE clue (
  idClue INT PRIMARY KEY AUTO_INCREMENT,
  idRoom INT NOT NULL,
  name VARCHAR(40) NOT NULL,
  description VARCHAR(200),
  price DECIMAL(5,2) NOT NULL,
  isActive BOOLEAN DEFAULT TRUE
);

CREATE TABLE decoration (
  idDecoration INT PRIMARY KEY AUTO_INCREMENT,
  idRoom INT NOT NULL,
  name VARCHAR(40) NOT NULL,
  description varchar(200),
  price DECIMAL(5,2) NOT NULL,
  isActive BOOLEAN DEFAULT TRUE
);


CREATE TABLE certificate (
  idCertificate INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(40) NOT NULL,
  description VARCHAR(200),
  isActive BOOLEAN DEFAULT TRUE
);

CREATE TABLE reward (
  idReward INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(40) NOT NULL,
  description VARCHAR(200),
  isActive BOOLEAN DEFAULT TRUE
);


CREATE TABLE ticket (
  idTicket INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(40) NOT NULL,
  description VARCHAR(200),
  price DECIMAL(5,2) NOT NULL,  
  isActive BOOLEAN DEFAULT TRUE
);

CREATE TABLE sale (
	idSale INT PRIMARY KEY AUTO_INCREMENT,
	idTicket INT NOT NULL,
	idPlayer INT NOT NULL,
	idRoom INT NOT NULL,
	players INT NOT NULL,
	price DECIMAL(5, 2) NOT NULL,
	completion INT NOT NULL,
	dateSale TIMESTAMP,
    isActive BOOLEAN DEFAULT TRUE
);

CREATE TABLE certificatewin (
	idCertificateWin INT PRIMARY KEY AUTO_INCREMENT,
	idCertificate INT NOT NULL,
	idPlayer INT NOT NULL,
	idRoom INT NOT NULL,
	description VARCHAR(200),
	dateDelivery TIMESTAMP,
    isActive BOOLEAN DEFAULT TRUE
);

CREATE TABLE rewardwin (
	idRewardWin INT PRIMARY KEY AUTO_INCREMENT,
	idReward INT NOT NULL,
	idPlayer INT NOT NULL,
	description VARCHAR(200),
	dateDelivery TIMESTAMP,
    isActive BOOLEAN DEFAULT TRUE
);


-- Insert data test

INSERT INTO escaperoom (idEscaperoom, name) VALUES
(1, 'Escape Room');

INSERT INTO player (idPlayer, name, email, password, isSubscribed, isActive) VALUES
(1, 'Alan Turin', 'AlanTurin@gmail.com', 'passDeATurin', FALSE, TRUE),
(2, 'Joan Clarke', 'JoanClarke@gmail.com', 'passDeJClarke', FALSE, TRUE),
(3, 'Katherine Johnson', 'KatherineJohnson@gmail.com', 'passDeKJohnson', FALSE, TRUE),
(4, 'Dorothy Vaughan', 'DorothyVaughan@gmail.com', 'passDeDVaughan', FALSE, TRUE),
(5, 'Mary Jackson', 'MaryJackson@gmail.com', 'passDeMJackson', FALSE, TRUE),
(6, 'Albert Einstein', 'AlbertEinstein@gmail.com', 'passDeAEinstein', FALSE, TRUE),
(7, 'Marie Curie', 'MarieCurie@gmail.com', 'passDeMCurie', FALSE, TRUE),
(8, 'Isaac Newton', 'IsaacNewton@gmail.com', 'passDeINewton', FALSE, TRUE),
(9, 'Alexander Fleming', 'AlexanderFleming@gmail.com', 'passDeAFleming', FALSE, TRUE),
(10, 'Dmitri Mendeléyev', 'DmitriMendeleyev@gmail.com', 'passDeDMendeleyev', FALSE, TRUE),
(11, 'Wilhelm Conrad Röntgen', 'WilhelmConradRontgen@gmail.com', 'passDeWiCRontgen', FALSE, TRUE),
(12, 'Tim Berners-Lee', 'TimBernersLee@gmail.com', 'passDeTBernersLee', FALSE, TRUE);

INSERT INTO room (idRoom, idEscapeRoom, name, description, price, idDifficulty, idTheme, isActive) VALUES
(1, 1, 'Muerte en las piramedes', 'Un vertiginoso recorrido por las piramedes de Egipto buscando al culpable', 25, 1, 1, TRUE),
(2, 1, 'Donde esta mi licor', 'Podras ayudar a nuestro gangster a encontrar su licor perdido en la liy seca?', 40, 1, 1, TRUE),
(3, 1, 'Escapada a Marte', 'Realizaremos un viaje hasta Marte, podras sobrevivir a todos los incidentes?', 40, 1, 1, TRUE);

INSERT INTO clue (idClue, idRoom, name, description, price, isActive) VALUES
(1, 1, 'Pergamino', 'Una historia para leer', 12, TRUE),
(2, 1, 'Figura', 'Una vella afrodita', 18, TRUE),
(3, 2, 'Botella', 'El preciado tesoro', 10, TRUE),
(4, 3, 'Casco', 'Importante para salir al espacio exterior', 60, TRUE),
(5, 3, 'Bota', 'Que puede decirnos?', 40, TRUE);

INSERT INTO decoration (idDecoration, idRoom, name, description, price, isActive) VALUES
(1, 1, 'Piramide', 'Aquí se desarrolla nuestra historia', 12, TRUE),
(2, 1, 'Silla', 'Que hace aquí una silla victoriana', 18, TRUE),
(3, 2, 'Mesa', 'En esta mesa se escribio el final', 60, TRUE),
(4, 3, 'Perchero', 'Tendremos que guardar las chaquetas', 60, TRUE),
(5, 3, 'Cocina', 'Un lugar donde reunirse', 40, TRUE);


INSERT INTO certificate (idCertificate, name, description, isActive) VALUES
(1, 'Sala completada', 'Has demostrado que podias terminar nuestra sala', TRUE),
(2, 'El mas rapido', 'Eres un relampago', TRUE),
(3, 'Mas desafios completados', 'Eres nuestro heroe', TRUE);


INSERT INTO reward (idReward, name, description, isActive) VALUES
(1, 'Reward 01', 'Lo has logrado', TRUE),
(2, 'Reward 02', 'Lo has logrado otra vez', TRUE),
(3, 'Reward 03', 'Eres un genio', TRUE);


INSERT INTO ticket (idTicket, name, description, price, isActive) VALUES
(1, 'Normal', 'Ticket con tiempo estandard para la resolución de la habitación', 100, TRUE),
(2, 'Rapido', 'Ticket para jugar una habitación con la mitad de pistas', 50, TRUE),
(3, 'Extendido', 'Ticket con tiempo extra para la resolución de la habitación', 200, TRUE),
(4, 'Super', 'Ticket con tiempo extra y ayudas para la resolución de la habitación', 250, TRUE);

INSERT INTO sale (idSale, idTicket, idPlayer, idRoom, players, price, completion, dateSale, isActive) VALUES
(1, 1, 1, 1, 2, 200, 100, '2025-06-07 14:13:31', TRUE),
(2, 2, 2, 1, 6, 300, 98, '2025-06-07 14:13:31', TRUE),
(3, 3, 6, 2, 4, 800, 100, '2025-06-07 14:13:31', TRUE),
(4, 4, 8, 3, 2, 500, 90, '2025-06-07 14:13:31', TRUE);


INSERT INTO certificatewin (idCertificateWin, idCertificate, idPlayer, idRoom, description, dateDelivery, isActive) VALUES
(1, 1, 1, 1, 'No esperabamos menos de ti', '2025-06-07 14:13:31', TRUE),
(2, 2, 1, 2, 'No esperabamos menos de ti', '2025-06-07 14:13:31', TRUE),
(3, 3, 1, 3, 'No esperabamos menos de ti', '2025-06-07 14:13:31', TRUE),
(4, 1, 2, 1, 'No esperabamos menos de ti', '2025-06-07 14:13:31', TRUE),
(5, 2, 3, 2, 'No esperabamos menos de ti', '2025-06-07 14:13:31', TRUE);

INSERT INTO rewardwin (idRewardWin, idReward, idPlayer, description, dateDelivery, isActive) VALUES
(1, 1, 1, 'No esperabamos menos de ti', '2025-06-07 14:13:31', TRUE),
(2, 2, 1, 'No esperabamos menos de ti', '2025-06-07 14:13:31', TRUE),
(3, 3, 1, 'No esperabamos menos de ti', '2025-06-07 14:13:31', TRUE),
(4, 1, 2, 'No esperabamos menos de ti', '2025-06-07 14:13:31', TRUE),
(5, 2, 3, 'No esperabamos menos de ti', '2025-06-07 14:13:31', TRUE);

