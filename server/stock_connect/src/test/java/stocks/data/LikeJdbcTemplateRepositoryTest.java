package stocks.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import stocks.models.Like;
import stocks.models.Message;
import stocks.models.User;
import java.util.List;
import org.springframework.dao.DuplicateKeyException;

import java.sql.Timestamp;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LikeJdbcTemplateRepositoryTest {
    @Autowired
    LikeJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFindAll(){
        List<Like> likes = repository.findAll();
        assertNotNull(likes);
        assertTrue(likes.size() > 0);
    }

    @Test
    void shouldFindById(){
        Like like = repository.findById(1);
        assertTrue(like.isLiked());
    }

    @Test
    void shouldFindByUserId(){
        List<Like> likes = repository.findByUserId(1);
        assertTrue(likes.get(1).isLiked());
    }

    @Test
    void shouldFindByMessageId(){
        List<Like> likes = repository.findByMessageId(1);
        assertTrue(likes.get(0).isLiked());
    }

    @Test
    void shouldAdd() {
        User user = new User("john.doe@example.com", "John", "Doe", "password123", 1, 1, "johndoe");
        Message message = new Message(2, 2, 2, "I love this company!", new Timestamp(2023-01-02));
        Like like = new Like(0, true, user, message);

        assertTrue(repository.add(like));
        assertThrows(DuplicateKeyException.class, () -> repository.add(like), "Cannot add a like to the message twice");
    }


    @Test
    void shouldUpdate() {
        User user = new User("john.doe@example.com", "John", "Doe", "password123", 1, 1, "johndoe");
        Message message = new Message(1, 1, 1, "Great stock!", new Timestamp(2023-01-01));
        Like like = new Like(1, false, user, message);
        assertTrue(repository.update(like));
    }

    @Test
    void shouldDelete() {
        assertTrue(repository.delete(2));
    }
}