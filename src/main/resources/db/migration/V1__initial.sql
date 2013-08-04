
/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table burner
# ------------------------------------------------------------

CREATE TABLE `burner` (
	`burner_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
	`gpio_id` int(10) unsigned DEFAULT NULL COMMENT 'if null, no valve attached',
	`monitor_id` int(10) unsigned DEFAULT NULL COMMENT 'if null, no temp monitor attached',
	`name` varchar(200) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
	`description` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
	`disabled` tinyint(1) unsigned NOT NULL DEFAULT '0',
	PRIMARY KEY (`burner_id`),
	UNIQUE KEY `name` (`name`),
	UNIQUE KEY `gpio_id` (`gpio_id`),
	UNIQUE KEY `monitor_id` (`monitor_id`),
	CONSTRAINT `burner_ibfk_2` FOREIGN KEY (`monitor_id`) REFERENCES `temp_monitor` (`monitor_id`) ON DELETE SET NULL ON UPDATE CASCADE,
	CONSTRAINT `burner_ibfk_1` FOREIGN KEY (`gpio_id`) REFERENCES `gpio` (`gpio_id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

LOCK TABLES `burner` WRITE;
/*!40000 ALTER TABLE `burner` DISABLE KEYS */;

INSERT INTO `burner` (`burner_id`, `gpio_id`, `monitor_id`, `name`, `description`, `disabled`)
	VALUES
	(1,4,1,'HLT Burner','The hot liquor tank burner - 90k BTU jet burner',0),
	(2,3,2,'MLT','The mash lauter tun burner - 90k BTU jet burner',0),
	(3,2,NULL,'Boil','The main boil burner - Blichmann',1);

/*!40000 ALTER TABLE `burner` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table gpio
# ------------------------------------------------------------

CREATE TABLE `gpio` (
	`gpio_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
	`rasp_pi_name` varchar(20) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
	`pin` int(11) NOT NULL,
	PRIMARY KEY (`gpio_id`),
	UNIQUE KEY `pin` (`pin`),
	UNIQUE KEY `rasp_pi_name` (`rasp_pi_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

LOCK TABLES `gpio` WRITE;
/*!40000 ALTER TABLE `gpio` DISABLE KEYS */;

INSERT INTO `gpio` (`gpio_id`, `rasp_pi_name`, `pin`)
	VALUES
	(1,'gpio_00',11),
	(2,'gpio_01',12),
	(3,'gpio_02',13),
	(4,'gpio_03',15),
	(5,'gpio_04',16),
	(6,'gpio_05',18),
	(7,'gpio_06',22),
	(8,'gpio_07',7);

/*!40000 ALTER TABLE `gpio` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table pump
# ------------------------------------------------------------

CREATE TABLE `pump` (
	`pump_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
	`gpio_id` int(10) unsigned DEFAULT NULL,
	`name` varchar(200) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
	`disabled` tinyint(1) unsigned NOT NULL DEFAULT '0',
	PRIMARY KEY (`pump_id`),
	UNIQUE KEY `name` (`name`),
	UNIQUE KEY `gpio_id` (`gpio_id`),
	CONSTRAINT `pump_ibfk_1` FOREIGN KEY (`gpio_id`) REFERENCES `gpio` (`gpio_id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

LOCK TABLES `pump` WRITE;
/*!40000 ALTER TABLE `pump` DISABLE KEYS */;

INSERT INTO `pump` (`pump_id`, `gpio_id`, `name`, `disabled`)
	VALUES
	(1,1,'Pump One',0),
	(2,2,'Pump Two',0);

/*!40000 ALTER TABLE `pump` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table temp_monitor
# ------------------------------------------------------------

CREATE TABLE `temp_monitor` (
	`monitor_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
	`name` varchar(200) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
	`serial` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
	`directory` varchar(200) COLLATE utf8_unicode_ci DEFAULT '/sys/bus/w1/devices',
	`disabled` tinyint(1) NOT NULL DEFAULT '0',
	PRIMARY KEY (`monitor_id`),
	UNIQUE KEY `name` (`name`),
	UNIQUE KEY `serial` (`serial`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

LOCK TABLES `temp_monitor` WRITE;
/*!40000 ALTER TABLE `temp_monitor` DISABLE KEYS */;

INSERT INTO `temp_monitor` (`monitor_id`, `name`, `serial`, `directory`, `disabled`)
	VALUES
	(1,'HLT Sensor',NULL,'/sys/bus/w1/devices',0),
	(2,'MLT Sensor',NULL,'/sys/bus/w1/devices',0),
	(3,'Multi Use',NULL,'/sys/bus/w1/devices',0);

/*!40000 ALTER TABLE `temp_monitor` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
