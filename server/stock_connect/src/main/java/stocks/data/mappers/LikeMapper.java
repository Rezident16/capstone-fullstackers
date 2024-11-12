package stocks.data.mappers;

import org.springframework.jdbc.core.RowMapper;
import stocks.models.Like;
import stocks.models.Message;
import stocks.models.User;

import java.sql.ResultSet;
import java.sql.SQLException;

/*
* public class MessageMapper implements RowMapper<Message> {
    @Override
    public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
        Message message = new Message();
        message.setMessageId(rs.getInt("message_id"));
        message.setContent(rs.getString("content"));
        message.setDateOfPost(rs.getTimestamp("date_of_post"));
        message.setStockId(rs.getInt("stock_id"));
        message.setUserId(rs.getInt("user_id"));

        return message;
    }
}*/
public class LikeMapper implements RowMapper<Like> {
    @Override
    public Like mapRow(ResultSet rs, int rowNum) throws SQLException {
        Like like = new Like();
        like.setLikeId(rs.getInt("likes_id"));
        like.setLiked(rs.getBoolean("isliked"));

        // Map User with only user_id
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        like.setUser(user);

        // Map Message with only message_id
        Message message = new Message();
        message.setMessageId(rs.getInt("message_id"));
        like.setMessage(message);

        return like;
    }
}
