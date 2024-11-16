package stocks.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
        jdbcTemplate.execute("DROP TABLE IF EXISTS likes");
        jdbcTemplate.execute("DROP TABLE IF EXISTS user_stocks");
        jdbcTemplate.execute("DROP TABLE IF EXISTS message");
        jdbcTemplate.execute("DROP TABLE IF EXISTS stock");
        jdbcTemplate.execute("DROP TABLE IF EXISTS \"user\"");
        jdbcTemplate.execute("DROP TABLE IF EXISTS roles");

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS roles (" +
                "role_id INT PRIMARY KEY, " +
                "role_name VARCHAR(50) NOT NULL)");

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS \"user\" (" +
                "user_id INT PRIMARY KEY, " +
                "first_name VARCHAR(30), " +
                "last_name VARCHAR(30), " +
                "password VARCHAR(100) NOT NULL, " +
                "username VARCHAR(25) NOT NULL, " +
                "email VARCHAR(50) NOT NULL, " +
                "role_id INT, " +
                "CONSTRAINT fk_user_role_id FOREIGN KEY (role_id) REFERENCES roles(role_id))");

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS stock (" +
                "stock_id INT PRIMARY KEY, " +
                "stock_name VARCHAR(50), " +
                "stock_description VARCHAR(255), " +
                "ticker VARCHAR(10))");

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS message (" +
                "message_id INT PRIMARY KEY, " +
                "content VARCHAR(255), " +
                "date_of_post TIMESTAMP, " +
                "stock_id INT, " +
                "CONSTRAINT fk_message_stock_id FOREIGN KEY (stock_id) REFERENCES stock(stock_id), " +
                "user_id INT, " +
                "CONSTRAINT fk_message_user_id FOREIGN KEY (user_id) REFERENCES \"user\"(user_id))");

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS user_stocks (" +
                "user_stock_id INT PRIMARY KEY, " +
                "user_id INT, " +
                "CONSTRAINT fk_user_stock_user_id FOREIGN KEY (user_id) REFERENCES \"user\"(user_id), " +
                "stock_id INT, " +
                "CONSTRAINT fk_user_stock_stock_id FOREIGN KEY (stock_id) REFERENCES stock(stock_id))");

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS likes (" +
                "like_id INT PRIMARY KEY, " +
                "isliked BOOLEAN NOT NULL, " +
                "user_id INT, " +
                "CONSTRAINT fk_likes_user_id FOREIGN KEY (user_id) REFERENCES \"user\"(user_id), " +
                "message_id INT, " +
                "CONSTRAINT fk_likes_message_id FOREIGN KEY (message_id) REFERENCES message(message_id), " +
                "CONSTRAINT unique_user_message UNIQUE (user_id, message_id))");
    }

    private void seedData() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword1 = passwordEncoder.encode("password123");
        String hashedPassword2 = passwordEncoder.encode("password123");
        String hashedPassword3 = passwordEncoder.encode("password789");

        jdbcTemplate.update("INSERT INTO roles (role_id, role_name) VALUES (1, 'Chatter') " +
                "ON CONFLICT (role_id) DO UPDATE SET role_name=EXCLUDED.role_name");
        jdbcTemplate.update("INSERT INTO roles (role_id, role_name) VALUES (2, 'Admin') " +
                "ON CONFLICT (role_id) DO UPDATE SET role_name=EXCLUDED.role_name");

        jdbcTemplate.update("INSERT INTO \"user\" (user_id, first_name, last_name, password, username, email, role_id) VALUES " +
                "(1, 'John', 'Doe', ?, 'johndoe', 'johndoe@example.com', 1) " +
                "ON CONFLICT (user_id) DO UPDATE SET first_name=EXCLUDED.first_name, last_name=EXCLUDED.last_name, password=EXCLUDED.password, username=EXCLUDED.username, email=EXCLUDED.email, role_id=EXCLUDED.role_id",
                hashedPassword1);

        jdbcTemplate.update("INSERT INTO \"user\" (user_id, first_name, last_name, password, username, email, role_id) VALUES " +
                "(2, 'Jane', 'Smith', ?, 'janesmith', 'janesmith@example.com', 2) " +
                "ON CONFLICT (user_id) DO UPDATE SET first_name=EXCLUDED.first_name, last_name=EXCLUDED.last_name, password=EXCLUDED.password, username=EXCLUDED.username, email=EXCLUDED.email, role_id=EXCLUDED.role_id",
                hashedPassword2);

        jdbcTemplate.update("INSERT INTO \"user\" (user_id, first_name, last_name, password, username, email, role_id) VALUES " +
                "(3, 'Alice', 'Brown', ?, 'alicebrown', 'alicebrown@example.com', 1) " +
                "ON CONFLICT (user_id) DO UPDATE SET first_name=EXCLUDED.first_name, last_name=EXCLUDED.last_name, password=EXCLUDED.password, username=EXCLUDED.username, email=EXCLUDED.email, role_id=EXCLUDED.role_id",
                hashedPassword3);

        jdbcTemplate.update("INSERT INTO stock (stock_id, stock_name, stock_description, ticker) VALUES " +
                "(1, 'Apple Inc.', 'Technology Company', 'AAPL') " +
                "ON CONFLICT (stock_id) DO UPDATE SET stock_name=EXCLUDED.stock_name, stock_description=EXCLUDED.stock_description, ticker=EXCLUDED.ticker");

        jdbcTemplate.update("INSERT INTO stock (stock_id, stock_name, stock_description, ticker) VALUES " +
                "(2, 'Tesla Inc.', 'Electric Vehicle Manufacturer', 'TSLA') " +
                "ON CONFLICT (stock_id) DO UPDATE SET stock_name=EXCLUDED.stock_name, stock_description=EXCLUDED.stock_description, ticker=EXCLUDED.ticker");

        jdbcTemplate.update("INSERT INTO stock (stock_id, stock_name, stock_description, ticker) VALUES " +
                "(3, 'Amazon.com', 'E-commerce Giant', 'AMZN') " +
                "ON CONFLICT (stock_id) DO UPDATE SET stock_name=EXCLUDED.stock_name, stock_description=EXCLUDED.stock_description, ticker=EXCLUDED.ticker");

        jdbcTemplate.update("INSERT INTO stock (stock_id, stock_name, stock_description, ticker) VALUES " +
                "(4, 'Microsoft Corporation', 'Software Company', 'MSFT') " +
                "ON CONFLICT (stock_id) DO UPDATE SET stock_name=EXCLUDED.stock_name, stock_description=EXCLUDED.stock_description, ticker=EXCLUDED.ticker");

        jdbcTemplate.update("INSERT INTO stock (stock_id, stock_name, stock_description, ticker) VALUES " +
                "(5, 'Alphabet Inc.', 'Parent Company of Google', 'GOOGL') " +
                "ON CONFLICT (stock_id) DO UPDATE SET stock_name=EXCLUDED.stock_name, stock_description=EXCLUDED.stock_description, ticker=EXCLUDED.ticker");

        jdbcTemplate.update("INSERT INTO stock (stock_id, stock_name, stock_description, ticker) VALUES " +
                "(6, 'Facebook, Inc.', 'Social Media Company', 'FB') " +
                "ON CONFLICT (stock_id) DO UPDATE SET stock_name=EXCLUDED.stock_name, stock_description=EXCLUDED.stock_description, ticker=EXCLUDED.ticker");

        jdbcTemplate.update("INSERT INTO stock (stock_id, stock_name, stock_description, ticker) VALUES " +
                "(7, 'Netflix, Inc.', 'Streaming Service', 'NFLX') " +
                "ON CONFLICT (stock_id) DO UPDATE SET stock_name=EXCLUDED.stock_name, stock_description=EXCLUDED.stock_description, ticker=EXCLUDED.ticker");

        jdbcTemplate.update("INSERT INTO stock (stock_id, stock_name, stock_description, ticker) VALUES " +
                "(8, 'NVIDIA Corporation', 'Graphics Processing Units', 'NVDA') " +
                "ON CONFLICT (stock_id) DO UPDATE SET stock_name=EXCLUDED.stock_name, stock_description=EXCLUDED.stock_description, ticker=EXCLUDED.ticker");

        jdbcTemplate.update("INSERT INTO stock (stock_id, stock_name, stock_description, ticker) VALUES " +
                "(9, 'PayPal Holdings, Inc.', 'Online Payments', 'PYPL') " +
                "ON CONFLICT (stock_id) DO UPDATE SET stock_name=EXCLUDED.stock_name, stock_description=EXCLUDED.stock_description, ticker=EXCLUDED.ticker");

        jdbcTemplate.update("INSERT INTO stock (stock_id, stock_name, stock_description, ticker) VALUES " +
                "(10, 'Adobe Inc.', 'Software Company', 'ADBE') " +
                "ON CONFLICT (stock_id) DO UPDATE SET stock_name=EXCLUDED.stock_name, stock_description=EXCLUDED.stock_description, ticker=EXCLUDED.ticker");

        jdbcTemplate.update("INSERT INTO message (message_id, content, date_of_post, stock_id, user_id) VALUES " +
                "(1, 'I think Apple stock will go up!', '2024-11-10 10:15:00', 1, 1) " +
                "ON CONFLICT (message_id) DO UPDATE SET content=EXCLUDED.content, date_of_post=EXCLUDED.date_of_post, stock_id=EXCLUDED.stock_id, user_id=EXCLUDED.user_id");

        jdbcTemplate.update("INSERT INTO message (message_id, content, date_of_post, stock_id, user_id) VALUES " +
                "(2, 'Tesla is doing great this quarter!', '2024-11-10 11:20:00', 2, 2) " +
                "ON CONFLICT (message_id) DO UPDATE SET content=EXCLUDED.content, date_of_post=EXCLUDED.date_of_post, stock_id=EXCLUDED.stock_id, user_id=EXCLUDED.user_id");

        jdbcTemplate.update("INSERT INTO message (message_id, content, date_of_post, stock_id, user_id) VALUES " +
                "(3, 'Amazon sales are booming.', '2024-11-10 12:30:00', 3, 3) " +
                "ON CONFLICT (message_id) DO UPDATE SET content=EXCLUDED.content, date_of_post=EXCLUDED.date_of_post, stock_id=EXCLUDED.stock_id, user_id=EXCLUDED.user_id");

        jdbcTemplate.update("INSERT INTO message (message_id, content, date_of_post, stock_id, user_id) VALUES " +
                "(4, 'Microsoft is releasing new products.', '2024-11-11 09:00:00', 4, 1) " +
                "ON CONFLICT (message_id) DO UPDATE SET content=EXCLUDED.content, date_of_post=EXCLUDED.date_of_post, stock_id=EXCLUDED.stock_id, user_id=EXCLUDED.user_id");

        jdbcTemplate.update("INSERT INTO message (message_id, content, date_of_post, stock_id, user_id) VALUES " +
                "(5, 'Google is investing in AI.', '2024-11-11 10:30:00', 5, 2) " +
                "ON CONFLICT (message_id) DO UPDATE SET content=EXCLUDED.content, date_of_post=EXCLUDED.date_of_post, stock_id=EXCLUDED.stock_id, user_id=EXCLUDED.user_id");

        jdbcTemplate.update("INSERT INTO message (message_id, content, date_of_post, stock_id, user_id) VALUES " +
                "(6, 'Facebook is changing its name to Meta.', '2024-11-11 11:45:00', 6, 3) " +
                "ON CONFLICT (message_id) DO UPDATE SET content=EXCLUDED.content, date_of_post=EXCLUDED.date_of_post, stock_id=EXCLUDED.stock_id, user_id=EXCLUDED.user_id");

        jdbcTemplate.update("INSERT INTO message (message_id, content, date_of_post, stock_id, user_id) VALUES " +
                "(7, 'Netflix is adding more original content.', '2024-11-12 08:20:00', 7, 1) " +
                "ON CONFLICT (message_id) DO UPDATE SET content=EXCLUDED.content, date_of_post=EXCLUDED.date_of_post, stock_id=EXCLUDED.stock_id, user_id=EXCLUDED.user_id");

        jdbcTemplate.update("INSERT INTO message (message_id, content, date_of_post, stock_id, user_id) VALUES " +
                "(8, 'NVIDIA is leading in GPU technology.', '2024-11-12 09:50:00', 8, 2) " +
                "ON CONFLICT (message_id) DO UPDATE SET content=EXCLUDED.content, date_of_post=EXCLUDED.date_of_post, stock_id=EXCLUDED.stock_id, user_id=EXCLUDED.user_id");

        jdbcTemplate.update("INSERT INTO message (message_id, content, date_of_post, stock_id, user_id) VALUES " +
                "(9, 'PayPal is expanding its services.', '2024-11-12 11:10:00', 9, 3) " +
                "ON CONFLICT (message_id) DO UPDATE SET content=EXCLUDED.content, date_of_post=EXCLUDED.date_of_post, stock_id=EXCLUDED.stock_id, user_id=EXCLUDED.user_id");

        jdbcTemplate.update("INSERT INTO message (message_id, content, date_of_post, stock_id, user_id) VALUES " +
                "(10, 'Adobe is launching new creative tools.', '2024-11-12 12:40:00', 10, 1) " +
                "ON CONFLICT (message_id) DO UPDATE SET content=EXCLUDED.content, date_of_post=EXCLUDED.date_of_post, stock_id=EXCLUDED.stock_id, user_id=EXCLUDED.user_id");

        jdbcTemplate.update("INSERT INTO message (message_id, content, date_of_post, stock_id, user_id) VALUES " +
                "(11, 'What do yall think about Apple', '2024-11-10 10:16:00', 1, 2) " +
                "ON CONFLICT (message_id) DO UPDATE SET content=EXCLUDED.content, date_of_post=EXCLUDED.date_of_post, stock_id=EXCLUDED.stock_id, user_id=EXCLUDED.user_id");

        jdbcTemplate.update("INSERT INTO user_stocks (user_stock_id, user_id, stock_id) VALUES " +
                "(1, 1, 1) " +
                "ON CONFLICT (user_stock_id) DO UPDATE SET user_id=EXCLUDED.user_id, stock_id=EXCLUDED.stock_id");

        jdbcTemplate.update("INSERT INTO user_stocks (user_stock_id, user_id, stock_id) VALUES " +
                "(2, 1, 2) " +
                "ON CONFLICT (user_stock_id) DO UPDATE SET user_id=EXCLUDED.user_id, stock_id=EXCLUDED.stock_id");

        jdbcTemplate.update("INSERT INTO user_stocks (user_stock_id, user_id, stock_id) VALUES " +
                "(3, 2, 3) " +
                "ON CONFLICT (user_stock_id) DO UPDATE SET user_id=EXCLUDED.user_id, stock_id=EXCLUDED.stock_id");

        jdbcTemplate.update("INSERT INTO likes (like_id, isliked, user_id, message_id) VALUES " +
                "(1, true, 1, 2) " +
                "ON CONFLICT (like_id) DO UPDATE SET isliked=EXCLUDED.isliked, user_id=EXCLUDED.user_id, message_id=EXCLUDED.message_id");

        jdbcTemplate.update("INSERT INTO likes (like_id, isliked, user_id, message_id) VALUES " +
                "(2, false, 2, 1) " +
                "ON CONFLICT (like_id) DO UPDATE SET isliked=EXCLUDED.isliked, user_id=EXCLUDED.user_id, message_id=EXCLUDED.message_id");

        jdbcTemplate.update("INSERT INTO likes (like_id, isliked, user_id, message_id) VALUES " +
                "(3, true, 3, 3) " +
                "ON CONFLICT (like_id) DO UPDATE SET isliked=EXCLUDED.isliked, user_id=EXCLUDED.user_id, message_id=EXCLUDED.message_id");
    }
}