package stocks.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import stocks.models.Like;
import stocks.models.Message;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
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
    void shouldFindAll() {
        List<Like> likes = repository.findAll();
        assertNotNull(likes);
        assertTrue(likes.size() > 0);
    }

    @Test
    void shouldFindById() {
        Like like = repository.findById(1);
        assertNotNull(like);
        assertEquals(1, like.getLikeId());
        assertTrue(like.isLiked());
    }

    @Test
    void shouldFindByUserId() {
        List<Like> likes = repository.findByUserId(1);
        assertNotNull(likes);
        assertTrue(likes.size() > 0);
    }

    @Test
    void shouldFindByMessageId() {
        List<Like> likes = repository.findByMessageId(1);
        assertNotNull(likes);
        assertTrue(likes.size() > 0);
    }

    @Test
    void shouldAdd() {
        Like like = new Like(0, true, 1, 1);
        Like actual = repository.add(like);
        assertNotNull(actual);
        assertTrue(actual.getLikeId() > 0);
    }

    @Test
    void shouldUpdate() {
        Like like = new Like(2, true, 3, 1);
        assertTrue(repository.update(like));
    }

    @Test
    void shouldDelete() {
        assertTrue(repository.delete(1));
        assertNull(repository.findById(1));
    }

}
