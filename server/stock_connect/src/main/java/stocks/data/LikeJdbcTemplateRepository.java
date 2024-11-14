package stocks.data;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import stocks.data.mappers.LikeMapper;
import stocks.models.Like;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Repository
public class LikeJdbcTemplateRepository implements LikeRepository {
    private final JdbcTemplate jdbcTemplate;

    public LikeJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Like> findAll() {
        final String sql = "SELECT like_id, isliked, user_id, message_id FROM likes;";
        return jdbcTemplate.query(sql, new LikeMapper());
    }

    @Override
    public Like findById(int likeId) {
        final String sql = "SELECT like_id, isliked, user_id, message_id FROM likes WHERE like_id = ?;";
        //return jdbcTemplate.query(sql, new LikeMapper(), likeId).stream().findAny().orElse(null);
        return jdbcTemplate.query(sql, new LikeMapper(), likeId).stream().findFirst().orElse(null);
    }

    @Override
    public List<Like> findByUserId(int userId) {
        final String sql = "SELECT like_id, isliked, user_id, message_id FROM likes WHERE user_id = ?;";
        return jdbcTemplate.query(sql, new LikeMapper(), userId);
    }

    @Override
    public List<Like> findByMessageId(int messageId) {
        final String sql = "SELECT like_id, isliked, user_id, message_id FROM likes WHERE message_id = ?;";
        return jdbcTemplate.query(sql, new LikeMapper(), messageId);
    }

    @Override
    public Like add(Like like) {
        final String sql = "INSERT INTO likes (isliked, user_id, message_id) VALUES (?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        int rowsAffected = jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setBoolean(1, like.isLiked());
                ps.setInt(2, like.getUserId());
                ps.setInt(3, like.getMessageId());
                return ps;
            }
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        like.setLikeId(keyHolder.getKey().intValue());
        return like;
    }

    @Override
    public boolean update(Like like) {
        final String sql = "update likes set isliked = ?, user_id = ?, message_id = ? where like_id = ?;";
        return jdbcTemplate.update(sql, like.isLiked(), like.getUserId(), like.getMessageId(),  like.getLikeId()) > 0;
    }

    @Override
    public boolean delete(int likeId) {
        final String sql = "DELETE FROM likes WHERE like_id = ?;";
        return jdbcTemplate.update(sql, likeId) > 0;
    }
}
