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


insert into roles (role_id, role_name) values
        (1, 'Chatter'),
        (2, 'Admin');
    
    -- Users
    insert into user (user_id, first_name, last_name, `password`, username, email, role_id) values
        (1, 'John', 'Doe', 'password123', 'johndoe', 'johndoe@example.com', 1),
        (2, 'Jane', 'Smith', 'password456', 'janesmith', 'janesmith@example.com', 2),
        (3, 'Alice', 'Brown', 'password789', 'alicebrown', 'alicebrown@example.com', 1);
    
    -- Stocks
    insert into stock (stock_id, stock_name, stock_description, ticker) values
        (1, 'Apple Inc.', 'Technology Company', 'AAPL'),
        (2, 'Tesla Inc.', 'Electric Vehicle Manufacturer', 'TSLA'),
        (3, 'Amazon.com', 'E-commerce Giant', 'AMZN'),
        (4, 'Microsoft Corporation', 'Software Company', 'MSFT'),
        (5, 'Alphabet Inc.', 'Parent Company of Google', 'GOOGL'),
        (6, 'Facebook, Inc.', 'Social Media Company', 'FB'),
        (7, 'Netflix, Inc.', 'Streaming Service', 'NFLX'),
        (8, 'NVIDIA Corporation', 'Graphics Processing Units', 'NVDA'),
        (9, 'PayPal Holdings, Inc.', 'Online Payments', 'PYPL'),
        (10, 'Adobe Inc.', 'Software Company', 'ADBE');
    
    -- Messages
    -- Messages
    insert into message (message_id, content, date_of_post, stock_id, user_id) values
        (1, 'I think Apple stock will go up!', '2024-11-10 10:15:00', 1, 1),
        (2, 'Tesla is doing great this quarter!', '2024-11-10 11:20:00', 2, 2),
        (3, 'Amazon sales are booming.', '2024-11-10 12:30:00', 3, 3),
        (4, 'Microsoft is releasing new products.', '2024-11-11 09:00:00', 4, 1),
        (5, 'Google is investing in AI.', '2024-11-11 10:30:00', 5, 2),
        (6, 'Facebook is changing its name to Meta.', '2024-11-11 11:45:00', 6, 3),
        (7, 'Netflix is adding more original content.', '2024-11-12 08:20:00', 7, 1),
        (8, 'NVIDIA is leading in GPU technology.', '2024-11-12 09:50:00', 8, 2),
        (9, 'PayPal is expanding its services.', '2024-11-12 11:10:00', 9, 3),
        (10, 'Adobe is launching new creative tools.', '2024-11-12 12:40:00', 10, 1),
        (11, 'What do yall think about Apple', '2024-11-10 10:16:00', 1, 2);
    
    -- User Stocks
    insert into user_stocks (user_stock_id, user_id, stock_id) values
        (1, 1, 1),
        (2, 1, 2),
        (3, 2, 3);
    
    -- Likes
    insert into likes (like_id, isliked, user_id, message_id) values
        (1, true, 1, 2),
        (2, false, 2, 1),
        (3, true, 3, 3);