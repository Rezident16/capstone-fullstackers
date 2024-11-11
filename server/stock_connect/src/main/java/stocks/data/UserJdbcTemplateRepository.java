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
        // limit until we develop a paging solution
        final String sql = "select user_id, first_name, last_name, username, email, role_id from user limit 1000;";
        return jdbcTemplate.query(sql, new UserMapper());
    }

    @Transactional
    @Override
    public User findById(int userId) {

        final String sql = "select user_id, first_name, last_name, username "
                + "from user "
                + "where user_id = ?;";

        return jdbcTemplate.query(sql, new UserMapper(), userId).stream()
                .findAny().orElse(null);
    }

    @Transactional
    @Override
    public User findByUsername(String username) {

        final String sql = "select user_id, first_name, last_name, username "
                + "from user "
                + "where username = ?;";

        return jdbcTemplate.query(sql, new UserMapper(), username).stream()
                .findAny().orElse(null);
    }

    @Transactional
    @Override
    public User findByEmail(String email) {

        final String sql = "select user_id, first_name, last_name, username, email"
                + "from user "
                + "where email = ?;";

        return jdbcTemplate.query(sql, new UserMapper(), email).stream()
                .findAny().orElse(null);
    }

    @Override
    public boolean add(User user) {
        final String sql = "insert into user (first_name, last_name, `password`, username, email, role_id) values "
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
                + "`password` = ?, "
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
        return jdbcTemplate.update(
                "delete from user where user_id = ?", userId) > 0;
    }

}
