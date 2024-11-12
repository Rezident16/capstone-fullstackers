package stocks.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import stocks.models.Message;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MessageJdbcTemplateRepositoryTest {

    @Autowired
    private MessageJdbcTemplateRepository messageRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setup() {
        // Call the procedure to reset the database to a known good state
        jdbcTemplate.execute("CALL set_known_good_state();");
    }

    @Test
    public void shouldFindMessageById() {
        // Test retrieving a message by its ID
        var message = messageRepository.findById(1);
        assertNotNull(message, "Message should be found.");
        assertEquals(1, message.getMessageId(), "Message ID should match.");
        assertEquals("Great stock!", message.getContent(), "Content should match.");
    }

    @Test
    public void shouldFindMessagesByStockId() {
        // Test retrieving messages by stock ID
        var messages = messageRepository.findByStockId(1);
        assertNotNull(messages, "Messages for the stock should be found.");
        assertTrue(messages.size() > 0, "There should be at least one message.");
    }

    @Test
    public void shouldFindMessagesByUserId() {
        // Test retrieving messages by user ID
        var messages = messageRepository.findByUserId(1);
        assertNotNull(messages, "Messages for the user should be found.");
        assertTrue(messages.size() > 0, "There should be at least one message.");
    }

    @Test
    public void shouldAddMessage() {
        // Test adding a new message
        Message newMessage = new Message();
        newMessage.setContent("Excited for new Apple products!");
        newMessage.setStockId(1);  // Associating with Apple stock
        newMessage.setUserId(1);   // John Doe
        newMessage.setDateOfPost(new Timestamp(System.currentTimeMillis()));

        Message added = messageRepository.add(newMessage);
        assertNotNull(added);
    }

    @Test
    public void shouldDeleteMessage() {
        // Test deleting a message
        boolean deleted = messageRepository.deleteById(2);
        assertTrue(deleted, "Message should be deleted.");

        // Verify that the message no longer exists
        var message = messageRepository.findById(2);
        assertNull(message, "Message should not be found after deletion.");
    }
}
