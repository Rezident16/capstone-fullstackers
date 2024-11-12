drop database if exists stonks;
create database stonks;
use stonks;

create table roles (
	role_id int primary key auto_increment,
    role_name varchar(50) not null
);

create table user (
	user_id int primary key auto_increment,
    first_name varchar(30),
    last_name varchar(30),
    `password` varchar(100) not null,
    username varchar(25) not null,
    email varchar(50) not null,
    role_id int,
    constraint fk_user_role_id
 		foreign key (role_id)
         references roles(role_id)
);

create table stock (
	stock_id int primary key auto_increment,
    stock_name varchar(50),
    stock_description  varchar(255),
    ticker varchar(10)
);

create table message (
	message_id int primary key auto_increment,
    content varchar(255),
    date_of_post datetime,
    stock_id int,
    constraint fk_message_stock_id
		foreign key (stock_id)
        references stock(stock_id),
	user_id int,
	constraint fk_message_user_id
		foreign key (user_id)
        references user(user_id)
);

create table user_stocks (
	user_stock_id int primary key auto_increment,
    user_id int,
    constraint fk_user_stock_user_id
		foreign key (user_id)
        references user(user_id),
	stock_id int,
	constraint fk_user_stock_stock_id
		foreign key (stock_id)
        references stock(stock_id)
);

create table likes (
	like_id int primary key auto_increment,
    isliked boolean not null,
    user_id int,
    constraint fk_likes_user_id
		foreign key (user_id)
        references user(user_id),
	message_id int,
	constraint fk_likes_message_id
		foreign key(message_id)
        references message(message_id),
	CONSTRAINT unique_user_message UNIQUE (user_id, message_id)
);
