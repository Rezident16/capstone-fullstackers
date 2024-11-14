package stocks.data.mappers;

import org.springframework.jdbc.core.RowMapper;
import stocks.models.Like;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LikeMapper implements RowMapper<Like> {

    @Override
    public Like mapRow(ResultSet rs, int rowNum) throws SQLException {
        Like like = new Like();
        like.setLikeId(rs.getInt("like_id"));
        like.setMessageId(rs.getInt("message_id"));
        like.setUserId(rs.getInt("user_id"));
        like.setLiked(rs.getBoolean("isliked"));
        return like;
    }
}
