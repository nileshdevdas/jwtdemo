CREATE TABLE `tbl_sec_users` (
  `email` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `firstname` varchar(100) DEFAULT NULL,
  `lastname` varchar(100) DEFAULT NULL,
  `id` varchar(100) NOT NULL,
  `guid` varchar(100) DEFAULT NULL,
  `role` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `tbl_sec_users_un` (`email`)
) 


CREATE TABLE `tbl_sec_roles` (
  `role_name` varchar(100) DEFAULT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
)