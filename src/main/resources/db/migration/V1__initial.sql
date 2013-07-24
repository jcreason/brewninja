
CREATE TABLE `burner` (
  `burner_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(200) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `description` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `disabled` tinyint(1) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`burner_id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


CREATE TABLE `gpio` (
  `gpio_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `rasp_pi_name` varchar(20) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `pin` int(11) NOT NULL,
  PRIMARY KEY (`gpio_id`),
  UNIQUE KEY `pin` (`pin`),
  UNIQUE KEY `rasp_pi_name` (`rasp_pi_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

INSERT INTO `gpio` (`gpio_id`, `rasp_pi_name`, `pin`)
VALUES
	(1,'gpio_0',11),
	(2,'gpio_1',12),
	(3,'gpio_2',13),
	(4,'gpio_3',15),
	(5,'gpio_4',16),
	(6,'gpio_5',18),
	(7,'gpio_6',22),
	(8,'gpio_7',7);


CREATE TABLE `pump` (
  `pump_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `gpio_id` int(10) unsigned DEFAULT NULL,
  `name` varchar(200) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `disabled` tinyint(1) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`pump_id`),
  KEY `gpio_id` (`gpio_id`),
  CONSTRAINT `pump_ibfk_1` FOREIGN KEY (`gpio_id`) REFERENCES `gpio` (`gpio_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


CREATE TABLE `temp_monitor` (
  `monitor_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `burner_id` int(10) unsigned DEFAULT NULL,
  `name` varchar(200) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `serial` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `directory` varchar(200) COLLATE utf8_unicode_ci DEFAULT '/sys/bus/w1/devices',
  `disabled` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`monitor_id`),
  UNIQUE KEY `name` (`name`),
  UNIQUE KEY `serial` (`serial`),
  KEY `burner_id` (`burner_id`),
  CONSTRAINT `temp_monitor_ibfk_1` FOREIGN KEY (`burner_id`) REFERENCES `burner` (`burner_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


CREATE TABLE `valve` (
  `valve_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `gpio_id` int(10) unsigned DEFAULT NULL,
  `burner_id` int(10) unsigned NOT NULL COMMENT 'burner this valve controls',
  `name` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `disabled` tinyint(1) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`valve_id`),
  KEY `burner_id` (`burner_id`),
  KEY `gpio_id` (`gpio_id`),
  CONSTRAINT `valve_ibfk_1` FOREIGN KEY (`burner_id`) REFERENCES `burner` (`burner_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `valve_ibfk_2` FOREIGN KEY (`gpio_id`) REFERENCES `gpio` (`gpio_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
