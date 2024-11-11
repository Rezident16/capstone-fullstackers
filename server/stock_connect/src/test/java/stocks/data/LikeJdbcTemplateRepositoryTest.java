package stocks.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import stocks.models.Like;
import stocks.models.Message;
import stocks.models.User;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = LikeJdbcTemplateRepository.class)
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
    void shouldAdd() {
        User user = new User("something@something.com", "SomeFirstName", "SomeLastName", "SomePassword@2024", 0, 3, "SomeUserName");
        Message message = new Message(4, 3, 5, "Some Content", new Date(11 / 11 / 2024));
        Like like = new Like(0, true, user, message);

        assertTrue(repository.add(like));

//        repository.add(like);
//        fail("cannot add a like to the message twice");

    }

    @Test
    void shouldUpdate() {
    }

    @Test
    void shouldDelete() {
    }
}