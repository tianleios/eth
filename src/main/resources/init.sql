
create database eth_db;

CREATE table IF NOT EXISTS `eth_user`(

  `id` VARCHAR(30)  PRIMARY KEY ,
  `mobile` VARCHAR(15) NOT NULL,
  `address` VARCHAR(100) NOT NULL,
  `password` VARCHAR(15) NOT NULL,
  `eth_password` VARCHAR(30) NOT NULL,
  `create_datetime` DATETIME NOT NULL,
  `update_datetime` DATETIME NOT NULL,
   UNIQUE KEY (`address`),
   UNIQUE KEY (`mobile`)

);

CREATE table IF NOT EXISTS `eth_account`(

  `id` integer AUTO_INCREMENT PRIMARY KEY ,
  `user_id` VARCHAR(30) NOT NULL,
  `amount` VARCHAR(30) NOT NULL DEFAULT '0'

) AUTO_INCREMENT=1000;

CREATE table IF NOT EXISTS `eth_bill`(

  `id` integer AUTO_INCREMENT PRIMARY KEY ,
  `account_id` integer NOT NULL ,
  `from` VARCHAR(100) NOT NULL,
  `to` VARCHAR(100) NOT NULL,
  `amount` VARCHAR(30) NOT NULL DEFAULT '0'

) AUTO_INCREMENT=1000;


INSERT INTO eth_user(`id`,`mobile`,`address`,`password`,`eth_password`,`create_datetime`,`update_datetime`) VALUES ('u000000000000','00000000000','0xb1cd852c72141bdac6ccf314d9ea82d2af03f2ac','password','123456',now(),now());

INSERT INTO eth_account(`id`,`user_id`,`amount`) VALUES ('3000','u000000000000',0);