
create database eth_db;


CREATE table IF NOT EXISTS `eth_user`(

  `id` integer AUTO_INCREMENT PRIMARY KEY ,
  `mobile` VARCHAR(15) NOT NULL,
  `address` VARCHAR(100) NOT NULL,
  `password` VARCHAR(15) NOT NULL,
  `eth_password` VARCHAR(30) NOT NULL,
  `create_datetime` DATETIME NOT NULL,
  `update_datetime` DATETIME NOT NULL

);

