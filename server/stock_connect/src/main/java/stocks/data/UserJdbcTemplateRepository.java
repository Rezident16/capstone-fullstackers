package stocks.data;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import stocks.data.mappers.UserMapper;
import stocks.models.User;

import java.util.List;

@Repository
public class UserJdbcTemplateRepository implements UserRepository{
    private final JdbcTemplate jdbcTemplate;

    public UserJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> findAll() {
        // The SQL query now needs to include all columns mapped by the UserMapper.
        final String sql = "SELECT user_id, first_name, last_name, password, username, email, role_id FROM user LIMIT ?";

        int limit = 1000;  // Default limit (this can be adjusted or parameterized)

        return jdbcTemplate.query(sql, new UserMapper(), limit);
    }


    @Override
    public User findById(int userId) {

        final String sql = "SELECT user_id, first_name, last_name, password, username, email, role_id "
                + "FROM user "
                + "WHERE user_id = ?;";

        return jdbcTemplate.query(sql, new UserMapper(), userId).stream()
                .findAny().orElse(null);
    }



    @Override
    public User findByUsername(String username) {

        final String sql = "select user_id, first_name, last_name, username, password, email, role_id from user where username = ?;";

        return jdbcTemplate.query(sql, new UserMapper(), username).stream()
                .findAny().orElse(null);
    }


    @Override
    public User findByEmail(String email) {

        final String sql = "SELECT user_id, first_name, last_name, password, username, email, role_id "
                + "FROM user "
                + "WHERE email = ?;";

        return jdbcTemplate.query(sql, new UserMapper(), email).stream()
                .findAny().orElse(null);
    }


    @Override
    public boolean add(User user) {
        final String sql = "insert into user (first_name, last_name, password, username, email, role_id) values "
                + "(?,?,?,?,?,?);";

        return jdbcTemplate.update(sql,
                user.getFirstName(),
                user.getLastName(),
                user.getPassword(),
                user.getUsername(),
                user.getEmail(),
                user.getRoleId()) > 0;
    }

    @Override
    public boolean update(User user) {

        final String sql = "update user set "
                + "first_name = ?, "
                + "last_name = ?, "
                + "password = ?, "
                + "username = ?, "
                + "email = ?, "
                + "role_id = ? "
                + "where user_id = ?;";

        return jdbcTemplate.update(sql,
                user.getFirstName(),
                user.getLastName(),
                user.getPassword(),
                user.getUsername(),
                user.getEmail(),
                user.getRoleId(),
                user.getUserId()) > 0;
    }

    @Override
    public boolean deleteById(int userId) {
        // First, delete all dependent records in related tables

        // Delete likes associated with the user
        jdbcTemplate.update("DELETE FROM likes WHERE user_id = ?", userId);

        // Delete user_stocks associated with the user
        jdbcTemplate.update("DELETE FROM user_stocks WHERE user_id = ?", userId);

        // Delete messages associated with the user
        jdbcTemplate.update("DELETE FROM message WHERE user_id = ?", userId);

        // Finally, delete the user
        return jdbcTemplate.update("DELETE FROM user WHERE user_id = ?", userId) > 0;
    }


}
