-- --------------------------------------------------------
-- Host:                         localhost
-- Versión del servidor:         10.11.11-MariaDB-0+deb12u1 - Debian 12
-- SO del servidor:              debian-linux-gnu
-- HeidiSQL Versión:             12.10.0.7000
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Volcando estructura de base de datos para escaperoom
DROP DATABASE IF EXISTS escaperoom;
CREATE DATABASE IF NOT EXISTS escaperoom /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;
USE escaperoom;

-- Volcando estructura para tabla escaperoom.certificate
DROP TABLE IF EXISTS certificate;
CREATE TABLE IF NOT EXISTS certificate (
  idCertificate int(11) NOT NULL AUTO_INCREMENT,
  name varchar(40) NOT NULL,
  description varchar(200) DEFAULT NULL,
  isActive tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (idCertificate)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;



-- Volcando estructura para tabla escaperoom.certificatewin
DROP TABLE IF EXISTS certificatewin;
CREATE TABLE IF NOT EXISTS certificatewin (
  idCertificateWin int(11) NOT NULL AUTO_INCREMENT,
  idCertificate int(11) NOT NULL,
  idPlayer int(11) NOT NULL,
  idRoom int(11) NOT NULL,
  description varchar(200) DEFAULT NULL,
  dateDelivery timestamp NOT NULL DEFAULT current_timestamp(),
  isActive tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (idCertificateWin),
  KEY certificatewin_certificate_FK (idCertificate),
  KEY certificatewin_player_FK (idPlayer),
  KEY certificatewin_room_FK (idRoom),
  CONSTRAINT certificatewin_certificate_FK FOREIGN KEY (idCertificate) REFERENCES certificate (idCertificate) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT certificatewin_player_FK FOREIGN KEY (idPlayer) REFERENCES player (idPlayer) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT certificatewin_room_FK FOREIGN KEY (idRoom) REFERENCES room (idRoom) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;



-- Volcando estructura para tabla escaperoom.clue
DROP TABLE IF EXISTS clue;
CREATE TABLE IF NOT EXISTS clue (
  idClue int(11) NOT NULL AUTO_INCREMENT,
  idRoom int(11) NOT NULL,
  name varchar(40) NOT NULL,
  description varchar(200) DEFAULT NULL,
  price decimal(15,2) NOT NULL,
  isActive tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (idClue),
  KEY clue_room_FK (idRoom),
  CONSTRAINT clue_room_FK FOREIGN KEY (idRoom) REFERENCES room (idRoom) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;



-- Volcando estructura para tabla escaperoom.decoration
DROP TABLE IF EXISTS decoration;
CREATE TABLE IF NOT EXISTS decoration (
  idDecoration int(11) NOT NULL AUTO_INCREMENT,
  idRoom int(11) NOT NULL,
  name varchar(40) NOT NULL,
  description varchar(200) DEFAULT NULL,
  price decimal(15,2) NOT NULL,
  isActive tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (idDecoration),
  KEY decoration_room_FK (idRoom),
  CONSTRAINT decoration_room_FK FOREIGN KEY (idRoom) REFERENCES room (idRoom) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;



-- Volcando estructura para tabla escaperoom.escaperoom
DROP TABLE IF EXISTS escaperoom;
CREATE TABLE IF NOT EXISTS escaperoom (
  idEscaperoom int(11) NOT NULL AUTO_INCREMENT,
  name varchar(40) NOT NULL,
  PRIMARY KEY (idEscaperoom)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;



-- Volcando estructura para vista escaperoom.inventory
DROP VIEW IF EXISTS inventory;
-- Creando tabla temporal para superar errores de dependencia de VIEW
CREATE TABLE inventory (
	inventory VARCHAR(1) NOT NULL COLLATE 'utf8mb4_general_ci',
	id INT(11) NOT NULL,
	name VARCHAR(1) NOT NULL COLLATE 'utf8mb4_general_ci',
	price DECIMAL(15,2) NOT NULL
) ENGINE=MyISAM;

-- Volcando estructura para tabla escaperoom.notifications
DROP TABLE IF EXISTS notifications;
CREATE TABLE IF NOT EXISTS notifications (
  idNotification int(11) NOT NULL AUTO_INCREMENT,
  idPlayer int(11) NOT NULL,
  message text NOT NULL,
  dateTimeSent timestamp NULL DEFAULT current_timestamp(),
  isActive tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (idNotification),
  KEY notifications_player_FK (idPlayer),
  CONSTRAINT notifications_player_FK FOREIGN KEY (idPlayer) REFERENCES player (idPlayer) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;



-- Volcando estructura para tabla escaperoom.player
DROP TABLE IF EXISTS player;
CREATE TABLE IF NOT EXISTS player (
  idPlayer int(11) NOT NULL AUTO_INCREMENT,
  name varchar(40) NOT NULL,
  email varchar(40) NOT NULL,
  password varchar(20) NOT NULL,
  RegistrationDate timestamp NULL DEFAULT current_timestamp(),
  isSubscribed tinyint(1) NOT NULL DEFAULT 0,
  isActive tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (idPlayer)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;



-- Volcando estructura para tabla escaperoom.reward
DROP TABLE IF EXISTS reward;
CREATE TABLE IF NOT EXISTS reward (
  idReward int(11) NOT NULL AUTO_INCREMENT,
  name varchar(40) NOT NULL,
  description varchar(200) DEFAULT NULL,
  isActive tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (idReward)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;



-- Volcando estructura para tabla escaperoom.rewardwin
DROP TABLE IF EXISTS rewardwin;
CREATE TABLE IF NOT EXISTS rewardwin (
  idRewardWin int(11) NOT NULL AUTO_INCREMENT,
  idReward int(11) NOT NULL,
  idPlayer int(11) NOT NULL,
  description varchar(200) DEFAULT NULL,
  dateDelivery timestamp NOT NULL DEFAULT current_timestamp(),
  isActive tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (idRewardWin),
  KEY rewardwin_reward_FK (idReward),
  KEY rewardwin_player_FK (idPlayer),
  CONSTRAINT rewardwin_player_FK FOREIGN KEY (idPlayer) REFERENCES player (idPlayer) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT rewardwin_reward_FK FOREIGN KEY (idReward) REFERENCES reward (idReward) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;



-- Volcando estructura para tabla escaperoom.room
DROP TABLE IF EXISTS room;
CREATE TABLE IF NOT EXISTS room (
  idRoom int(11) NOT NULL AUTO_INCREMENT,
  idEscapeRoom int(11) NOT NULL,
  name varchar(40) NOT NULL,
  description varchar(200) DEFAULT NULL,
  price decimal(15,2) NOT NULL,
  idDifficulty int(11) NOT NULL,
  idTheme int(11) NOT NULL,
  isActive tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (idRoom),
  KEY room_escaperoom_FK (idEscapeRoom),
  CONSTRAINT room_escaperoom_FK FOREIGN KEY (idEscapeRoom) REFERENCES escaperoom (idEscaperoom)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;



-- Volcando estructura para tabla escaperoom.sale
DROP TABLE IF EXISTS sale;
CREATE TABLE IF NOT EXISTS sale (
  idSale int(11) NOT NULL AUTO_INCREMENT,
  idTicket int(11) NOT NULL,
  idPlayer int(11) NOT NULL,
  idRoom int(11) NOT NULL,
  players int(11) NOT NULL,
  price decimal(5,2) NOT NULL,
  completion int(11) NOT NULL,
  dateSale timestamp NOT NULL DEFAULT current_timestamp(),
  isActive tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (idSale),
  KEY sale_ticket_FK (idTicket),
  KEY sale_player_FK (idPlayer),
  KEY sale_room_FK (idRoom),
  CONSTRAINT sale_player_FK FOREIGN KEY (idPlayer) REFERENCES player (idPlayer) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT sale_room_FK FOREIGN KEY (idRoom) REFERENCES room (idRoom) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT sale_ticket_FK FOREIGN KEY (idTicket) REFERENCES ticket (idTicket) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;



-- Volcando estructura para tabla escaperoom.ticket
DROP TABLE IF EXISTS ticket;
CREATE TABLE IF NOT EXISTS ticket (
  idTicket int(11) NOT NULL AUTO_INCREMENT,
  name varchar(40) NOT NULL,
  description varchar(200) DEFAULT NULL,
  price decimal(15,2) NOT NULL,
  isActive tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (idTicket)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;



-- Eliminando tabla temporal y crear estructura final de VIEW
DROP TABLE IF EXISTS inventory;
CREATE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW inventory AS select final.inventory AS inventory,final.id AS id,final.name AS name,final.price AS price from (select 'Room' AS inventory,r.idRoom AS id,r.name AS name,r.price AS price from room r where r.isActive = 1 union all select 'Clue' AS inventory,c.idClue AS id,c.name AS name,c.price AS price from clue c where c.isActive = 1 union all select 'Decoration' AS inventory,d.idDecoration AS id,d.name AS name,d.price AS price from decoration d where d.isActive = 1) final
;

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;


