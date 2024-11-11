drop database if exists stonks;
create database stonks;
use stonks;

-- Create roles table
create table roles (
    role_id int primary key auto_increment,
    role_name varchar(50) not null
);

-- Create user table
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

-- Create stock table
create table stock (
    stock_id int primary key auto_increment,
    stock_name varchar(50),
    stock_description varchar(255),
    ticker varchar(10)
);

-- Create message table
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

-- Create user_stocks table
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

-- Create likes table
create table likes (
    likes_id int primary key auto_increment,
    isliked boolean not null,
    user_id int,
    constraint fk_likes_user_id
        foreign key (user_id)
        references user(user_id),
    message_id int,
    constraint fk_likes_message_id
        foreign key (message_id)
        references message(message_id)
);

delimiter //

create procedure set_known_good_state()
begin
    -- Delete existing data and reset auto-increment values
    delete from likes where likes_id > 0;
    alter table likes auto_increment = 1;
    delete from user_stocks where user_stock_id > 0;
    alter table user_stocks auto_increment = 1;
    delete from message where message_id > 0;
    alter table message auto_increment = 1;
    delete from stock where stock_id > 0;
    alter table stock auto_increment = 1;
    delete from user where user_id > 0;
    alter table user auto_increment = 1;
    delete from roles where role_id > 0;
    alter table roles auto_increment = 1;

    -- Insert seed data into roles table
    insert into roles (role_name) values ('Admin'), ('User');

    -- Insert seed data into user table
    insert into user (first_name, last_name, `password`, username, email, role_id) values
    ('John', 'Doe', 'password123', 'johndoe', 'john.doe@example.com', 1),
    ('Jane', 'Smith', 'password123', 'janesmith', 'jane.smith@example.com', 2);

    -- Insert seed data into stock table
    insert into stock (stock_name, stock_description, ticker) values
    ('Apple Inc.', 'Technology company', 'AAPL'),
    ('Tesla Inc.', 'Electric vehicle manufacturer', 'TSLA');

    -- Insert seed data into message table
    insert into message (content, date_of_post, stock_id, user_id) values
    ('Great stock!', '2023-01-01 10:00:00', 1, 1),
    ('I love this company!', '2023-01-02 11:00:00', 2, 2);

    -- Insert seed data into user_stocks table
    insert into user_stocks (user_id, stock_id) values
    (1, 1),
    (2, 2);

    -- Insert seed data into likes table
    insert into likes (isliked, user_id, message_id) values
    (true, 1, 1),
    (false, 2, 2);
end //

delimiter ;
