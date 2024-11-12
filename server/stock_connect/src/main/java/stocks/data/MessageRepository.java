package stocks.data;

import stocks.models.Message;

import java.util.List;

public interface MessageRepository {
    Message findById(int messageId);

    List<Message> findByUserId(int userId);

    List<Message> findByStockId(int stockId);

    Message add(Message message);

    boolean update(Message message);

    boolean deleteById(int messageId);
}
