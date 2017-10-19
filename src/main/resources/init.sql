
create database eth_db;


CREATE table IF NOT EXISTS `eth_user`(

  `id` VARCHAR(30)  PRIMARY KEY ,
  `mobile` VARCHAR(15) NOT NULL,
  `address` VARCHAR(100) NOT NULL,
  `password` VARCHAR(15) NOT NULL,
  `eth_password` VARCHAR(30) NOT NULL,
  `create_datetime` DATETIME NOT NULL,
  `update_datetime` DATETIME NOT NULL,
   KEY (`address`),
   KEY (`mobile`)

);

CREATE table IF NOT EXISTS `eth_account`(

  `id` integer AUTO_INCREMENT PRIMARY KEY ,
  `user_id` VARCHAR(30) NOT NULL,
  `amount` BIGINT NOT NULL DEFAULT 0

);

CREATE table IF NOT EXISTS `eth_bill`(

  `id` integer AUTO_INCREMENT PRIMARY KEY ,
  `account_id` integer NOT NULL ,
  `from` VARCHAR(100) NOT NULL,
  `to` VARCHAR(100) NOT NULL,
  `amount` BIGINT NOT NULL DEFAULT 0

);


