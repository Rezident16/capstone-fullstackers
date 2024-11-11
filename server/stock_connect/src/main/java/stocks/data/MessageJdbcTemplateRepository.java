package stocks.data;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import stocks.data.mappers.MessageMapper;
import stocks.models.Message;

import java.util.List;

@Repository
public class MessageJdbcTemplateRepository implements MessageRepository{

    private final JdbcTemplate jdbcTemplate;
    public MessageJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Message findById(int messageId) {
        final String sql = "select message_id, content, date_of_post, stock_id, user_id "
                + "from message "
                + "where message_id = ?;";

        return jdbcTemplate.query(sql, new MessageMapper(), messageId).stream()
                .findAny().orElse(null);
    }


    @Override
    public List<Message> findByUserId(int userId) {
        final String sql = "select message_id, content, date_of_post, stock_id, user_id "
                + "from message "
                + "where user_id = ?;";

        return jdbcTemplate.query(sql, new MessageMapper(), userId);
    }


    @Override
    public List<Message> findByStockId(int stockId) {
        final String sql = "select message_id, content, date_of_post, stock_id, user_id "
                + "from message "
                + "where stock_id = ?;";

        return jdbcTemplate.query(sql, new MessageMapper(), stockId);
    }


    @Override
    public boolean add(Message message) {
        final String sql = "insert into message (content, date_of_post, stock_id, user_id) values "
                + "(?,?,?,?);";

        return jdbcTemplate.update(sql,
                message.getContent(),
                message.getDateOfPost(),
                message.getStockId(),
                message.getUserId()) > 0;
    }

    @Override
    public boolean update(Message message) {

        final String sql = "update message set "
                + "content = ?, "
                + "date_of_post = ?, "
                + "stock_id = ?, "
                + "user_id = ?, "
                + "where message_id = ?;";

        return jdbcTemplate.update(sql,
                message.getContent(),
                message.getDateOfPost(),
                message.getStockId(),
                message.getUserId(),
                message.getMessageId()) > 0;
    }

    @Override
    public boolean deleteById(int messageId) {
        // First, delete all dependent records in related tables

        // Delete likes associated with the message
        jdbcTemplate.update("DELETE FROM likes WHERE message_id = ?", messageId);

        // Finally, delete the message
        return jdbcTemplate.update("DELETE FROM message WHERE message_id = ?", messageId) > 0;
    }

    // Need to add likes to the messages
}
