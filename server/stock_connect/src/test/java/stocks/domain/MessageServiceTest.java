package stocks.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import stocks.data.MessageRepository;
import stocks.models.Message;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class MessageServiceTest {

    @Autowired
    MessageService service;

    @MockBean
    MessageRepository repository;

    @Test
    void shouldNotAddWhenInvalid() {
        // Test when content is invalid (null)
        Message message = makeMessage();
        message.setContent("   ");
        Result<Message> result = service.add(message);
        assertEquals(ResultType.INVALID, result.getType());

        // Test when userId is invalid
        message = makeMessage();
        message.setUserId(0);
        result = service.add(message);
        assertEquals(ResultType.INVALID, result.getType());

        // Test when stockId is invalid
        message = makeMessage();
        message.setStockId(0);
        result = service.add(message);
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldAdd() {
        Message message = makeMessage();
        Message mockOut = makeMessage();
        mockOut.setMessageId(1);

        when(repository.add(message)).thenReturn(mockOut);

        Result<Message> result = service.add(message);
        assertEquals(ResultType.SUCCESS, result.getType());
        assertEquals(mockOut, result.getPayload());
    }

    @Test
    void shouldNotUpdateWhenInvalid() {
        // Test when messageId is 0
        Message message = makeMessage();
        message.setMessageId(0);
        Result<Message> result = service.update(message);
        assertEquals(ResultType.INVALID, result.getType());

        // Test when content is invalid (empty)
        message = makeMessage();
        message.setContent("   ");
        result = service.update(message);
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldUpdate() {
        Message message = makeMessage();
        message.setMessageId(1);

        when(repository.update(message)).thenReturn(true);

        Result<Message> result = service.update(message);
        assertEquals(ResultType.SUCCESS, result.getType());
    }

    @Test
    void shouldNotUpdateWhenNotFound() {
        Message message = makeMessage();
        message.setMessageId(1);

        when(repository.update(message)).thenReturn(false);

        Result<Message> result = service.update(message);
        assertEquals(ResultType.NOT_FOUND, result.getType());
        assertTrue(result.getMessages().contains("MessageId: 1 not found"));
    }

    @Test
    void shouldDelete() {
        when(repository.deleteById(1)).thenReturn(true);

        boolean result = service.deleteById(1);
        assertTrue(result);
        verify(repository).deleteById(1); // Verify repository delete was called
    }

    @Test
    void shouldNotDeleteWhenNotFound() {
        when(repository.deleteById(1)).thenReturn(false);

        boolean result = service.deleteById(1);
        assertFalse(result);
    }

    private Message makeMessage() {
        Message message = new Message();
        message.setContent("Test message content");
        message.setUserId(1);
        message.setStockId(100);
        return message;
    }
}
