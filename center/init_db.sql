drop database if exists IIS;
create database IIS;
use IIS;
create table user
(
	id int(11) NOT NULL AUTO_INCREMENT,
	username varchar(20),
	password varchar(20),
	PRIMARY KEY(id)	
)
DEFAULT CHARSET=UTF8;

create table data
(
	id int(11) NOT NULL AUTO_INCREMENT,
	user_id int(11),
	module_name varchar(20),
	send varchar(20),
	value varchar(20),
	time timestamp,
	PRIMARY KEY(id)
);
insert into user(username,password) value('Â¬Ã÷','123');

insert into user(username,password) value('yjj','456');

insert into data(user_id,module_name,send,value,time) value(1,'speed','NULL','11','2018-12-15 10:33:32');