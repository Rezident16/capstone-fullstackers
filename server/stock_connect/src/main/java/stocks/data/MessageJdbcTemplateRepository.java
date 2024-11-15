package stocks.data;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
<<<<<<< HEAD
import stocks.data.mappers.MessageMapper;
=======
import org.springframework.transaction.annotation.Transactional;

import stocks.data.mappers.LikeMapper;
import stocks.data.mappers.MessageMapper;
import stocks.data.mappers.UserMapper;
import stocks.models.AppUser;
import stocks.models.Like;
>>>>>>> ccc62f791f13e9925773795a2887f8746d198a43
import stocks.models.Message;

import java.util.List;

@Repository
public class MessageJdbcTemplateRepository implements MessageRepository {

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
    @Transactional
    public List<Message> findByStockId(int stockId) {
        final String sql = "select message_id, content, date_of_post, stock_id, user_id "
                + "from message "
                + "where stock_id = ?;";

<<<<<<< HEAD
        return jdbcTemplate.query(sql, new MessageMapper(), stockId);
=======
        List<Message> messages = jdbcTemplate.query(sql, new MessageMapper(), stockId);

        if (!messages.isEmpty()) {
            for (Message message : messages) {
                addLikes(message);
                addUser(message);
            }
        }

        return messages;
>>>>>>> ccc62f791f13e9925773795a2887f8746d198a43
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

        final String sql = "UPDATE message SET content = ?, date_of_post = ?, stock_id = ?, user_id = ? WHERE message_id = ?";

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
<<<<<<< HEAD
=======

    // Need to add likes to the messages
    private void addLikes(Message message) {
        final String sql = "select l.like_id, l.isliked, l.user_id, l.message_id " +
                "from likes l " +
                "inner join message m on m.message_id = l.message_id " +
                "where m.message_id = ?;";

        List<Like> messageLikes = jdbcTemplate.query(sql, new LikeMapper(), message.getMessageId());
        message.setLikes(messageLikes);
    }

    private void addUser(Message message) {
        final String sql = "select u.user_id, u.username, u.password, u.first_name, u.last_name, u.email, u.role_id " +
                "from user u " +
                "inner join message m on m.user_id = u.user_id " +
                "where m.message_id = ?;";

        AppUser messageUser = jdbcTemplate.query(sql, new UserMapper(), message.getMessageId()).stream()
                .findAny().orElse(null);
        message.setAppUser(messageUser);
    }
>>>>>>> ccc62f791f13e9925773795a2887f8746d198a43
}
