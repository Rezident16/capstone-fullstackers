package stocks.data.mappers;

import org.springframework.jdbc.core.RowMapper;
import stocks.models.Message;
import stocks.models.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MessageMapper implements RowMapper<Message> {
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
}
