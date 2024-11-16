package stocks.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class DatabaseInitializer {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void initializeDatabase() {
        createTables();
        seedData();
    }

    private void createTables() {
        jdbcTemplate.execute("DROP DATABASE IF EXISTS stock_connect;");
        jdbcTemplate.execute("CREATE DATABASE stock_connect;");
        jdbcTemplate.execute("USE stock_connect;");

        jdbcTemplate.execute("CREATE TABLE roles (" +
                "role_id INT PRIMARY KEY AUTO_INCREMENT, " +
                "role_name VARCHAR(50) NOT NULL)");

        jdbcTemplate.execute("CREATE TABLE user (" +
                "user_id INT PRIMARY KEY AUTO_INCREMENT, " +
                "first_name VARCHAR(30), " +
                "last_name VARCHAR(30), " +
                "password VARCHAR(100) NOT NULL, " +
                "username VARCHAR(25) NOT NULL, " +
                "email VARCHAR(50) NOT NULL, " +
                "role_id INT, " +
                "CONSTRAINT fk_user_role_id FOREIGN KEY (role_id) REFERENCES roles(role_id))");

        jdbcTemplate.execute("CREATE TABLE stock (" +
                "stock_id INT PRIMARY KEY AUTO_INCREMENT, " +
                "stock_name VARCHAR(50), " +
                "stock_description VARCHAR(255), " +
                "ticker VARCHAR(10))");

        jdbcTemplate.execute("CREATE TABLE message (" +
                "message_id INT PRIMARY KEY AUTO_INCREMENT, " +
                "content VARCHAR(255), " +
                "date_of_post DATETIME, " +
                "stock_id INT, " +
                "CONSTRAINT fk_message_stock_id FOREIGN KEY (stock_id) REFERENCES stock(stock_id), " +
                "user_id INT, " +
                "CONSTRAINT fk_message_user_id FOREIGN KEY (user_id) REFERENCES user(user_id))");

        jdbcTemplate.execute("CREATE TABLE user_stocks (" +
                "user_stock_id INT PRIMARY KEY AUTO_INCREMENT, " +
                "user_id INT, " +
                "CONSTRAINT fk_user_stock_user_id FOREIGN KEY (user_id) REFERENCES user(user_id), " +
                "stock_id INT, " +
                "CONSTRAINT fk_user_stock_stock_id FOREIGN KEY (stock_id) REFERENCES stock(stock_id))");

        jdbcTemplate.execute("CREATE TABLE likes (" +
                "like_id INT PRIMARY KEY AUTO_INCREMENT, " +
                "isliked BOOLEAN NOT NULL, " +
                "user_id INT, " +
                "CONSTRAINT fk_likes_user_id FOREIGN KEY (user_id) REFERENCES user(user_id), " +
                "message_id INT, " +
                "CONSTRAINT fk_likes_message_id FOREIGN KEY (message_id) REFERENCES message(message_id), " +
                "CONSTRAINT unique_user_message UNIQUE (user_id, message_id))");
    }

    private void seedData() {
        jdbcTemplate.update("INSERT INTO roles (role_id, role_name) VALUES (1, 'Chatter') ON DUPLICATE KEY UPDATE role_name='Chatter'");
        jdbcTemplate.update("INSERT INTO roles (role_id, role_name) VALUES (2, 'Admin') ON DUPLICATE KEY UPDATE role_name='Admin'");

        jdbcTemplate.update("INSERT INTO user (user_id, first_name, last_name, password, username, email, role_id) VALUES " +
                "(1, 'John', 'Doe', 'password123', 'johndoe', 'johndoe@example.com', 1) " +
                "ON DUPLICATE KEY UPDATE first_name='John', last_name='Doe', password='password123', username='johndoe', email='johndoe@example.com', role_id=1");

        jdbcTemplate.update("INSERT INTO user (user_id, first_name, last_name, password, username, email, role_id) VALUES " +
                "(2, 'Jane', 'Smith', 'password456', 'janesmith', 'janesmith@example.com', 2) " +
                "ON DUPLICATE KEY UPDATE first_name='Jane', last_name='Smith', password='password456', username='janesmith', email='janesmith@example.com', role_id=2");

        jdbcTemplate.update("INSERT INTO user (user_id, first_name, last_name, password, username, email, role_id) VALUES " +
                "(3, 'Alice', 'Brown', 'password789', 'alicebrown', 'alicebrown@example.com', 1) " +
                "ON DUPLICATE KEY UPDATE first_name='Alice', last_name='Brown', password='password789', username='alicebrown', email='alicebrown@example.com', role_id=1");

        jdbcTemplate.update("INSERT INTO stock (stock_id, stock_name, stock_description, ticker) VALUES " +
                "(1, 'Apple Inc.', 'Technology Company', 'AAPL') " +
                "ON DUPLICATE KEY UPDATE stock_name='Apple Inc.', stock_description='Technology Company', ticker='AAPL'");

        jdbcTemplate.update("INSERT INTO stock (stock_id, stock_name, stock_description, ticker) VALUES " +
                "(2, 'Tesla Inc.', 'Electric Vehicle Manufacturer', 'TSLA') " +
                "ON DUPLICATE KEY UPDATE stock_name='Tesla Inc.', stock_description='Electric Vehicle Manufacturer', ticker='TSLA'");

        jdbcTemplate.update("INSERT INTO stock (stock_id, stock_name, stock_description, ticker) VALUES " +
                "(3, 'Amazon.com', 'E-commerce Giant', 'AMZN') " +
                "ON DUPLICATE KEY UPDATE stock_name='Amazon.com', stock_description='E-commerce Giant', ticker='AMZN'");

        jdbcTemplate.update("INSERT INTO stock (stock_id, stock_name, stock_description, ticker) VALUES " +
                "(4, 'Microsoft Corporation', 'Software Company', 'MSFT') " +
                "ON DUPLICATE KEY UPDATE stock_name='Microsoft Corporation', stock_description='Software Company', ticker='MSFT'");

        jdbcTemplate.update("INSERT INTO stock (stock_id, stock_name, stock_description, ticker) VALUES " +
                "(5, 'Alphabet Inc.', 'Parent Company of Google', 'GOOGL') " +
                "ON DUPLICATE KEY UPDATE stock_name='Alphabet Inc.', stock_description='Parent Company of Google', ticker='GOOGL'");

        jdbcTemplate.update("INSERT INTO stock (stock_id, stock_name, stock_description, ticker) VALUES " +
                "(6, 'Facebook, Inc.', 'Social Media Company', 'FB') " +
                "ON DUPLICATE KEY UPDATE stock_name='Facebook, Inc.', stock_description='Social Media Company', ticker='FB'");

        jdbcTemplate.update("INSERT INTO stock (stock_id, stock_name, stock_description, ticker) VALUES " +
                "(7, 'Netflix, Inc.', 'Streaming Service', 'NFLX') " +
                "ON DUPLICATE KEY UPDATE stock_name='Netflix, Inc.', stock_description='Streaming Service', ticker='NFLX'");

        jdbcTemplate.update("INSERT INTO stock (stock_id, stock_name, stock_description, ticker) VALUES " +
                "(8, 'NVIDIA Corporation', 'Graphics Processing Units', 'NVDA') " +
                "ON DUPLICATE KEY UPDATE stock_name='NVIDIA Corporation', stock_description='Graphics Processing Units', ticker='NVDA'");

        jdbcTemplate.update("INSERT INTO stock (stock_id, stock_name, stock_description, ticker) VALUES " +
                "(9, 'PayPal Holdings, Inc.', 'Online Payments', 'PYPL') " +
                "ON DUPLICATE KEY UPDATE stock_name='PayPal Holdings, Inc.', stock_description='Online Payments', ticker='PYPL'");

        jdbcTemplate.update("INSERT INTO stock (stock_id, stock_name, stock_description, ticker) VALUES " +
                "(10, 'Adobe Inc.', 'Software Company', 'ADBE') " +
                "ON DUPLICATE KEY UPDATE stock_name='Adobe Inc.', stock_description='Software Company', ticker='ADBE'");

        jdbcTemplate.update("INSERT INTO message (message_id, content, date_of_post, stock_id, user_id) VALUES " +
                "(1, 'I think Apple stock will go up!', '2024-11-10 10:15:00', 1, 1) " +
                "ON DUPLICATE KEY UPDATE content='I think Apple stock will go up!', date_of_post='2024-11-10 10:15:00', stock_id=1, user_id=1");

        jdbcTemplate.update("INSERT INTO message (message_id, content, date_of_post, stock_id, user_id) VALUES " +
                "(2, 'Tesla is doing great this quarter!', '2024-11-10 11:20:00', 2, 2) " +
                "ON DUPLICATE KEY UPDATE content='Tesla is doing great this quarter!', date_of_post='2024-11-10 11:20:00', stock_id=2, user_id=2");

        jdbcTemplate.update("INSERT INTO message (message_id, content, date_of_post, stock_id, user_id) VALUES " +
                "(3, 'Amazon sales are booming.', '2024-11-10 12:30:00', 3, 3) " +
                "ON DUPLICATE KEY UPDATE content='Amazon sales are booming.', date_of_post='2024-11-10 12:30:00', stock_id=3, user_id=3");

        jdbcTemplate.update("INSERT INTO message (message_id, content, date_of_post, stock_id, user_id) VALUES " +
                "(4, 'Microsoft is releasing new products.', '2024-11-11 09:00:00', 4, 1) " +
                "ON DUPLICATE KEY UPDATE content='Microsoft is releasing new products.', date_of_post='2024-11-11 09:00:00', stock_id=4, user_id=1");

        jdbcTemplate.update("INSERT INTO message (message_id, content, date_of_post, stock_id, user_id) VALUES " +
                "(5, 'Google is investing in AI.', '2024-11-11 10:30:00', 5, 2) " +
                "ON DUPLICATE KEY UPDATE content='Google is investing in AI.', date_of_post='2024-11-11 10:30:00', stock_id=5, user_id=2");

        jdbcTemplate.update("INSERT INTO message (message_id, content, date_of_post, stock_id, user_id) VALUES " +
                "(6, 'Facebook is changing its name to Meta.', '2024-11-11 11:45:00', 6, 3) " +
                "ON DUPLICATE KEY UPDATE content='Facebook is changing its name to Meta.', date_of_post='2024-11-11 11:45:00', stock_id=6, user_id=3");

        jdbcTemplate.update("INSERT INTO message (message_id, content, date_of_post, stock_id, user_id) VALUES " +
                "(7, 'Netflix is adding more original content.', '2024-11-12 08:20:00', 7, 1) " +
                "ON DUPLICATE KEY UPDATE content='Netflix is adding more original content.', date_of_post='2024-11-12 08:20:00', stock_id=7, user_id=1");

        jdbcTemplate.update("INSERT INTO message (message_id, content, date_of_post, stock_id, user_id) VALUES " +
                "(8, 'NVIDIA is leading in GPU technology.', '2024-11-12 09:50:00', 8, 2) " +
                "ON DUPLICATE KEY UPDATE content='NVIDIA is leading in GPU technology.', date_of_post='2024-11-12 09:50:00', stock_id=8, user_id=2");

        jdbcTemplate.update("INSERT INTO message (message_id, content, date_of_post, stock_id, user_id) VALUES " +
                "(9, 'PayPal is expanding its services.', '2024-11-12 11:10:00', 9, 3) " +
                "ON DUPLICATE KEY UPDATE content='PayPal is expanding its services.', date_of_post='2024-11-12 11:10:00', stock_id=9, user_id=3");

        jdbcTemplate.update("INSERT INTO message (message_id, content, date_of_post, stock_id, user_id) VALUES " +
                "(10, 'Adobe is launching new creative tools.', '2024-11-12 12:40:00', 10, 1) " +
                "ON DUPLICATE KEY UPDATE content='Adobe is launching new creative tools.', date_of_post='2024-11-12 12:40:00', stock_id=10, user_id=1");

        jdbcTemplate.update("INSERT INTO message (message_id, content, date_of_post, stock_id, user_id) VALUES " +
                "(11, 'What do yall think about Apple', '2024-11-10 10:16:00', 1, 2) " +
                "ON DUPLICATE KEY UPDATE content='What do yall think about Apple', date_of_post='2024-11-10 10:16:00', stock_id=1, user_id=2");

        jdbcTemplate.update("INSERT INTO user_stocks (user_stock_id, user_id, stock_id) VALUES " +
                "(1, 1, 1) " +
                "ON DUPLICATE KEY UPDATE user_id=1, stock_id=1");

        jdbcTemplate.update("INSERT INTO user_stocks (user_stock_id, user_id, stock_id) VALUES " +
                "(2, 1, 2) " +
                "ON DUPLICATE KEY UPDATE user_id=1, stock_id=2");

        jdbcTemplate.update("INSERT INTO user_stocks (user_stock_id, user_id, stock_id) VALUES " +
                "(3, 2, 3) " +
                "ON DUPLICATE KEY UPDATE user_id=2, stock_id=3");

        jdbcTemplate.update("INSERT INTO likes (like_id, isliked, user_id, message_id) VALUES " +
                "(1, true, 1, 2) " +
                "ON DUPLICATE KEY UPDATE isliked=true, user_id=1, message_id=2");

        jdbcTemplate.update("INSERT INTO likes (like_id, isliked, user_id, message_id) VALUES " +
                "(2, false, 2, 1) " +
                "ON DUPLICATE KEY UPDATE isliked=false, user_id=2, message_id=1");

        jdbcTemplate.update("INSERT INTO likes (like_id, isliked, user_id, message_id) VALUES " +
                "(3, true, 3, 3) " +
                "ON DUPLICATE KEY UPDATE isliked=true, user_id=3, message_id=3");
    }
}
