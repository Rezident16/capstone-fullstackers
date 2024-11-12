package stocks.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import stocks.data.LikeRepository;
import stocks.models.Like;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class LikeServiceTest {

    @Autowired
    LikeService service;

    @MockBean
    LikeRepository repository;

    @Test
    void shouldAdd() {
        Like like = new Like(0, true, 1, 1);
        Like mockOut = new Like(4, true, 1, 1);

        when(repository.add(like)).thenReturn(mockOut);

        Result<Like> actual = service.add(like);
        assertEquals(ResultType.SUCCESS, actual.getType());
        assertEquals(mockOut, actual.getPayload());
    }

    @Test
    void shouldNotAddWhenInvalid() {
        Like like = new Like(10, false, 2, 2);

        Result<Like> actual = service.add(like);
        assertEquals(ResultType.INVALID, actual.getType());

        like.setLikeId(0);
        like.setUserId(6);
        actual = service.add(like);
        assertEquals(ResultType.INVALID, actual.getType());

        like.setUserId(1);
        like.setMessageId(10);
        actual = service.add(like);
        assertEquals(ResultType.INVALID, actual.getType());
    }

    @Test
    void shouldUpdate() {
        Like like = new Like(3, false, 3, 3);

        when(repository.update(like)).thenReturn(true);
        Result<Like> actual = service.update(like);
        assertEquals(ResultType.SUCCESS, actual.getType());
    }

    @Test
    void shouldNotUpdateMissing() {
        Like like = new Like(10, false, 1, 1);

        when(repository.update(like)).thenReturn(false);
        Result<Like> actual = service.update(like);
        assertEquals(ResultType.NOT_FOUND, actual.getType());
    }

    @Test
    void shouldNotUpdateWhenInvalid() {
        Like like = new Like(3, false, 10, 1);

        Result<Like> actual = service.update(like);
        assertEquals(ResultType.INVALID, actual.getType());

        like.setUserId(1);
        like.setMessageId(10);
        actual = service.update(like);
        assertEquals(ResultType.INVALID, actual.getType());
    }

}