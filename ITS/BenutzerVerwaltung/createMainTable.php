<?php

	$db = new SQLite3('db/sqldb.db');//, 0666, $sqliteerror)
	$db->exec('DROP TABLE user');
	$db->exec('DROP TABLE mail_auth');
	$db->exec('DROP TABLE pass_request');
	
    $db->exec('CREATE TABLE user (ID INTEGER PRIMARY KEY AUTOINCREMENT, pass varchar(255) NOT NULL,pass_changed boolean NOT NULL,mail varchar(255) NOT NULL,role int NOT NULL)');
    $db->exec('CREATE TABLE mail_auth (ID INTEGER PRIMARY KEY AUTOINCREMENT, user_id int NOT NULL,phrase varchar(255) NOT NULL,request_time int NOT NULL,new_mail varchar(255) NOT NULL)');
	$db->exec('CREATE TABLE pass_request (ID INTEGER PRIMARY KEY AUTOINCREMENT, user_id int NOT NULL,phrase varchar(255) NOT NULL,request_time int NOT NULL)');
	
	$pass_hash = password_hash("lpon", PASSWORD_BCRYPT);
	$db->exec("INSERT INTO user(pass, pass_changed, mail, role) VALUES('".$pass_hash."', 0, 'admin@lpon.lpon', 2)");
	$db->exec("INSERT INTO user(pass, pass_changed, mail, role) VALUES('".$pass_hash."', 0, 'user1@lpon.lpon', 1)");
	$db->exec("INSERT INTO user(pass, pass_changed, mail, role) VALUES('".$pass_hash."', 0, 'user2@lpon.lpon', 1)");
	
	$result = $db->query('SELECT * FROM user');
	while ($row = $result->fetchArray(SQLITE3_ASSOC)) {
		echo print_r($row, true);
	}
?>