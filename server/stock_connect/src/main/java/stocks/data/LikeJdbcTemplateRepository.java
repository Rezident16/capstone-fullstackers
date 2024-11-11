package stocks.data;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import stocks.models.Like;

@Repository
public class LikeJdbcTemplateRepository implements LikeRepository{
    private final JdbcTemplate jdbcTemplate;

    public LikeJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean add(Like like) {

        final String sql = "insert into likes(likes_id, isliked, user_id, message_id) values (?, ?, ?, ?);";

        return jdbcTemplate.update(sql, like.getLikeId(), like.isLiked(), like.getUser().getUserId(), like.getMessage().getMessageId()) > 0;
    }

    @Override
    public boolean update(Like like) {
        final String sql = "update likes set isliked = ? where likes_id = ? and user_id = ? and message_id = ?;";

        return jdbcTemplate.update(sql, like.isLiked(), like.getLikeId(), like.getUser().getUserId(), like.getMessage().getMessageId()) > 0;
    }

    @Override
    public boolean delete(int likeId) {

        final String sql = "delete from likes where likes_id = ?; ";

        return jdbcTemplate.update(sql, likeId) > 0;
    }
}
