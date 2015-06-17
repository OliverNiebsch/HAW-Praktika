<?php

	$db = new SQLite3('db/sqldb.db');//, 0666, $sqliteerror)
    $db->exec('CREATE TABLE user (ID int NOT NULL AUTO_INCREMENT PRIMARY KEY,pass varchar(255) NOT NULL,pass_changed boolean NOT NULL,mail varchar(255) NOT NULL,role int NOT NULL)');
    $db->exec('CREATE TABLE mail_auth (`id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,user_id int NOT_NULL,request_time datetime NOT_NULL,new_mail varchar(255) NOT NULL)');
	$db->exec('CREATE TABLE pass_request (`id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,user_id int NOT NULL,request datetime NOT NULL)');
	
	$db->exec($db, "INSERT INTO user(pass, pass_changed, mail, role) VALUES('lpon', 0, 'admin@lpon.lpon', 2)");
	$db->exec($db, "INSERT INTO user(pass, pass_changed, mail, role) VALUES('user1', 0, 'user1@lpon.lpon', 1)");
	$db->exec($db, "INSERT INTO user(pass, pass_changed, mail, role) VALUES('user2', 0, 'user2@lpon.lpon', 1)");
	
	$result = $db->query('SELECT * FROM user');
	var_dump($result->fetchArray());
?>