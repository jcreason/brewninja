
UPDATE `burner` SET `name` = 'HLT' WHERE `burner_id` = '1';

ALTER TABLE `burner` ADD `pump_id` INT(10)  UNSIGNED  NULL  DEFAULT NULL  AFTER `monitor_id`;

ALTER TABLE `burner` ADD UNIQUE INDEX (`pump_id`);

ALTER TABLE `burner` ADD FOREIGN KEY (`pump_id`) REFERENCES `pump` (`pump_id`) ON DELETE SET NULL ON UPDATE CASCADE;

UPDATE `burner` SET `pump_id` = '1' WHERE `burner_id` = '1';

UPDATE `burner` SET `pump_id` = '2' WHERE `burner_id` = '2';
