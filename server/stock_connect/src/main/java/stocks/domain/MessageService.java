package stocks.domain;

import org.springframework.stereotype.Service;
import stocks.data.MessageRepository;
import stocks.models.Message;
import stocks.websockets.MessageSockets;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository repository;
    private final MessageSockets webSocketHandler;

    public MessageService(MessageRepository repository, MessageSockets webSocketHandler) {
        this.repository = repository;
        this.webSocketHandler = webSocketHandler;
    }

    public Message findById(int messageId) {
        return repository.findById(messageId);
    }

    public List<Message> findByUserId(int userId) {
        return repository.findByUserId(userId);
    }

    public List<Message> findByStockId(int stockId) {
        List<Message> messages = repository.findByStockId(stockId);
        System.out.print(messages.size() + " in service");
        for (Message message : messages) {
            System.out.println(message.getContent());
        }
        return repository.findByStockId(stockId);
    }

    public Result<Message> add(Message message) {
        Result<Message> result = validate(message);
        if (!result.isSuccess()) {
            return result;
        }

        message = repository.add(message);
        result.setPayload(message);
        webSocketHandler.sendMessageToAll("New message created " + message.getContent());
        return result;
    }

    public Result<Message> update(Message message) {
        Result<Message> result = validate(message);
        if (!result.isSuccess()) {
            return result;
        }

        if (message.getMessageId() <= 0) {
            result.addMessage("messageId must be set for `update` operation", ResultType.INVALID);
            return result;
        }

        if (!repository.update(message)) {
            String msg = String.format("MessageId: %s not found", message.getMessageId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }

        webSocketHandler.sendMessageToAll("Message updated: " + message.getContent());

        return result;
    }

    public boolean deleteById(int messageId) {
        boolean result = repository.deleteById(messageId);
        if (result) {
            webSocketHandler.sendMessageToAll("Message " + messageId + " deleted");
            return true;
        } else {
            return false;
        }
    }

    private Result<Message> validate(Message message) {
        Result<Message> result = new Result<>();

        if (message == null) {
            result.addMessage("message cannot be null", ResultType.INVALID);
            return result;
        }

        if (message.getContent() == null || message.getContent().isBlank()) {
            result.addMessage("content is required", ResultType.INVALID);
        }

        if (message.getUserId() <= 0) {
            result.addMessage("userId is required", ResultType.INVALID);
        }

        if (message.getStockId() <= 0) {
            result.addMessage("stockId is required", ResultType.INVALID);
        }

        return result;
    }
}
